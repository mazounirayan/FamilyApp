package com.example.familyapp.views.fragments

import UserRepository
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.familyapp.AuthenticationActivity
import com.example.familyapp.MainActivity
import com.example.familyapp.R
import com.example.familyapp.utils.LocalStorage
import com.example.familyapp.utils.SessionManager
import com.example.familyapp.viewmodel.UserViewModel
import com.example.familyapp.viewmodel.factories.UserViewModelFactory
import com.example.familyapp.views.PagerHandlerProfile
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay

class ProfileFragment : Fragment() {

    private lateinit var localStorage: LocalStorage

    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserRepository(this.requireContext()),this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        localStorage = LocalStorage(requireContext())
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeTiles(view)
        setupLogoutButton(view)
    }

    private fun setupTile(view: View, tileId: Int, title: String, iconName: String, action: () -> Unit) {
        val tileView = view.findViewById<View>(tileId)
        val textView = tileView.findViewById<TextView>(R.id.txtTitle)
        val imageView = tileView.findViewById<ImageView>(R.id.imgIcon)

        textView.text = title
        val iconResId = resources.getIdentifier(iconName, "drawable", requireContext().packageName)

        if (iconResId != 0) {
            imageView.setImageResource(iconResId)
        } else {
            Log.e("setupTile", "Icône introuvable : $iconName")
            imageView.setImageResource(R.drawable.ic_default_icon)
        }

        tileView.setOnClickListener { action() }
    }

    private fun initializeTiles(view: View) {
        setupTile(view, R.id.tileEditProfile, "Modifier le profil", "ic_person") {
            navigateToProfilePage()
        }
        setupTile(view, R.id.tileChangeTheme, "Changer le thème", "ic_brightness_6") {
            showThemeDialog()
        }
        setupTile(view, R.id.tileShareCode, "Partager le code d'invitation", "ic_share") {
            shareInvitationCode()
        }
        setupTile(view, R.id.tileSettings, "Paramètres", "ic_settings") {
            navigateToSettingsPage()
        }
        setupTile(view, R.id.tileChangeRole, "Changer le rôle", "ic_supervisor_account") {
            navigateToChangeRolePage()
        }
        setupTile(view, R.id.tileRewards, "Récompenses", "ic_card_giftcard") {
            navigateToRewardsPage()
        }
    }

    private fun setupLogoutButton(view: View) {
        view.findViewById<View>(R.id.btnLogout).setOnClickListener {
            logout()
        }
    }

    private fun navigateToProfilePage() {
        (requireActivity() as? PagerHandlerProfile)?.displayProfilePage()
    }

    private fun navigateToRewardsPage() {
        (requireActivity() as? PagerHandlerProfile)?.displayRewardsPage()
    }

    private fun navigateToSettingsPage() {
        (requireActivity() as? PagerHandlerProfile)?.displaySettingsPage()
    }

    private fun navigateToChangeRolePage() {
        Toast.makeText(requireContext(), "Naviguer vers le changement de rôle", Toast.LENGTH_SHORT).show()
    }

    private fun showThemeDialog() {
        val items = arrayOf("Mode Clair", "Mode Sombre", "Système")
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Choisir le thème")
            .setItems(items) { _, which ->
                when (which) {
                    0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
                requireContext().getSharedPreferences("app_settings", AppCompatActivity.MODE_PRIVATE)
                    .edit()
                    .putInt("theme_mode", which)
                    .apply()
            }
            .show()
    }

    private fun shareInvitationCode() {
        val inviteCode = "ABC123"
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Rejoignez-moi avec ce code d'invitation : $inviteCode")
        }
        startActivity(Intent.createChooser(shareIntent, "Partager via"))
    }

    private fun logout() {
     //   sessionManager = SessionManager(requireContext())

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Déconnexion")
            .setMessage("Êtes-vous sûr de vouloir vous déconnecter ?")
            .setPositiveButton("Déconnexion") { _, _ ->
                userViewModel.logout(SessionManager.currentUser!!.id)
                localStorage.logout()

                requireContext().getSharedPreferences("user_session", AppCompatActivity.MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply()

                Toast.makeText(requireContext(), "Déconnecté avec succès", Toast.LENGTH_SHORT).show()

                val intent = Intent(requireContext(), AuthenticationActivity::class.java)
                startActivity(intent)

                requireActivity().finish()
            }
            .setNegativeButton("Annuler", null)
            .show()
    }

}
