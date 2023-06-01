package com.walletmix.paymixbusiness.ui.view.tabs.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.walletmix.paymixbusiness.data.network.api_response.Banners
import com.walletmix.paymixbusiness.data.network.api_response.HomePageResponseModel
import com.walletmix.paymixbusiness.data.network.api_response.RecentComments
import com.walletmix.paymixbusiness.data.network.api_response.RecentTransactions
import com.walletmix.paymixbusiness.ui.adapter.RecentCommentsAdapter
import com.walletmix.paymixbusiness.ui.adapter.RecentTransactionAdapter
import com.walletmix.paymixbusiness.R
import com.walletmix.paymixbusiness.databinding.FragmentHomeBinding
import com.walletmix.paymixbusiness.base.BaseFragment
import com.walletmix.paymixbusiness.data.prefs.PrefKeys
import com.walletmix.paymixbusiness.ui.adapter.ImageSliderAdapter
import com.walletmix.paymixbusiness.ui.view.WebViewActivity
import com.walletmix.paymixbusiness.ui.view.tabs.home.HomeFrgContract
import com.walletmix.paymixbusiness.ui.view.tabs.home.HomeFrgPresenter
import com.walletmix.paymixbusiness.ui.view.tabs.transaction.TransactionFragment
import com.walletmix.paymixbusiness.utils.Keys.IntentKeys
import com.walletmix.paymixbusiness.utils.Navigator
import java.util.*


class HomeFragment : BaseFragment<HomeFrgPresenter>(), HomeFrgContract.View {

    // Image Slider
    private lateinit var sliderAdapter: ImageSliderAdapter
    private var images: ArrayList<Banners> = ArrayList()
    private var bannerImage:String = ""
    private var timer: Timer? = null
    private var currentImage = 0
    private val SLIDER_DELAY: Long = 5000.toLong()
    private val SLIDER_GAP: Long = 2500.toLong()
    private var isRegisteredSliderPageChangeListener: Boolean = false


    //////Recent Transaction
    private var recentTransactionList: ArrayList<RecentTransactions> = ArrayList()
    private lateinit var recentTransactionAdapter: RecentTransactionAdapter

    //////Recent Transaction
    private var recentCommnetsList = ArrayList<RecentComments>()
    private lateinit var recentCommentsAdapter: RecentCommentsAdapter


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }


    override fun getFragmentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        saveInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewReady(getArguments: Bundle?) {


        //refresh task

        binding.swipeRefreshLayout.setColorSchemeResources(R.color.orange)
        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshContent()
        }


        mPresenter.homePage()

        showTransactionLayout()


        //////Recent Transaction
        //recentTransaction()

        //////Recent Comments
        //recentComments()


        binding.recentTransaction.tvSeeAll.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragment_frame, TransactionFragment())
            transaction?.disallowAddToBackStack()
            transaction?.commit()



            val mBottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
            mBottomNavigationView.selectedItemId = R.id.bottom_nav_transaction

        }

//        binding.recentComment.tvCommentSeeAll.setOnClickListener {
//            Navigator.sharedInstance.navigate(context, CommentsActivity::class.java)
//        }


