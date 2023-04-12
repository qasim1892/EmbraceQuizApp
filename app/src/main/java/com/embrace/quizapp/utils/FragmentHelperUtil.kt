package com.embrace.quizapp.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

object FragmentHelperUtil {

    @JvmStatic
    fun replaceAndAddContentFragment(
        activity: AppCompatActivity?, containerId: Int, fragment: Fragment?, tag: String?
    ) {
        if (activity != null && !activity.isFinishing && fragment != null) {
            val manager = activity.supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.replace(containerId, fragment, tag)
            transaction.addToBackStack(tag)
            transaction.commitAllowingStateLoss()
        }
    }

    fun replaceContentFragment(
        activity: AppCompatActivity?, containerId: Int, fragment: Fragment?, tag: String?
    ) {
        if (activity != null && !activity.isFinishing && fragment != null) {
            activity.supportFragmentManager.beginTransaction().replace(containerId, fragment, tag)
                .commitAllowingStateLoss()
        }
    }
}