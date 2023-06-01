package com.walletmix.paymixbusiness.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.walletmix.paymixbusiness.data.network.api_response.RecentComments
import com.walletmix.paymixbusiness.R

class RecentCommentsAdapter(



    private var context: Context,

    private var recentCommentsList: ArrayList<RecentComments>


) :

    RecyclerView.Adapter<RecentCommentsAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recent_comment_item_layout, parent, false)
        return MyViewHolder(v)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        val recentComments = recentCommentsList[position]

        //holder.id.text = recentTransaction.id
        holder.title.text = recentComments.title
        holder.message.text = recentComments.comment
        holder.createdAt.text = recentComments.createdAt
       // holder.orderId.text = recentComments.wmxId.toString()
        holder.txnId.text = recentComments.wmxId.toString()

    }
    override fun getItemCount(): Int {
        return recentCommentsList.size
    }
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //var id: TextView = itemView.findViewById<View>(R.id.tv_transaction_id) as TextView
        var title: TextView = itemView.findViewById<View>(R.id.tv_title) as TextView
        var message: TextView = itemView.findViewById<View>(R.id.tv_message) as TextView
        var createdAt: TextView = itemView.findViewById<View>(R.id.tv_created_at) as TextView
        //var orderId: TextView = itemView.findViewById<View>(R.id.tv_order_id) as TextView
        var txnId: TextView = itemView.findViewById<View>(R.id.tv_txn_id) as TextView
    }
}