package ng.mint.ocrscanner.views.mvc

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.widget.doAfterTextChanged
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard
import ng.mint.ocrscanner.MainActivity
import ng.mint.ocrscanner.callbacks.ProcessCardDetail
import ng.mint.ocrscanner.databinding.ActivityMainBinding
import ng.mint.ocrscanner.model.CardResult
import ng.mint.ocrscanner.views.activities.BaseActivity

class MainActivityViewsMvc(
    context: Context,
    private val binding: ActivityMainBinding,
    private val process: ProcessCardDetail
) {

    private var activity: BaseActivity = context as BaseActivity

    init {

        binding.panInputField.doAfterTextChanged {

            binding.cardInformationLayout.visibility = View.GONE
        }

        binding.nextButton.setOnClickListener {
            activity.hideKeyboard()
            val panNumber = binding.panInputField.text?.toString() ?: ""
            if (panNumber.length > 5) {
                process.processCard(panNumber.take(8))
            }
        }

        binding.scanButton.setOnClickListener(this::onScanClicked)

    }


    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == MainActivity.MY_SCAN_REQUEST_CODE && data != null) {
            val scanResult =
                data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT) as? CreditCard
            scanResult?.run {
                val panNumber = this.formattedCardNumber?.replace(" ", "") ?: ""
                if (panNumber.length > 5) {
                    process.processCard(panNumber)
                }
            }
        }

    }

    private fun onScanClicked(view: View) {

        activity.requestPermissionsFromDevice()

        val scanIntent = Intent(activity, CardIOActivity::class.java)
        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, false) // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false) // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false) // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true) // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true) // default: false

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        activity.startActivityForResult(scanIntent, MainActivity.MY_SCAN_REQUEST_CODE)
    }

    fun updateValue(cardResult: CardResult) {
        when (cardResult) {
            is CardResult.Success -> {
                binding.bankData = cardResult.data
            }
            is CardResult.Failure -> {
                binding.cardInformationLayout.visibility = View.GONE
            }
        }
    }

}