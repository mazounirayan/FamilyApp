package com.example.familyapp.views

import RewardsFragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.familyapp.pages.interfaces.PagerHandler
import com.example.familyapp.views.fragments.Settings.ProfileFragment
import com.example.familyapp.views.fragments.Settings.SettingsFragment

class ViewPagerAdapteurProfile(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 3 // 3 pages

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProfileFragment()
            1 -> RewardsFragment()
            2 -> SettingsFragment()
            else -> ProfileFragment() // Par défaut, afficher la page du profil
        }
    }
}

