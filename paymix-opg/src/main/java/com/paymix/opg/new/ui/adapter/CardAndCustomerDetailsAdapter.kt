package com.walletmix.paymixbusiness.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.walletmix.paymixbusiness.R

class CardAndCustomerDetailsAdapter (


    private var context: Context,

   // private var customerDetailsList: ArrayList<CustomerDetails>


) :


    RecyclerView.Adapter<CardAndCustomerDetailsAdapter.MyViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_and_customer_details_layout, parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {



    }
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {




    }
}