package com.paymix.opg.base

import android.content.Context
import com.paymix.opg.rx.AppSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

open class BasePresenter<V : BaseContract.View> (var mView: V? = null) : BaseContract.Presenter{

    protected val TAG: String  by lazy {
        this.javaClass.simpleName
    }

    @Inject
    lateinit var appSchedulerProvider: AppSchedulerProvider

    @Inject
    lateinit var context: Context

    protected var compositeDisposable: CompositeDisposable? = CompositeDisposable()

    override fun detachView() {
        mView?.let {
            mView = null
        }
    }

    override fun clearDisposable() {
        compositeDisposable?.let {
            compositeDisposable!!.clear()
            compositeDisposable = null
        }
    }
}