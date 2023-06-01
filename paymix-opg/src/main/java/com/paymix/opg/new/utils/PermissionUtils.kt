package com.walletmix.paymixbusiness.utils
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import com.walletmix.paymixbusiness.data.prefs.PreferenceManager


class PermissionUtils(private val preferenceManager: PreferenceManager) {
    lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

//   fun requestpermissionInit(context:Context){
//       val notificationManager: NotificationManager by lazy { context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
//       // Sets up permissions request launcher.
//       requestPermissionLauncher = registerForActivityResult(
//           ActivityResultContracts.RequestPermission()
//       ) {
//           if (it) {
//               Log.d("permission","show dummy notificaiton")
//           } else {
//               Log.d("permission","Please grant Notification permission from App Settings")
//           }
//       }
//   }


    private fun shouldAskPermission(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }


    public fun shouldAskPermission(context: Context, permission: String): Boolean {
        if (shouldAskPermission()) {
            val permissionResult = ActivityCompat.checkSelfPermission(context, permission)
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                return true
            }
        }
        return false
    }

    fun checkPermission(context: Context, permission: String, listener: OnPermissionAskListener) {

        if (shouldAskPermission(context, permission)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context as AppCompatActivity, permission)) {
                listener.onPermissionPreviouslyDenied()
            } else {
                if (preferenceManager.isFirstTimeAskingPermission(permission)) {
                    preferenceManager.firstTimeAskingPermission(permission, false)
                    listener.onNeedPermission()
                } else {
                    listener.onPermissionPreviouslyDeniedWithNeverAskAgain()
                }
            }
        } else {
            listener.onPermissionGranted()
        }
    }





    interface OnPermissionAskListener {
        fun onNeedPermission()
        fun onPermissionPreviouslyDenied()
        fun onPermissionPreviouslyDeniedWithNeverAskAgain()
        fun onPermissionGranted()
    }
}