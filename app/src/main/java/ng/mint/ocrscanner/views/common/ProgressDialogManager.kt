package ng.mint.ocrscanner.views.common

import android.app.ProgressDialog
import android.content.Context
import androidx.annotation.NonNull

class ProgressDialogManager(private val context: Context) {

    private var progressDialog: ProgressDialog? = null

    fun showLoading(@NonNull message: String) {
        this.progressDialog = ProgressDialog(context)
        this.progressDialog?.setCancelable(false)
        this.progressDialog?.setMessage(message)
        this.progressDialog?.show()
    }

    fun dismissDialog() = progressDialog?.dismiss()
}