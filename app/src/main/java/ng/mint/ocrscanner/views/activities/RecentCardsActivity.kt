package ng.mint.ocrscanner.views.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import ng.mint.ocrscanner.database.Database
import ng.mint.ocrscanner.databinding.ActivityRecentCardsBinding
import ng.mint.ocrscanner.model.RecentCard
import ng.mint.ocrscanner.model.RecentCardsState
import ng.mint.ocrscanner.viewmodel.CardViewModelFactory
import ng.mint.ocrscanner.viewmodel.CardsRepository
import ng.mint.ocrscanner.viewmodel.CardsViewModel
import ng.mint.ocrscanner.views.mvc.RecentlyViewedCardsViewMvc

class RecentCardsActivity : BaseActivity() {

    companion object {

        fun start(context: Context) = Intent(context, RecentCardsActivity::class.java)

    }

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

    private lateinit var viewMvc: RecentlyViewedCardsViewMvc

    private lateinit var binding: ActivityRecentCardsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecentCardsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewMvc = RecentlyViewedCardsViewMvc(this, binding)
        setLightStatusBar(binding.root)
        viewModel.getRecentCardDataListLive().observe(this, this::observeData)

    }

    private fun observeData(data: List<RecentCard>) {
        if (data.isEmpty()) viewMvc.setData(RecentCardsState.EmptyList)
        else viewMvc.setData(RecentCardsState.RecentCardList(data.toMutableList()))
    }

    override fun onBackPressed() {
        finish()
    }
}