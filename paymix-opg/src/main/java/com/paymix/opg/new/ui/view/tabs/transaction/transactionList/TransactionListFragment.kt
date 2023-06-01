package com.walletmix.paymixbusiness.ui.view.tabs.transaction.transactionList

import ProgressRequestBody
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.walletmix.paymixbusiness.file.FileUploader
import com.walletmix.paymixbusiness.data.network.api_response.merchant.MerchantUpdateResponse
import com.walletmix.paymixbusiness.data.network.api_response.transaction.TransactionData
import com.walletmix.paymixbusiness.data.network.api_response.transaction.TransactionDetailsResponseModel
import com.walletmix.paymixbusiness.data.network.api_response.transaction.TransactionResponseModel
import com.walletmix.paymixbusiness.service.paging.TransactionViewModel
import com.walletmix.paymixbusiness.ui.adapter.TransactionAdapter
import com.walletmix.paymixbusiness.ui.view.comments.CommentsActivity
import com.walletmix.paymixbusiness.utils.GetFile
import com.walletmix.paymixbusiness.utils.OnIntentReceived
import com.walletmix.paymixbusiness.utils.pagination.PaginationScrollListener
import com.walletmix.paymixbusiness.R
import com.walletmix.paymixbusiness.databinding.FragmentTransactionListBinding
import com.walletmix.paymixbusiness.base.BaseFragment
import com.walletmix.paymixbusiness.data.network.APIs
import com.walletmix.paymixbusiness.data.prefs.PrefKeys
import com.walletmix.paymixbusiness.ui.view.WebViewActivity
import com.walletmix.paymixbusiness.ui.view.tabs.transaction.TransactionFragment
import com.walletmix.paymixbusiness.utils.AppConstants
import com.walletmix.paymixbusiness.utils.AppUtils
import com.walletmix.paymixbusiness.utils.FileUtils
import com.walletmix.paymixbusiness.utils.ImageCompresser
import com.walletmix.paymixbusiness.utils.Keys.IntentKeys
import com.walletmix.paymixbusiness.utils.MyAlertService
import com.walletmix.paymixbusiness.utils.Navigator
import com.walletmix.paymixbusiness.utils.PermissionUtils
import com.walletmix.paymixbusiness.utils.showToast
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import javax.inject.Inject


class TransactionListFragment : BaseFragment<TransactionListFrgPresenter>(), TransactionListFrgContract.View ,SwipeRefreshLayout.OnRefreshListener {

    companion object {
        @JvmStatic
        fun newInstance() = TransactionFragment()

        var imageUri: Uri? = null
        private var imagePath: String? = null
    }


    private var transactionList = ArrayList<TransactionData>()
    private lateinit var transactionAdapter: TransactionAdapter


    //private lateinit var context : Context
    private var _binding: FragmentTransactionListBinding? = null
    private val binding get() = _binding!!


    lateinit var url: String
    var updateflag = false

    @Inject
    lateinit var permissionUtils: PermissionUtils

