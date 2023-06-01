package com.walletmix.paymixbusiness.ui.view.auth.resetPassword

import com.walletmix.paymixbusiness.ui.view.auth.resetPassword.ResetPasswordContract
import dagger.Binds
import dagger.Module

@Module
abstract  class ResetPasswordViewModel {
    @Binds
    abstract fun provideResetPasswordView(activity: ResetPasswordActivity): ResetPasswordContract.View
}