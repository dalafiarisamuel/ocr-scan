package ng.mint.ocrscanner.views.fragments

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ng.mint.ocrscanner.R
import ng.mint.ocrscanner.contracts.ScanCreditCardContract
import ng.mint.ocrscanner.databinding.FragmentCardInformationBinding
import ng.mint.ocrscanner.model.CardResult
import ng.mint.ocrscanner.networking.ConnectionDetector
import ng.mint.ocrscanner.viewmodel.CardsViewModel
import ng.mint.ocrscanner.views.activities.BaseActivity
import ng.mint.ocrscanner.views.common.MessageDialogManager
import ng.mint.ocrscanner.views.common.ProgressDialogManager
import javax.inject.Inject

@AndroidEntryPoint
class CardInformationFragment(
    var viewModel: CardsViewModel? = null
) : Fragment(R.layout.fragment_card_information) {

    companion object {

        const val MY_SCAN_REQUEST_CODE = 500
    }

    val binding by viewBinding(FragmentCardInformationBinding::bind)

    @Inject
    lateinit var connectionDetector: ConnectionDetector

    @Inject
    lateinit var progressDialog: ProgressDialogManager

    @Inject
    lateinit var messageDialog: MessageDialogManager

    private val openScanCreditCard = registerForActivityResult(ScanCreditCardContract()) {

        it?.run {
            val panNumber = this.formattedCardNumber?.replace(" ", "") ?: ""
            if (panNumber.length > 5) {
                processCard(panNumber)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = viewModel ?: ViewModelProvider(this)[CardsViewModel::class.java]
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.panInputField.doAfterTextChanged {

            binding.cardInformationLayout.visibility = View.GONE
        }

        binding.nextButton.setOnClickListener {
            (activity as? BaseActivity)?.hideKeyboard()
            val panNumber = binding.panInputField.text.toString()
            if (panNumber.length > 5) {
                processCard(panNumber.take(8))
            }
        }

        binding.scanButton.setOnClickListener { openScanCreditCard.launch(MY_SCAN_REQUEST_CODE) }

        binding.recentCards.setOnClickListener {
            (activity as? BaseActivity)?.hideKeyboard()
            it.findNavController()
                .navigate(R.id.action_cardInformationFragment_to_recentlyViewedCardsFragment)

        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel?.cardResult?.collectLatest { observeData(it) }
            }
        }


    }

    private fun observeData(cardResult: CardResult) {
        binding.nextButton.isClickable = true
        progressDialog.dismissDialog()
        if (cardResult is CardResult.Failure) {
            messageDialog.displayMessage(getString(R.string.no_bin_number_was_found))
        }
        updateValue(cardResult)

    }

    private fun updateValue(cardResult: CardResult) {
        when (cardResult) {
            is CardResult.Success -> {
                binding.bankData = cardResult.data
            }
            is CardResult.EmptyState, CardResult.Failure -> {
                binding.cardInformationLayout.visibility = View.GONE
            }
        }
    }

    private fun processCard(value: String) {

        when {
            connectionDetector.isConnectingToInternet() -> {
                binding.nextButton.isClickable = false
                progressDialog.showLoading(getString(R.string.processing))
                viewModel?.processCardDetail(value)
            }

            else -> {
                messageDialog.displayMessage(
                    String.format(getString(R.string.no_internet_card_saved_for_future), value)
                )
                viewModel?.insertOfflineCard(value)

                binding.panInputField.text = null

            }

        }

    }


}