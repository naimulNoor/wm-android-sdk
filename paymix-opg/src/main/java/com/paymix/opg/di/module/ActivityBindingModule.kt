package com.paymix.opg.di.module


import com.paymix.opg.di.scope.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module(includes = [FragmentBindingModule::class])
abstract class ActivityBindingModule {

//    @ActivityScope
//    @ContributesAndroidInjector(modules = [SplashViewModule::class])
//    abstract fun bindEventSplashActivity(): SplashScreenActivity
//
//
//
//
//    @ActivityScope
//    @ContributesAndroidInjector(modules = [DashBoardViewModel::class])
//    abstract fun bindEventDashBoard(): DashBoardActivity

  
}