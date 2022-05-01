package ng.mint.ocrscanner.views.fragments

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ng.mint.ocrscanner.R
import ng.mint.ocrscanner.adapters.RecentCardsAdapter
import ng.mint.ocrscanner.callbacks.DataHandler
import ng.mint.ocrscanner.callbacks.SwipeToDeleteCallback
import ng.mint.ocrscanner.databinding.FragmentRecentlyViewedCardsBinding
import ng.mint.ocrscanner.model.RecentCard
import ng.mint.ocrscanner.viewmodel.CardsViewModel

@AndroidEntryPoint
class RecentlyViewedCardsFragment(
    var viewModel: CardsViewModel? = null
) : Fragment(R.layout.fragment_recently_viewed_cards) {
    val binding by viewBinding(FragmentRecentlyViewedCardsBinding::bind)


    val dataHandler = object : DataHandler {
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

        viewModel = viewModel ?: ViewModelProvider(this)[CardsViewModel::class.java]

        binding.backArrow.setOnClickListener { it.findNavController().popBackStack() }

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel
        binding.dataHandler = dataHandler

        val swipeToDeleteCallback = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val adapter = binding.savedRecyclerview.adapter as? RecentCardsAdapter
                adapter?.run {
                    val data = this.getItemAt(viewHolder.adapterPosition)
                    if (data != null) {
                        viewModel?.deleteRecentCard(data)
                    }
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.savedRecyclerview)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)

    }
}