package ng.mint.ocrscanner.views.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ng.mint.ocrscanner.R
import ng.mint.ocrscanner.adapters.RecentCardsAdapter
import ng.mint.ocrscanner.database.Database
import ng.mint.ocrscanner.databinding.FragmentRecentlyViewedCardsBinding
import ng.mint.ocrscanner.model.RecentCard
import ng.mint.ocrscanner.model.RecentCardsState
import ng.mint.ocrscanner.viewmodel.CardViewModelFactory
import ng.mint.ocrscanner.viewmodel.CardsRepository
import ng.mint.ocrscanner.viewmodel.CardsViewModel
import ng.mint.ocrscanner.views.activities.BaseActivity

class RecentlyViewedCardsFragment : Fragment(R.layout.fragment_recently_viewed_cards) {
    private val binding by viewBinding(FragmentRecentlyViewedCardsBinding::bind)

    private lateinit var viewModel: CardsViewModel
    private lateinit var activity: BaseActivity


    private val adapter = RecentCardsAdapter {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity = getActivity() as BaseActivity

        binding.backArrow.setOnClickListener {
            it.findNavController().popBackStack()
        }


        val layoutMan = LinearLayoutManager(context)
        binding.savedRecyclerview.layoutManager = layoutMan
        binding.savedRecyclerview.adapter = adapter

        val repository = CardsRepository(Database.getInstance(activity))

        val viewModelFactory =
            CardViewModelFactory(activity.application, repository)


        viewModel = ViewModelProvider(this, viewModelFactory).get(
            CardsViewModel::class.java
        )

        viewModel.getRecentCardDataListLive().observe(viewLifecycleOwner, this::observeData)
    }

    private fun observeData(data: List<RecentCard>) {
        if (data.isEmpty()) setData(RecentCardsState.EmptyList)
        else setData(RecentCardsState.RecentCardList(data.toMutableList()))
    }

    private fun setData(dataState: RecentCardsState) {
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