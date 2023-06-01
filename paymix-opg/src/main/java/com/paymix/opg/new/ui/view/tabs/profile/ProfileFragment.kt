package com.walletmix.paymixbusiness.ui.view.tabs.profile

import ProgressRequestBody
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.github.chrisbanes.photoview.PhotoView
import com.walletmix.paymixbusiness.file.FileUploader
import com.walletmix.paymixbusiness.ui.view.dialog.DialogError
import com.walletmix.paymixbusiness.data.network.api_response.merchant.GetMarchentResponse
import com.walletmix.paymixbusiness.data.network.api_response.merchant.MerchantUpdateResponse
import com.walletmix.paymixbusiness.ui.view.tabs.profile.ProfileFrgContract
import com.walletmix.paymixbusiness.ui.view.tabs.profile.ProfileFrgPresenter
import com.walletmix.paymixbusiness.R
import com.walletmix.paymixbusiness.databinding.FragmentProfileBinding
import com.walletmix.paymixbusiness.base.BaseFragment
import com.walletmix.paymixbusiness.data.prefs.PrefKeys
import com.walletmix.paymixbusiness.ui.view.dashboard.DashBoardActivity
import com.walletmix.paymixbusiness.utils.*
import io.reactivex.schedulers.Schedulers
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class ProfileFragment : BaseFragment<ProfileFrgPresenter>(), ProfileFrgContract.View {


    private var mAlertDialog: AlertDialog.Builder? = null
    var dialog: Dialog?=null

    lateinit var url:String

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    var updateflag=false

    @Inject
    lateinit var permissionUtils: PermissionUtils

    private var GALLERY_PERMISSION = arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE)
    private var fileToUpload: File? = null
    private lateinit var fileUploader: FileUploader
    private var attachmentUri: Uri? = null

    private var profileEditable = false
    private var bankEditable = false
    private var OrganizationEditable = false


    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()

        var imageUri: Uri? = null
        private var imagePath: String? = null
    }

    override fun getFragmentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        saveInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewReady(getArguments: Bundle?) {



        binding.swipeRefreshLayout.setColorSchemeResources(R.color.orange)
        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshContent()
        }


        mPresenter.profile()


        fileUploader = FileUploader(getContext())

        binding.profileDetails.root.isVisible = false
        binding.bankDetails.root.isVisible = false
        binding.clOrganizationDetails.root.isVisible = false
        binding.clAttachmentsDetails.root.isVisible = false


        ////////profile
        binding.clProfile.setOnClickListener {
            if (binding.profileDetails.root.isVisible){
                binding.profileDetails.root.isVisible = false
                binding.spProfile.setImageResource(R.drawable.profile_down_arrow)
            }else{
                binding.profileDetails.root.isVisible = true
                binding.spProfile.setImageResource(R.drawable.profile_up_arrow)
            }
        }

//        binding.profileDetails.tvUsernameChange.setOnClickListener {
//            makeProfileEditable()
//
//        }

        /////////org
        binding.clOrganization.setOnClickListener {
            if (binding.clOrganizationDetails.root.isVisible){
                binding.clOrganizationDetails.root.isVisible = false
                binding.spOrganization.setImageResource(R.drawable.profile_down_arrow)
            }else{
                binding.clOrganizationDetails.root.isVisible = true
                binding.spOrganization.setImageResource(R.drawable.profile_up_arrow)
            }
        }

        /////////bank
        binding.clBank.setOnClickListener {
            if (binding.bankDetails.root.isVisible){
                binding.bankDetails.root.isVisible = false
                binding.spBank.setImageResource(R.drawable.profile_down_arrow)
            }else{
                binding.bankDetails.root.isVisible = true
                binding.spBank.setImageResource(R.drawable.profile_up_arrow)
            }
        }

        /////////attachments
        binding.clAttachments.setOnClickListener {
            if (binding.clAttachmentsDetails.root.isVisible){
                binding.clAttachmentsDetails.root.isVisible = false
                binding.spAttachment.setImageResource(R.drawable.profile_down_arrow)
            }else{
                binding.clAttachmentsDetails.root.isVisible = true
                binding.spAttachment.setImageResource(R.drawable.profile_up_arrow)
            }
        }


//        binding.ivProfileImage.setOnClickListener {
//
//
//            val alertBuilder = AlertDialog.Builder(context)
//            alertBuilder.setCancelable(true)
//            alertBuilder.setItems(
//                arrayOf<CharSequence>(
//                    getString(R.string.camera),
//                    getString(R.string.gallery)
//                )
//            ) { _, option ->
//                when (option) {
//                    0 -> {
//                        if (hasCameraAndStoragePermission()) {
//                            takePicture()
//                        } else {
//                            manageCameraAndStoragePermission()
//                        }
//                    }
//
//                    1 -> openGallery()
//                }
//            }
//            alertBuilder.create().show()
//
//        }



