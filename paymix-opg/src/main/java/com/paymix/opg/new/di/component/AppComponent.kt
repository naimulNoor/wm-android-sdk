package com.walletmix.paymixbusiness.di.component

import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import com.walletmix.paymixbusiness.base.BaseApplication
import com.walletmix.paymixbusiness.di.module.ActivityBindingModule
import com.walletmix.paymixbusiness.di.module.ApplicationModule
import com.walletmix.paymixbusiness.di.module.NetworkModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    NetworkModule::class,
    ApplicationModule::class,
    ActivityBindingModule::class])

interface AppComponent {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: BaseApplication): Builder

        fun build(): AppComponent
    }

    fun inject(app: BaseApplication)
}