    private var GALLERY_PERMISSION = arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE)
    private var fileToUpload: File? = null
    private lateinit var fileUploader: FileUploader
    private var attachmentUri: Uri? = null
    private val mIntentListener: OnIntentReceived? = null
    val REQUEST_CODE = 101

    lateinit var transactionViewModel: TransactionViewModel

    lateinit var mBottomSheetDialog: BottomSheetDialog
    // var orderDetailsList = ArrayList<OrderDetails>()

    private lateinit var pdfUri: Uri


    private var fileName: Any? = "invoice.jpg"
    private var fileSize: String? = null

    var formattedDateSecond:String? =null
    var formattedDate:String? =null


    var attFileIcon:Any?=null
    var attFileName:Any?=null
    var attFileSize :Any?=null
    var attatchmentSection:Any?=null
    var doneButton:Any?=null



    private var selectedFileId: String? = null


    ////order
    var orderTxnId: String = ""
    var orderBankType: String = ""
    var orderRefId: String = ""
    var orderWebsite: String = ""
    var orderProgressCompletion: String = ""

    ///transaction details
    var merchent_Amount: String = ""
    var extra_Charge: String = ""
    var payment_Amount: String = ""
    var request_Ip: String = ""
    var payable_Amount: String = ""
    var release_Status: String = ""
    var Charge: String = ""

    var Position: Int = 0
    var Status: String = ""



    ///customer
    var customerName: String = ""
    var customerNumber: String = ""
    var customerAddress: String = ""
    var customerEmail: String = ""
    var customerAttemptTime: String = ""
    var customerTrxnTime: String = ""


    //file upload

    var key_name: String=""
    var uploadFile: Any?=""


    var selectedBank: String = ""
    var selectedTransaction: String = ""


    //pagination
    val PAGE_START = 1
    private var currentPage: Int = PAGE_START
    private var isLastPagedata = false
    private val totalPagedata = 10
    private var isLoadingdata = false
    var banks=HashMap<String,String>()
    var card=HashMap<String,String>()
    var itemCount = 0


    //filter

    var WMtrnxId:String=""
    var orderId:String=""
    var cardNumber:String=""
    var minAmount:String=""
    var maxAmount:String=""
    var dateRange:String?=null
    var filterBank:String?=null
    var paymentModule:String?=null
    var trnxID:String=""



    override fun getFragmentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        saveInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewReady(getArguments: Bundle?) {
        fileUploader = FileUploader(context)

        binding.swipeRefreshLayout.setOnRefreshListener(this)

        //get data from preferences
        card= mPrefManager.getStringHashMap(PrefKeys.APP_PAYMENT_MODULE)!!
        banks= mPrefManager.getStringHashMap(PrefKeys.APP_BANKS)!!
        Log.d("api-banks:::", banks.toString())
        Log.d("api-cards:::", card.toString())


        recycleViewAdapterSetup()
        mPresenter.TransactionList(1,"","","","","",null,"","","")



        binding.filter.setOnClickListener {
            TappedBottomSheet(3,"","",null,null)
        }

//        transactionAdapter = TransactionAdapter(context, transactionList)
//
//        binding.rvTransactions.setOnClickListener { interfaceCallback }
//        transactionAdapter.getTxnIdCallback = interfaceCallback

        //pagination
        /**
         * add scroll listener while user reach in bottom load more will call
         */






//        binding.rvTransactions.addOnScrollListener(object : PaginationScrollListener(mLayoutManager) {
//            protected fun loadMoreItems() {
//                isLoading = true
//                currentPage++
//                preparedListItem()
//                Log.d("api-call:::", currentPage.toString())
//            }
//
//            val isLastPage: Boolean
//            val isLoading: Boolean
//        })


    }

    private fun recycleViewAdapterSetup() {

        val linearLayoutManager = LinearLayoutManager(context)
        binding.rvTransactions.layoutManager = linearLayoutManager
        transactionAdapter = TransactionAdapter(context, ArrayList<TransactionData>())
        binding.rvTransactions.adapter = transactionAdapter

        transactionAdapter.mButtonSheetCallback = interfaceButtomSheetCallback!!
        transactionAdapter.getTxnIdCallback = interfaceGetTxnCallback

        binding.rvTransactions.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {

            override fun loadMoreItems() {
                isLoadingdata = true
                Log.d("api-call:::", currentPage.toString())
                currentPage += 1
                Log.d("api-call:::", currentPage.toString())

                mPresenter.TransactionList(currentPage,WMtrnxId,orderId,cardNumber,minAmount,maxAmount,dateRange,filterBank,paymentModule,trnxID)
            }

            override val isLastPage: Boolean
                get() = isLastPagedata
            override val isLoading: Boolean
                get() = isLoadingdata

        })
    }


    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {

            val uriFilePath = result.getUriFilePath(getContext()) // optional usage
            imagePath = uriFilePath
            imagePath?.let {
                mAppLogger.logD(it)

                fileToUpload = ImageCompresser.instance.getCompressedImageFile(it, getContext())
                if (imagePath != null) {
                    val file = File(imagePath)
                    if (file.exists())
                        file.delete()

                    fileName = file.path.substring(file.path.lastIndexOf("/")+1);
                    fileSize = java.lang.String.valueOf(file.length() / 1024)
                    val fileLength = file.length()
                    setfileNameAndSize(0)

                    Log.d("FileName", fileName as String)
                    Log.d("FileSize", fileLength.toString())

                }



                //binding.filter.setImageBitmap(getBitmapFromImagePath(it))



                Log.d("ImagePath",it)
                val x: Bitmap? = getBitmapFromImagePath(it)

                Log.d("attImagePath", x.toString())

                key_name="media"
                uploadFile=null

                //uploadFile(selectedFileId!!,"media",null)





            }


        } else {
            // An error occurred.
            val exception = result.error
        }

    }

    private fun setfileNameAndSize(i: Int) {
        (attatchmentSection as LinearLayout).visibility=View.VISIBLE
        (doneButton as Button).visibility=View.VISIBLE

        (attFileName as TextView).text=fileName as String
        (attFileSize as TextView).text=fileSize
        if(i==0){
            (attFileIcon as ImageView).setImageResource(R.drawable.img_placeholder)
        }else{
            (attFileIcon as ImageView).setImageResource(R.drawable.pdf_icon)
        }



    }


    /** Checking Camera Permission  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */
    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            getContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    /** Checking Storage Permission  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */
    private fun hasStoragePermission(): Boolean {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> true
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> ContextCompat.checkSelfPermission(
                getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED

            else -> true
        }
    }

    /** Checking Camera and Storage Permission  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */
    private fun hasCameraAndStoragePermission(): Boolean {
        return hasCameraPermission() && hasStoragePermission()
    }

    private fun manageCameraAndStoragePermission() {
        val storagePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val cameraPermission = Manifest.permission.CAMERA
        val listOfPermissionNeeded = java.util.ArrayList<String>()
        if (!hasCameraPermission()) {
            listOfPermissionNeeded.add(cameraPermission)
        }
        if (!hasStoragePermission()) {
            listOfPermissionNeeded.add(storagePermission)
        }
        mAlertService.showConfirmationAlert(getContext(),
            getString(R.string.permission_required),
            getString(R.string.permission_msg_for_camera),
            getString(R.string.button_not_now),
            getString(R.string.buttoin_continue),
            object : MyAlertService.AlertListener {

                override fun negativeBtnDidTapped() {}

                override fun positiveBtnDidTapped() {
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        listOfPermissionNeeded.toTypedArray(),
                        AppConstants.REQUEST_CAMERA
                    )
                }
            })

    }


    /** Creating Image File  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */
    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "IMG_$timeStamp"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            imageFileName,
            /** prefix */
            ".jpg",
            /** suffix */
            storageDir
            /** directory */
        ).apply {
            imagePath = absolutePath

        }

    }

    private fun takePicture() {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(context.packageManager) != null) {
            var imageFile: File? = null
            try {
                imageFile = createImageFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            imageFile?.let {
                val authorities: String = context.packageName + ".provider"
                /** Authorities That we provided in Manifest.xml*/
                imageUri = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    Uri.fromFile(imageFile)
                } else {
                    FileProvider.getUriForFile(getContext(), authorities, imageFile)
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(intent, AppConstants.LAUNCH_CAMERA)

                //set file name



            }
        }

    }


    private fun openGallery() {
        var cropImageOptions = CropImageOptions()
        cropImageOptions.imageSourceIncludeCamera = false
        cropImageOptions.imageSourceIncludeGallery = true

        cropImageOptions.fixAspectRatio = true
        cropImageOptions.aspectRatioX = 500
        cropImageOptions.aspectRatioY = 500

        var cropImageContractOptions =
            CropImageContractOptions(imageUri, cropImageOptions)
        cropImage.launch(cropImageContractOptions)
    }

    private fun manageGallery() {
        permissionUtils.checkPermission(
            getContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE,
            object : PermissionUtils.OnPermissionAskListener {
                override fun onNeedPermission() {
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        GALLERY_PERMISSION,
                        AppConstants.REQUEST_GALLERY
                    )
                }

                override fun onPermissionPreviouslyDenied() {
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        GALLERY_PERMISSION,
                        AppConstants.REQUEST_GALLERY
                    )
                }

                override fun onPermissionPreviouslyDeniedWithNeverAskAgain() {
                    mAlertService.showConfirmationAlert(context,
                        getString(R.string.permission_required),
                        getString(R.string.permission_storage_denied_one),
                        getString(R.string.button_not_now),
                        getString(R.string.okay),
                        object : MyAlertService.AlertListener {

                            override fun negativeBtnDidTapped() {}

                            override fun positiveBtnDidTapped() {
                                AppUtils.shared.goToSettings(getContext())
                            }
                        })

                }

                override fun onPermissionGranted() {
                    openGallery()
                }

            })

    }

    @SuppressLint("CheckResult")
    fun uploadFile() {

        mAppLogger.logD("file-attachment")
        if (key_name == "invoice") {
            try {
                val videoPart = ProgressRequestBody(fileToUpload!!)
                videoPart.getProgressSubject()
                    .subscribeOn(Schedulers.io())
                    .subscribe { percentage ->
                        Log.i("PROGRESS", "${percentage}%")
                    }

                var getFile: GetFile? = GetFile()
                var myFile = getFile!!.getFile(getContext(), uploadFile as Uri)
                var fileDate = FileUtils.sharedInstance.createMultipartBodyFromFile(key_name, myFile)

                mPresenter.UploadInvoice(
                    selectedFileId!!,
                    fileDate
                )



            } catch (e: Exception) {
                mAppLogger.logD("file-attachment:error")
                mAppLogger.logE(e.toString())
            }

        } else {
            mAppLogger.logD("file-attachment:image")
            //for image upload
            if (fileToUpload != null) {
                try {
                    val videoPart = ProgressRequestBody(fileToUpload!!)
                    videoPart.getProgressSubject()
                        .subscribeOn(Schedulers.io())
                        .subscribe { percentage ->
                            Log.i("PROGRESS", "${percentage}%")
                        }

                    mPresenter.UploadInvoice(
                        selectedFileId!!,
                        fileUploader.getMultipartBodyPartFromFile("invoice", fileToUpload)
                    )
                } catch (e: Exception) {
                    mAppLogger.logD("file-attachment:error")
                    mAppLogger.logE(e.toString())
                }

            }
        }


    }

    private fun getBitmapFromImagePath(imagePath: String?): Bitmap? {
        imagePath?.let {
            val imageFile = File(imagePath)
            if (imageFile.exists()) {
                val myBitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
                val ei = androidx.exifinterface.media.ExifInterface(imageFile.absolutePath)
                val orientation = ei.getAttributeInt(
                    androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION,
                    androidx.exifinterface.media.ExifInterface.ORIENTATION_UNDEFINED
                )
                val rotatedBitmap: Bitmap? = when (orientation) {
                    androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_90 -> TransformationUtils.rotateImage(
                        myBitmap,
                        90
                    )
                    androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_180 -> TransformationUtils.rotateImage(
                        myBitmap,
                        180
                    )
                    androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_270 -> TransformationUtils.rotateImage(
                        myBitmap,
                        270
                    )
                    androidx.exifinterface.media.ExifInterface.ORIENTATION_NORMAL -> myBitmap
                    else -> myBitmap
                }
                return rotatedBitmap
            }
            return null
        }
        return null
    }


    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // Initialize result data
            val data: Intent? = result.data

            // check condition
            if (data != null) {
                val sUri: Uri? = data.data
                val id: String? = data.dataString
                fileToUpload = File(sUri!!.path!!)
                Log.d("upload-file", fileToUpload!!.name)

                //set file name and size
                fileName= fileToUpload!!.path.substring(fileToUpload!!.path.lastIndexOf("/")+1);
                fileSize=Integer.parseInt((fileToUpload!!.length()/1024).toString()).toString()
                setfileNameAndSize(1)




            }
        }

    private fun selectPdf() {
        val pdfIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        pdfIntent.type = "application/pdf"
        //pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
        resultLauncher.launch(pdfIntent)
        //startActivityForResult(pdfIntent, 12)
    }

    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, getIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, getIntent)

        // For loading Image
        if (resultCode != AppCompatActivity.RESULT_CANCELED) {
            when (requestCode) {
                AppConstants.LAUNCH_CAMERA -> {
                    mAppLogger.logD("launch camera")
                    mAppLogger.logD(imagePath.toString())
                    try {

                        var cropImageOptions = CropImageOptions()
                        cropImageOptions.imageSourceIncludeGallery = false
                        cropImageOptions.imageSourceIncludeCamera = true
                        cropImageOptions.fixAspectRatio = true
                        cropImageOptions.aspectRatioX = 500
                        cropImageOptions.aspectRatioY = 500

                        var cropImageContractOptions =
                            CropImageContractOptions(imageUri, cropImageOptions)
                        cropImage.launch(cropImageContractOptions)


                    } catch (e: Exception) {
                        mAppLogger.logE(e.toString())
                        e.printStackTrace()
                    }

                }

                AppConstants.LAUNCH_GALLERY -> if (resultCode == Activity.RESULT_OK && getIntent != null) {

                    if (getIntent != null && getIntent.data != null) {
                        val imageSelected = getIntent.data
                        try {
                            val realImagePath = fileUploader.getRealPathFromUri(imageSelected!!)
                            if (realImagePath != null) {


                                var cropImageOptions = CropImageOptions()
                                cropImageOptions.imageSourceIncludeCamera = false
                                cropImageOptions.imageSourceIncludeGallery = true

                                cropImageOptions.fixAspectRatio = true
                                cropImageOptions.aspectRatioX = 500
                                cropImageOptions.aspectRatioY = 500

                                var cropImageContractOptions = CropImageContractOptions(
                                    imageUri, cropImageOptions
                                )
                                cropImage.launch(cropImageContractOptions)

//                                fileToUpload = ImageCompresser.instance.getCompressedImageFile(
//                                        realImagePath,
//                                        getContext()
//                                    )
//                                val bitmap = BitmapFactory.decodeFile(fileToUpload!!.absolutePath)
//                                binding.documentLayout.ivProfileLogo.setImageURI(imageSelected)

                            }
                        } catch (e: Exception) {
                            Log.e("LOG .....", "${e.localizedMessage}")
                        }
                    }


                }

                AppConstants.LAUNCH_PDF -> {

                }
            }
        }

        if (requestCode == REQUEST_CODE) {

//            pdfUri = getIntent?.data!!
//            val uri: Uri = getIntent?.data!!
//            val uriString: String = uri.toString()
//            var pdfName: String? = null
//            if (uriString.startsWith("content://")) {
//                var myCursor: Cursor? = null
//                try {
//                    myCursor = requireContext().contentResolver.query(uri, null, null, null, null)
//                    if (myCursor != null && myCursor.moveToFirst()) {
//                        pdfName =
//                            myCursor.getString(myCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
//                        //pdfTextView.text = pdfName
//                    }
//                } finally {
//                    myCursor?.close()
//                }
//            }


        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            AppConstants.REQUEST_CAMERA ->
                if (grantResults.isNotEmpty()) {
                    var isBothPermissionGranted = true
                    for (i in permissions.indices) {
                        if (permissionUtils.shouldAskPermission(getContext(), permissions[i])) {
                            isBothPermissionGranted = false
                            break
                        }
                    }
                    if (isBothPermissionGranted) {
                        takePicture()
                    } else {
                        manageCameraAndStoragePermission()
                    }
                }

            AppConstants.REQUEST_GALLERY -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            }
        }

    }


    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun TappedBottomSheet(
        item: Int,
        id: String,
        status: String,
        position: Int?,
        transaction: TransactionData?
    ) {
        val layoutInflater = LayoutInflater.from(context)

        mBottomSheetDialog = context?.let { BottomSheetDialog(it) }!!
        var sheetview: View? = null


        //view details section
        if (item == 1) {

            mPresenter.TransactionDetails(id)
            Status=status
            Position=position!!



        }
        //upload invoice section
        else if (item == 2) {
            //quick service expand
//            if (mBehavior!!.state == BottomSheetBehavior.STATE_EXPANDED) {
//                mBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
//            }


            sheetview = layoutInflater.inflate(R.layout.bottom_sheet_upload_invoice_layout, null)

            val camera = sheetview.findViewById<LinearLayout>(R.id.ll_camera)
            val media = sheetview.findViewById<LinearLayout>(R.id.ll_media)
             attFileIcon = sheetview.findViewById<ImageView>(R.id.iv_attachment_image)
             attFileName = sheetview.findViewById<TextView>(R.id.tv_attachment_file_name)
             attFileSize = sheetview.findViewById<TextView>(R.id.tv_attachment_file_size)
            val delete = sheetview.findViewById<ImageView>(R.id.iv_attachment_file_delete)
             doneButton = sheetview.findViewById<Button>(R.id.btn_done)
             attatchmentSection = sheetview.findViewById<LinearLayout>(R.id.attachments_section)
            val trnxId = sheetview.findViewById<TextView>(R.id.tranx_id)


            Log.d("In Bottom fileName", fileName as String)
            trnxId.text="WMX Txn ID - ${transaction!!.bppWmxTxnID}"

            selectedFileId= transaction.bppWmxTxnID.toString()


//            val x: Bitmap? = getBitmapFromImagePath(imagePath)
//
//            Log.d("attImagePath", x.toString())


            //uploadFile("profile_photo", null)


            camera.setOnClickListener {
                //mCallback?.onHandleSelection("Camera")

                if (hasCameraAndStoragePermission()) {
                    takePicture()
                } else {
                    manageCameraAndStoragePermission()
                }
            }
            media.setOnClickListener {

                // mCallback?.onHandleSelection("Media")
                // manageGallery()
                selectPdf()


//                mPresenter.UploadInvoice(
//                    id,
//                    fileUploader.getMultipartBodyPartFromFile("invoice", fileToUpload)
//                )

            }
            delete.setOnClickListener {

                (attatchmentSection as LinearLayout).visibility=View.GONE
            }
            (doneButton as Button?)!!.setOnClickListener {
                uploadFile()
            }


            val arrowButton: ImageView =
                sheetview.findViewById<ImageView>(R.id.iv_close)

            arrowButton.setOnClickListener { mBottomSheetDialog.cancel() }

            if (sheetview != null) {
                mBottomSheetDialog!!.setContentView(sheetview)
            }
            mBottomSheetDialog!!.window!!.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            mBottomSheetDialog.setCancelable(false)
            mBottomSheetDialog!!.show()

        }
        else if (item == 3) {
            //quick service expand
//            if (mBehavior!!.state == BottomSheetBehavior.STATE_EXPANDED) {
//                mBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
//            }

            sheetview = layoutInflater.inflate(R.layout.bottom_sheet_transaction_filter_layout, null)
            val arrowButton: ImageView = sheetview.findViewById<ImageView>(R.id.iv_close)



            val txnId = sheetview.findViewById<EditText>(R.id.et_txn_id)
            val orderIdet = sheetview.findViewById<EditText>(R.id.et_order_id)
            val cardNumberet = sheetview.findViewById<EditText>(R.id.et_card_number)
            val amountMax = sheetview.findViewById<EditText>(R.id.et_amount_max)
            val dateRangetv = sheetview.findViewById<TextView>(R.id.tv_date_range)
            val amountMin = sheetview.findViewById<EditText>(R.id.et_amount_min)
            val transactionId = sheetview.findViewById<EditText>(R.id.et_transaction_id)
            val submitButton = sheetview.findViewById<Button>(R.id.btn_submit)


            val selectBank = sheetview.findViewById<Spinner>(R.id.tv_select_bank)
            val selectPaymentModule = sheetview.findViewById<Spinner>(R.id.tv_select_transaction)




            var bankfilterList = ArrayList<String>()
            bankfilterList.add("Select Bank")
            for( item in banks.toList()){
                bankfilterList.add(item.second)
            }

            var cardfilterList = ArrayList<String>()
            cardfilterList.add("Select Payment Module")
            for( item in card.toList()){
                cardfilterList.add(item.second)
            }



            if (selectBank != null) {
                val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, bankfilterList.toList())
                selectBank.adapter = adapter

                selectBank.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        selectedBank =bankfilterList[position]
//
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {

                    }
                }
            }

            if (selectPaymentModule != null) {
                val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, cardfilterList)
                selectPaymentModule.adapter = adapter

                selectPaymentModule.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
