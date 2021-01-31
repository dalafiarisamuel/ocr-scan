package ng.mint.ocrscanner.views.activities

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import ng.mint.ocrscanner.networking.ConnectionDetector
import ng.mint.ocrscanner.networking.RequestHandler

abstract class BaseActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSION_ALL = 1
    }

    open val requestHandler: RequestHandler by lazy {
        RequestHandler(lifecycleScope)
    }

    open val connectionDetector: ConnectionDetector by lazy {
        ConnectionDetector(this)
    }

    open var progressDialog: ProgressDialog? = null

    private val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )


    open fun makeActivityFullScreen() {
        if (Build.VERSION.SDK_INT >= 21) {
            this.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    open fun changeStatusBarColorToTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            this.window.statusBarColor = Color.TRANSPARENT
        }
    }

    open fun setLightStatusBar(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = view.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            view.systemUiVisibility = flags
            this.window.statusBarColor = view.context.resources.getColor(android.R.color.white)
        }
    }

    open fun showLoading(@NonNull message: String) {
        this.progressDialog = ProgressDialog(this)
        this.progressDialog?.setCancelable(false)
        this.progressDialog?.setMessage(message)
        this.progressDialog?.show()
    }

    open fun notifyUser(message: String?) {
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setCancelable(false)
        alertBuilder.setMessage(message)
        alertBuilder.setPositiveButton(
            getText(android.R.string.ok)
        ) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
        alertBuilder.show()
    }

    open fun hideKeyboard(activity: Activity = this) {
        val imm =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val f = activity.currentFocus
        if (null != f && null != f.windowToken && EditText::class.java.isAssignableFrom(f.javaClass)) {
            imm.hideSoftInputFromWindow(f.windowToken, 0)
        } else activity.window
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }


    open fun requestPermissionsFromDevice() {
        if (!permissionGranted(*permissions)) {
            ActivityCompat.requestPermissions(
                this,
                permissions,
                PERMISSION_ALL
            )
        }

    }

    open fun permissionGranted(vararg permissions: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }
}