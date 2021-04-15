package ng.mint.ocrscanner.adapters

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import ng.mint.ocrscanner.callbacks.DataHandler
import ng.mint.ocrscanner.capitalizeWords
import ng.mint.ocrscanner.model.CardResponse
import ng.mint.ocrscanner.model.RecentCardsState

object CustomBindAdapter {

    @JvmStatic
    @BindingAdapter("setCardScheme", "placeHolder", requireAll = false)
    fun TextView.setCardScheme(data: CardResponse?, placeHolder: String) {
        this.text = data?.scheme ?: placeHolder
    }

    @JvmStatic
    @BindingAdapter("customText", requireAll = false)
    fun TextView.setCustomText(customText: String?) {
        this.text = customText?.capitalizeWords()
    }

    @JvmStatic
    @BindingAdapter("dataState", "dataHandler", requireAll = true)
    fun RecyclerView.setData(dataState: RecentCardsState, dataHandler: DataHandler) {

        when (dataState) {
            is RecentCardsState.EmptyList -> {
                this.visibility = View.GONE
            }
            is RecentCardsState.RecentCardList -> {
                this.visibility = View.VISIBLE
                when (val currentAdapter = this.adapter as? RecentCardsAdapter) {
                    null -> {
                        val adapter = RecentCardsAdapter(dataHandler)
                        this.adapter = adapter
                        adapter.addData(dataState.data)
                    }
                    else -> {
                        currentAdapter.addData(dataState.data)
                    }

                }

            }
        }

    }

    @JvmStatic
    @BindingAdapter("dataStateVisibility", requireAll = false)
    fun View.dataUiState(dataState: RecentCardsState) {
        when (dataState) {
            is RecentCardsState.EmptyList -> {
                this.visibility = View.VISIBLE
            }
            is RecentCardsState.RecentCardList -> {
                this.visibility = View.GONE
            }
        }

    }
}