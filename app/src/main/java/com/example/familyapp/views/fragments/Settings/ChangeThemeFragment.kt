package com.example.familyapp.views.fragments.Settings


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.familyapp.R
import com.google.android.material.switchmaterial.SwitchMaterial

class ThemeFragment : Fragment() {

    private var switchTheme: SwitchMaterial? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_theme, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        switchTheme = view.findViewById(R.id.switchTheme)
        setupThemeSwitch()
    }

    private fun setupThemeSwitch() {
        val sharedPrefs = requireActivity().getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        val isDarkMode = sharedPrefs.getBoolean("dark_mode", false)

        switchTheme?.isChecked = isDarkMode

        switchTheme?.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefs.edit().putBoolean("dark_mode", isChecked).apply()

            applyTheme(isChecked)
        }
    }

    private fun applyTheme(isDarkMode: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        switchTheme = null
    }
}