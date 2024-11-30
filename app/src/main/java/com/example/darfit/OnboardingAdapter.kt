// OnboardingAdapter.kt
package com.example.darfit

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3 // Количество экранов для пролистывания

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BlankFragment1()
            1 -> BlankFragment2()
            2 -> BlankFragment3()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}
