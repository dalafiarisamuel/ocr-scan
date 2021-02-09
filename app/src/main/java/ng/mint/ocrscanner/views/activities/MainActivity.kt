package ng.mint.ocrscanner.views.activities

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import ng.mint.ocrscanner.R
import ng.mint.ocrscanner.callbacks.ProcessCardDetail
import ng.mint.ocrscanner.database.Database
import ng.mint.ocrscanner.databinding.ActivityMainBinding
import ng.mint.ocrscanner.model.CardResult
import ng.mint.ocrscanner.viewmodel.CardViewModelFactory
import ng.mint.ocrscanner.viewmodel.CardsRepository
import ng.mint.ocrscanner.viewmodel.CardsViewModel
import ng.mint.ocrscanner.views.mvc.MainActivityViewsMvc


class MainActivity : BaseActivity(), ProcessCardDetail {

    private lateinit var binding: ActivityMainBinding
    private lateinit var view: MainActivityViewsMvc
    private val repository: CardsRepository by lazy {
        CardsRepository(Database.getInstance(this))
    }

    private val viewModelFactory: CardViewModelFactory by lazy {
        CardViewModelFactory(application, repository)
    }
    private val viewModel: CardsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            CardsViewModel::class.java
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissionsFromDevice()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        view = MainActivityViewsMvc(this, binding, this)
        setLightStatusBar(binding.root)
        viewModel.data.observe(this, this::observeData)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        view.onActivityResult(requestCode = requestCode, resultCode = resultCode, data = data)
    }

    private fun observeData(cardResult: CardResult) {
        progressDialog.dismissDialog()
        if (cardResult is CardResult.Failure) {
            messageDialog.displayMessage(getString(R.string.no_bin_number_was_found))
        }
        view.updateValue(cardResult)

    }

    override fun processCard(value: String) {

        when (internetConnection.isConnectingToInternet()) {
            true -> {
                progressDialog.showLoading(getString(R.string.processing))
                viewModel.processCardDetail(value)
            }

            false -> messageDialog.displayMessage(getString(R.string.no_internet_connection))

        }

    }


}