//        binding.text.setOnClickListener {
//
//            val calendarConstraintBuilder = CalendarConstraints.Builder()
//            calendarConstraintBuilder.setValidator(DateValidatorPointForward.now())
//
//
//            val datePicker = MaterialDatePicker.Builder.dateRangePicker()
//                .setCalendarConstraints(calendarConstraintBuilder.build())
//                //.setPositiveButtonText("Pick")
//                //.setSelection(Pair(
//                //MaterialDatePicker.thisMonthInUtcMilliseconds(),
//                //MaterialDatePicker.todayInUtcMilliseconds()
//                //))
//                .build()
//
//
//
//
//            // fragmentManager?.let { it1 -> datePicker.show(it1, "DatePicker") }
//
//            datePicker.show(
//                activity?.supportFragmentManager!!,"date_range_picker"
//            )
//
//            datePicker.addOnPositiveButtonClickListener {
//                context.showToast("${datePicker.headerText} is selected")
//
//
//
//                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
//                calendar.timeInMillis = it.first
//                val format = SimpleDateFormat("yyyy/MM/dd")
//                val formattedDate = format.format(calendar.time)
//
//                binding.text.text = formattedDate
//
//
//                Log.d("formattedFirstDate",formattedDate)
//            }
//
//            datePicker.addOnNegativeButtonClickListener {
//                context.showToast("${datePicker.headerText} is cancelled")
//            }
//
//            datePicker.addOnCancelListener {
//                context.showToast("Date Picker Cancelled")
//            }
//
//
//
//
//        }

    }


    private fun refreshContent() {
        binding.swipeRefreshLayout.isRefreshing = true
        if (mNetworkUtils.isConnectedToNetwork(context)) {
            mPresenter.homePage()
        } else {
            onNetworkUnavailable()
        }
    }

    override fun onNetworkCallStarted(loadingMessage: String) {
        if (!binding.swipeRefreshLayout.isRefreshing) super.onNetworkCallStarted(loadingMessage)
    }

    override fun onNetworkCallEnded() {
        if (binding.swipeRefreshLayout.isRefreshing) binding.swipeRefreshLayout.isRefreshing = false
        else
            super.onNetworkCallEnded()
    }



    //    private fun recentTransaction(){
