package com.walletmix.paymixbusiness.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.walletmix.paymixbusiness.R

class MerchentTransactionDetailsAdapter (


    private var context: Context,



) :


    RecyclerView.Adapter<MerchentTransactionDetailsAdapter.MyViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.merchent_transaction_details_layout, parent, false)
        return MyViewHolder(v)
    }
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//
//        //val transactionDetails = transactionDetailsList[position]
//
//        holder.merchentAmount.text = transactionDetails.merchantAmmount
//        holder.extraCharge.text = transactionDetails.extraCharge
//        holder.paymentAmount.text = transactionDetails.payedAmmount
//        holder.requestIp.text = transactionDetails.ip
//        holder.payableAmount.text = transactionDetails.payedAmmount
//        holder.releaseStatus.text = transactionDetails.releaseStatus
//        holder.charge.text = transactionDetails.charge


    }
    override fun getItemCount(): Int {
        return 0
    }
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var merchentAmount: TextView = itemView.findViewById<View>(R.id.tv_merchent_amount) as TextView
        var extraCharge: TextView = itemView.findViewById<View>(R.id.tv_extra_charge) as TextView
        var paymentAmount: TextView = itemView.findViewById<View>(R.id.tv_payment_amount) as TextView
        var requestIp: TextView = itemView.findViewById<View>(R.id.tv_request_ip) as TextView
        var payableAmount: TextView = itemView.findViewById<View>(R.id.tv_payable_amount) as TextView
        var releaseStatus: TextView = itemView.findViewById<View>(R.id.tv_release_status) as TextView
        var charge: TextView = itemView.findViewById<View>(R.id.tv_charge) as TextView





    }
}