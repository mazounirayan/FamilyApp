package com.example.familyapp

import RewardsFragment
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.familyapp.views.fragments.Settings.*
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.familyapp.views.Holders.PagerHandlerProfile
import com.example.familyapp.views.ViewPagerAdapteurProfile



class ProfileActivity : AppCompatActivity(), PagerHandlerProfile {

    private lateinit var familyAppPager: ViewPager2
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            Toast.makeText(this, "Bouton retour cliqué", Toast.LENGTH_SHORT).show()
            handleBackNavigation()
        }



        // Gestion du bouton retour physique avec OnBackPressedDispatcher
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackNavigation()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        // Gestion des insets pour un affichage correct avec le ViewPager
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.viewPager)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUpPager()
    }

    private fun setUpPager() {
        familyAppPager = findViewById(R.id.viewPager)
        val pagerAdapter = ViewPagerAdapteurProfile(this)
        familyAppPager.adapter = pagerAdapter
        displayProfilePage()
    }


    private fun handleBackNavigation() {
        if (familyAppPager.currentItem == 0) {
            finish()
        } else {
            familyAppPager.currentItem -= 1
        }
    }


    // Méthodes pour naviguer entre les pages
    override fun displayProfilePage() {
        familyAppPager.currentItem = 0
    }

    override fun displayRewardsPage() {
        familyAppPager.currentItem = 1
    }

    override fun displaySettingsPage() {
        familyAppPager.currentItem = 2
    }
}

