package com.walletmix.paymixbusiness.ui.view.verification

import dagger.Binds
import dagger.Module

@Module
abstract class VerificationViewModule {

    @Binds
    abstract fun provideVerificationView(activity: VerificationActivity): VerificationContract.View

}