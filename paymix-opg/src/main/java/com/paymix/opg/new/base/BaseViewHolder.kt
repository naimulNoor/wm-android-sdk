package com.walletmix.paymixbusiness.base
import android.view.View

abstract class BaseViewHolder protected constructor(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

    protected val TAG: String  by lazy {
        this.javaClass.simpleName
    }

    abstract fun bind(position: Int)

}