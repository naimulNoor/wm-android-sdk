package com.walletmix.paymixbusiness.ui.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.walletmix.paymixbusiness.base.BasePagingViewHolder
import com.walletmix.paymixbusiness.data.network.api_response.transaction.TransactionData
import com.walletmix.paymixbusiness.R
import com.walletmix.paymixbusiness.data.prefs.PrefKeys
import com.walletmix.paymixbusiness.data.prefs.PreferenceManager
import com.walletmix.paymixbusiness.utils.PermissionUtils
import javax.inject.Inject

class TransactionAdapter (

    private var context: Context,
    private var transactionList: ArrayList<TransactionData>,
) : RecyclerView.Adapter<BasePagingViewHolder>() {

    var dialog: Dialog?=null
    lateinit var url:String

    private val VIEW_TYPE_LOADING = 0
    private val VIEW_TYPE_NORMAL = 1
    private var isLoaderVisible = false

    @Inject
    lateinit var permissionUtils: PermissionUtils

    var  getTxnIdCallback: CallbackGetTxnId? = null
    lateinit var  mButtonSheetCallback: CallbackBottomSheetInterface
    lateinit var intent:Intent


    private fun setBankType(bppBank: String, bankType: TextView) {
        var preferenceManager=PreferenceManager(context)
        var banks=preferenceManager.getStringHashMap(PrefKeys.APP_BANKS)
        try{
            bankType.text = banks!!.getValue(bppBank)
        }catch (e:Exception){
            bankType.text = "No Bank"
        }

    }

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasePagingViewHolder {


//        val v = LayoutInflater.from(parent.context).inflate(R.layout.transactions_item_layout, parent, false)
//        return MyViewHolder(v)

            return when (viewType) {
            VIEW_TYPE_NORMAL -> MyViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.transactions_item_layout, parent, false)
            )

            VIEW_TYPE_LOADING -> FooterHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false))

            else -> FooterHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false))
            }

    }
    override fun getItemViewType(position: Int): Int {
        return  VIEW_TYPE_NORMAL
//        return if (isLoaderVisible) {
//            if (position == transactionList.size - 1)
//                if(transactionList.size==1){
//                    VIEW_TYPE_NORMAL
//                }else{
//                    VIEW_TYPE_LOADING
//                }
//
//            else VIEW_TYPE_NORMAL
//        } else {
//            VIEW_TYPE_NORMAL
//        }
    }
    override fun onBindViewHolder(holder: BasePagingViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }
    private fun getItem(position: Int): TransactionData {
        return transactionList[position]
    }


    fun add(response: TransactionData) {
        transactionList.add(response)
        notifyItemInserted(transactionList.size - 1)
    }

    fun addAll(item: ArrayList<TransactionData>) {
        for (response in item) {
            add(response)
        }
    }


    private fun remove(postItems: TransactionData) {
        val position: Int = transactionList.indexOf(postItems)
        if (position > -1) {
            transactionList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun addLoading() {
        isLoaderVisible = true
        add(TransactionData("","","","",null,"",))
    }

    fun removeLoading() {
        isLoaderVisible = false
        val position: Int = transactionList.size - 1
        val item: TransactionData = getItem(position)
        if(item!=null){
            transactionList.removeAt(position)
            notifyItemRemoved(position)
        }


    }

    fun clear() {
        while (itemCount > 0) {
            Log.d("remove-txn",itemCount.toString())
            remove(getItem(0))
        }
    }




    inner class MyViewHolder internal constructor(itemView: View) : BasePagingViewHolder(itemView) {



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

        override fun clear() {

        }

        override fun onBind(position: Int) {
            super.onBind(position)

            val transaction = transactionList[position]
            val transactionPosition = position+1

            //holder.transactionPosition.text = transactionPosition.toString()
            orderId.text = transaction.merchantOrderID

            amount.text = transaction.bppBankAmount.toString()
            taxId.text = transaction.bppWmxTxnID.toString()
            time.text = transaction.bppCreatedAt

            setBankType(transaction.bppBank,bankType)

            var transaction_status = ""

           if (transaction.bppTransactionStatus == "1"){
                transaction_status = "Success"
            }
            else if (transaction.bppTransactionStatus == "2"){
                transaction_status = "Rejected"
            }
            else if (transaction.bppTransactionStatus == "3"){
                transaction_status = "Canceled"
            }
            else if (transaction.bppTransactionStatus == "4"){
                transaction_status = "Refund"
            }else{
               transaction_status = "Attempt"
           }

            transactionStatus.text = transaction_status

            when (transaction.bppTransactionStatus) {
                "4" -> {
                    transactionStatus.setTextColor(Color.parseColor("#FF741E"))
                }
                null -> {
                    transactionStatus.setTextColor(Color.parseColor("#5CB8FC"))
                }
                "3" -> {
                    transactionStatus.setTextColor(Color.parseColor("#F5BB00"))
                }
                "2" -> {
                    transactionStatus.setTextColor(Color.parseColor("#F84D5C"))
                }
                "1" -> {
                    transactionStatus.setTextColor(Color.parseColor("#00D433"))
                }

                else -> {
                    transactionStatus.setTextColor(Color.parseColor("#00D433"))
                }
            }

            viewDetails.setOnClickListener {  mButtonSheetCallback.onHandleSelection(1,transaction.bppWmxTxnID.toString(),transaction.bppTransactionStatus.toString(),transactionPosition,transaction) }
            uploadInvoice.setOnClickListener { mButtonSheetCallback.onHandleSelection(2,transaction.bppWmxTxnID.toString(),transaction.bppTransactionStatus.toString(),transactionPosition,transaction) }
            viewInvoice.setOnClickListener { mButtonSheetCallback.onHandleSelection(5,transaction.bppWmxTxnID.toString(),transaction.bppTransactionStatus.toString(),transactionPosition,transaction)}
            comments.setOnClickListener {mButtonSheetCallback.onHandleSelection(4,transaction.bppWmxTxnID.toString(),transaction.bppTransactionStatus.toString(),transactionPosition,transaction)}

            getTxnIdCallback?.getTxnId(transaction.bppWmxTxnID.toString())
        }

    }



    interface CallbackBottomSheetInterface {

        fun onHandleSelection(
            type: Int,
            id: String,
            status: String,
            position: Int,
            transaction: TransactionData
        )
    }

    interface CallbackGetTxnId{
        fun getTxnId(txnId:String)
    }




    class FooterHolder internal constructor(itemView: View?) : BasePagingViewHolder(itemView) {


        override fun clear() {

        }
    }


}