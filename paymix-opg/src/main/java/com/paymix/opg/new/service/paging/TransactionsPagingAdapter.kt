package com.walletmix.paymixbusiness.service.paging


import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import com.walletmix.paymixbusiness.data.network.api_response.transaction.TransactionData
import com.walletmix.paymixbusiness.ui.adapter.TransactionAdapter
import com.walletmix.paymixbusiness.R
import com.walletmix.paymixbusiness.utils.PermissionUtils
import javax.inject.Inject


class TransactionsPagingAdapter : PagingDataAdapter<TransactionData, TransactionsPagingAdapter.MyViewHolder>(COMPARATOR) {

    private lateinit var transactionList: ArrayList<TransactionData>
    var dialog: Dialog?=null
    lateinit var url:String
    @Inject
    lateinit var permissionUtils: PermissionUtils
    var  getTxnIdCallback: TransactionAdapter.CallbackGetTxnId? = null
    lateinit var  mButtonSheetCallback: TransactionAdapter.CallbackBottomSheetInterface
    lateinit var intent: Intent

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //var transactionPosition: TextView = itemView.findViewById<View>(R.id.tv_transaction_position) as TextView
        var orderId: TextView = itemView.findViewById<View>(R.id.tv_order_id) as TextView
        var bankType: TextView = itemView.findViewById<View>(R.id.tv_bank_type) as TextView
        var amount: TextView = itemView.findViewById<View>(R.id.tv_amount) as TextView
        var taxId: TextView = itemView.findViewById<View>(R.id.tv_tax_id) as TextView
        var time: TextView = itemView.findViewById<View>(R.id.tv_time) as TextView
        var transactionStatus: TextView = itemView.findViewById<View>(R.id.tv_transaction_status) as TextView

        var viewDetails: TextView = itemView.findViewById<View>(R.id.btn_view_details) as Button
        var viewInvoice: TextView = itemView.findViewById<View>(R.id.btn_view_invoice) as Button
        var uploadInvoice: TextView = itemView.findViewById<View>(R.id.btn_upload_invoice) as Button
        var comments: TextView = itemView.findViewById<View>(R.id.btn_comments) as Button
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val transaction = transactionList[position]

        var transactionPosition = position+1

        //holder.transactionPosition.text = transactionPosition.toString()
        holder.orderId.text = transaction.merchantOrderID
        holder.bankType.text = transaction.bppBank
        holder.amount.text = transaction.bppBankAmount.toString()
        holder.taxId.text = transaction.bppWmxTxnID.toString()
        holder.time.text = transaction.bppCreatedAt
        holder.transactionStatus.text = transaction.bppTransactionStatus.toString()

        when (transaction.bppTransactionStatus) {
            "4" -> {
                holder.transactionStatus.setTextColor(Color.parseColor("#FF741E"))
            }
            null -> {
                holder.transactionStatus.setTextColor(Color.parseColor("#FF741E"))
            }
            "3" -> {
                holder.transactionStatus.setTextColor(Color.parseColor("#DC0808"))
            }
            "2" -> {
                holder.transactionStatus.setTextColor(Color.parseColor("#DC0808"))
            }
            "1" -> {
                holder.transactionStatus.setTextColor(Color.parseColor("#18A24F"))
            }

            else -> {}
        }

        holder.viewDetails.setOnClickListener {  mButtonSheetCallback?.onHandleSelection(1,transaction.bppWmxTxnID.toString(),transaction.bppTransactionStatus.toString(),transactionPosition,transaction) }
        holder.uploadInvoice.setOnClickListener { mButtonSheetCallback?.onHandleSelection(2,transaction.bppWmxTxnID.toString(),transaction.bppTransactionStatus.toString(),transactionPosition,transaction) }
//        holder.viewInvoice.setOnClickListener { mButtonSheetCallback?.onHandleSelection(3,transaction.bppWmxTxnID.toString(),transaction.bppTransactionStatus.toString(),transactionPosition,transaction)}
//        holder.comments.setOnClickListener {mButtonSheetCallback?.onHandleSelection(4,transaction.bppWmxTxnID.toString(),transaction.bppTransactionStatus.toString(),transactionPosition,transaction)}

        getTxnIdCallback?.getTxnId(transaction.bppWmxTxnID.toString())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.transactions_item_layout, parent, false)
        return MyViewHolder(v)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<TransactionData>() {
            override fun areItemsTheSame(oldItem: TransactionData, newItem: TransactionData): Boolean {
                return oldItem.merchantOrderID == newItem.merchantOrderID
            }

            override fun areContentsTheSame(oldItem: TransactionData, newItem: TransactionData): Boolean {
                return oldItem == newItem
            }
        }
    }
}


