package com.walletmix.paymixbusiness.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.walletmix.paymixbusiness.ui.view.tabs.transaction.transactionList.TransactionListFragment
import com.walletmix.paymixbusiness.ui.view.tabs.transaction.transactionSummery.TransactionSummeryFragment

class TransactionViewPagerFragmentAdapter (fragmentActivity: FragmentActivity, private var totalCount: Int) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return totalCount
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TransactionListFragment()
            1 -> TransactionSummeryFragment()
            else -> TransactionListFragment()
        }
    }

}