//
//
//        val linearLayoutManager = LinearLayoutManager(context)
//        binding.recentTransaction.rvRecentTransaction.layoutManager = linearLayoutManager
//
//        var totalSuccessAmount = 0.00
//        var totalFailedAmount = 0.00
//        var totalCancelledAmount = 0.00
//        var totalAttemptAmount = 0.00
//
//        var totalSuccessTransaction = 0
//        var totalFailedTransaction = 0
//        var totalCancelledTransaction = 0
//        var totalAttemptTransaction = 0
//
//
//        lateinit var jsonString: String
//        try {
//            val inputStr :InputStream = context.assets.open("mock_api/homepage.json")
//            jsonString = inputStr.bufferedReader().use { it.readText() }.trimIndent()
//
//
//            var jsonObj = JSONObject(jsonString)
//            val jsonob = jsonObj.getJSONObject("data")
//            val jsonArr = jsonob.getJSONArray("recent_transaction")
//
//            Log.d("recent_transaction", jsonArr.toString())
//
//
//
//            for (i in 0 until (jsonArr.length())){
//
//                val jsonObject = jsonArr.getJSONObject(i)
//
//                Log.d("recent_trans_object", jsonArr.getJSONObject(i).toString())
//
//                val gson = Gson()
//                val trans = gson.fromJson(jsonObject.toString(), RecentTransactions::class.java)
////                Log.d("Transaction amount",trans.amount)
//
//
//
//                    val amount = trans.amount
//                    val amountDouble: Double? = amount.toDouble()
//
//                if (trans.status== "Success"){
//                    totalSuccessTransaction += 1
//
//                    if (amountDouble != null) {
//                        totalSuccessAmount += amountDouble
//                    }
//                }
//                else if (trans.status=="Failed"){
//                    totalFailedTransaction += 1
//
//                    if (amountDouble != null) {
//                        totalFailedAmount += amountDouble
//                    }
//                }
//                else if (trans.status=="Cancled"){
//                    totalCancelledTransaction += 1
//
//                    if (amountDouble != null) {
//                        totalCancelledAmount += amountDouble
//                    }
//                }
//                else if (trans.status=="Attempt"){
//                    totalAttemptTransaction += 1
//
//                    if (amountDouble != null) {
//                        totalAttemptAmount += amountDouble
//                    }
//                }
//
//
//                recentTransactionList.add(RecentTransactions(trans.id,trans.amount,trans.status))
//
//            }
//
//            binding.tranSummeryLayoutOne.tvSuccessAmount.text = totalSuccessAmount.toString()  ///success Amount
//            binding.tranSummeryLayoutTwo.tvSuccessAmount.text = totalFailedAmount.toString()  ///rejected Amount
//            binding.tranSummeryLayoutTree.tvSuccessAmount.text = totalCancelledAmount.toString() //canceled Amount
//            binding.tranSummeryLayoutFour.tvSuccessAmount.text = totalAttemptAmount.toString() // Attempt Amount
//
//            binding.tranSummeryLayoutOne.tvTotalTransaction.text = totalSuccessTransaction.toString()
//            binding.tranSummeryLayoutTwo.tvTotalTransaction.text = totalFailedTransaction.toString()
//            binding.tranSummeryLayoutTree.tvTotalTransaction.text = totalCancelledTransaction.toString()
//            binding.tranSummeryLayoutFour.tvTotalTransaction.text = totalAttemptTransaction.toString()
//
//
//            recentTransactionAdapter = RecentTransactionAdapter(context, recentTransactionList)
//            binding.recentTransaction.rvRecentTransaction.adapter = recentTransactionAdapter
//
//
//        } catch (ioException: IOException) {
//            Log.d("Exception",ioException.toString())
//        }
//    }
//    private fun recentComments() {
//        val linearLayoutManager = LinearLayoutManager(context)
//        binding.recentComment.rvRecentComments.layoutManager = linearLayoutManager
//
//
//        lateinit var jsonString: String
//        try {
//            val inputStr: InputStream = context.assets.open("mock_api/homepage.json")
//            jsonString = inputStr.bufferedReader().use { it.readText() }.trimIndent()
//
//
//            var jsonObj = JSONObject(jsonString)
//            val jsonob = jsonObj.getJSONObject("data")
//            val jsonArr = jsonob.getJSONArray("recent_comments")
//
//            Log.d("recent_comments", jsonArr.toString())
//
//
//
//            for (i in 0 until (jsonArr.length())) {
//
//                val jsonObject = jsonArr.getJSONObject(i)
//
//                Log.d("recent_comments_object", jsonArr.getJSONObject(i).toString())
//
//                val gson = Gson()
//                val trans = gson.fromJson(jsonObject.toString(), RecentComments::class.java)
//                Log.d("Comments message", trans.message)
//
//                recentCommnetsList.add(RecentComments(trans.id, trans.type, trans.message))
//
//            }
//
//            recentCommentsAdapter = RecentCommentsAdapter(context, recentCommnetsList)
//            binding.recentComment.rvRecentComments.adapter = recentCommentsAdapter
//
//
//        } catch (ioException: IOException) {
//            Log.d("Exception", ioException.toString())
//        }
//    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpImageSlider() {
        sliderAdapter = ImageSliderAdapter(context, images)
        //sliderAdapter.imageSliderClickListener = imageSliderCallback
        //roni's code
        //sliderAdapter.imageSliderLongClickListener = imageSliderLongCallback

        binding.homeBanner.slidePager.adapter = sliderAdapter
        if (images.size > 1) {
            try {
                binding.homeBanner.tabsDotIndicator.setupWithViewPager(
                    binding.homeBanner.slidePager,
                    true
                )
            } catch (e: Exception) {

            }

            binding.homeBanner.slidePager.addOnPageChangeListener(sliderImageChangeListener)
            //roni's code
            binding.homeBanner.slidePager.setOnTouchListener(sliderImageTouchListener)

            isRegisteredSliderPageChangeListener = true

            // Auto Change of Images
            scheduleImageSlider()
        } else {
            binding.homeBanner.tabsDotIndicator.visibility = View.GONE
        }
    }


    private fun scheduleImageSlider() {
        val handler = Handler(Looper.getMainLooper())
        val updateImageRunnable = Runnable {
            binding.homeBanner.slidePager.let {
                currentImage = if (currentImage == images.size) 0 else ++currentImage
                binding.homeBanner.slidePager.setCurrentItem(currentImage, true)
            }
        }
        timer?.cancel() // Cancelling if timer already exists...
        timer = Timer()
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                handler.post(updateImageRunnable)
            }
        }, SLIDER_DELAY, SLIDER_GAP)
    }

    private fun showTransactionLayout() {

        binding.payableAmountLayout.tvTitle.text = "Payable Amount"
        binding.payableAmountLayout.llTransaction.visibility = View.INVISIBLE


//        binding.imageView6.setOnClickListener {
//            if (binding.tranSummeryLayoutTree.root.isVisible) {
//                binding.tranSummeryLayoutTree.root.isVisible = false
//                binding.tranSummeryLayoutTwo.root.isVisible = false
//                binding.tranSummeryLayoutFour.root.isVisible = false
//
//                binding.imageView6.setImageResource(R.drawable.down_arrow)
//
//
//            } else {
//                binding.tranSummeryLayoutTree.root.isVisible = true
//                binding.tranSummeryLayoutTwo.root.isVisible = true
//                binding.tranSummeryLayoutFour.root.isVisible = true
//                binding.imageView6.setImageResource(R.drawable.up_arrow)
//
//                binding.tranSummeryLayoutTwo.tvTitle.text = "Rejected Amount"
//                binding.tranSummeryLayoutTree.tvTitle.text = "Cancelled Amount"
//                binding.tranSummeryLayoutFour.tvTitle.text = "Attempt Amount"
//
//
//            }
//        }
    }

    private val sliderImageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            currentImage = position
            scheduleImageSlider() // Rescheduling Image Slider....

        }

        override fun onPageScrollStateChanged(state: Int) {}


        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }
    }


    private var imageSliderCallback: ImageSliderAdapter.ImageSliderClickListener? =
        object : ImageSliderAdapter.ImageSliderClickListener {
            override fun handleTap(banner: Banners) {
                banner.let { tappedBanner ->

                    if (mNetworkUtils.isConnectedToNetwork(context)) {
                        val bundle = Bundle()
                        bundle.putString(IntentKeys.WEB_URL, tappedBanner.image)
                        Navigator.sharedInstance.navigateWithBundle(
                            context,
                            WebViewActivity::class.java,
                            IntentKeys.DATA_BUNDLE,
                            bundle
                        )
                    } else {
                        showErrorDialog(
                            title = "no internet connection",
                            message = "no internet_ msg",
                            titleFullRed = true
                        )
                    }


                }
            }
        }


    private val sliderImageTouchListener = object : View.OnTouchListener {
        override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
            when (p1?.action) {

                MotionEvent.ACTION_CANCEL -> {

                }

                MotionEvent.ACTION_UP -> {

                }

                MotionEvent.ACTION_MOVE -> {

                }

            }
            return false
        }

    }

    override fun homePageResponse(response: HomePageResponseModel) {
        mPrefManager.putObject(PrefKeys.HOME_BANNER, response)
        Log.d("Banner : ", response.toString())
        images = response.data?.banners!!

        sliderAdapter = ImageSliderAdapter(context, images)
//        binding.homeBanner.slidePager.adapter = sliderAdapter
//        binding.homeBanner.tabsDotIndicator.setupWithViewPager(binding.homeBanner.slidePager)
        setUpImageSlider()

        if (images.isEmpty()){
            binding.homeBanner.root.visibility = View.GONE
        }else{
            binding.homeBanner.root.visibility = View.VISIBLE
        }



        ///recent Transaction
        val linearLayoutManager = LinearLayoutManager(context)
        binding.recentTransaction.rvRecentTransaction.layoutManager = linearLayoutManager
        recentTransactionList = response.data!!.recentTransactions
        recentTransactionAdapter = RecentTransactionAdapter(context, recentTransactionList)
        binding.recentTransaction.rvRecentTransaction.adapter = recentTransactionAdapter

        val linearLayoutManager2 = LinearLayoutManager(context)
        binding.recentComment.rvRecentComments.layoutManager = linearLayoutManager2
        recentCommnetsList= response.data.recentComments
        recentCommentsAdapter = RecentCommentsAdapter(context, recentCommnetsList)
        binding.recentComment.rvRecentComments.adapter = recentCommentsAdapter






//        var totalSuccessAmount = 0.00
//        var totalFailedAmount = 0.00
//        var totalCancelledAmount = 0.00
//        var totalAttemptAmount = 0.00
//
//        var totalSuccessTransaction = 0
//        var totalFailedTransaction = 0
//        var totalCancelledTransaction = 0
//        var totalAttemptTransaction = 0
//
//        for (i in 0 until (response.data.recentTransaction.size)) {
//
//            val recentTransaction = recentTransactionList[i]
//
//            val amount = recentTransaction.bppBankAmount
//
//
//            if (recentTransaction.bppTransactionStatus == "Success") {
//                totalSuccessTransaction += 1
//                totalSuccessAmount += amount
//            } else if (recentTransaction.bppTransactionStatus == "Failed") {
//                totalFailedTransaction += 1
//                totalFailedAmount += amount
//
//            } else if (recentTransaction.bppTransactionStatus == "Cancled") {
//                totalCancelledTransaction += 1
//                totalCancelledAmount += amount
//
//            } else if (recentTransaction.bppTransactionStatus == "Attempt") {
//                totalAttemptTransaction += 1
//                totalAttemptAmount += amount
//            }
//        }

//        binding.tranSummeryLayoutOne.tvSuccessAmount.text = totalSuccessAmount.toString()  ///success Amount
//        binding.tranSummeryLayoutTwo.tvSuccessAmount.text = totalFailedAmount.toString()  ///rejected Amount
//        binding.tranSummeryLayoutTree.tvSuccessAmount.text = totalCancelledAmount.toString() //canceled Amount
//        binding.tranSummeryLayoutFour.tvSuccessAmount.text = totalAttemptAmount.toString() // Attempt Amount
//
//        binding.tranSummeryLayoutOne.tvTotalTransaction.text = totalSuccessTransaction.toString()
//        binding.tranSummeryLayoutTwo.tvTotalTransaction.text = totalFailedTransaction.toString()
//        binding.tranSummeryLayoutTree.tvTotalTransaction.text = totalCancelledTransaction.toString()
//        binding.tranSummeryLayoutFour.tvTotalTransaction.text = totalAttemptTransaction.toString()

        if (recentTransactionList.isEmpty()) {
            binding.recentTransaction.llRecentTransactionNoData.isVisible = true
            binding.recentTransaction.llRecentTransactionList.isVisible = false
        } else {
            binding.recentTransaction.llRecentTransactionNoData.isVisible = false
            binding.recentTransaction.llRecentTransactionList.isVisible = true

        }


        ///for recent comment
        val comments = response.data.recentComments

        if (comments.isEmpty()) {
            binding.recentComment.llRecentCommentsNoData.isVisible = true
            binding.recentComment.llRecentCommentsList.isVisible = false
        } else {
            binding.recentComment.llRecentCommentsNoData.isVisible = false
            binding.recentComment.llRecentCommentsList.isVisible = true

        }

        ///transaction Summery
        val successAmount = response.data!!.transactionSummery?.totalSuccessAmount
        val successCount = response.data!!.transactionSummery?.totalSuccessCount
        val payableAmount = response.data.transactionSummery?.payable

//        val attemptAmount = response.data!!.transactionSummery?.totalAttemptAmount
//        val attemptCount = response.data!!.transactionSummery?.totalAttemptCount
//
//        val canceledAmount = response.data!!.transactionSummery?.totalCanceledAmount
//        val canceledCount = response.data!!.transactionSummery?.totalCanceledCount
//
//        val rejectedAmount = response.data!!.transactionSummery?.totalRejectedAmount
//        val rejectedCount = response.data!!.transactionSummery?.totalRejectedCount

        binding.tranSummeryLayoutOne.tvSuccessAmount.text = successAmount  ///success Amount
        binding.payableAmountLayout.tvSuccessAmount.text = payableAmount
//        binding.tranSummeryLayoutTwo.tvSuccessAmount.text = rejectedAmount  ///rejected Amount
//        binding.tranSummeryLayoutTree.tvSuccessAmount.text = canceledAmount //canceled Amount
//        binding.tranSummeryLayoutFour.tvSuccessAmount.text = attemptAmount // Attempt Amount

        binding.tranSummeryLayoutOne.tvTotalTransaction.text = successCount.toString()

//        binding.tranSummeryLayoutTwo.tvTotalTransaction.text = rejectedCount.toString()
//        binding.tranSummeryLayoutTree.tvTotalTransaction.text = canceledCount.toString()
//        binding.tranSummeryLayoutFour.tvTotalTransaction.text = attemptCount.toString()


    }


}