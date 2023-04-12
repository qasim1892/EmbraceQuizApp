package com.embrace.quizapp.features.home.ui

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.embrace.quizapp.R
import com.embrace.quizapp.base.BaseActivity
import com.embrace.quizapp.databinding.ActivityHomeBinding
import com.embrace.quizapp.utils.FragmentHelperUtil

class HomeActivity : BaseActivity() {

    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceContentFragment(HomeFragment())
    }


    private fun replaceContentFragment(fragment: Fragment) {
        FragmentHelperUtil.replaceContentFragment(
            activity = this,
            containerId = R.id.frame_layout,
            fragment = fragment,
            fragment.javaClass.simpleName
        )
    }

    fun replaceAndAddContentFragment(fragment: Fragment) {
        FragmentHelperUtil.replaceAndAddContentFragment(
            activity = this,
            containerId = R.id.frame_layout,
            fragment = fragment,
            fragment.javaClass.simpleName
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}