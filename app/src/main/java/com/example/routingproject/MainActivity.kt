package com.example.routingproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.routingproject.ui.login.LoginPageActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferencesHelper = SharedPreferencesHelper(this)

        val homePageIntent = Intent(this,HomePage::class.java)
        val loginIintent = Intent(this, LoginPageActivity::class.java)

        if(sharedPreferencesHelper.getJwtToken().equals(null)){
            startActivity(loginIintent)
        }else{
            startActivity(homePageIntent)
        }
    }
}
