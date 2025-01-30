package com.example.familyapp

import RewardsFragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.commit
import com.example.familyapp.viewmodel.ProfileViewModel
import com.example.familyapp.views.fragments.Settings.ChangeRoleFragment
import com.example.familyapp.views.fragments.Settings.EditProfileFragment
import com.example.familyapp.views.fragments.Settings.SettingsFragment
import com.example.familyapp.views.fragments.Settings.ShareCodeFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initializeTiles()
        setupLogoutButton()
    }

    private fun setupTile(view: View, profileTile: ProfileTile, action: () -> Unit) {
        val textView = view.findViewById<TextView>(R.id.txtTitle)
        val imageView = view.findViewById<ImageView>(R.id.imgIcon)

        // Afficher le titre
        textView?.text = profileTile.title

        // Récupérer et afficher l'icône
        val iconResId = resources.getIdentifier(profileTile.iconName, "drawable", packageName)
        if (iconResId != 0) {
            imageView?.setImageResource(iconResId)
        } else {
            Log.e("setupTile", "Icon resource not found: ${profileTile.iconName}")
            imageView?.setImageResource(R.drawable.ic_default_icon)
        }

        // Gérer l'action au clic
        view.setOnClickListener { action() }
    }

    private fun initializeTiles() {
        val editProfileTile = ProfileTile("Modifier le profil", "ic_person")
        val changeThemeTile = ProfileTile("Changer le thème", "ic_brightness_6")
        val shareCodeTile = ProfileTile("Partager le code d'invitation", "ic_share")
        val settingsTile = ProfileTile("Paramètres de l'application", "ic_settings")
        val changeRoleTile = ProfileTile("Changer le rôle d'un enfant", "ic_supervisor_account")
        val rewardsTile = ProfileTile("Voir toutes les récompenses", "ic_card_giftcard")

        setupTile(findViewById(R.id.tileEditProfile), editProfileTile) {
            // Action pour modifier le profil
            navigateToEditProfile()
        }

        setupTile(findViewById(R.id.tileChangeTheme), changeThemeTile) {
            // Action pour changer le thème
            showThemeDialog()
        }

        setupTile(findViewById(R.id.tileShareCode), shareCodeTile) {
            // Action pour partager le code
            shareInvitationCode()
        }

        setupTile(findViewById(R.id.tileSettings), settingsTile) {
            // Action pour les paramètres
            navigateToSettings()
        }

        setupTile(findViewById(R.id.tileChangeRole), changeRoleTile) {
            // Action pour changer le rôle
            navigateToChangeRole()
        }

        setupTile(findViewById(R.id.tileRewards), rewardsTile) {
            // Action pour voir les récompenses
            navigateToRewards()
        }
    }

    private fun setupLogoutButton() {
        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            // Action pour la déconnexion
            logout()
        }
    }

    // Fonctions de navigation et d'actions
    private fun navigateToEditProfile() {
        val fragment = EditProfileFragment()
        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, fragment)
            addToBackStack(null)
        }
    }

    private fun showThemeDialog() {
        val items = arrayOf("Mode Clair", "Mode Sombre", "Système")
        MaterialAlertDialogBuilder(this)
            .setTitle("Choisir le thème")
            .setItems(items) { _, which ->
                when (which) {
                    0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
                // Sauvegarder la préférence
                getSharedPreferences("app_settings", MODE_PRIVATE).edit()
                    .putInt("theme_mode", which)
                    .apply()
            }
            .show()
    }

    private fun shareInvitationCode() {
        // Supposons que nous avons un code d'invitation unique
        val inviteCode = "ABC123" // À remplacer par votre logique de génération de code

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Rejoignez-moi avec ce code d'invitation : $inviteCode")
        }
        startActivity(Intent.createChooser(shareIntent, "Partager via"))
    }

    private fun navigateToSettings() {
        val fragment = SettingsFragment()
        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, fragment)
            addToBackStack(null)
        }
    }

    private fun navigateToChangeRole() {
        val fragment = ChangeRoleFragment()
        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, fragment)
            addToBackStack(null)
        }
    }

    private fun navigateToRewards() {
        val fragment = RewardsFragment()
        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, fragment)
            addToBackStack(null)
        }
    }

    private fun logout() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Déconnexion")
            .setMessage("Êtes-vous sûr de vouloir vous déconnecter ?")
            .setPositiveButton("Déconnexion") { _, _ ->
                // Effacer les données de session
                getSharedPreferences("user_session", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply()

                // Naviguer vers l'écran de connexion
                val intent = Intent(this, AuthenticationActivity::class.java)
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Annuler", null)
            .show()
    }
}

data class ProfileTile(
    val title: String,
    val iconName: String
)