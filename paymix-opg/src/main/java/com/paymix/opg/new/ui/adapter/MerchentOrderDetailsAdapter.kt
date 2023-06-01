package com.walletmix.paymixbusiness.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.walletmix.paymixbusiness.R

class MerchentOrderDetailsAdapter (


    private var context: Context,




) :


    RecyclerView.Adapter<MerchentOrderDetailsAdapter.MyViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.merchent_order_details_layout, parent, false)
        return MyViewHolder(v)
    }
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {



    }
    override fun getItemCount(): Int {
      return 0
    }
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {





    }
}