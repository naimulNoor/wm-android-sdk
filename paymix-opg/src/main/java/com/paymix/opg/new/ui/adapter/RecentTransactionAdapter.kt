package com.walletmix.paymixbusiness.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.walletmix.paymixbusiness.data.network.api_response.RecentTransactions
import com.walletmix.paymixbusiness.R

class RecentTransactionAdapter(



    private var context: Context,

    private var recentTransactionList: ArrayList<RecentTransactions>


) :


    RecyclerView.Adapter<RecentTransactionAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recent_transaction_item_layout, parent, false)
        return MyViewHolder(v)
    }
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        val recentTransaction = recentTransactionList[position]


        var transaction_status = ""

        if (recentTransaction.bppTxnFStatus == "1"){
            transaction_status = "Success"
        }
        else if (recentTransaction.bppTxnFStatus == "2"){
            transaction_status = "Rejected"
        }
        else if (recentTransaction.bppTxnFStatus == "3"){
            transaction_status = "Canceled"
        }
        else if (recentTransaction.bppTxnFStatus == "4"){
            transaction_status = "Refund"
        }else{
            transaction_status = "Attempt"
        }

        holder.id.text = recentTransaction.wmxTrxnId.toString()
        holder.amount.text = recentTransaction.bppBankAmount.toString()
        holder.status.text = transaction_status






        when (recentTransaction.bppTxnFStatus) {
            "4" -> {
                holder.status.setTextColor(Color.parseColor("#FF741E"))
            }
            null -> {
                holder.status.setTextColor(Color.parseColor("#5CB8FC"))
            }
            "3" -> {
                holder.status.setTextColor(Color.parseColor("#F5BB00"))
            }
            "2" -> {
                holder.status.setTextColor(Color.parseColor("#F84D5C"))
            }
            "1" -> {
                holder.status.setTextColor(Color.parseColor("#00D433"))
            }

            else -> {
                holder.status.setTextColor(Color.parseColor("#00D433"))
            }
        }



    }
    override fun getItemCount(): Int {
        return recentTransactionList.size
    }
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var id: TextView = itemView.findViewById<View>(R.id.tv_transaction_id) as TextView
        var amount: TextView = itemView.findViewById<View>(R.id.tv_transaction_amount) as TextView
        var status: TextView = itemView.findViewById<View>(R.id.tv_transaction_status) as TextView
    }

}