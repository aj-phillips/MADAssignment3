package com.example.assignment3

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import java.io.File


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        // When profile picture is clicked, run the openCamera function
        findPreference<Preference>("profilePicture")?.setOnPreferenceClickListener {
            openCamera()
            true
        }

        // When Reset Storage Data is clicked, run the resetStorageData function
        findPreference<Preference>("resetStorageData")?.setOnPreferenceClickListener {
            resetStorageData()
            true
        }
    }

    // Open the separate camera activity
    private fun openCamera() {
        val openCameraActivity = Intent(activity, CameraActivity::class.java)
        startActivity(openCameraActivity)
    }

    // This function is for when the Reset Storage Data settings preference is clicked
    private fun resetStorageData() {
        fun deletePreferences()
        {
            // Deletes all preferences stored - can't really error out
            this.context?.let {
                PreferenceManager.getDefaultSharedPreferences(it).edit().clear().apply()
            }

            // If we've already got permissions to manage external storage
            if (Environment.isExternalStorageManager()) {
                // Set file variable to our ProfileImage.jpg
                val file = File(Environment.getExternalStorageDirectory(),
                    "/Android/media/com.example.assignment3/ProfileImage.jpg")

                // If the file exists, try to delete it, else just return, as there's nothing to be done
                if (file.exists()) {
                    val result = file.delete()

                    if (result){
                        Toast.makeText(this.context, "Profile image successfully removed",
                            Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(this.context, "Profile image failed to be removed",
                            Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    return
                }

            }
            else {
                // Open the app permissions screen so the user can give manage permissions
                try {
                    val uri: Uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)
                    startActivity(intent)
                }
                catch (ex: Exception) {
                    val intent = Intent()
                    intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                    startActivity(intent)
                }
            }
        }

        // Refreshes the screen by reloading the root preferences xml
        fun refreshScreen() {
            preferenceScreen = null
            addPreferencesFromResource(R.xml.root_preferences)

            Toast.makeText(this.context, "Preferences reset", Toast.LENGTH_SHORT).show()
        }

        // Dialog box for easier management and organisation of the reset function
        fun showResetDialogBox() {
            val builder = AlertDialog.Builder(this.context)
            builder.setTitle("Deletion Confirmation")
            builder.setMessage("Do you want to delete your app data?")

            builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
                deletePreferences()
                refreshScreen()
            }

            builder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->
                refreshScreen()
            }

            builder.show()
        }

        showResetDialogBox()
    }
}