//        binding.clAttachmentsDetails.ivOrgaLogo.setOnClickListener {
//            zoomImage()
//
//        }

        binding.clAttachmentsDetails.ivOrgaLogo.setOnLongClickListener {
            zoomImage()
            return@setOnLongClickListener true
        }


    }

    private fun refreshContent() {
        binding.swipeRefreshLayout.isRefreshing = true
        if (mNetworkUtils.isConnectedToNetwork(context)) {
            mPresenter.profile()
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

    override fun profilePageResponse(response: GetMarchentResponse) {

        if(updateflag){
            mAppLogger.logD(response.data.merchantDetails.username.toString())
            val data: MutableMap<String, String> = mutableMapOf()
            data["app_bar_name"]=response.data.merchantDetails.username.toString()
            data["app_bar_image"]=response.data.merchantDetails.logo.toString()

            mPrefManager.putObject(PrefKeys.APP_BAR_DATA,data)

            val dashBoardActivity=activity as DashBoardActivity
            dashBoardActivity.setAppBar(dashBoardActivity.binding.appBar)
            updateflag=false
        }


        /////////Profile
        binding.profileDetails.tvMerchentName.text = response.data.merchantDetails.merchant_name
        binding.profileDetails.tvMerchantWebsite.text = response.data.merchantDetails.website_url
        binding.profileDetails.etMerchantUsername.setText(response.data.merchantDetails.username)
        //binding.profileDetails.etMerchantPassword.setText(response.data.merchantDetails.)
        binding.profileDetails.tvMerchantEmail.text = response.data.merchantDetails.email
        binding.profileDetails.tvMerchantPhoneNumber.text = response.data.merchantDetails.contact_no
        //AppUtils.shared.showImage(getContext(),response.data.merchantDetails.logo,binding.merchantImg)


        Log.d("user name",response.data.merchantDetails.username.toString())


        ///////Organization
        binding.clOrganizationDetails.tvOrgName.text = response.data.merchantDetails.org_name
        binding.clOrganizationDetails.tvOrgAddress.text = response.data.merchantDetails.org_address
        binding.clOrganizationDetails.etOrgContractNumber.setText(response.data.merchantDetails.org_contact_no)
        binding.clOrganizationDetails.etOrgMobileNumber.setText(response.data.merchantDetails.org_mobile_no)
        binding.clOrganizationDetails.etOrgPhoneNumber.setText(response.data.merchantDetails.org_phone)
        binding.clOrganizationDetails.etOrgBusinessType.setText(response.data.merchantDetails.business_type)
        binding.clOrganizationDetails.tvOrgProductType.text = response.data.merchantDetails.org_product

        //////Bank
        binding.bankDetails.tvBankName.text = response.data.merchantDetails.bank_name
        binding.bankDetails.tvBranchName.text = response.data.merchantDetails.bank_branch
        binding.bankDetails.etAccountNumber.setText(response.data.merchantDetails.bank_account_no)
        binding.bankDetails.etAccountName.setText(response.data.merchantDetails.bank_account_name)

        Log.d("Bank AccountName",response.data.merchantDetails.bank_account_name.toString())

        /////Attachment

        AppUtils.shared.showImage(getContext(),response.data.merchantDetails.logo, binding.clAttachmentsDetails.ivOrgaLogo)
        //AppUtils.shared.showImage(getContext(),response.data.merchantDetails.trade_licence, binding.clAttachmentsDetails.ivOrgTradeLicense)
        AppUtils.shared.showImage(getContext(),response.data.merchantDetails.passport, binding.clAttachmentsDetails.ivOrgPassport)
        AppUtils.shared.showImage(getContext(),response.data.merchantDetails.nid, binding.clAttachmentsDetails.ivNid)





        url = response.data.merchantDetails.logo.toString()

    }

    override fun updateMerchantDocumentResponse(response: MerchantUpdateResponse) {
        updateflag=true
        mPresenter.profile()

    }


    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {

            val uriFilePath = result.getUriFilePath(getContext()) // optional usage
            imagePath =uriFilePath
            imagePath?.let {
                mAppLogger.logD(it)

                fileToUpload = ImageCompresser.instance.getCompressedImageFile(it, getContext())
                if (imagePath != null) {
                    val file = File(imagePath)
                    if (file.exists())
                        file.delete()
                }


                //mAppLogger("uploadfile::${uploadType}")

                //binding.merchantImg.setImageBitmap(getBitmapFromImagePath(it))
                uploadFile("profile_photo", null)





            }


        } else {
            // An error occurred.
            val exception = result.error
        }
    }
    override fun onNetworkUnavailable() {

        val animId: Int = R.raw.failed
        try{
            val dialogError: DialogError = DialogError.newInstance(
                getString(R.string.no_internet_connection),
                getString(R.string.no_internet_msg),
                titleFullRed = true,animId
            )
            dialogError.dialogErrorOkCallback = object : DialogError.DialogErrorOkCallback {
                override fun okBtnDidTapped() {

                }
            }
            fragmentManager?.let { dialogError.show(it,DialogError.TAG) }

        }catch (_: Exception){}

    }
    fun zoomImage(){
        mAlertDialog = AlertDialog.Builder(context)
        mAlertDialog!!.setCancelable(true)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_zoom_image, null)
        mAlertDialog!!.setView(view)
        val newdialog = mAlertDialog!!.create()

        newdialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        newdialog?.window?.setGravity(Gravity.CENTER)
        view.setOnClickListener {
            Log.d("banner click", "clink")
            dialog!!.dismiss()
        }



        Thread {
            var image =  Uri.parse(url)
            activity?.runOnUiThread(Runnable {
                val photoView = view.findViewById<PhotoView>(R.id.zoom_image)
                //photoView.setImageBitmap(BitmapFactory.decodeFile(url));

                AppUtils.shared.showImage(getContext(),url,photoView)
            })
        }.start()

        newdialog.setView(view)
        newdialog.show()
        dialog=newdialog
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
        val listOfPermissionNeeded = ArrayList<String>()
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
            }
        }
    }

    private fun openGallery() {
        var cropImageOptions= CropImageOptions()
        cropImageOptions.imageSourceIncludeCamera=false
        cropImageOptions.imageSourceIncludeGallery=true

        cropImageOptions.fixAspectRatio=true
        cropImageOptions.aspectRatioX=500
        cropImageOptions.aspectRatioY=500

        var cropImageContractOptions=
            CropImageContractOptions(imageUri,cropImageOptions)
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
    fun uploadFile(key_name: String, uploadFile: Any?){

        mAppLogger.logD("file-attachment")
            if(fileToUpload!=null){
                try{
                    val videoPart = ProgressRequestBody(fileToUpload!!)
                    videoPart.getProgressSubject()
                        .subscribeOn(Schedulers.io())
                        .subscribe { percentage ->
                            Log.i("PROGRESS", "${percentage}%")
                        }
                    val data = HashMap<String, RequestBody>()

                    mPresenter.setUpdateProfileDocument(data,fileUploader.getMultipartBodyPartFromFile(key_name, fileToUpload))
                }catch (e:Exception){
                    mAppLogger.logE(e.toString())
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
                val rotatedBitmap: Bitmap?
                rotatedBitmap = when (orientation) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, getIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, getIntent)

        // For loading Image
        if (resultCode != AppCompatActivity.RESULT_CANCELED) {
            when (requestCode) {
                AppConstants.LAUNCH_CAMERA->{
                    mAppLogger.logD("launch camera")
                    mAppLogger.logD(imagePath.toString())
                    try {

                        var cropImageOptions=CropImageOptions()
                        cropImageOptions.imageSourceIncludeGallery=false
                        cropImageOptions.imageSourceIncludeCamera=true
                        cropImageOptions.fixAspectRatio=true
                        cropImageOptions.aspectRatioX=500
                        cropImageOptions.aspectRatioY=500

                        var cropImageContractOptions=CropImageContractOptions(imageUri,cropImageOptions)
                        cropImage.launch(cropImageContractOptions)





                    } catch (e: Exception) {
                        mAppLogger.logE(e.toString())
                        e.printStackTrace()
                    }

                }
                AppConstants.LAUNCH_GALLERY -> if (resultCode == RESULT_OK && getIntent != null) {

                    if (getIntent != null && getIntent.data != null) {
                        val imageSelected = getIntent.data
                        try {
                            val realImagePath = fileUploader.getRealPathFromUri(imageSelected!!)
                            if (realImagePath != null) {


                                var cropImageOptions=CropImageOptions()
                                cropImageOptions.imageSourceIncludeCamera=false
                                cropImageOptions.imageSourceIncludeGallery=true

                                cropImageOptions.fixAspectRatio=true
                                cropImageOptions.aspectRatioX=500
                                cropImageOptions.aspectRatioY=500

                                var cropImageContractOptions=CropImageContractOptions(
                                    imageUri,cropImageOptions)
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
                AppConstants.LAUNCH_PDF->{

                }
            }
        }

    }


    private fun makeProfileEditable() {
        profileEditable = true

        binding.btnUpdateProfile.isVisible = true

//        binding.profileDetails.ivUsernameEditPen.isVisible = false
//        binding.profileDetails.tvUsernameChange.isVisible = false

        binding.profileDetails.etMerchantUsername.isFocusableInTouchMode = true
        binding.profileDetails.etMerchantUsername.isCursorVisible = true
        binding.profileDetails.etMerchantUsername.setSelection(binding.profileDetails.etMerchantUsername.text!!.length)

    }



}