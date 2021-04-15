package ng.mint.ocrscanner.views.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ng.mint.ocrscanner.R
import ng.mint.ocrscanner.callbacks.DataHandler
import ng.mint.ocrscanner.databinding.FragmentRecentlyViewedCardsBinding
import ng.mint.ocrscanner.model.RecentCard
import ng.mint.ocrscanner.viewmodel.CardsViewModel

@AndroidEntryPoint
class RecentlyViewedCardsFragment : Fragment(R.layout.fragment_recently_viewed_cards) {
    private val binding by viewBinding(FragmentRecentlyViewedCardsBinding::bind)

    private val viewModel: CardsViewModel by viewModels()

    private val dataHandler = object : DataHandler {
        override fun emitData(data: RecentCard) {
            val action =
                RecentlyViewedCardsFragmentDirections.actionRecentlyViewedCardsFragmentToRecentCardDetailFragment(
                    data
                )
            binding.savedRecyclerview.findNavController().navigate(action)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backArrow.setOnClickListener { it.findNavController().popBackStack() }

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel
        binding.dataHandler = dataHandler
    }
}