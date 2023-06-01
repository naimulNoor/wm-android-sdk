package com.paymix.opg.file

import android.content.Context
import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.wallemix.paymix.opg.R

import java.lang.Exception

class PicassoImageLoader private constructor(){

    companion object {
        val shared = PicassoImageLoader()
    }

    fun loadImage(context: Context, imageUrl: String, imageView: ImageView, placeHolderImageId: Int) {
        Picasso.get()
            .load(imageUrl)
            .networkPolicy(NetworkPolicy.OFFLINE)
            .placeholder(placeHolderImageId)
            .into(imageView, object : Callback {
                override fun onSuccess() {}

                override fun onError(e: Exception?) {
                    Picasso.get()
                        .load(imageUrl)
                        .placeholder(placeHolderImageId)
                        .error(placeHolderImageId)
                        .into(imageView)
                }
            })
    }

    fun loadImage(context: Context, imageUrl: String, imageView: ImageView) {
        Picasso.get()
            .load(imageUrl)
            .networkPolicy(NetworkPolicy.OFFLINE)
            .placeholder(R.drawable.img_placeholder)
            .into(imageView, object : Callback {
                override fun onSuccess() {}
                override fun onError(e: Exception?) {
                    Picasso.get()
                        .load(imageUrl)
                        .into(imageView)
                }

            })
    }

    fun loadImage(context: Context, imageUrl: String?) {
        if (imageUrl != null && !imageUrl.isEmpty())
            Picasso.get().load(imageUrl)
    }
}
