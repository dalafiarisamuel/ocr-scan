package ng.mint.ocrscanner.views.common

import android.content.Context

class DialogsCompositionRoot(private val context: Context) {

    private val progressDialogManager: ProgressDialogManager by lazy { ProgressDialogManager(context) }
    private val messageDialogManager: MessageDialogManager by lazy { MessageDialogManager(context) }

    val progressDialog get() = progressDialogManager
    val messageDialog get() = messageDialogManager
}