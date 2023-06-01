package com.walletmix.paymixbusiness.ui.view.tabs.home

import com.walletmix.paymixbusiness.ui.view.tabs.home.HomeFragment
import dagger.Binds
import dagger.Module

@Module
abstract class HomeFrgViewModule {
    @Binds
    abstract fun provideView(activity: HomeFragment): HomeFrgContract.View
}