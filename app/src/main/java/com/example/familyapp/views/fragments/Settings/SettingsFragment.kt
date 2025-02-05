package com.example.familyapp.views.fragments.Settings

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.familyapp.R
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPreferences()
    }

    private fun setupPreferences() {
        val titleNotifications = view?.findViewById<TextView>(R.id.titleNotifications)
        val switchNotifications = view?.findViewById<SwitchMaterial>(R.id.switchNotifications)

        val titleSound = view?.findViewById<TextView>(R.id.titleSound)
        val switchSound = view?.findViewById<SwitchMaterial>(R.id.switchSound)

        val titleVibration = view?.findViewById<TextView>(R.id.titleVibration)
        val switchVibration = view?.findViewById<SwitchMaterial>(R.id.switchVibration)

        // Charger les préférences sauvegardées
        val prefs = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)

        // Initialiser les switches avec les valeurs sauvegardées
        switchNotifications?.isChecked = prefs.getBoolean("notifications_enabled", true)
        switchSound?.isChecked = prefs.getBoolean("sound_enabled", true)
        switchVibration?.isChecked = prefs.getBoolean("vibration_enabled", true)

        // Gérer les changements
        switchNotifications?.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("notifications_enabled", isChecked).apply()
            updateNotificationSettings(isChecked)
        }

        switchSound?.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("sound_enabled", isChecked).apply()
        }

        switchVibration?.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("vibration_enabled", isChecked).apply()
        }
    }

    private fun updateNotificationSettings(enabled: Boolean) {
        if (enabled) {
            // Vérifier et demander les permissions si nécessaire
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED) {
                    requestNotificationPermission()
                }
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                NOTIFICATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            NOTIFICATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // Permission refusée
                    view?.findViewById<SwitchMaterial>(R.id.switchNotifications)?.isChecked = false
                    Toast.makeText(
                        context,
                        "Permission des notifications nécessaire pour cette fonctionnalité",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    companion object {
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 123
    }
}