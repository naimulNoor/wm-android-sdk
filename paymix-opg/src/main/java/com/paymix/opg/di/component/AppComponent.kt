package com.paymix.opg.di.component

import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import com.paymix.opg.base.BaseApplication
import com.paymix.opg.di.module.ActivityBindingModule
import com.paymix.opg.di.module.ApplicationModule
import com.paymix.opg.di.module.NetworkModule
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