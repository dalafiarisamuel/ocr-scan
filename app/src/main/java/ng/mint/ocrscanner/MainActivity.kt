package ng.mint.ocrscanner

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard
import ng.mint.ocrscanner.callbacks.CardCallable
import ng.mint.ocrscanner.databinding.ActivityMainBinding
import ng.mint.ocrscanner.model.CardResponse
import ng.mint.ocrscanner.views.BaseActivity
import okhttp3.ResponseBody
import retrofit2.Call


class MainActivity : BaseActivity() {

    companion object {
        const val MY_SCAN_REQUEST_CODE = 100
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        makeActivityFullScreen(this.window)
        changeStatusBarColorToTransparent(this.window)
        setLightStatusBar(binding.relativeLayout)

        binding.panInputField.doAfterTextChanged {

            binding.cardInformationLayout.visibility = View.GONE
        }

        binding.nextButton.setOnClickListener {
            hideKeyboard()
            val panNumber = binding.panInputField.text?.toString() ?: ""
            if (panNumber.length > 5) {
                processCardNumber(panNumber)
            }
        }


    }

    fun onScanClicked(view: View) {

        requestPermissionsFromDevice()

        val scanIntent = Intent(this, CardIOActivity::class.java)
        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, false) // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false) // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false) // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true) // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true) // default: false

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE)
    }

    private fun processCardNumber(cardNumber: String) {

        if (connectionDetector.isConnectingToInternet()) {

            showLoading(getString(R.string.processing))

            requestHandler.getCardDetail(cardNumber.take(8), object : CardCallable {
                override fun onGetCard(cardResponse: CardResponse?) {
                    progressDialog?.dismiss()
                    cardResponse?.run { displayCardContent(this) }
                }

                override fun onGetCardError(error: ResponseBody?, responseCode: Int) {
                    progressDialog?.dismiss()

                }

                override fun onGetCardFailureCall(call: Call<CardResponse>?, t: Throwable?) {
                    progressDialog?.dismiss()

                }
            })
        } else {
            notifyUser(getString(R.string.no_internet_connection))
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_SCAN_REQUEST_CODE && data != null) {
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

    private fun displayCardContent(cardResponse: CardResponse) {
        when (cardResponse.success) {

            true -> {
                binding.bankData = cardResponse
            }
            false -> {
                binding.cardInformationLayout.visibility = View.GONE
                notifyUser(cardResponse.reason ?: getString(R.string.no_bin_number_was_found))
            }
        }

    }
}