package ng.mint.ocrscanner.views.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ng.mint.ocrscanner.R
import ng.mint.ocrscanner.databinding.FragmentRecentCardDetailBinding

@AndroidEntryPoint
class RecentCardDetailFragment : Fragment(R.layout.fragment_recent_card_detail) {

    private val binding by viewBinding(FragmentRecentCardDetailBinding::bind)
    private val args: RecentCardDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val data = args.recentCard
        binding.recentCard = data

        binding.backArrow.setOnClickListener {
            it.findNavController().popBackStack()
        }

    }
}