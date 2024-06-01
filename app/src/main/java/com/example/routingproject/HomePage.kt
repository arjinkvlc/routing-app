package com.example.routingproject

import com.example.routingproject.schedule.ScheduleFragment
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.routingproject.databinding.ActivityHomePageBinding
import com.example.routingproject.ui.explore.ExploreFragment
import com.example.routingproject.ui.favorite.FavoritesFragment
import com.example.routingproject.ui.profile.ProfileFragment

class HomePage : AppCompatActivity() {
    lateinit var binding: ActivityHomePageBinding
    private lateinit var latitudeList:ArrayList<String>
    private lateinit var longitudeList:ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //switch between fragments
        val homeFragment = HomeFragment()
        val scheduleFragment = ScheduleFragment()
        val exploreFragment = ExploreFragment()
        val favoritesFragment = FavoritesFragment()
        val profileFragment = ProfileFragment()
        if (intent.getStringArrayListExtra("latitudeList")!=null) {
            latitudeList= intent.getStringArrayListExtra("latitudeList")!!
            longitudeList= intent.getStringArrayListExtra("longitudeList")!!
            Log.d("testlng2",latitudeList.toString())
            val bundle=Bundle()
            bundle.putStringArrayList("latitudeList", latitudeList)
            bundle.putStringArrayList("longitudeList", longitudeList)
            homeFragment.arguments=bundle
        }
        replaceFragment(homeFragment)
        binding.navigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeButton -> replaceFragment(homeFragment)
                R.id.mapButton -> replaceFragment(scheduleFragment)
                R.id.discoverButton -> replaceFragment(exploreFragment)
                R.id.favoritesButton -> replaceFragment(favoritesFragment)
                R.id.profileButton -> replaceFragment(profileFragment)
                else -> {
                }
            }
            true
        }
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(binding.frameLayout.id, fragment).commit()
    }


}
