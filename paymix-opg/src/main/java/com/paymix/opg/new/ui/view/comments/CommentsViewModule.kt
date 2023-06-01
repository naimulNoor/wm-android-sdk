package com.walletmix.paymixbusiness.ui.view.comments

import dagger.Binds
import dagger.Module

@Module
abstract class CommentsViewModule {

    @Binds
    abstract fun provideDashboardView(activity: CommentsActivity): CommentsContract.View
}