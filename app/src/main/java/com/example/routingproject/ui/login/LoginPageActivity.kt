package com.example.routingproject.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.routingproject.HomePage
import com.example.routingproject.data.api.RetrofitClient
import com.example.routingproject.data.api.UserRepository
import com.example.routingproject.databinding.ActivityLoginPageBinding
import com.example.routingproject.ui.register.RegisterPageActivity
import kotlinx.coroutines.launch

class LoginPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginPageBinding
    private lateinit var loginViewModel: LoginPageViewModel
    private var loginSucceeded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize user repository
        val userRepository = UserRepository(RetrofitClient.apiService,this)
        loginViewModel = ViewModelProvider(
            this,
            LoginPageViewModelFactory(userRepository, this)
        )[LoginPageViewModel::class.java]

        binding.loginButton.setOnClickListener {
            val loginKey = binding.usernameInputText.text.toString()
            val password = binding.passwordInputText.text.toString()
            lifecycleScope.launch {
                authenticate(loginKey, password)
            }
        }

        binding.registerTextView.setOnClickListener {
            val intent = Intent(this, RegisterPageActivity::class.java)
            startActivity(intent)
        }
    }

    private suspend fun authenticate(loginKey: String, password: String) {
        Log.d("LoginPageActivity", "$loginKey with this password: $password")
        try {
            val loginResponse = loginViewModel.authenticate(loginKey, password)
            if (loginResponse.succeeded) {
                loginSucceeded = true
                saveUserDataToPreferences(loginResponse.data.userName, loginResponse.data.email)
                switchHomePage()
            } else {
                loginSucceeded = false
                Toast.makeText(this, "Your info is not true", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Log.e("LoginPageActivity", "Authentication error: ${e.localizedMessage}")
            Toast.makeText(this, "Invalid username or password.", Toast.LENGTH_LONG).show()
        }
    }

    private fun switchHomePage() {
        val intent = Intent(this, HomePage::class.java)
        startActivity(intent)
    }
    private fun saveUserDataToPreferences(username: String, email: String) {
        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("username", username)
            putString("email", email)
            apply()
        }
    }
}

class LoginPageViewModelFactory(private val userRepository: UserRepository, private val context: Context) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginPageViewModel::class.java)) {
            return LoginPageViewModel(userRepository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
