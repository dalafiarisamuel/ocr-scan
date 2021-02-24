package ng.mint.ocrscanner.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import ng.mint.ocrscanner.R
import ng.mint.ocrscanner.database.Database
import ng.mint.ocrscanner.databinding.FragmentCardInformationBinding
import ng.mint.ocrscanner.model.CardResult
import ng.mint.ocrscanner.networking.ConnectionDetector
import ng.mint.ocrscanner.viewmodel.CardViewModelFactory
import ng.mint.ocrscanner.viewmodel.CardsRepository
import ng.mint.ocrscanner.viewmodel.CardsViewModel
import ng.mint.ocrscanner.views.activities.BaseActivity
import ng.mint.ocrscanner.views.common.MessageDialogManager
import ng.mint.ocrscanner.views.common.ProgressDialogManager
import java.io.IOException

class CardInformationFragment : Fragment(R.layout.fragment_card_information) {

    companion object {

        const val MY_SCAN_REQUEST_CODE = 500
    }

    private val binding by viewBinding(FragmentCardInformationBinding::bind)
    private lateinit var activity: BaseActivity

    private lateinit var progressDialog: ProgressDialogManager
    private lateinit var messageDialog: MessageDialogManager
    private lateinit var internetConnection: ConnectionDetector
    private lateinit var viewModel: CardsViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity = getActivity() as BaseActivity

        progressDialog = activity.progressDialog
        messageDialog = activity.messageDialog
        internetConnection = activity.internetConnection


        binding.panInputField.doAfterTextChanged {

            binding.cardInformationLayout.visibility = View.GONE
        }

        binding.nextButton.setOnClickListener {
            activity.hideKeyboard()
            val panNumber = binding.panInputField.text.toString()
            if (panNumber.length > 5) {
                processCard(panNumber.take(8))
            }
        }

        binding.scanButton.setOnClickListener(this::onScanClicked)

        binding.recentCards.setOnClickListener {
            activity.hideKeyboard()
            it.findNavController()
                .navigate(R.id.action_cardInformationFragment_to_recentlyViewedCardsFragment)

        }

        val repository = CardsRepository(Database.getInstance(activity))

        val viewModelFactory =
            CardViewModelFactory(activity.application, repository)


        viewModel = ViewModelProvider(this, viewModelFactory).get(
            CardsViewModel::class.java
        )

        lifecycleScope.launchWhenCreated {
            viewModel.data.catch {
                if (this is IOException) emit(CardResult.Failure)
            }.collect { observeData(it) }
        }

    }

    private fun onCardActivityResult(requestCode: Int, data: Intent?) {

        if (requestCode == MY_SCAN_REQUEST_CODE && data != null) {
            val scanResult =
                data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT) as? CreditCard
            scanResult?.run {
                val panNumber = this.formattedCardNumber?.replace(" ", "") ?: ""
                if (panNumber.length > 5) {
                    processCard(panNumber)
                }
            }
        }

    }

    private fun observeData(cardResult: CardResult) {
        progressDialog.dismissDialog()
        if (cardResult is CardResult.Failure) {
            messageDialog.displayMessage(getString(R.string.no_bin_number_was_found))
        }
        updateValue(cardResult)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        onCardActivityResult(requestCode = requestCode, data = data)
    }

    private fun onScanClicked(@Suppress("UNUSED_PARAMETER") view: View) {

        val scanIntent = Intent(activity, CardIOActivity::class.java)
        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, false) // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false) // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false) // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true) // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true) // default: false

        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE)
    }

    private fun updateValue(cardResult: CardResult) {
        when (cardResult) {
            is CardResult.Success -> {
                binding.bankData = cardResult.data
            }
            is CardResult.Failure -> {
                binding.cardInformationLayout.visibility = View.GONE
            }
        }
    }

    private fun processCard(value: String) {

        when (internetConnection.isConnectingToInternet()) {
            true -> {
                progressDialog.showLoading(getString(R.string.processing))
                viewModel.processCardDetail(value)
            }

            false -> messageDialog.displayMessage(getString(R.string.no_internet_connection))

        }

    }


}