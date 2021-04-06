package ng.mint.ocrscanner.contracts

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard

class ScanCreditCardContract : ActivityResultContract<Int, CreditCard?>() {

    override fun createIntent(context: Context, input: Int?): Intent {

        return Intent(context, CardIOActivity::class.java).apply {
            putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, false) // default: false
            putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false) // default: false
            putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false) // default: false
            putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true) // default: false
            putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true) // default: false
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): CreditCard? {
        val scanResult =
            intent?.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT) as? CreditCard
        return when {
            resultCode == Activity.RESULT_OK && scanResult != null -> scanResult
            else -> null
        }
    }
}