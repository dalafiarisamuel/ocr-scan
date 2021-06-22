package ng.mint.ocrscanner.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ng.mint.ocrscanner.R
import ng.mint.ocrscanner.adapters.CustomBindAdapter.setCustomText
import ng.mint.ocrscanner.callbacks.DataHandler
import ng.mint.ocrscanner.model.RecentCard


class RecentCardsAdapter(private val caller: DataHandler) :
    ListAdapter<RecentCard, RecentCardsAdapter.RecentCardHolder>(DiffCallRecentCard()) {

    fun addData(data: List<RecentCard>) = submitList(data)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentCardHolder {

        return RecentCardHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recent_cards_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecentCardHolder, position: Int) =
        holder.bind(currentList[position])

    override fun getItemCount(): Int = currentList.size

    fun getItemAt(position: Int): RecentCard? = currentList[position]

    inner class RecentCardHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val bin: TextView = view.findViewById(R.id.bin)
        private val cardCountry: TextView = view.findViewById(R.id.card_country)
        private val cardPhone: TextView = view.findViewById(R.id.card_phone)
        private val cardType: TextView = view.findViewById(R.id.card_type)
        private val cardView: CardView = view.findViewById(R.id.root_layout)

        fun bind(data: RecentCard) {
            bin.setCustomText("${data.type}, ${data.bin}")
            cardCountry.setCustomText("${data.emoji} ${data.country}")
            cardPhone.setCustomText(data.phone)
            cardType.setCustomText("${data.scheme}, ${data.currency}")
            cardView.setOnClickListener { caller.emitData(data) }
        }

    }

    class DiffCallRecentCard : DiffUtil.ItemCallback<RecentCard>() {

        override fun areItemsTheSame(oldItem: RecentCard, newItem: RecentCard): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RecentCard, newItem: RecentCard): Boolean {
            return oldItem == newItem
        }
    }
}