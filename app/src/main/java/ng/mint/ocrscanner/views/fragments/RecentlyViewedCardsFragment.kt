package ng.mint.ocrscanner.views.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import ng.mint.ocrscanner.R
import ng.mint.ocrscanner.adapters.RecentCardsAdapter
import ng.mint.ocrscanner.databinding.FragmentRecentlyViewedCardsBinding
import ng.mint.ocrscanner.model.RecentCard
import ng.mint.ocrscanner.model.RecentCardsState
import ng.mint.ocrscanner.viewmodel.CardsViewModel
import ng.mint.ocrscanner.views.activities.BaseActivity

@AndroidEntryPoint
class RecentlyViewedCardsFragment : Fragment(R.layout.fragment_recently_viewed_cards) {
    private val binding by viewBinding(FragmentRecentlyViewedCardsBinding::bind)

    private val viewModel: CardsViewModel by viewModels()
    private lateinit var activity: BaseActivity

    private val adapter = RecentCardsAdapter {

        val action =
            RecentlyViewedCardsFragmentDirections.actionRecentlyViewedCardsFragmentToRecentCardDetailFragment(
                it
            )
        binding.savedRecyclerview.findNavController().navigate(action)

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
        binding.lifecycleOwner = viewLifecycleOwner


        lifecycleScope.launchWhenCreated {
            viewModel.getRecentCardDataListLive().catch {
                emit(mutableListOf())
            }.collect { observeData(it) }
        }
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