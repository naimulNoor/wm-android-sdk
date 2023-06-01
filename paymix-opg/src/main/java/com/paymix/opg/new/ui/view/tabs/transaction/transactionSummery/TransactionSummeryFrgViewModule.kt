package com.walletmix.paymixbusiness.ui.view.tabs.transaction.transactionSummery

import dagger.Binds
import dagger.Module

@Module
abstract class TransactionSummeryFrgViewModule {
    @Binds
    abstract fun provideView(activity: TransactionSummeryFragment): TransactionSummeryFrgContract.View
}
