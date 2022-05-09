package com.example.assignment3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // nav_view references the bottom navigation bar in activity_main.xml
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)

        // fragment references the navigation host fragment in activity_main
        // Getting the host fragment via the supportFragmentManager is required, as using just the
        // FragmentContainerView isn't friendly
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        val navController = navHostFragment.navController

        setSupportActionBar(findViewById(R.id.toolbar))

        // Sets navigation menu links to the fragments we've created
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.coursesFragment, R.id.societiesFragment, R.id.settingsFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)

        bottomNavigationView.setupWithNavController(navController)
    }
}