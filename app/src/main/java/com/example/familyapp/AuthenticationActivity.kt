package com.example.familyapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.familyapp.pages.interfaces.PagerHandler
import com.example.familyapp.pages.views.ViewPagerAdapter

class AuthenticationActivity : AppCompatActivity(), PagerHandler {

    private lateinit var familyAppPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_authentication)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.viewPager)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUpPager()
    }

    private fun setUpPager() {

        this.familyAppPager = findViewById(R.id.viewPager)
        val pagerAdapter = ViewPagerAdapter(this, this)
        this.familyAppPager.adapter = pagerAdapter

        displaySelectionPage()
    }

    fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun displaySelectionPage() {
        this.familyAppPager.currentItem = 0
    }



    override fun displayLoginPage() {
        this.familyAppPager.currentItem = 1
    }

    override fun displaySignInPage() {
        this.familyAppPager.currentItem = 2
    }


    override fun navigateToLoginFragment() {
        this.familyAppPager.currentItem = 1
    }

}
