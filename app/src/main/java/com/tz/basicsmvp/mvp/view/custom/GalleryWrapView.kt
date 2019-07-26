//package com.tz.basicsmvp.mvp.view.custom
//
//import android.content.Context
//import android.util.AttributeSet
//import android.widget.FrameLayout
//import android.widget.LinearLayout
//import android.app.Activity
//import android.content.res.Resources
//import android.widget.ImageView
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.Glide
//import android.graphics.*
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import android.view.View
//import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
//import android.graphics.BitmapShader
//import android.os.Handler
//import android.widget.RelativeLayout
//import androidx.viewpager.widget.PagerAdapter
//import androidx.viewpager.widget.ViewPager
//import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
//import com.blankj.utilcode.util.LogUtils
//import com.tz.basicsmvp.R
//
//
///**
// * @ComputerCode: tianzhen
// * @Author: TianZhen
// * @QQ: 959699751
// * @CreateTime: Created on 2019/5/23 10:40
// * @Package: cn.eclicks.drivingtest.widget
// * @Description: 画廊包装类，功能都在内部实现，
// **/
//class GalleryWrapView @JvmOverloads constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int = 0) :
//    FrameLayout(context, attr, defStyleAttr), ViewPager.OnPageChangeListener{
//
//    private var mLayout: View? = null//布局
//    private var mContext: Activity? = null//上下文
//    private var mViewPager: ViewPager? = null//viewpager
//    private var mPagerAdapter: BannerPagerAdapter? = null//adapter
//    private var mLineIndicator: LinearLayout? = null//指示器集合容器
//    private var mImageView: ArrayList<ImageView>? = null//小圆点imageview对象
//    private var mList: List<String>? = null//url数组
//    private var currentIndex = 0//当前实际page
//    private var startCurrentIndex = 0//当前page
//    private var secondTime: Long = 0
//    private var firstTime: Long = 0
//
//    private var mHandler: Handler? = null
//    private var mAutoRollRunnable: AutoRollRunnable? = null
//    private var mRollTime = 5000
//
//    private var resId_piont_press = R.drawable.ic_banner_point_press
//    private var resId_piont = R.drawable.ic_banner_point
//    private var isPoint = false//开启指示器
//
//    init {
//        this.mContext = context as Activity
//    }
//
//    interface OnClickBannerListener {
//        fun onBannerClick(position: Int)
//    }
//
//    private var mBannerListener: OnClickBannerListener? = null
//    fun addBannerListener(listener: OnClickBannerListener): GalleryWrapView {
//        if(mBannerListener == null){
//            mBannerListener = listener
//        }
//        return this
//    }
//
//    /**
//     * 初始化viewpager
//     * @param list  url集合
//     * @param isGallery 是否使用3D画廊效果
//     */
//    fun initBanner(list: List<String>, isGallery: Boolean, alpha :Float = 1.0f ): GalleryWrapView {
//        mList = list
//        startCurrentIndex = 0
//
////        if(mLayout == null){
//        //引入布局
//        mLayout = LayoutInflater.from(mContext).inflate(R.layout.banner_view_layout, null)
//        mViewPager = mLayout?.findViewById(R.id.viewPager) as ViewPager//关闭
//        mLineIndicator = mLayout?.findViewById(R.id.lineIndicator) as LinearLayout
//        //初始化位置
//        mList?.run {
//            currentIndex = startCurrentIndex % this.size
//        }
//
//        mPagerAdapter = mContext?.let { BannerPagerAdapter(mList!!, it) }
//        mPagerAdapter?.setOnClickImagesListener(object : BannerPagerAdapter.OnClickImagesListener {
//            override fun onImagesClick(position: Int) {
//                mBannerListener?.onBannerClick(position)
//            }
//        })
//
//        mViewPager?.adapter = mPagerAdapter
//        if (isGallery) {
//            mViewPager?.setPageTransformer(true, ZoomPageTransformer(alpha))
//        }
//
//        mViewPager?.currentItem = startCurrentIndex
//        mViewPager?.offscreenPageLimit = 2//设置预加载的数量，这里设置了2,会预加载中心item左边两个Item和右边两个Item
//        mViewPager?.addOnPageChangeListener(this)
////        }else{
////            mPagerAdapter?.let {
////                it.setList(mList!!)
////            }
////        }
//        return this
//    }
//
//    /**
//     * 添加默认图片,当加载失败后显示
//     * @param resId_img
//     * @return
//     */
//    fun addDefaultImg(resId_img: Int): GalleryWrapView {
//        mPagerAdapter?.setDefaultImg(resId_img)
//        return this
//    }
//
//    /**
//     * 添加圆角
//     * @param corners
//     * @return
//     */
//    fun addRoundCorners(corners: Int): GalleryWrapView {
//        mPagerAdapter?.setmRoundCorners(corners)
//        return this
//    }
//
//    var rowMargin: Int = 0
//    /**
//     *
//     * @param columnMargin 两个Page之间的距离
//     * @param rowMargin  page的外边距,中间item距离边界的间距
//     * 注意当添加了3D画廊效果时,columnMargin尽量设小。android系统已经进行了x、y的缩放
//     */
//    fun addPageMargin(columnMargin: Int, rowMar: Int): GalleryWrapView {
////        if(!isAdded){
//        rowMargin = rowMar
//        mViewPager?.pageMargin = dip2px(columnMargin)
//        val layout = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
//            RelativeLayout.LayoutParams.WRAP_CONTENT)
//        layout.setMargins(0, 0, dip2px(rowMargin), 0)
//        mViewPager?.layoutParams = layout
////        }
//        return this
//    }
//
//    /**
//     * 添加小圆点
//     * @param distance 间距
//     */
//    fun addPoint(distance: Int): GalleryWrapView {
//        isPoint = true
//        mImageView = ArrayList()
//        for (i in 0 until mList!!.size) {
//            val imageView = ImageView(mContext)
//
//            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//            params.setMargins(dip2px(distance) / 2, 0, dip2px(distance) / 2, 0)
//            imageView.layoutParams = params
//            if (i == currentIndex) {
//                imageView.setImageResource(resId_piont_press)
//            } else {
//                imageView.setImageResource(resId_piont)
//            }
//            mImageView!![i] = imageView
//            mLineIndicator?.addView(imageView)
//        }
//
//        return this
//    }
//
//    /**
//     * 添加小圆点
//     * @param distance 间距
//     * @param piont_press 替换选中图标
//     * @param piont       替换未选中图片
//     */
//    fun addPoint(distance: Int, piont_press: Int, piont: Int): GalleryWrapView {
//        isPoint = true
//        resId_piont_press = piont_press
//        resId_piont = piont
//        mImageView = ArrayList()
//        for (i in 0 until mList!!.size) {
//            val imageView = ImageView(mContext)
//
//            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//            params.setMargins(dip2px(distance) / 2, 0, dip2px(distance) / 2, 0)
//            imageView.layoutParams = params
//            if (i == currentIndex) {
//                imageView.setImageResource(resId_piont_press)
//            } else {
//                imageView.setImageResource(resId_piont)
//            }
//            mImageView!![i] = imageView
//            mLineIndicator?.addView(imageView)
//        }
//        return this
//    }
//
//    /**
//     * 添加小圆点底部间距
//     * @param paddBottom
//     */
//    fun addPointBottom(paddBottom: Int): GalleryWrapView {
//        mLineIndicator?.setPadding(0, 0, 0, dip2px(paddBottom))
//        return this
//    }
//
//
//    var isAdded = false
//    /**
//     * 配置完成,将布局添加到父容器
//     */
//    fun finishConfig(): GalleryWrapView {
//        if(!isAdded){
//            isAdded = true
//            this.addView(mLayout)
//        }else{
//            this.removeAllViews()
//            this.addView(mLayout)
////            mLayout?.invalidate()
//        }
//        return this
//    }
//
//    //开始轮播
//    fun addStartTimer(time: Int): GalleryWrapView {
//        mRollTime = time
//        if (mHandler == null) {
//            mHandler = Handler()
//        }
//        if (mAutoRollRunnable == null) {
//            mAutoRollRunnable = AutoRollRunnable()
//        }
//
//        mAutoRollRunnable?.start()
//        return this
//    }
//
//    // 停止轮播
//    fun stopTimer() {
//        mAutoRollRunnable?.stop()
//    }
//
//    fun dip2px(dpValue: Int):Int {
//        var den = mContext?.resources?.displayMetrics?.density!!
//        return (dpValue * den + 0.5f)?.toInt()
//    }
//
//    inner class AutoRollRunnable :Runnable{
//        //是否在轮播的标志
//        var isRunning = false
//        override fun run() {
//            if (isRunning) {
//                var index = mViewPager?.currentItem?.plus(1)  //下一个页
//                index?.run {
//                    mViewPager?.currentItem = index//设置此次要显示的pager
//                    currentIndex=index% mList?.size!!
//                }
//                setImageBackground(currentIndex)
//                mHandler?.postDelayed(this, (1000*mRollTime).toLong())
//            }
//        }
//
//        fun start() {
//            if (!isRunning) {
//                isRunning = true
//                mHandler?.removeCallbacks(this)
//                mHandler?.postDelayed(this, (1000*mRollTime).toLong())
//            }
//        }
//        fun stop() {
//            if (isRunning) {
//                mHandler?.removeCallbacks(this)
//                isRunning = false
//            }
//        }
//    }
//
//    override fun onPageScrollStateChanged(p0: Int) {}
//
//    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
//        LogUtils.i("p0=$p0,p1=$p1,p2=$p2,currentIndex=$currentIndex")
//    }
//
//    var marginRightSpace = true
//    override fun onPageSelected(position: Int) {
//        mList?.run {
//            currentIndex=position % this.size
//            setImageBackground(currentIndex)
//        }
//
//        //下面是主要实现，最后一页空出距离向左，原本是向右的（缺点：慢划仔细看，其实view是有抖动的）
//        var marginRightSp = true
//        mList?.let {
//            marginRightSp = currentIndex != it.size-1
//        }
//
//        if(marginRightSp != marginRightSpace){
//            marginRightSpace = marginRightSp
//
//            if(marginRightSpace){
//                val layout = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
//                    RelativeLayout.LayoutParams.WRAP_CONTENT)
//                layout.setMargins(0, 0, dip2px(rowMargin), 0)
//                mViewPager?.layoutParams = layout
//            }else{
//                val layout = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
//                    RelativeLayout.LayoutParams.WRAP_CONTENT)
//                layout.setMargins(dip2px(rowMargin) - dip2px(10), 0, dip2px(10), 0)
//                mViewPager?.layoutParams = layout
//            }
//        }
////        LogUtils.i("position=$position")
//
//    }
//
//    /**
//     * 改变指示器
//     * @param selectItemsIndex
//     */
//    private fun setImageBackground(selectItemsIndex: Int) {
//        if (isPoint) {
//            mImageView?.let {
//                for (i in 0 until it.size) {
//                    if (i == selectItemsIndex) {
//                        it[i].setImageResource(resId_piont_press)
//                    } else {
//                        it[i].setImageResource(resId_piont)
//                    }
//                }
//            }
//        }
//    }
//
//}
//
//class BannerPagerAdapter(private val dataList: List<String>, private val mContext: Context) : PagerAdapter() {
//    private var defaultImg = R.drawable.ic_banner_error//默认图片
//    private var mRoundCorners :Int = -1
//    private var mImagesListener: OnClickImagesListener? = null
//    private var mList : MutableList<String> ? =null
//
//    init {
//        mList =  mutableListOf()
//        this.mList?.addAll(dataList)
//    }
//
//    /**
//     * 不支持刷新数据，因为viewpager目的是显示很大的视图，Google 未使用
//     * */
//    fun setList(dataList: List<String>){
//        //this.mList?.let {
//        //    it.clear()
//        //    it.addAll(dataList)
//        //    this.notifyDataSetChanged()
//        //}
//    }
//
//    /**
//     * 默认
//     * @param defaultImg
//     */
//    fun setDefaultImg(defaultImg: Int) {
//        this.defaultImg = defaultImg
//    }
//
//    /**
//     * 设置圆角
//     * @param mRoundCorners
//     */
//    fun setmRoundCorners(mRoundCorners: Int) {
//        this.mRoundCorners = mRoundCorners
//    }
//
//    /**
//     * 点击回调
//     */
//    interface OnClickImagesListener {
//        fun onImagesClick(position: Int)
//    }
//
//    fun setOnClickImagesListener(listener: OnClickImagesListener) {
//        mImagesListener = listener
//    }
//
//    override fun getCount(): Int {
////        return 500000
//        return mList?.size!!
//    }
//
//    override fun isViewFromObject(view: View, `object`: Any): Boolean {
//        return view === `object`
//    }
//
//    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        container.removeView(`object` as View)
//    }
//
//    override fun instantiateItem(container: ViewGroup, position: Int): Any {
//        val view = LayoutInflater.from(mContext).inflate(R.layout.banner_img_layout, container, false)
//        val imageView = view.findViewById(R.id.img) as ImageView
//
//        val index = position % mList?.size!!
//        LoadImage(mList!![index], imageView)
//        //OnClick
//        imageView.setOnClickListener { mImagesListener!!.onImagesClick(index) }
//
//        container.addView(view)
//        return view
//    }
//
//    /**
//     * 加载图片
//     */
//    fun LoadImage(url: String, imageview: ImageView) {
//        if (mRoundCorners == -1) {
//            Glide.with(mContext)
//                .load(url)
////                    .centerCrop()
//                .dontAnimate()//防止设置placeholder导致第一次不显示网络图片,只显示默认图片的问题
//                .placeholder(defaultImg)
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageview)
//        } else {
//            Glide.with(mContext)
//                .load(url)
////                    .centerCrop()
//                .dontAnimate()//防止设置placeholder导致第一次不显示网络图片,只显示默认图片的问题
//                .placeholder(defaultImg)
//                .transform(CornerTransform(mContext, mRoundCorners))
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageview)
//        }
//    }
//}
//
//
//
///**
// * Created by Administrator on 2018/11/28.
// * 自定义圆角
// */
//class CornerTransform @JvmOverloads constructor(context: Context, dp: Int = 0) : BitmapTransformation(context) {
//
//    init {
//        radius = Resources.getSystem().displayMetrics.density * dp
//    }
//
//    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap? {
//        return roundCrop(pool, toTransform)
//    }
//
//    override fun getId(): String {
//        return javaClass.name + Math.round(radius)
//    }
//
//    companion object {
//
//        private var radius: Float = 0f
//
//        private fun roundCrop(pool: BitmapPool, source: Bitmap?): Bitmap? {
//            if (source == null) return null
//
//            var result = pool.get(source.width, source.height, Bitmap.Config.ARGB_8888)
//            if (result == null) {
//                result = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
//            }
//
//            val canvas = Canvas(result)
//            val paint = Paint()
//            paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
//            paint.isAntiAlias = true
//            val rectF = RectF(0f, 0f, source.width.toFloat(), source.height.toFloat())
//            canvas.drawRoundRect(rectF, radius, radius, paint)
//            return result
//        }
//    }
//
//}
//
//
///**
// * Created by Administrator on 2018/11/28.
// * 3D画廊效果其实就是ViewPager的item切换时，进行缩放的动画效果
// */
//class ZoomPageTransformer @JvmOverloads constructor(alpha: Float = 1.0f) : ViewPager.PageTransformer {
//
//    private var MIN_ALPHA = 1.0f//最小透明度
//
//    init {
//        this.MIN_ALPHA = alpha
//    }
//
//    override fun transformPage(view: View, position: Float) {
//        //setScaleY只支持api11以上
//        /**
//         * (-oo,-1) 相对于左边第一页，其左边的所有页面 **
//         * x、y拉伸为MIN_SCALE、透明度MIN_ALPHA
//         */
//        if (position < -1) {
//            view.scaleX = MIN_SCALE
//            view.scaleY = MIN_SCALE
//            view.alpha = MIN_ALPHA
//        } else if (position < 1) {
//            val scaleFactor = MIN_SCALE + (1 - Math.abs(position)) * (MAX_SCALE - MIN_SCALE)
//            //[0, 1 ） 相对于当前选中页，其右边第一页 **
//            if (position > 0) {
//                view.translationX = -scaleFactor
//            } else if (position < 0) {
//                view.translationX = scaleFactor
//            }// [-1, 0 ) 相对于当前选中页，其左边的第一页**
//            view.scaleY = scaleFactor
//            view.scaleX = scaleFactor
//
//            // float alpha = 1f -  Math.abs(position) * (1 - );
//
//            val alpha = MIN_ALPHA + (1 - MIN_ALPHA) * (1 - Math.abs(position))
//            view.alpha = alpha
//
//        } else { // (1,+Infinity]
//            view.scaleX = MIN_SCALE
//            view.scaleY = MIN_SCALE
//            view.alpha = MIN_ALPHA
//        }
//        /**
//         * [1,+oo） 相对于右边第一页，其右边的所有页面
//         * x、y拉伸为MIN_SCALE、透明度MIN_ALPHA
//         */
//        /**
//         * [-1, 1 )当前页的左右第一页
//         */
//    }
//
//    companion object {
//
//        private val MAX_SCALE = 1.0f//0缩放
//
//        private val MIN_SCALE = 0.85f//0.85缩放
//    }
//
//}