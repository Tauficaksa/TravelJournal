package com.balaji.mytraveljournal

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment

class Dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        val bottomnav=findViewById<BottomNavigationView>(R.id.bottom_nav_bar)
        loadFragment(HomeFragment())
        bottomnav.setOnItemSelectedListener {
            item->when(item.itemId){
                R.id.home->loadFragment(HomeFragment())
                R.id.add->loadFragment(AddFragment())
                R.id.map -> loadFragment(MapsFragment())
                R.id.profile->loadFragment(ProfileFragment())

            }
            true
        }
    }
    private fun loadFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentloader,fragment)
            .commit()
    }
}