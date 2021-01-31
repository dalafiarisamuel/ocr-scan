package ng.mint.ocrscanner

import android.content.Intent
import android.os.Bundle
import ng.mint.ocrscanner.databinding.ActivityMainBinding
import ng.mint.ocrscanner.views.activities.BaseActivity
import ng.mint.ocrscanner.views.presenter.MainActivityView


class MainActivity : BaseActivity() {

    companion object {
        const val MY_SCAN_REQUEST_CODE = 100
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var view: MainActivityView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        view = MainActivityView(this, binding)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        view.onActivityResult(requestCode = requestCode, resultCode = resultCode, data = data)
    }


}