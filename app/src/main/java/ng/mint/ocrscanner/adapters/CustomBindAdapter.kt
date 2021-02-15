package ng.mint.ocrscanner.adapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import ng.mint.ocrscanner.capitalizeWords
import ng.mint.ocrscanner.model.CardResponse

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
}