package com.walletmix.paymixbusiness.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.walletmix.paymixbusiness.data.network.api_response.Banners
import com.walletmix.paymixbusiness.utils.file.PicassoImageLoader
import com.walletmix.paymixbusiness.R
import java.util.ArrayList

class ImageSliderAdapter (
    private val context: Context,
    private val images: ArrayList<Banners>
) : PagerAdapter() {

    private val inflator: LayoutInflater = LayoutInflater.from(context)
    var imageSliderClickListener: ImageSliderClickListener? = null
    var imageSliderLongClickListener: ImageSliderLongClickListener?=null

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val myImageLayout = inflator.inflate(R.layout.slider_layout, view, false)
        val sliderImage = myImageLayout.findViewById<ImageView>(R.id.img_slider)
        images[position]!!.image?.let {
            PicassoImageLoader.shared.loadImage(context,
                it, sliderImage)
        }
        view.addView(myImageLayout, 0)
        sliderImage.setOnClickListener {
            imageSliderClickListener?.handleTap(images[position])
        }

        //ronis'code
        sliderImage.setOnLongClickListener {
            imageSliderLongClickListener?.handleLongTap(images[position])
            return@setOnLongClickListener true
        }

        return myImageLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    interface ImageSliderClickListener {
        fun handleTap(banner:Banners)
    }
    //ronis'code
    interface ImageSliderLongClickListener {
        fun handleLongTap(banner: Banners)
    }
}
