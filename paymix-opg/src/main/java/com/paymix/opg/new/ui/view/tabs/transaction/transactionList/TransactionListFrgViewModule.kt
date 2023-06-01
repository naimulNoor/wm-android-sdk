package com.walletmix.paymixbusiness.ui.view.tabs.transaction.transactionList

import dagger.Binds
import dagger.Module

@Module
abstract class TransactionListFrgViewModule {

    @Binds
    abstract fun provideView(activity: TransactionListFragment): TransactionListFrgContract.View
}