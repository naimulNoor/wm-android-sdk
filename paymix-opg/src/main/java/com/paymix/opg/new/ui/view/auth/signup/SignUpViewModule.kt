package com.walletmix.paymixbusiness.ui.view.auth.signup
import dagger.Binds
import dagger.Module

@Module
abstract  class SignUpViewModule {
    @Binds
    abstract fun provideSignUpView(activity: SignUpActivity): SignUpContract.View
}