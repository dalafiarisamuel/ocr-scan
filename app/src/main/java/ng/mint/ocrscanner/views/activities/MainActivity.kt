package ng.mint.ocrscanner.views.activities

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import ng.mint.ocrscanner.R
import ng.mint.ocrscanner.database.Database
import ng.mint.ocrscanner.databinding.ActivityMainBinding
import ng.mint.ocrscanner.viewmodel.CardViewModelFactory
import ng.mint.ocrscanner.viewmodel.CardsRepository
import ng.mint.ocrscanner.viewmodel.CardsViewModel


class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private val repository: CardsRepository by lazy {
        CardsRepository(Database.getInstance(this))
    }

    private val viewModelFactory: CardViewModelFactory by lazy {
        CardViewModelFactory(application, repository)
    }
    private lateinit var viewModel: CardsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissionsFromDevice()

        viewModel = ViewModelProvider(this, viewModelFactory).get(
            CardsViewModel::class.java
        )


        binding = ActivityMainBinding.inflate(layoutInflater)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        setContentView(binding.root)
        setLightStatusBar(binding.root)


    }

}

