package com.walletmix.paymixbusiness.ui.view.dashboard

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.walletmix.paymixbusiness.data.network.api_response.merchant.GetMarchentResponse
import com.walletmix.paymixbusiness.base.BaseApplication
import com.walletmix.paymixbusiness.base.MvpBaseActivity
import com.walletmix.paymixbusiness.data.prefs.PrefKeys
import com.walletmix.paymixbusiness.ui.view.auth.login.LoginActivity

import com.walletmix.paymixbusiness.ui.view.tabs.home.HomeFragment
import com.walletmix.paymixbusiness.ui.view.tabs.profile.ProfileFragment
import com.walletmix.paymixbusiness.ui.view.tabs.transaction.TransactionFragment
import com.walletmix.paymixbusiness.utils.Keys.IntentKeys
import com.walletmix.paymixbusiness.utils.Navigator
import com.walletmix.paymixbusiness.utils.PermissionUtils
import com.walletmix.paymixbusiness.R
import com.walletmix.paymixbusiness.databinding.ActivityDashBoardBinding
import com.walletmix.paymixbusiness.ui.dialog.DialogProgress
import com.walletmix.paymixbusiness.ui.view.marchant.form.MerchantFormActivity
import com.walletmix.paymixbusiness.data.network.api_response.HomePageResponseModel
import com.walletmix.paymixbusiness.ui.view.auth.resetPassword.ResetPasswordActivity
import timber.log.Timber
import javax.inject.Inject


