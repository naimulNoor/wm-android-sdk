package com.walletmix.paymixbusiness.ui.view.dashboard

import dagger.Binds
import dagger.Module

@Module
abstract class DashBoardViewModel {
    @Binds
    abstract fun provideCommentsView(activity: DashBoardActivity): DashBoardContract.View
}