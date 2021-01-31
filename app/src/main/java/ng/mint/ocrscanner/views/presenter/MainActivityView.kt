package ng.mint.ocrscanner.views.presenter

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.widget.doAfterTextChanged
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard
import ng.mint.ocrscanner.MainActivity
import ng.mint.ocrscanner.R
import ng.mint.ocrscanner.callbacks.CardCallable
import ng.mint.ocrscanner.databinding.ActivityMainBinding
import ng.mint.ocrscanner.model.CardResponse
import ng.mint.ocrscanner.networking.ConnectionDetector
import ng.mint.ocrscanner.networking.RequestHandler
import okhttp3.ResponseBody
import retrofit2.Call

class MainActivityView(
    private val mainActivity: MainActivity,
    private val binding: ActivityMainBinding
) {

    private val context: Context get() = mainActivity
    private val connectionDetector: ConnectionDetector get() = mainActivity.connectionDetector
    private val requestHandler: RequestHandler get() = mainActivity.requestHandler

    init {

        binding.panInputField.doAfterTextChanged {

            binding.cardInformationLayout.visibility = View.GONE
        }

        binding.nextButton.setOnClickListener {
            mainActivity.hideKeyboard()
            val panNumber = binding.panInputField.text?.toString() ?: ""
            if (panNumber.length > 5) {
                processCardNumber(panNumber)
            }
        }

        binding.scanButton.setOnClickListener(this::onScanClicked)

        mainActivity.also {

            it.makeActivityFullScreen()
            it.changeStatusBarColorToTransparent()
            it.setLightStatusBar(binding.relativeLayout)
        }

    }


    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == MainActivity.MY_SCAN_REQUEST_CODE && data != null) {
            val scanResult =
                data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT) as? CreditCard
            scanResult?.run {

                val panNumber = this.formattedCardNumber?.replace(" ", "") ?: ""
                if (panNumber.length > 5) {
                    processCardNumber(panNumber)
                }
            }
        }

    }

    private fun onScanClicked(view: View) {

        mainActivity.requestPermissionsFromDevice()

        val scanIntent = Intent(mainActivity, CardIOActivity::class.java)
        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, false) // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false) // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false) // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true) // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true) // default: false

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        mainActivity.startActivityForResult(scanIntent, MainActivity.MY_SCAN_REQUEST_CODE)
    }


    private fun processCardNumber(cardNumber: String) {

        if (connectionDetector.isConnectingToInternet()) {

            mainActivity.showLoading(context.getString(R.string.processing))

            requestHandler.getCardDetail(cardNumber.take(8), object : CardCallable {
                override fun onGetCard(cardResponse: CardResponse?) {
                    mainActivity.progressDialog?.dismiss()
                    cardResponse?.run { binding.bankData = cardResponse }
                }

                override fun onGetCardError(error: ResponseBody?, responseCode: Int) {
                    mainActivity.progressDialog?.dismiss()
                    binding.cardInformationLayout.visibility = View.GONE
                    mainActivity.notifyUser(context.getString(R.string.no_bin_number_was_found))

                }

                override fun onGetCardFailureCall(call: Call<CardResponse>?, t: Throwable?) {
                    mainActivity.progressDialog?.dismiss()

                }
            })
        } else {
            mainActivity.notifyUser(context.getString(R.string.no_internet_connection))
        }

    }

}