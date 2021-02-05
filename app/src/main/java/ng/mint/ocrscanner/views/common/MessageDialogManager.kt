package ng.mint.ocrscanner.views.common

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

class MessageDialogManager(private val context: Context) {

    private var alertBuilder: AlertDialog.Builder? = null

    fun displayMessage(message: String) {
        alertBuilder = AlertDialog.Builder(context)
        alertBuilder?.setCancelable(false)
        alertBuilder?.setMessage(message)
        alertBuilder?.setPositiveButton(
            context.getText(android.R.string.ok)
        ) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
        alertBuilder?.show()
    }
}