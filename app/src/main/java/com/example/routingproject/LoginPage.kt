package com.example.routingproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.routingproject.databinding.ActivityLoginPageBinding

class LoginPage : AppCompatActivity() {
  private lateinit var binding: ActivityLoginPageBinding
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityLoginPageBinding.inflate(layoutInflater)
    setContentView(binding.root)
    binding.loginButton.setOnClickListener(){
      val intent=Intent(this,HomePage::class.java)
      startActivity(intent)    }
    binding.registerTextView.setOnClickListener(){
      val intent=Intent(this,RegisterPage::class.java)
      startActivity(intent)
    }
  }
}
