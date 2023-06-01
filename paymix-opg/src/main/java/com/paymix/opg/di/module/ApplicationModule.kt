package com.paymix.opg.di.module

import android.content.Context
import androidx.navigation.Navigator
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import com.paymix.opg.base.BaseApplication
import com.paymix.opg.data.prefs.PreferenceManager
import com.paymix.opg.rx.AppSchedulerProvider
import com.paymix.opg.utils.*

@Module
class ApplicationModule {

    @Provides
    fun provideContext(baseApp: BaseApplication): Context {
        return baseApp
    }

    @Provides
    @Singleton
    fun providePreferenceManager(context: Context): PreferenceManager {
        return PreferenceManager(context)
    }

//    @Provides
//    @Singleton
//    fun providePermissionUtils(preferenceManager: PreferenceManager): PermissionUtils {
//        return PermissionUtils(preferenceManager)
//    }

    @Provides
    @Singleton
    fun provideAlertService(): MyAlertService {
        return MyAlertService()
    }

    @Provides
    @Singleton
    fun provideAppSchedule(): AppSchedulerProvider {
        return AppSchedulerProvider()
    }


    @Provides
    @Singleton
    fun provideAppLogger(): AppLogger {
        return AppLogger()
    }

    @Provides
    @Singleton
    fun provideNetworkUtils(): NetworkUtils {
        return NetworkUtils()
    }
}