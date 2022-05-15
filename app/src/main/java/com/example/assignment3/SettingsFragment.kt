package com.example.assignment3

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.google.android.gms.location.FusedLocationProviderClient
import java.io.File


class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

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

        // When Reset Storage Data is clicked, run the resetStorageData function
        findPreference<Preference>("testGeolocationBtn")?.setOnPreferenceClickListener {
            getLastKnownLocation()
            true
        }
    }

    // Checks and sets default preferences
    private fun setDefaultPreferences()
    {
        val preferences = this.context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
        val editor = preferences?.edit()

        if (!preferences?.all?.containsKey("name")!!)
        {
            editor?.putString("name", "Guest")
        }

        if (!preferences.all?.containsKey("gender")!!)
        {
            editor?.putString("gender", "Prefer_not_to_say")
        }

        if (!preferences.all?.containsKey("image_taken")!!)
        {
            editor?.putBoolean("image_taken", false)
        }

        editor?.apply()
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
        }

        // Dialog box for easier management and organisation of the reset function
        fun showResetDialogBox() {
            val builder = AlertDialog.Builder(this.context)
            builder.setTitle("Deletion Confirmation")
            builder.setMessage("Do you want to delete your app data?")

            builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                deletePreferences()
                Toast.makeText(this.context, "Preferences reset", Toast.LENGTH_SHORT).show()
                setDefaultPreferences()
                refreshScreen()
            }

            builder.setNegativeButton("No") { _: DialogInterface, _: Int ->

            }

            builder.show()
        }

        showResetDialogBox()
    }

    // Get location
    fun getLastKnownLocation() {
        var locLatitude = 0F
        var locLongitude = 0F

        if (this.context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED && this.context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location->
                if (location != null) {
                    // use your location object
                    // get latitude , longitude and other info from this
                    locLatitude = location.latitude.toFloat()
                    locLongitude = location.longitude.toFloat()
                }
            }

        val builder = AlertDialog.Builder(this.context)
        builder.setTitle("Geolocation Data")
        builder.setMessage("Latitude: $locLatitude\nLongitude: $locLongitude")

        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            Toast.makeText(this.context,
                android.R.string.ok, Toast.LENGTH_SHORT).show()
        }

        builder.show()

    }
}