//                        Toast.makeText(context,
//                            getString(R.string.selected_item) + " " +
//                                    "" + languages[position], Toast.LENGTH_SHORT).show()

                        selectedTransaction =cardfilterList[position].toString()
//                        context.showToast(selectedTransaction)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        // write code to perform some action
                    }
                }
            }



                dateRangetv.setOnClickListener {

                    val calendarConstraintBuilder = CalendarConstraints.Builder()

                    // set the validator point forward from june
                    // this mean the all the dates before the June month
                    // are blocked
                    calendarConstraintBuilder.setValidator(DateValidatorPointForward.now())

                    val datePicker = MaterialDatePicker.Builder.dateRangePicker()
                        //.setPositiveButtonText("Pick")
                        //.setSelection(Pair(
                        //MaterialDatePicker.thisMonthInUtcMilliseconds(),
                       //MaterialDatePicker.todayInUtcMilliseconds()
                       //))
                        .build()




                    // fragmentManager?.let { it1 -> datePicker.show(it1, "DatePicker") }

                    datePicker.show(activity?.supportFragmentManager!!,"date_range_picker")

                    // Setting up the event for when ok is clicked
                    datePicker.addOnPositiveButtonClickListener { context.showToast("${datePicker.headerText} is selected")



                        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                        calendar.timeInMillis = it.first
                        val format = SimpleDateFormat("yyyy-MM-dd")
                         formattedDate = format.format(calendar.time)

                        val calenderSecond = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                        calenderSecond.timeInMillis = it.second
                        val formatSecond = SimpleDateFormat("yyyy-MM-dd")
                         formattedDateSecond = formatSecond.format(calenderSecond.time)
                        dateRangetv.text = "$formattedDate - $formattedDateSecond"
                        dateRangetv.text


                        Log.d("formattedFirstDate",formattedDate!!)
                    }

                    datePicker.addOnNegativeButtonClickListener {
                        context.showToast("${datePicker.headerText} is cancelled")
                    }

                    datePicker.addOnCancelListener {
                        context.showToast("Cancelled")
                    }
                }




            submitButton.setOnClickListener {

                WMtrnxId= txnId.text.toString()
                orderId = orderIdet.text.toString()
                cardNumber = cardNumberet.text.toString()
                minAmount = amountMin.text.toString()
                dateRange = dateRangetv.text.toString()
                maxAmount = amountMax.text.toString()
                filterBank = transactionId.text.toString()
                filterBank = selectedBank
                paymentModule = selectedTransaction

                if(dateRange==""){
                    dateRange=null
                }else{
                    dateRange= formattedDate.plus(" ").plus("-").plus(" ").plus(formattedDateSecond)
                    //dateRange=URLEncoder.encode(dateRange, "UTF-8")
                    //dateRange=URLEncoder.encode(dateRange)
                }
                if(filterBank=="Select Bank"){
                    filterBank=null
                }else{
                    var data=banks.filter {
                        it.value==filterBank
                    }
                    filterBank=data.keys.first().toString()

                }
                if(paymentModule=="Select Payment Module"){
                    paymentModule=null
                }else{
                    var data=card.filter {
                        it.value==paymentModule
                    }
                    paymentModule=data.keys.first().toString()
                }

                currentPage=PAGE_START
                isLastPagedata=false
                transactionAdapter.clear()

                mPresenter.TransactionList(currentPage,WMtrnxId,orderId,cardNumber,minAmount,maxAmount,dateRange,filterBank,paymentModule,trnxID)
                if (mBottomSheetDialog.isShowing){
                    mBottomSheetDialog.dismiss()
                }



            }

            




            arrowButton.setOnClickListener { mBottomSheetDialog.cancel() }

            if (sheetview != null) {
                mBottomSheetDialog!!.setContentView(sheetview)
            }
            mBottomSheetDialog!!.window!!.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            mBottomSheetDialog.setCancelable(false)
            mBottomSheetDialog!!.show()

        }
        else if (item == 4){

//            val intent = Intent(context, CommentsActivity::class.java)
//            intent.putExtra("TXN_ID", id)
//            startActivity(intent)
//
//            mPrefManager.put(PrefKeys.TXN_ID, id)
//            Navigator.sharedInstance.navigateWithBundle(context, CommentsActivity::class.java)

            val bundle = Bundle()
            bundle.putString(PrefKeys.TXN_ID, id)

            Navigator.sharedInstance.navigateWithBundle(
                context,
                CommentsActivity::class.java,
                IntentKeys.DATA_BUNDLE,
                bundle
            )
        }
        else if (item == 5){
            //call webview
            if (mNetworkUtils.isConnectedToNetwork(context)) {
                var url = APIs.VIEW_INVOICE+transaction!!.bppWmxTxnID
                val bundle = Bundle()
                bundle.putString(IntentKeys.WEB_URL, url)
                bundle.putString(IntentKeys.TITLE, "Invoice Details")
                bundle.putBoolean(IntentKeys.IS_AUTH_NEEDED,true)
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


    private var interfaceButtomSheetCallback: TransactionAdapter.CallbackBottomSheetInterface? =
        object : TransactionAdapter.CallbackBottomSheetInterface {
            override fun onHandleSelection(
                type: Int,
                id: String,
                status: String,
                position: Int,
                transaction: TransactionData
            ) {
                type.let { tappedIntent ->

                    if (mNetworkUtils.isConnectedToNetwork(context)) {

                        TappedBottomSheet(type, id,status,position,transaction)


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

    private var interfaceGetTxnCallback: TransactionAdapter.CallbackGetTxnId? =
        object : TransactionAdapter.CallbackGetTxnId {
            override fun getTxnId(txnId: String) {
                txnId.let { tappedIntent ->

                    if (mNetworkUtils.isConnectedToNetwork(context)) {

                        //mPresenter.TransactionDetails(txnId)

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




    override fun TransactionListResponse(response: TransactionResponseModel) {

        binding.swipeRefreshLayout.isRefreshing=false
        Log.d("tnx-response",response.toString())
        transactionList = response.data.data



        if (transactionList.isEmpty()) {

            binding.llTransactionList.isVisible = false
            binding.llNoDataFound.isVisible = true
            binding.filter.setImageResource(R.drawable.filter_black_white)
            //binding.filter.isClickable = false

        } else {
            binding.llTransactionList.isVisible = true
            binding.llNoDataFound.isVisible = false
            binding.filter.setImageResource(R.drawable.filter)
            binding.filter.isClickable = true
        }

        if (currentPage != PAGE_START) {
            transactionAdapter.removeLoading()
        }

        transactionAdapter.addAll(transactionList)

        if (response.data.nextPageURL!=null) {
            transactionAdapter.addLoading()
        }else{
            isLastPagedata=true
        }
        isLoadingdata = false




            //mPresenter.TransactionDetails("61")
            //mPresenter.TransactionDetails(orderId)

            //context.showToast("Transaction List Response Method in called")

    }

    @SuppressLint("SetTextI18n")
    override fun TransactionDetailsResponse(response: TransactionDetailsResponseModel) {

        //context.showToast("Transaction Details Response Method in called")
        //context.showToast("TransactionDetailsResponse is Called")


        response.data?.ippCustomerDetails?.let { Log.d("show customer details :", it) }
        response.data?.ippCustomerDetails
        response.data?.ippCsutomerDetails?.customerState


        ///for order details
        orderTxnId = response.data?.bppWmxTxnId.toString()
        orderBankType = response.data?.bppBank.toString()
        orderRefId = response.data?.bppRefId.toString()
        orderWebsite = response.data?.mUrl.toString()
        orderProgressCompletion = response.data?.bppServiceRatio.toString()


        ///transaction Details

//        response.data?.bppIsChecked.toString()
        merchent_Amount = response.data?.bppMerchantAmount.toString()
        extra_Charge = response.data?.bppExtraCharge.toString()
        payment_Amount = response.data?.bppBankAmount.toString()
        request_Ip = response.data?.bppRequestIp.toString()
        payable_Amount = response.data?.bppIsChecked.toString()
        release_Status = response.data?.bppIsPaid.toString()
        Charge = response.data?.bppWmxCharge.toString()


        //////customer
        customerName = response.data?.ippCsutomerDetails?.customerName.toString()
        customerNumber = response.data?.ippCsutomerDetails?.customerPhone.toString()
        customerEmail = response.data?.ippCsutomerDetails?.customerEmail.toString()
        customerAddress = response.data?.ippCsutomerDetails?.customerAdd + "," + response.data?.ippCsutomerDetails?.customerCity + ",Postcode" + response.data?.ippCsutomerDetails?.customerPostcode + "," + response.data?.ippCsutomerDetails?.customerCountry
        customerAttemptTime = response.data?.ippAttemptTime.toString()
        customerTrxnTime = response.data?.bppTxnTime.toString()

        response.data?.ippCsutomerDetails?.customerEmail?.let { Log.d("Customer Email :", it) }

        var sheetview: View? = null

        sheetview = layoutInflater.inflate(R.layout.bottom_sheet_dailog_layout, null)
        val statusTv = sheetview.findViewById<TextView>(R.id.tv_status)
        val positionTv = sheetview.findViewById<TextView>(R.id.tv_position)


        var transaction_status = ""

        if (Status == "1"){
            transaction_status = "Success"
        }
        else if (Status == "2"){
            transaction_status = "Rejected"
        }
        else if (Status == "3"){
            transaction_status = "Canceled"
        }
        else if (Status == "4"){
            transaction_status = "Refund"
        }else{
            transaction_status = "Attempt"
        }


        when (Status) {
            "4" -> {
                statusTv.setTextColor(Color.parseColor("#FF741E"))
            }
            null -> {
                statusTv.setTextColor(Color.parseColor("#FF741E"))
            }
            "3" -> {
                statusTv.setTextColor(Color.parseColor("#DC0808"))
            }
            "2" -> {
                statusTv.setTextColor(Color.parseColor("#DC0808"))
            }
            "1" -> {
                statusTv.setTextColor(Color.parseColor("#18A24F"))
            }
        }


        statusTv.text = transaction_status

        var positionText : String = ""

        positionText = if (Position<10){
            "(SL #0$Position)"
        }else{
            "(SL #$Position)"
        }

        positionTv.text = positionText


        ///////for order details
        val txnId = sheetview.findViewById<TextView>(R.id.tv_txn_id)
        val bankType = sheetview.findViewById<TextView>(R.id.tv_bank_type)
        val refId = sheetview.findViewById<TextView>(R.id.tv_ref_id)
        val website = sheetview.findViewById<TextView>(R.id.tv_website)
        val progressCompletion = sheetview.findViewById<TextView>(R.id.tv_progress_completion)

        txnId.text = orderTxnId
        bankType.text = orderBankType
        refId.text = orderRefId
        website.text = orderWebsite
        progressCompletion.text = orderProgressCompletion

        ///transaction Details
        val merchentAmount = sheetview.findViewById<TextView>(R.id.tv_merchent_amount)
        val extraCharge = sheetview.findViewById<TextView>(R.id.tv_extra_charge)
        val paymentAmount = sheetview.findViewById<TextView>(R.id.tv_payment_amount)
        val requestIp = sheetview.findViewById<TextView>(R.id.tv_request_ip)
        val payableAmount = sheetview.findViewById<TextView>(R.id.tv_payable_amount)
        val releaseStatus = sheetview.findViewById<TextView>(R.id.tv_release_status)
        val charge = sheetview.findViewById<TextView>(R.id.tv_charge)

        merchentAmount.text = merchent_Amount
        extraCharge.text = extra_Charge
        paymentAmount.text = payment_Amount
        requestIp.text = request_Ip
        payableAmount.text = payable_Amount
        releaseStatus.text = release_Status
        charge.text = Charge


        ///customers Details
        val name = sheetview.findViewById<TextView>(R.id.tv_name)
        val number = sheetview.findViewById<TextView>(R.id.tv_number)
        val email = sheetview.findViewById<TextView>(R.id.tv_email)
        val address = sheetview.findViewById<TextView>(R.id.tv_address)
        val attemptTime = sheetview.findViewById<TextView>(R.id.tv_attempt_time)
        val trxnTime = sheetview.findViewById<TextView>(R.id.tv_trxn_time)

        name.text = customerName
        number.text = customerNumber
        email.text = customerEmail
        address.text = customerAddress
        attemptTime.text = customerAttemptTime
        trxnTime.text = customerTrxnTime
        val arrowButton: ImageView = sheetview.findViewById<ImageView>(R.id.iv_close)
        arrowButton.setOnClickListener { mBottomSheetDialog.cancel() }






        if (sheetview != null) {
            mBottomSheetDialog.setContentView(sheetview)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog!!.window!!.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        mBottomSheetDialog.setCancelable(false)
        mBottomSheetDialog!!.show()


    }

    override fun UploadInvoiceResponse(response: MerchantUpdateResponse) {
        mBottomSheetDialog.cancel()
        showSuccessDialog(title = "Success",message = "Invoice Upload Successfully")

    }

    override fun onRefresh() {
        currentPage=PAGE_START
        isLastPagedata=false
        transactionAdapter.clear()
        mPresenter.TransactionList(currentPage,WMtrnxId,orderId,cardNumber,minAmount,maxAmount,dateRange,filterBank,paymentModule,trnxID)

    }

}
