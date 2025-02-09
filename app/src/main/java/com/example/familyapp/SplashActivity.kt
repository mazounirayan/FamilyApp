package com.example.familyapp

import UserRepository
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.familyapp.utils.LocalStorage
import com.example.familyapp.viewmodel.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var localStorage: LocalStorage
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Initialisation du LocalStorage
        localStorage = LocalStorage(this)

        val userRepository = UserRepository(this)
        userViewModel = UserViewModel(userRepository, this)

        lifecycleScope.launch {
            delay(2000)

            if (localStorage.isLoggedIn()) {
                val token = localStorage.getToken()
                if (token != null) {
                    observeUserByToken(token)
                } else {
                    navigateToAuthentication()
                }
            } else {
                navigateToAuthentication()
            }
        }
    }

    private fun observeUserByToken(token: String) {
        // Observez les données utilisateur récupérées par le token
        userViewModel.userDataByToken.observe(this) { user ->
            if (user != null) {
                navigateToMain()
            } else {
                navigateToAuthentication()
            }
        }

        // Observez les erreurs potentielles lors de la requête
        userViewModel.userByTokenResult.observe(this) { result ->
            result.fold(
                onSuccess = {
                },
                onFailure = { exception ->
                    navigateToAuthentication()
                }
            )
        }

        // Appel à la méthode `getUserByToken` dans le ViewModel
        userViewModel.getUserByToken(token)
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun navigateToAuthentication() {
        startActivity(Intent(this, AuthenticationActivity::class.java))
        finish()
    }
}
