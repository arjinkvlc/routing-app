package com.example.routingproject

import ScheduleFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.routingproject.databinding.ActivityHomePageBinding


class HomePage : AppCompatActivity() {
  lateinit var binding: ActivityHomePageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityHomePageBinding.inflate(layoutInflater)
      setContentView(binding.root)
      //switch between fragments
      val homeFragment=HomeFragment()
      val scheduleFragment=ScheduleFragment()
      val favouritesFragment=FavouritesFragment()
      val profileFragment=ProfileFragment()
      replaceFragment(homeFragment)
      binding.navigationView.setOnItemSelectedListener{
        when (it.itemId) {
          R.id.homeButton -> replaceFragment(homeFragment)
          R.id.mapButton -> replaceFragment(scheduleFragment)
          R.id.favButton -> replaceFragment(favouritesFragment)
          R.id.profileButton -> replaceFragment(profileFragment)
          else -> {
          }
        }
        true
      }
    }
    fun replaceFragment(fragment: Fragment){
    supportFragmentManager.beginTransaction().replace(binding.frameLayout.id,fragment).commit()

  }


}
