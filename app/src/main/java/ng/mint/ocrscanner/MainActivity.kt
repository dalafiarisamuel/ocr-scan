package ng.mint.ocrscanner

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard
import kotlinx.android.synthetic.main.activity_main.*
import ng.mint.ocrscanner.callbacks.CardCallable
import ng.mint.ocrscanner.model.CardResponse
import ng.mint.ocrscanner.views.BaseActivity
import okhttp3.ResponseBody
import retrofit2.Call


class MainActivity : BaseActivity() {

    companion object {
        const val MY_SCAN_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        makeActivityFullScreen(this.window)
        changeStatusBarColorToTransparent(this.window)
        setLightStatusBar(relativeLayout)

        panInputField?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                card_information_layout.visibility = View.GONE
            }
        })

        nextButton.setOnClickListener {
            hideKeyboard()
            val panNumber = panInputField.text?.toString() ?: ""
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

        showLoading(getString(R.string.processing))

        if (connectionDetector.isConnectingToInternet()) {

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
                card_information_layout.visibility = View.VISIBLE

                card_scheme?.text = cardResponse.scheme
                card_type?.text = cardResponse.type
                card_level?.text = cardResponse.category
                card_country?.text =
                    (cardResponse.country?.emoji ?: "").plus(" ")
                        .plus(cardResponse.country?.name ?: "")
                card_bank?.text = cardResponse.bank?.name
            }
            false -> {
                card_information_layout.visibility = View.GONE
                notifyUser(cardResponse.reason ?: getString(R.string.no_bin_number_was_found))
            }
        }

    }
}