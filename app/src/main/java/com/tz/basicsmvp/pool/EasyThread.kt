package cn.eclicks.drivingtest.pool

import java.util.concurrent.*

/**
 * @ComputerCode: tianzhen
 * @Author: TianZhen
 * @QQ: 959699751
 * @CreateTime: Created on 2019/4/19 19:49
 * @Package: com.tz.autolayout.pool
 * @Description:
 **/
class EasyThread private constructor(
    type: Int, size: Int, priority: Int, // ==== There are default configs
    private val defName: String// default thread name
    , private val defCallback: Callback// default thread callback
    , private val defDeliver: Executor// default thread deliver
    , pool: ExecutorService?
) : Executor {
    /**
     * get thread pool that be created.
     * @return thread pool
     */
    val executor: ExecutorService?

    // to make sure no conflict for multi thread config.
    private val local: ThreadLocal<Configs>

    private val localConfigs: Configs
        @Synchronized get() {
            var configs = local.get()
            if (configs == null) {
                configs = Configs()
                configs.name = defName
                configs.callback = defCallback
                configs.deliver = defDeliver
                local.set(configs)
            }
            return configs
        }

    init {
        var pool = pool
        if (pool == null) {
            pool = createPool(type, size, priority)
        }
        this.executor = pool
        this.local = ThreadLocal()
    }

    /**
     * Set thread name for current task. if not set. the default name should be used.
     * @param name thread name
     * @return EasyThread
     */
    fun setName(name: String): EasyThread {
        localConfigs.name = name
        return this
    }

    /**
     * Set thread callback for current task, if not set, the default callback should be used.
     * @param callback thread callback
     * @return EasyThread
     */
    fun setCallback(callback: Callback): EasyThread {
        localConfigs.callback = callback
        return this
    }

    /**
     * Set the delay time for current task.
     *
     *
     * Attention: it only take effects when your thread pool is create by [Builder.createScheduled]
     * @param time time length
     * @param unit time unit
     * @return EasyThread
     */
    fun setDelay(time: Long, unit: TimeUnit): EasyThread {
        val delay = unit.toMillis(time)
        localConfigs.delay = Math.max(0, delay)
        return this
    }

    /**
     * Set the thread deliver for current task. if not set, the default deliver should be used.
     * @param deliver thread deliver
     * @return EasyThread
     */
    fun setDeliver(deliver: Executor): EasyThread {
        localConfigs.deliver = deliver
        return this
    }

    /**
     * Launch task
     * @param runnable task
     */
    override fun execute(runnable: Runnable) {
        var runnable = runnable
        val configs = localConfigs
        runnable = RunnableWrapper<Any>(configs).setRunnable(runnable)
        DelayTaskDispatcher.get().postDelay(configs.delay, executor!!, runnable)
        resetLocalConfigs()
    }

    /**
     * Launch async task, and the callback are used for receive the result of callable task.
     * @param callable callable
     * @param callback callback
     * @param <T> type
    </T> */
    fun <T> async(callable: Callable<T>, callback: AsyncCallback<T>) {
        val configs = localConfigs
        configs.asyncCallback = callback
        val runnable = RunnableWrapper<T>(configs)
            .setCallable(callable)
        DelayTaskDispatcher.get().postDelay(configs.delay, executor!!, runnable)
        resetLocalConfigs()
    }

    /**
     * Launch task
     * @param callable callable
     * @param <T> type
     * @return [Future]
    </T> */
    fun <T> submit(callable: Callable<T>): Future<T> {
        var callable = callable
        val result: Future<T>
        callable = CallableWrapper(localConfigs, callable)
        result = executor!!.submit(callable)
        resetLocalConfigs()
        return result
    }

    private fun createPool(type: Int, size: Int, priority: Int): ExecutorService {
        when (type) {
            Builder.TYPE_CACHEABLE -> return Executors.newCachedThreadPool(DefaultFactory(priority))
            Builder.TYPE_FIXED -> return Executors.newFixedThreadPool(size, DefaultFactory(priority))
            Builder.TYPE_SCHEDULED -> return Executors.newScheduledThreadPool(size, DefaultFactory(priority))
            Builder.TYPE_SINGLE -> return Executors.newSingleThreadExecutor(DefaultFactory(priority))
            else -> return Executors.newSingleThreadExecutor(DefaultFactory(priority))
        }
    }

    @Synchronized
    private fun resetLocalConfigs() {
        local.set(null)
    }

    private class DefaultFactory internal constructor(private val priority: Int) : ThreadFactory {

        override fun newThread(runnable: Runnable): Thread {
            val thread = Thread(runnable)
            thread.priority = priority
            return thread
        }
    }

    class Builder private constructor(size: Int, internal var type: Int, internal var pool: ExecutorService?) {
        internal var size: Int = 0
        internal var priority = Thread.NORM_PRIORITY
        internal var name: String = ""
        internal var callback: Callback? = null
        internal var deliver: Executor? = null

        init {
            this.size = Math.max(1, size)
        }

        /**
         * Set default thread name to used.
         * @param name default thread name
         * @return Builder itself
         */
        fun setName(name: String): Builder {
            if (!Tools.isEmpty(name)) {
                this.name = name
            }
            return this
        }

        /**
         * Set default thread priority to used.
         * @param priority thread priority
         * @return  itself
         */
        fun setPriority(priority: Int): Builder {
            this.priority = priority
            return this
        }

        /**
         * Set default thread callback to used.
         * @param callback thread callback
         * @return itself
         */
        fun setCallback(callback: Callback): Builder {
            this.callback = callback
            return this
        }

        /**
         * Set default thread deliver to used.
         * @param deliver default thread deliver
         * @return itself
         */
        fun setDeliver(deliver: Executor): Builder {
            this.deliver = deliver
            return this
        }

        /**
         * Create a thread manager to used with some configurations.
         * @return  EasyThread instance
         */
        fun build(): EasyThread {
            priority = Math.max(Thread.MIN_PRIORITY, priority)
            priority = Math.min(Thread.MAX_PRIORITY, priority)

            size = Math.max(1, size)
            if (Tools.isEmpty(name)) {
                // set default thread name
                when (type) {
                    TYPE_CACHEABLE -> name = "CACHEABLE"
                    TYPE_FIXED -> name = "FIXED"
                    TYPE_SINGLE -> name = "SINGLE"
                    else -> name = "EasyThread"
                }
            }

            if (deliver == null) {
                if (Tools.isAndroid) {
                    deliver = AndroidDeliver.getInstance()
                } else {
                    deliver = JavaDeliver.getInstance()
                }
            }

            return EasyThread(type, size, priority, name, callback!!, deliver!!, pool)
        }

        companion object {
            internal val TYPE_CACHEABLE = 0
            internal val TYPE_FIXED = 1
            internal val TYPE_SINGLE = 2
            internal val TYPE_SCHEDULED = 3

            fun create(pool: ExecutorService): Builder {
                return Builder(1, TYPE_SINGLE, pool)
            }

            /**
             * Create thread pool by **Executors.newCachedThreadPool()**
             * @return Builder itself
             */
            fun createCacheable(): Builder {
                return Builder(0, TYPE_CACHEABLE, null)
            }

            /**
             * Create thread pool by **Executors.newFixedThreadPool()**
             * @param size thread size
             * @return Builder itself
             */
            fun createFixed(size: Int): Builder {
                return Builder(size, TYPE_FIXED, null)
            }

            /**
             * Create thread pool by **Executors.newScheduledThreadPool()**
             * @param size thread size
             * @return Builder itself
             */
            fun createScheduled(size: Int): Builder {
                return Builder(size, TYPE_SCHEDULED, null)
            }

            /**
             * Create thread pool by **Executors.newSingleThreadPool()**
             *
             * @return Builder itself
             */
            fun createSingle(): Builder {
                return Builder(0, TYPE_SINGLE, null)
            }
        }
    }
}