class DashBoardActivity  : MvpBaseActivity<DashBoardPresenter>(), DashBoardContract.View , PopupMenu.OnMenuItemClickListener,
    NavigationView.OnNavigationItemSelectedListener {
    val handler = Handler()


    var bundle = Bundle()

    @Inject
    lateinit var permissionUtils: PermissionUtils

    lateinit var binding: ActivityDashBoardBinding



    override fun getContentView(): View {
        /*  if (mPrefManager.getBoolean(PrefKeys.LOGGED_IN, false)) {
              Navigator.sharedInstance.navigate(getContext(), HomeActivity::class.java)
              finish()
          }*/

        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        val view = binding.root
        return view


    }

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {

        // Initial Fragment >> HOME
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_frame, HomeFragment.newInstance())
            .commit()

        // Setup Listener
        binding.bottomNavigation.setOnNavigationItemSelectedListener(ss)




        binding.appBar.ivLogout.setOnClickListener(View.OnClickListener { v ->
//            val popup = PopupMenu(this@DashBoardActivity, v)
//            popup.setOnMenuItemClickListener(this@DashBoardActivity)
//            popup.inflate(R.menu.menu_logout)
//            popup.show()

            binding.drawerLayout.openDrawer(GravityCompat.END)

        })

        apiCall()


        Timber.tag("Token").d(mPrefManager.getString("token"))
        mPrefManager.getString("token")


        binding.navView.setNavigationItemSelectedListener(this)

    }


    private fun apiCall() {
        mPresenter.homePage()
        mPresenter.getmerchantProfile()
    }


    private var onBtnLogOutTapped = View.OnClickListener {
        mPresenter.logout()
    }


    override fun onResume() {
        super.onResume()
    }


    private val ss = object : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            if (binding.bottomNavigation.selectedItemId == item.itemId) return false
            when (item.itemId) {
                R.id.bottom_nav_home -> loadFragment(HomeFragment.newInstance())
                R.id.bottom_nav_profile -> loadFragment(ProfileFragment.newInstance())
                R.id.bottom_nav_transaction -> loadFragment(TransactionFragment.newInstance())

            }
            return true
        }
    }


    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_frame, fragment)
            .commitAllowingStateLoss()
    }


    override fun logOutDidSucceed() {

        mPrefManager.putBoolean(PrefKeys.LOGGED_IN, false)
        mPrefManager.putBoolean(PrefKeys.IS_CHILD_SELECCTED, false)

        mPrefManager.put(PrefKeys.CHILD_TOKEN, "")
        mPrefManager.put(PrefKeys.TOKEN, "")
        val bundle = Bundle()
        bundle.putSerializable(IntentKeys.IS_COME_FROM_NOTIFICATION, false)
        BaseApplication.islogindta = false

        try {
            Navigator.sharedInstance.navigateWithBundle(
                getContext(),
                LoginActivity::class.java,
                IntentKeys.DATA_BUNDLE,
                bundle
            )
            finish()
            Timber.tag("logout-error").d("Success")
        }catch (e:java.lang.Exception) {
            Timber.tag("logout-error").d(e.toString())
        }



        //clear all preferences data
        //mPrefManager.clearPreference()

        //sync data
        //mPresenter.getsplashScrren()
    }

    override fun merchantProfileSuccess(response: GetMarchentResponse) {

        //set app bar data

        response.data.merchantDetails.merchant_name?.let { mAppLogger.logD(it) }
        val data: MutableMap<String, String> = mutableMapOf()
        data["app_bar_name"]=  response.data.merchantDetails.merchant_name.toString()

        data["app_bar_image"]= response.data.merchantDetails.logo.toString()
        mPrefManager.putObject(PrefKeys.APP_BAR_DATA,data)

        //set up app bar
        this.setAppBar(binding.appBar)

    }


    override fun homePageResponse(response: HomePageResponseModel) {
        Timber.tag("hello").d(response.data.toString())

        //set app bar data
//
//        mAppLogger.logD(response.data?.merchantDetails?.username.toString())
//        val data: MutableMap<String, String> = mutableMapOf()
//        data["app_bar_name"]= response.data?.merchantDetails?.username.toString()
//
//        data["app_bar_image"]= response.data?.merchantDetails?.profilePhoto.toString()
//        mPrefManager.putObject(PrefKeys.APP_BAR_DATA,data)
//
//        //set up app bar
//        this.setAppBar(binding.appBar)


        //show updateProfile dialog
        if (response.data.merchantDetails.isVerified == 0) {
            showProgressDialogInit(response.data.merchantDetails.username)
        }


        showProgressDialogInit(response.data.merchantDetails.username)


    }



    private fun showProgressDialogInit(name: String) {
        try {
            var dialogProgress: DialogProgress?
            dialogProgress = DialogProgress.newInstance(name)
            dialogProgress?.show(supportFragmentManager, DialogProgress.TAG)
            dialogProgress!!.dialogUpdateButtonTapped =
                object : DialogProgress.DialogProgressUpdateCallback {
                    override fun updateButtonTappd() {
                        Navigator.sharedInstance.navigate(
                            getContext(),
                            MerchantFormActivity::class.java
                        )
                        finish()
                    }
                }

        } catch (e: Exception) {
            mAppLogger.logD(e.toString())

        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item != null) {
            return when (item.itemId) {
                R.id.log_out -> {
                    mPresenter.logout()
                    true
                }
                R.id.reset_password -> {
                    Navigator.sharedInstance.navigate(this, ResetPasswordActivity::class.java)
                    true
                }
                else -> false
            }
        }
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.log_out -> {
                mPresenter.logout()

            }
            R.id.reset_password -> {
                Navigator.sharedInstance.navigate(this, ResetPasswordActivity::class.java)

            }



        }
        binding.drawerLayout.closeDrawer(GravityCompat.END)
        return true
    }


//    fun getCountryCode(context: Context): List<RecentTransaction> {
//
//        lateinit var jsonString: String
//        try {
//            jsonString = context.assets.open("mock_api/homepage.json")
//                .bufferedReader()
//                .use { it.readText() }
//        } catch (ioException: IOException) {
//            Log.d("Exception",ioException.toString())
//        }
//
//        val listCountryType = object : TypeToken<List<HomePageResponseModel.kt>>() {}.type
//        return Gson().fromJson(jsonString, listCountryType)
//    }


}
