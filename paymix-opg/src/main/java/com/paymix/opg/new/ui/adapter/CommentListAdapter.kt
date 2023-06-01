package com.walletmix.paymixbusiness.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.walletmix.paymixbusiness.data.network.api_response.RecentTransactions
import com.walletmix.paymixbusiness.data.network.api_response.comment.CommentResponseModel
import com.walletmix.paymixbusiness.data.network.api_response.comment.DataComment
import com.walletmix.paymixbusiness.R

class CommentListAdapter (

    private var context: Context,

    private var commentList: ArrayList<DataComment>


) :


    RecyclerView.Adapter<CommentListAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentListAdapter.MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.comments_item_layout, parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    override fun onBindViewHolder(holder: CommentListAdapter.MyViewHolder, position: Int) {
        val comments = commentList[position]

        holder.title.text = comments.title
        holder.comments.text = comments.comment
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var title: TextView = itemView.findViewById<View>(R.id.tv_comment_title) as TextView
        var comments: TextView = itemView.findViewById<View>(R.id.tv_comment_text) as TextView
    }


}