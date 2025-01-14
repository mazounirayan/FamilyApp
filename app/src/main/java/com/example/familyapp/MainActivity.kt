package com.example.familyapp

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.familyapp.views.fragments.RewardsFragment
class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager)
        setupViewPager()
        setupNavigationClicks()
    }

    private fun setupViewPager() {
        val pagerAdapter = MainPagerAdapter(this)
        viewPager.adapter = pagerAdapter


        viewPager.isUserInputEnabled = false
    }

    private fun setupNavigationClicks() {
        findViewById<ImageView>(R.id.home_icon).setOnClickListener {
            viewPager.currentItem = 0
        }

        findViewById<ImageView>(R.id.messages_icon).setOnClickListener {
            viewPager.currentItem = 1
        }

        findViewById<ImageView>(R.id.add_icon).setOnClickListener {
            viewPager.currentItem = 2
        }

        findViewById<ImageView>(R.id.list_icon).setOnClickListener {
            viewPager.currentItem = 3
        }
    }
}

class MainPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> HomeFragment()
            2 -> RewardsFragment().apply {
                // role pour l'onglet Récompenses (admin)
                arguments = Bundle().apply {
                    putString("role", "admin")
                }
            }
            3 -> RewardsFragment().apply {
                // Role pour l'onglet Récompenses (user)
                arguments = Bundle().apply {
                    putString("role", "user")
                }
            }
            else -> HomeFragment()
        }
    }
}
