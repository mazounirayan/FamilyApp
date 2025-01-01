package com.example.familyapp.pages.views

import LoginFragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.familyapp.pages.fragments.SelectionFragment
import com.example.familyapp.pages.fragments.SignInFragment
import com.example.familyapp.pages.interfaces.PagerHandler

class ViewPagerAdapter(activity: AppCompatActivity, private val pagerHandler: PagerHandler) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 3 // Nombre de pages (Selection, Login, SignIn)

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SelectionFragment(pagerHandler)
            1 -> LoginFragment()
            2 -> SignInFragment()
            else -> throw IllegalStateException("Position inconnue : $position")
        }
    }
}
