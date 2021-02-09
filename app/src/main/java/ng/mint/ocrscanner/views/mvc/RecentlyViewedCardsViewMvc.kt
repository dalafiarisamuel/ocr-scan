package ng.mint.ocrscanner.views.mvc

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import ng.mint.ocrscanner.adapters.RecentCardsAdapter
import ng.mint.ocrscanner.databinding.ActivityRecentCardsBinding
import ng.mint.ocrscanner.model.RecentCardsState
import ng.mint.ocrscanner.views.activities.RecentCardsActivity

class RecentlyViewedCardsViewMvc(
    context: Context,
    private val binding: ActivityRecentCardsBinding
) {

    private val activity = context as RecentCardsActivity

    private val adapter = RecentCardsAdapter {

    }

    init {

        binding.backArrow.setOnClickListener {
            activity.onBackPressed()
        }

        val layoutMan = LinearLayoutManager(context)
        binding.savedRecyclerview.layoutManager = layoutMan
        binding.savedRecyclerview.adapter = adapter
    }

    fun setData(dataState: RecentCardsState) {
        when (dataState) {
            is RecentCardsState.EmptyList -> {
                binding.noResultDisplay.visibility = View.VISIBLE
                binding.savedRecyclerview.visibility = View.GONE
            }
            is RecentCardsState.RecentCardList -> {
                binding.noResultDisplay.visibility = View.GONE
                binding.savedRecyclerview.visibility = View.VISIBLE
                adapter.addData(dataState.data)
            }
        }

    }

}