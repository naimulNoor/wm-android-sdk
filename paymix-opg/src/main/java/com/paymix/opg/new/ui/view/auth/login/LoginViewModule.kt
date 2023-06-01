package com.walletmix.paymixbusiness.ui.view.auth.login


import dagger.Binds
import dagger.Module

@Module
abstract class LoginViewModule {

    @Binds
    abstract fun provideLoginView(activity: LoginActivity): LoginContract.View

}
