package ng.mint.ocrscanner.views.fragments

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ng.mint.ocrscanner.database.Database
import ng.mint.ocrscanner.viewmodel.CardViewModelFactory
import ng.mint.ocrscanner.viewmodel.CardsRepository
import ng.mint.ocrscanner.viewmodel.CardsViewModel
import ng.mint.ocrscanner.views.activities.BaseActivity

abstract class BaseFragment() : Fragment() {

    protected var activity: BaseActivity = getActivity() as BaseActivity

    protected val progressDialog get() = activity.progressDialog
    protected val messageDialog get() = activity.messageDialog
    protected val internetConnection get() = activity.internetConnection

    private val repository: CardsRepository by lazy {
        CardsRepository(Database.getInstance(activity))
    }

    private val viewModelFactory: CardViewModelFactory by lazy {
        CardViewModelFactory(activity.application, repository)
    }

    protected val viewModel: CardsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            CardsViewModel::class.java
        )
    }
}