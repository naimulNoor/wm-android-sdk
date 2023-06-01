package com.walletmix.paymixbusiness.ui.view.marchant.form


import ProgressRequestBody
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.google.android.gms.common.util.IOUtils
import com.walletmix.paymixbusiness.file.FileUploader
import com.walletmix.paymixbusiness.ui.view.dialog.DialogError
import com.walletmix.paymixbusiness.data.network.api_response.merchant.GetMarchentResponse
import com.walletmix.paymixbusiness.data.network.api_response.merchant.MerchantUpdateResponse
import com.walletmix.paymixbusiness.model.MerchantBank
import com.walletmix.paymixbusiness.model.MerchantOrganization
import com.walletmix.paymixbusiness.utils.GetFile
import com.walletmix.paymixbusiness.R
import com.walletmix.paymixbusiness.databinding.ActivityDocumentLayoutBinding
import com.walletmix.paymixbusiness.base.MvpBaseActivity
import com.walletmix.paymixbusiness.data.prefs.PrefKeys
import com.walletmix.paymixbusiness.ui.view.dashboard.DashBoardActivity
import com.walletmix.paymixbusiness.ui.view.merchant.form.MerchantFormPresenter
import com.walletmix.paymixbusiness.utils.*
import io.reactivex.schedulers.Schedulers
import okhttp3.RequestBody
import timber.log.Timber
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class MerchantFormActivity : MvpBaseActivity<MerchantFormPresenter>(), MerchantFormContract.View  {

    private lateinit var fileUploader: FileUploader
    private var fileToUpload: File? = null
    var bundle=Bundle()

    var stage:Int=4

    private lateinit var pdfUri: Uri

    private lateinit var binding:ActivityDocumentLayoutBinding
    private var GALLERY_PERMISSION = arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE)
    var merchantResponse: GetMarchentResponse? = null

     var uploadType=0
    //0 = profile photo
    //1 = org logo
    //2 = passport
    //3 = nid
    //4 = tradelicence
    //5 = tin/bin

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
                Log.d("uploadfile",uploadType.toString())
                //mAppLogger("uploadfile::${uploadType}")

                when(uploadType){
                    0-> {
                        binding.documentLayout.ivProfileLogo.setImageBitmap(getBitmapFromImagePath(it))
                        uploadFile("profile_photo", null)
                    }
                    1-> {
                        binding.documentLayout.ivOrgLogo.setImageBitmap(getBitmapFromImagePath(it))
                        uploadFile("logo", null)
                    }
                    2-> {
                        binding.documentLayout.ivPassport.setImageBitmap(getBitmapFromImagePath(it))
                        uploadFile("passport", null)
                    }
                    3-> {
                        binding.documentLayout.ivNid.setImageBitmap(getBitmapFromImagePath(it))
                        uploadFile("nid_copy", null)
                    }
                    4-> {
                        binding.documentLayout.ivTradeLicence.setImageBitmap(getBitmapFromImagePath(it))
                        uploadFile("trade_licence", null)
                    }
                    5-> {
                        binding.documentLayout.ivTinbin.setImageBitmap(getBitmapFromImagePath(it))
                        uploadFile("tin_bin", null)
                    }
                }



            }



        } else {
            // An error occurred.
            val exception = result.error
        }


