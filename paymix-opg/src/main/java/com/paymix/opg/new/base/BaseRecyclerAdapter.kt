package com.walletmix.paymixbusiness.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.walletmix.paymixbusiness.data.prefs.Keys
import com.walletmix.paymixbusiness.data.prefs.PreferenceManager
import com.walletmix.paymixbusiness.R

abstract class BaseRecyclerAdapter<D>(val context: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<BaseViewHolder>() {

    protected val TAG: String by lazy {
        this.javaClass.simpleName
    }

    val VIEW_TYPE_ITEM = 1
    val VIEW_TYPE_FOOTER = 2
    var mAppLanguage: String? = ""
    var mPrefs: PreferenceManager? = PreferenceManager(context)

    init {
        mAppLanguage = mPrefs?.getString(Keys.APP_LANGUAGE.name)
    }

    var dataList: ArrayList<D?> = ArrayList()
    protected var layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getItemViewType(position: Int): Int {
        return if (dataList[position] == null) VIEW_TYPE_FOOTER else VIEW_TYPE_ITEM
    }

    fun setData(dataList: ArrayList<D?>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    fun add(singleData: D?) {
        dataList.add(singleData)
        notifyItemInserted(dataList.size - 1)
    }

    fun addAll(dataList: ArrayList<D?>) {
        for (singleData in dataList) {
            add(singleData)
        }
    }

    fun getItem(position: Int): D? {
        return dataList[position]
    }

    fun addLoadingFooter() {
        dataList.add(null)
        notifyItemInserted(dataList.size - 1)
    }

    fun removeLoadingFooter() {
        if (dataList.isNotEmpty()) {
            dataList.removeAt(dataList.size - 1)
            notifyItemRemoved(dataList.size)
        }
    }

    fun remove(singleData: D?) {
        if (dataList.isNotEmpty()) {
            val position = dataList.indexOf(singleData)
            if (position > -1) {
                dataList.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }


    fun remove(position: Int) {
        if (dataList.isNotEmpty()) {
            dataList.removeAt(position)
            notifyItemRemoved(position)
        }
    }


    fun clearAdapter() {
        if (dataList.isNotEmpty()) {
            dataList.clear()
            notifyDataSetChanged()
        }
    }


    override fun getItemCount(): Int {
        return if (dataList.isEmpty()) 0 else dataList.size
    }


    class FooterViewHolder(itemView: View) : BaseViewHolder(itemView) {

        override fun bind(position: Int) {
            // Nothing to do...........
        }
    }

    fun getFooterViewHolder(viewGroup: ViewGroup): BaseViewHolder {
        return FooterViewHolder(
            layoutInflater.inflate(
                R.layout.item_view_loading_footer,
                viewGroup,
                false
            )
        )
    }

}