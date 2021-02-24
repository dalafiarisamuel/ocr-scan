package ng.mint.ocrscanner.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class CardViewModelFactory(private val app: Application, private val repository: CardsRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CardsViewModel(app, repository) as T
    }

}