package com.walletmix.paymixbusiness.ui.view.comments

import com.walletmix.paymixbusiness.data.network.api_response.comment.CommentResponseModel
import com.walletmix.paymixbusiness.base.BaseContract

interface CommentsContract {

    interface View : BaseContract.View {

        fun CommentListResponse(response: CommentResponseModel)
    }

    interface Presenter : BaseContract.Presenter {

        fun CommentList(dataMap: HashMap<String, String>)

    }
}