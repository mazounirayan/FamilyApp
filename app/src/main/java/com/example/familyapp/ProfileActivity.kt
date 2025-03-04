package com.example.familyapp

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.familyapp.views.PagerHandlerProfile
import com.example.familyapp.views.ViewPagerAdapteurProfile

class ProfileActivity : AppCompatActivity(), PagerHandlerProfile {

    private lateinit var familyAppPager: ViewPager2
//    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.viewPager)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setUpPager()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        toolbar.setNavigationOnClickListener {
            handleBackNavigation()
        }

        // backButton = findViewById(R.id.backButton)
        //  backButton.setOnClickListener {
        //   handleBackNavigation()
        //  }



        setUpPager()
    }
    private fun handleBackNavigation() {
        if (familyAppPager.currentItem == 0) {
            finish()
        } else {
            familyAppPager.currentItem -= 1
        }
    }

    private fun setUpPager() {
        this.familyAppPager = findViewById(R.id.viewPager)
        val pagerAdapter = ViewPagerAdapteurProfile(this)
        this.familyAppPager.adapter = pagerAdapter

        displayProfilePage()
    }

    override fun displayProfilePage() {
        this.familyAppPager.currentItem = 0
    }

    override fun displayRewardsPage() {
        this.familyAppPager.currentItem = 1
    }

    override fun displaySettingsPage() {
        this.familyAppPager.currentItem = 2
    }
}
