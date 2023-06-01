package com.walletmix.paymixbusiness.ui.view.tabs.transaction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.walletmix.paymixbusiness.ui.adapter.TransactionViewPagerFragmentAdapter
import com.walletmix.paymixbusiness.R

class TransactionFragment : Fragment() {

    private lateinit var tabLayout : TabLayout
    private lateinit var viewPager : ViewPager2

    companion object {
        @JvmStatic
        fun newInstance() = TransactionFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager = view.findViewById(R.id.viewPager)

        setupViewPager()
        setupTabLayout()




    }

    private fun setupTabLayout() {
        TabLayoutMediator(
           tabLayout, viewPager
        )

        { tab, position ->
            if (position==0){
            tab.text = "Transaction list"
            }else if(position==1){
                tab.text = "Summery"
            }

        }.attach()
    }

    private fun setupViewPager() {
        val adapter = TransactionViewPagerFragmentAdapter(context as FragmentActivity, 2)
        viewPager.adapter = adapter
    }



}


