package com.walletmix.paymixbusiness.ui.view.comments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.walletmix.paymixbusiness.data.network.api_response.comment.CommentResponseModel
import com.walletmix.paymixbusiness.data.network.api_response.comment.DataComment
import com.walletmix.paymixbusiness.ui.adapter.CommentListAdapter
import com.walletmix.paymixbusiness.databinding.ActivityCommentsBinding
import com.walletmix.paymixbusiness.base.MvpBaseActivity
import com.walletmix.paymixbusiness.data.prefs.PrefKeys
import com.walletmix.paymixbusiness.utils.AppUtils
import com.walletmix.paymixbusiness.utils.Keys.IntentKeys


class CommentsActivity : MvpBaseActivity<CommentsPresenter>(), CommentsContract.View {

    private lateinit var binding: ActivityCommentsBinding

    private var commentList = ArrayList<DataComment>()
    private lateinit var commentListAdapter: CommentListAdapter
    var txnId=""

    override fun getContentView(): View {


        binding = ActivityCommentsBinding.inflate(layoutInflater)
        val view = binding.root
        return view


    }

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {

       // val id  = mPrefManager.getString(PrefKeys.TXN_ID)
        val bundle = intent.getBundleExtra(IntentKeys.DATA_BUNDLE)
        if (bundle != null) {
             txnId = bundle.getString(PrefKeys.TXN_ID) ?: ""
        }


        Log.d("Txn ID ",txnId)

        val commentData = HashMap<String, String>()
        //commentData["id"] = "254889"
        commentData["id"] = txnId
        mPresenter.CommentList(commentData)
        binding.appBar.tvAppBarOrderId.text="Wmx Txn ID - " + txnId

        binding.submitBtn.setOnClickListener {

            val title = binding.etCommentTitle.text.toString()
            val comment = binding.etCommentText.text.toString()

            commentData["id"] = txnId
            commentData["title"] = title
            commentData["comment"] = comment

            mPresenter.CommentList(commentData)


            binding.etCommentTitle.setText("")
            binding.etCommentText.setText("")


            AppUtils.shared.hideKeyboard(getContext() as Activity)
        }


//        binding.appBar.backButton.setOnClickListener {
//            Navigator.sharedInstance.back(this,TransactionListFragment::class.java)
//            //Navigator.sharedInstance.navigate(this, TransactionListFragment::class.java)
//        }


    }

    private fun recycleViewAdapterSetup() {

        val linearLayoutManager = LinearLayoutManager(this)
        binding.rvCommentList.layoutManager = linearLayoutManager
        commentListAdapter = CommentListAdapter(this, commentList)
        binding.rvCommentList.adapter = commentListAdapter

    }

    override fun CommentListResponse(response: CommentResponseModel) {
        commentList=response.data

        val linearLayoutManager = LinearLayoutManager(this)
        binding.rvCommentList.layoutManager = linearLayoutManager

        commentListAdapter= CommentListAdapter(this, commentList)
        binding.rvCommentList.adapter = commentListAdapter
        recycleViewAdapterSetup()
    }
}