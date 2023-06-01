package com.walletmix.paymixbusiness.di.module


import com.walletmix.paymixbusiness.service.paging.TransactionViewModel
import com.walletmix.paymixbusiness.ui.view.tabs.profile.ProfileFrgViewModule
import com.walletmix.paymixbusiness.ui.view.tabs.transaction.transactionList.TransactionListFragment
import com.walletmix.paymixbusiness.ui.view.tabs.transaction.transactionList.TransactionListFrgViewModule
import com.walletmix.paymixbusiness.ui.view.tabs.transaction.transactionSummery.TransactionSummeryFragment
import com.walletmix.paymixbusiness.ui.view.tabs.transaction.transactionSummery.TransactionSummeryFrgViewModule
import com.walletmix.paymixbusiness.di.scope.FragmentScope
import com.walletmix.paymixbusiness.ui.view.tabs.home.HomeFragment
import com.walletmix.paymixbusiness.ui.view.tabs.home.HomeFrgViewModule
import com.walletmix.paymixbusiness.ui.view.tabs.profile.ProfileFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {


    @FragmentScope
    @ContributesAndroidInjector(modules = [HomeFrgViewModule::class])
    abstract fun bindHomeFragment(): HomeFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [ProfileFrgViewModule::class])
    abstract fun bindProfileFragment(): ProfileFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [TransactionListFrgViewModule::class,TransactionViewModel::class])
    abstract fun bindTransactionListFragment(): TransactionListFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [TransactionSummeryFrgViewModule::class])
    abstract fun bindTransactionSummeryFragment(): TransactionSummeryFragment


}