//        mPresenter.getMerchantProfileDetails()
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // Initialize result data
        val data: Intent? = result.data
        // check condition
        if (data != null) {
            val sUri: Uri? = data.data
            fileToUpload= File(sUri!!.getPath())
            if(uploadType==4){
                binding.documentLayout.etTradeLicense.setText("trade_licence.pdf")
                uploadFile("trade_licence",sUri)
            }else{
                binding.documentLayout.etTradeLicense.setText("tin_bin.pdf")
                uploadFile("tin_bin", sUri)
            }

        }
    }

    @Inject
    lateinit var permissionUtils: PermissionUtils

    companion object {
        private var imageUri: Uri? = null
        private var imagePath: String? = null
    }

    override fun getContentView(): View {

        binding = ActivityDocumentLayoutBinding.inflate(layoutInflater)
        return binding.root


    }

    @SuppressLint("CheckResult")
    fun uploadFile(key_name: String, uploadFile: Any?){
        mAppLogger.logD("file-attachment")
        if (key_name=="trade_licence" || key_name =="tin_bin"){
            try{
                val videoPart = ProgressRequestBody(fileToUpload!!)
                videoPart.getProgressSubject()
                    .subscribeOn(Schedulers.io())
                    .subscribe { percentage ->
                        Log.i("PROGRESS", "${percentage}%")
                    }
                val data = HashMap<String, RequestBody>()
                data["brand_color"] =fileUploader.createPartFromString(binding.documentLayout.etBrandColor.text.toString())


                var getFile: GetFile?= GetFile()
                var myFile=getFile!!.getFile(getContext(),uploadFile as Uri)
                var fileDate=FileUtils.sharedInstance.createMultipartBodyFromFile(key_name,myFile)
                mAppLogger.logD(myFile.absolutePath)

                mAppLogger.logD("file-attachment")

                mPresenter.setUpdateMerchantDocument(data,fileDate)
            }catch (e:Exception){
                mAppLogger.logE(e.toString())
            }
        }else{
            if(fileToUpload!=null){
                try{
                    val videoPart = ProgressRequestBody(fileToUpload!!)
                    videoPart.getProgressSubject()
                        .subscribeOn(Schedulers.io())
                        .subscribe { percentage ->
                            Log.i("PROGRESS", "${percentage}%")
                        }
                    val data = HashMap<String, RequestBody>()
                    data["brand_color"] =fileUploader.createPartFromString(binding.documentLayout.etBrandColor.text.toString())
                    mPresenter.setUpdateMerchantDocument(data,fileUploader.getMultipartBodyPartFromFile(key_name, fileToUpload))
                }catch (e:Exception){
                    mAppLogger.logE(e.toString())
                }

            }
        }
    }

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {
        fileUploader = FileUploader(getContext())
        //button task
        binding.submit.setOnClickListener(onSubmitButtonTapped)

        binding.previous.setOnClickListener (onPreviousButtonTapped)



        //api call
        mPresenter.getMerchantProfileDetails()

        setAppBar(binding.include)

        //listner



        ////roni two
        stage = 1

        when(stage){
            1->{
               binding.submit.text = "Next"
            }
            2->{
                //orgaization
                binding.submit.text = "Next"
            }
            3->{

                binding.submit.text = "Next"
            }
            4->{
                // attacment
                binding.submit.text = "Submit"

            }


        }
        //////roni two



        binding.documentLayout.ivProfileUpload.setOnClickListener(onCameraClickListner)
        binding.documentLayout.ivProfileFolder.setOnClickListener(onFileClickListner)


        binding.documentLayout.ivOrganizationUpload.setOnClickListener(onCameraClickListner)
        binding.documentLayout.ivOrganigationFolder.setOnClickListener(onFileClickListner)

        binding.documentLayout.ivPassportUpload.setOnClickListener(onCameraClickListner)
        binding.documentLayout.ivPassportFolder.setOnClickListener(onFileClickListner)

        binding.documentLayout.ivNidUpload.setOnClickListener(onCameraClickListner)
        binding.documentLayout.ivNidFolder.setOnClickListener(onFileClickListner)

        //binding.documentLayout.ivTradeUpload.setOnClickListener(onCameraClickListner)
        binding.documentLayout.ivTradeFolder.setOnClickListener(onFileClickListner)

        //binding.documentLayout.ivAgreementUpload.setOnClickListener(onCameraClickListner)
        binding.documentLayout.ivAgreementFolder.setOnClickListener(onFileClickListner)





       // binding.profileLayout.root.visibility=View.VISIBLE
        binding.organizationLayout.root.visibility=View.VISIBLE


    }

    override fun getMerchantResponse(response: GetMarchentResponse) {
        mAppLogger.logD("merchant-app")
        mAppLogger.logD(response.toString())
        merchantResponse=response
        mPrefManager.putObject(PrefKeys.MERCHANT_PROFILE,response)

        profileSectionTask()
        organizationSectionTask()
        bankSectionTask()
        documentSectionTask()
        previewSectionTask()



    }


    override fun updateMerchantDetailsResponse(response: MerchantUpdateResponse) {
        mAppLogger.logD("updateMerchantDetailsResponse")
        binding.organizationLayout.root.visibility=View.GONE
        binding.bankLayout.root.visibility=View.VISIBLE
        stage++

        mPresenter.getMerchantProfileDetails()

        binding.step.imgStep2.setImageResource(R.drawable.ic_check_circle)
        binding.previous.visibility = View.VISIBLE
    }

    override fun updateMerchantBankDetailsResponse(response: MerchantUpdateResponse) {
        mAppLogger.logD("updateMerchantBankDetailsResponse")
        binding.bankLayout.root.visibility=View.GONE
        binding.documentLayout.root.visibility=View.VISIBLE
        stage++

        binding.step.imgStep3.setImageResource(R.drawable.ic_check_circle)
        binding.previous.visibility = View.VISIBLE

    }

    override fun updateMerchantProfileResponse(response: MerchantUpdateResponse) {
        //stage 1
//        mAppLogger.logD("updateMerchantProfileResponse")
//        binding.profileLayout.root.visibility=View.GONE
//        binding.organizationLayout.root.visibility=View.VISIBLE
//        stage++
//
//        binding.step.imgStep1.setImageResource(R.drawable.ic_check_circle)
    }

    override fun updateMerchantDocumentResponse(response: MerchantUpdateResponse) {
        showToast("Update Successfully")
        mPresenter.getMerchantProfileDetails()
//
//        stage++
//        binding.documentLayout.root.visibility=View.GONE
//        binding.previewLayout.root.visibility =View.VISIBLE
//
////        binding.step.imgStep4.setImageResource(R.drawable.ic_check_circle)
//        binding.step.root.visibility = View.GONE
//        binding.previous.visibility = View.VISIBLE
//
//        Log.d("stage number", stage.toString())



    }

    private fun selectPdf() {
        val pdfIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        pdfIntent.type = "application/pdf"
        //pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
        resultLauncher.launch(pdfIntent)
        //startActivityForResult(pdfIntent, 12)
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
            dialogError.show(supportFragmentManager, DialogError.TAG)

        }catch (_: Exception){}

    }


    //on login tapped
    private  var onSubmitButtonTapped=View.OnClickListener {
        submitButtonTapped()
    }


    private  var onPreviousButtonTapped=View.OnClickListener {
        stage--

        when (stage) {
            1 -> {
                binding.organizationLayout.root.visibility = View.VISIBLE
                binding.bankLayout.root.visibility = View.GONE
                binding.documentLayout.root.visibility = View.GONE
                binding.previewLayout.root.visibility = View.GONE
                binding.previous.visibility = View.GONE

                binding.step.imgStep2.setImageResource(R.drawable.ic_unchecked_circle)

                binding.step.root.visibility = View.VISIBLE
            }

            2 -> {
                mAppLogger.logD("updateMerchantDetailsResponse")
                binding.organizationLayout.root.visibility = View.GONE
                binding.bankLayout.root.visibility = View.VISIBLE
                binding.documentLayout.root.visibility = View.GONE
                binding.previewLayout.root.visibility = View.GONE


                mPresenter.getMerchantProfileDetails()

                binding.step.imgStep2.setImageResource(R.drawable.ic_check_circle)
                binding.step.imgStep3.setImageResource(R.drawable.ic_unchecked_circle)
                binding.step.imgStep4.setImageResource(R.drawable.ic_unchecked_circle)
                binding.previous.visibility = View.VISIBLE

                binding.step.root.visibility = View.VISIBLE
            }

            3 -> {
                mAppLogger.logD("updateMerchantBankDetailsResponse")
                binding.bankLayout.root.visibility = View.GONE
                binding.documentLayout.root.visibility = View.VISIBLE
                binding.organizationLayout.root.visibility = View.GONE
                binding.previewLayout.root.visibility = View.GONE


                binding.step.imgStep3.setImageResource(R.drawable.ic_check_circle)
                binding.step.imgStep4.setImageResource(R.drawable.ic_unchecked_circle)
                binding.previous.visibility = View.VISIBLE

                binding.step.root.visibility = View.VISIBLE
            }

            4 -> {

                binding.documentLayout.root.visibility = View.GONE
                binding.previewLayout.root.visibility = View.VISIBLE
                binding.bankLayout.root.visibility = View.GONE
                binding.organizationLayout.root.visibility = View.GONE

                //binding.step.imgStep4.setImageResource(R.drawable.ic_check_circle)
                binding.step.root.visibility = View.GONE
                binding.previous.visibility = View.VISIBLE
            }
        }
        Timber.tag("Click Previous btn").d(stage.toString())

    }



    @SuppressLint("CheckResult")
    private fun submitButtonTapped() {

        when(stage){
//           1->{
//               var marchentName=binding.profileLayout.etMarchantName.text.toString()
////               var username=binding.profileLayout.etUserUsername.text.toString()
////               var email=binding.profileLayout.etUserEmail.text.toString()
////               var contact=binding.profileLayout.etPhoneNumber.text.toString()
////               var url=binding.profileLayout.etWebsiteUrl.text.toString()
//
//               mAppLogger.logD(marchentName)
////               mAppLogger.logD(username)
////               mAppLogger.logD(email)
////               mAppLogger.logD(contact)
////               mAppLogger.logD(url)
//               //var model=MerchantProfile(marchentName,username,email,contact,url)
//               var model=MerchantProfile(marchentName)
//               //profile
//              // mPresenter.setUpdateMerchantProfileDetails(model)
//
//
//
//
//           }
           1->{
               //orgaization

               var contactno=binding.organizationLayout.etContact.text.toString()
               var orgName=binding.organizationLayout.etOrgName.text.toString()
               var orgAddress=binding.organizationLayout.etOrgAddress.text.toString()
               var orgPhone=binding.organizationLayout.etPhone.text.toString()
               var orgProduct=binding.organizationLayout.etOrgProduct.text.toString()
               var orgMobile=binding.organizationLayout.etMobile.text.toString()

               var nid=binding.organizationLayout.etNidNumber.text.toString()
               var businessType=binding.organizationLayout.etBusinessType.text.toString()
               var passportNo=binding.organizationLayout.etPassportNo.text.toString()



               var model=MerchantOrganization(
                   contactno,
                   orgName,
                   orgAddress,
                   orgPhone,
                   orgProduct,
                   orgMobile,
                   nid,
                   businessType,
                   passportNo

               )
               //profile
               mPresenter.setUpdateMerchantDetails(model)





           }
           2->{
               var bankName=binding.bankLayout.etBankName.text.toString()
               var bankBranch=binding.bankLayout.etBranchName.text.toString()
               var bankAccountNumber=binding.bankLayout.etAccountNumber.text.toString()
               var bankAccountName=binding.bankLayout.etAccountName.text.toString()

               var model=MerchantBank(
                   bankName,
                   bankBranch,
                   bankAccountName,
                   bankAccountNumber,
                   )
               //profile
               mPresenter.setUpdateMerchantBankDetails(model)



           }
           3->{
               stage++
               binding.documentLayout.root.visibility=View.GONE
               binding.previewLayout.root.visibility =View.VISIBLE

               binding.step.imgStep4.setImageResource(R.drawable.ic_check_circle)
               binding.previous.visibility = View.VISIBLE

               binding.step.root.visibility = View.GONE
                // attacment
//               mAppLogger.logD("attatchment")
//               if(fileToUpload!=null){
//                   try{
//                       val videoPart = ProgressRequestBody(fileToUpload!!)
////                       videoPart.getProgressSubject()
////                           .subscribeOn(Schedulers.io())
////                           .subscribe { percentage ->
////                               Log.i("PROGRESS", "${percentage}%")
////                           }
//                       val data = HashMap<String, RequestBody>()
//                       data["brand_color"] =fileUploader.createPartFromString(binding.documentLayout.etBrandColor.text.toString())
//                       mPresenter.setUpdateMerchantDocument(data,fileUploader.getMultipartBodyPartFromFile("profile_photo", fileToUpload))
//                   }catch (e:Exception){
//                       mAppLogger.logE(e.toString())
//                   }
//
//               }

           }
            4->{
                Navigator.sharedInstance.navigate(this,DashBoardActivity::class.java)
                finish()
            }


       }


    }

    private fun profileSectionTask() {
        //profile section
//        binding.profileLayout.etMarchantName.setText(merchantResponse!!.data.merchantDetails.merchant_name)
//        binding.profileLayout.etWebsiteUrl.setText(merchantResponse!!.data.merchantDetails.website_url)
//        binding.profileLayout.etUserUsername.setText(merchantResponse!!.data.merchantDetails.username)
//        binding.profileLayout.etUserEmail.setText(merchantResponse!!.data.merchantDetails.email)
//        binding.profileLayout.etPhoneNumber.setText(merchantResponse!!.data.merchantDetails.contact_no)
    }
    private fun organizationSectionTask() {
        binding.organizationLayout.etOrgName.setText(merchantResponse!!.data.merchantDetails.org_name)
        binding.organizationLayout.etOrgAddress.setText(merchantResponse!!.data.merchantDetails.org_address)
        binding.organizationLayout.etMobile.setText(merchantResponse!!.data.merchantDetails.org_mobile_no)
        binding.organizationLayout.etPhone.setText(merchantResponse!!.data.merchantDetails.org_phone)
        binding.organizationLayout.etContact.setText(merchantResponse!!.data.merchantDetails.contact_no)
        binding.organizationLayout.etOrgProduct.setText(merchantResponse!!.data.merchantDetails.org_product)
        binding.organizationLayout.etBusinessType.setText(merchantResponse!!.data.merchantDetails.business_type)
        binding.organizationLayout.etPassportNo.setText(merchantResponse!!.data.merchantDetails.passport_no)
        binding.organizationLayout.etNidNumber.setText(merchantResponse!!.data.merchantDetails.nid_no)
    }
    private fun bankSectionTask() {
        binding.bankLayout.etBankName.setText(merchantResponse!!.data.merchantDetails.bank_name)
        binding.bankLayout.etBranchName.setText(merchantResponse!!.data.merchantDetails.bank_branch)
        binding.bankLayout.etAccountNumber.setText(merchantResponse!!.data.merchantDetails.bank_account_no)
        binding.bankLayout.etAccountName.setText(merchantResponse!!.data.merchantDetails.bank_account_name)
    }
    private fun documentSectionTask(){
        binding.documentLayout.etBrandColor.setText(merchantResponse!!.data.merchantDetails.brandColor)
        AppUtils.shared.showImage(getContext(),merchantResponse!!.data.merchantDetails.profile_photo, binding.documentLayout.ivProfileLogo)
        AppUtils.shared.showImage(getContext(),merchantResponse!!.data.merchantDetails.logo, binding.documentLayout.ivOrgLogo)
        AppUtils.shared.showImage(getContext(),merchantResponse!!.data.merchantDetails.nid, binding.documentLayout.ivNid)
        AppUtils.shared.showImage(getContext(),merchantResponse!!.data.merchantDetails.passport, binding.documentLayout.ivPassport)


    }

    private fun previewSectionTask() {
        //profile section
//        binding.previewLayout.profileDetails.tvMerchentName.text = merchantResponse!!.data.merchantDetails.merchant_name
//        binding.previewLayout.profileDetails.tvMerchantEmail.text = merchantResponse!!.data.merchantDetails.email
//        binding.previewLayout.profileDetails.tvMerchantWebsite.text = merchantResponse!!.data.merchantDetails.website_url
//        binding.previewLayout.profileDetails.tvMerchantPhoneNumber.text = merchantResponse!!.data.merchantDetails.contact_no
//        binding.previewLayout.profileDetails.etMerchantUsername.setText(merchantResponse!!.data.merchantDetails.username)

        //organization section
        binding.previewLayout.clOrganizationDetails.tvOrgName.text = merchantResponse!!.data.merchantDetails.org_name
        binding.previewLayout.clOrganizationDetails.tvOrgAddress.text = merchantResponse!!.data.merchantDetails.org_address
        binding.previewLayout.clOrganizationDetails.etOrgContractNumber.setText(merchantResponse!!.data.merchantDetails.org_contact_no)
        binding.previewLayout.clOrganizationDetails.etOrgMobileNumber.setText(merchantResponse!!.data.merchantDetails.org_mobile_no)
        binding.previewLayout.clOrganizationDetails.etOrgPhoneNumber.setText(merchantResponse!!.data.merchantDetails.org_phone)
        binding.previewLayout.clOrganizationDetails.etOrgBusinessType.setText(merchantResponse!!.data.merchantDetails.business_type)
        binding.previewLayout.clOrganizationDetails.tvOrgProductType.text = merchantResponse!!.data.merchantDetails.org_product
        binding.previewLayout.clOrganizationDetails.tvOrgPassportNumber.text = merchantResponse!!.data.merchantDetails.passport_no
        binding.previewLayout.clOrganizationDetails.tvOrgNidNumber.text = merchantResponse!!.data.merchantDetails.nid_no

        //bank section
        binding.previewLayout.bankDetails.tvBankName.text = merchantResponse!!.data.merchantDetails.bank_name
        binding.previewLayout.bankDetails.tvBranchName.text = merchantResponse!!.data.merchantDetails.bank_branch
        binding.previewLayout.bankDetails.etAccountName.setText(merchantResponse!!.data.merchantDetails.bank_account_name)
        binding.previewLayout.bankDetails.etAccountNumber.setText(merchantResponse!!.data.merchantDetails.bank_account_no)

        //attachment section

        AppUtils.shared.showImage(getContext(),merchantResponse!!.data.merchantDetails.profile_photo, binding.previewLayout.ivProfileLogo)
        AppUtils.shared.showImage(getContext(),merchantResponse!!.data.merchantDetails.logo, binding.previewLayout.ivOrgLogo)
        AppUtils.shared.showImage(getContext(),merchantResponse!!.data.merchantDetails.nid, binding.previewLayout.ivNid)
        AppUtils.shared.showImage(getContext(),merchantResponse!!.data.merchantDetails.passport, binding.previewLayout.ivOrgPassport)


    }

    override fun onBackPressed() {
        super.onBackPressed()
        Navigator.sharedInstance.navigate(getContext(),DashBoardActivity::class.java)
    }



    private  var onCameraClickListner=View.OnClickListener {
        when(it.id){
            binding.documentLayout.ivProfileUpload.id->uploadType=0
            binding.documentLayout.ivProfileFolder.id->uploadType=0

            binding.documentLayout.ivOrganizationUpload.id->uploadType=1
            binding.documentLayout.ivOrganigationFolder.id->uploadType=1

            binding.documentLayout.ivPassportUpload.id->uploadType=2
            binding.documentLayout.ivPassportFolder.id->uploadType=2

            binding.documentLayout.ivNidUpload.id->uploadType=3
            binding.documentLayout.ivNidFolder.id->uploadType=3

//            binding.documentLayout.ivTradeUpload.id->uploadType=4
            binding.documentLayout.ivTradeFolder.id->uploadType=4

//            binding.documentLayout.ivTradeUpload.id->uploadType=5
            binding.documentLayout.ivTradeFolder.id->uploadType=5

        }

        if (hasCameraAndStoragePermission()) {
            takePicture()
        } else {
            manageCameraAndStoragePermission()
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
                        this@MerchantFormActivity,
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
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
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
    private  var onFileClickListner=View.OnClickListener {
        when(it.id){
            binding.documentLayout.ivProfileUpload.id->uploadType=0
            binding.documentLayout.ivProfileFolder.id->uploadType=0

            binding.documentLayout.ivOrganizationUpload.id->uploadType=1
            binding.documentLayout.ivOrganigationFolder.id->uploadType=1

            binding.documentLayout.ivPassportUpload.id->uploadType=2
            binding.documentLayout.ivPassportFolder.id->uploadType=2

            binding.documentLayout.ivNidUpload.id->uploadType=3
            binding.documentLayout.ivNidFolder.id->uploadType=3

//            binding.documentLayout.ivTradeUpload.id->uploadType=4
            binding.documentLayout.ivTradeFolder.id->uploadType=4

//            binding.documentLayout.ivTradeUpload.id->uploadType=5
            binding.documentLayout.ivAgreementFolder.id->uploadType=5

        }
        if(it.id==binding.documentLayout.ivTradeFolder.id || it.id==binding.documentLayout.ivAgreementFolder.id ){
            //section pdf
            mAppLogger.logD("select pdf")
            selectPdf()
        }else{
            mAppLogger.logD("select galary")
            openGallery()
        }


    }
    private fun manageGallery() {
        permissionUtils.checkPermission(
            getContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE,
            object : PermissionUtils.OnPermissionAskListener {
                override fun onNeedPermission() {
                    ActivityCompat.requestPermissions(
                        this@MerchantFormActivity,
                        GALLERY_PERMISSION,
                        AppConstants.REQUEST_GALLERY
                    )
                }

                override fun onPermissionPreviouslyDenied() {
                    ActivityCompat.requestPermissions(
                        this@MerchantFormActivity,
                        GALLERY_PERMISSION,
                        AppConstants.REQUEST_GALLERY
                    )
                }

                override fun onPermissionPreviouslyDeniedWithNeverAskAgain() {
                    mAlertService.showConfirmationAlert(this@MerchantFormActivity,
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

    /** Dispatching Camera Intent  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */
    private fun takePicture() {
        mAppLogger.logE("takepicture")
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            var imageFile: File? = null
            try {
                imageFile = createImageFile()
            } catch (e: IOException) {
                mAppLogger.logD("take picture error")
                mAppLogger.logD(e.toString())
                e.printStackTrace()
            }
            mAppLogger.logD("take picture")
            mAppLogger.logD(imageFile!!.absolutePath)
            imageFile?.let {
                val authorities: String = applicationContext.packageName + ".provider"
                /** Authorities That we provided in Manifest.xml*/
                imageUri = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    Uri.fromFile(imageFile)
                } else {

                    FileProvider.getUriForFile(getContext(), authorities, imageFile)
                }
                mAppLogger.logD("imageuri")
                mAppLogger.logD(imageUri.toString())
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(intent, AppConstants.LAUNCH_CAMERA)
            }
        }
    }

    private fun openGallery() {

        var cropImageOptions=CropImageOptions()
        cropImageOptions.imageSourceIncludeCamera=false
        cropImageOptions.imageSourceIncludeGallery=true

        cropImageOptions.fixAspectRatio=true
        cropImageOptions.aspectRatioX=500
        cropImageOptions.aspectRatioY=500

        var cropImageContractOptions=CropImageContractOptions(imageUri,cropImageOptions)
        cropImage.launch(cropImageContractOptions)
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

    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, getIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, getIntent)

        // For loading Image
        if (resultCode != RESULT_CANCELED) {
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

                                var cropImageContractOptions=CropImageContractOptions(imageUri,cropImageOptions)
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

        // For loading PDF
        when (requestCode) {
            12 -> if (resultCode == RESULT_OK) {

                pdfUri = getIntent?.data!!
                val uri: Uri = getIntent?.data!!
                val uriString: String = uri.toString()
                var pdfName: String? = null
                if (uriString.startsWith("content://")) {
                    var myCursor: Cursor? = null
                    try {
                        myCursor = applicationContext!!.contentResolver.query(uri, null, null, null, null)
                        if (myCursor != null && myCursor.moveToFirst()) {
                            pdfName = myCursor.getString(myCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                            //pdfTextView.text = pdfName
                        }
                    } finally {
                        myCursor?.close()
                    }
                }
            }
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








//    fun openSomeActivityForResult() {
//        val intent = Intent(this, SomeActivity::class.java)
//        resultLauncher.launch(intent)
//    }
//
//    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            // There are no request codes
//            val data: Intent? = result.data
//            doSomeOperations()
//        }
//    }
open fun copy(context: Context, srcUri: Uri?, dstFile: File?) {
    try {
        val inputStream: InputStream =
            context.contentResolver.openInputStream(srcUri!!) ?: return
        val outputStream: OutputStream = FileOutputStream(dstFile)
        IOUtils.copyStream(inputStream, outputStream) // org.apache.commons.io
        inputStream.close()
        outputStream.close()
    } catch (e: java.lang.Exception) { // IOException
        e.printStackTrace()
    }
}
    fun getFileName(uri: Uri?): String? {
        if (uri == null) return null
        var fileName: String? = null
        val path = uri.path
        val cut = path!!.lastIndexOf('/')
        if (cut != -1) {
            fileName = path.substring(cut + 1)
        }
        return fileName
    }


}

