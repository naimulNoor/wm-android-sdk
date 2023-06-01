package com.walletmix.paymixbusiness.di.module

import com.walletmix.paymixbusiness.ui.view.auth.verify_otp.VerifyOtpModule
import com.walletmix.paymixbusiness.ui.view.comments.CommentsActivity
import com.walletmix.paymixbusiness.ui.view.comments.CommentsViewModule
import com.walletmix.paymixbusiness.ui.view.verification.VerificationActivity
import com.walletmix.paymixbusiness.ui.view.verification.VerificationViewModule
import com.walletmix.paymixbusiness.di.scope.ActivityScope
import com.walletmix.paymixbusiness.ui.view.auth.forgotPassword.ForgetPasswordViewModule
import com.walletmix.paymixbusiness.ui.view.auth.forgotPassword.ForgotPasswordActivity
import com.walletmix.paymixbusiness.ui.view.auth.login.LoginActivity
import com.walletmix.paymixbusiness.ui.view.auth.login.LoginViewModule
import com.walletmix.paymixbusiness.ui.view.auth.resetPassword.ResetPasswordActivity
import com.walletmix.paymixbusiness.ui.view.auth.resetPassword.ResetPasswordViewModel
import com.walletmix.paymixbusiness.ui.view.auth.signup.SignUpActivity
import com.walletmix.paymixbusiness.ui.view.auth.signup.SignUpViewModule
import com.walletmix.paymixbusiness.ui.view.auth.verify_otp.VerifyOtpActivity
import com.walletmix.paymixbusiness.ui.view.dashboard.DashBoardActivity
import com.walletmix.paymixbusiness.ui.view.dashboard.DashBoardViewModel
import com.walletmix.paymixbusiness.ui.view.marchant.form.MerchantFormActivity
import com.walletmix.paymixbusiness.ui.view.merchant.form.MerchantFormModule

import com.walletmix.paymixbusiness.ui.view.splash.SplashScreenActivity
import com.walletmix.paymixbusiness.ui.view.splash.SplashViewModule
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module(includes = [FragmentBindingModule::class])
abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [SplashViewModule::class])
    abstract fun bindEventSplashActivity(): SplashScreenActivity




    @ActivityScope
    @ContributesAndroidInjector(modules = [DashBoardViewModel::class])
    abstract fun bindEventDashBoard(): DashBoardActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [LoginViewModule::class])
    abstract fun bindLoginActivity(): LoginActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [SignUpViewModule::class])
    abstract fun bindSignUpActivity(): SignUpActivity


    @ActivityScope
    @ContributesAndroidInjector(modules = [ForgetPasswordViewModule::class])
    abstract fun bindEventForgotPassword(): ForgotPasswordActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [ResetPasswordViewModel::class])
    abstract fun bindEventResetPassword(): ResetPasswordActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [MerchantFormModule::class])
    abstract fun bindMerchantFormActivity(): MerchantFormActivity


    @ActivityScope
    @ContributesAndroidInjector(modules = [VerifyOtpModule::class])
    abstract fun bindVerifyOtpActivity(): VerifyOtpActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [VerificationViewModule::class])
    abstract fun bindVerificationActivity(): VerificationActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [CommentsViewModule::class])
    abstract fun bindCommentsActivity(): CommentsActivity
}