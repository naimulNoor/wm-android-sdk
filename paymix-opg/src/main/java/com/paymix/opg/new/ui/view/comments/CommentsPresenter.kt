package com.walletmix.paymixbusiness.ui.view.comments

import com.walletmix.paymixbusiness.data.network.api_response.comment.CommentResponseModel
import com.walletmix.paymixbusiness.data.network.api_service.TransectionApiService
import com.walletmix.paymixbusiness.base.BasePresenter
import com.walletmix.paymixbusiness.data.network.SSDisposableSingleObserver
import javax.inject.Inject

class CommentsPresenter
@Inject constructor(view: CommentsContract.View) : BasePresenter<CommentsContract.View>(view), CommentsContract.Presenter {


    @Inject
    lateinit var commentApiService: TransectionApiService

    override fun CommentList(dataMap: HashMap<String, String>) {
        if (this::commentApiService.isInitialized) {
            //mView?.onNetworkCallStarted(context.getString(R.string.please_wait))
            compositeDisposable?.add(
                commentApiService.getCommentList(dataMap)
                    .subscribeOn(appSchedulerProvider.io())
                    .unsubscribeOn(appSchedulerProvider.computation())
                    .observeOn(appSchedulerProvider.ui())
                    .subscribeWith(object :
                        SSDisposableSingleObserver<CommentResponseModel, CommentsContract.View>(mView) {
                        override fun onRequestSuccess(response: CommentResponseModel) {
                            mView?.CommentListResponse(response)
                        }

                    })
            )
        }
    }


}