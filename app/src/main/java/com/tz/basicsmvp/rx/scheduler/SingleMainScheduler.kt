package com.hazz.kotlinmvp.rx.scheduler

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
* @ComputerCode: tianzhen
* @Author: TianZhen
* @QQ: 959699751
* @CreateTime: Created on 2019/5/17 19:57
* @Package:
* @Description:
**/
class SingleMainScheduler<T> private constructor() : BaseScheduler<T>(Schedulers.single(), AndroidSchedulers.mainThread())
