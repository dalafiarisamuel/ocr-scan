package ng.mint.ocrscanner.adapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import ng.mint.ocrscanner.model.CardResponse

object CustomBindAdapter {

    @BindingAdapter("setCardScheme", "placeHolder", requireAll = false)
    fun TextView.setCardScheme(data: CardResponse?, placeHolder: String) {
        this.text = data?.scheme ?: placeHolder
    }
}