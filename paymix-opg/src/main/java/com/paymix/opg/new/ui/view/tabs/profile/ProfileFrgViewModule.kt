package com.walletmix.paymixbusiness.ui.view.tabs.profile

import com.walletmix.paymixbusiness.ui.view.tabs.profile.ProfileFragment
import dagger.Binds
import dagger.Module

@Module
abstract class ProfileFrgViewModule {
    @Binds
    abstract fun provideView(activity: ProfileFragment):ProfileFrgContract.View
}