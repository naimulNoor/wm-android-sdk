package com.walletmix.paymixbusiness.ui.view.merchant.form

import com.walletmix.paymixbusiness.ui.view.marchant.form.MerchantFormActivity
import com.walletmix.paymixbusiness.ui.view.marchant.form.MerchantFormContract
import dagger.Binds
import dagger.Module

@Module
abstract class MerchantFormModule {
    @Binds
    abstract fun provideSplashView(activity: MerchantFormActivity): MerchantFormContract.View
}