package com.example.assignment3

import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        findPreference<Preference>("profilePicture")?.setOnPreferenceClickListener {
            val signOutIntent = Intent(activity, CameraActivity::class.java)
            startActivity(signOutIntent)
            true
        }
    }
}