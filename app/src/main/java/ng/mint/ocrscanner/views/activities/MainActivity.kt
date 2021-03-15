package ng.mint.ocrscanner.views.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import ng.mint.ocrscanner.R
import ng.mint.ocrscanner.databinding.ActivityMainBinding
import ng.mint.ocrscanner.viewmodel.CardsViewModel

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: CardsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissionsFromDevice()

        binding = ActivityMainBinding.inflate(layoutInflater)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        viewModel
        navController

        setContentView(binding.root)
        setLightStatusBar(binding.root)


    }

}

