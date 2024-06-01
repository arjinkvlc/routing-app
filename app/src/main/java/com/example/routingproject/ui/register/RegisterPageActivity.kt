package com.example.routingproject.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.routingproject.data.api.RetrofitClient
import com.example.routingproject.data.api.UserRepository
import com.example.routingproject.databinding.ActivityRegisterPageBinding
import com.example.routingproject.ui.login.LoginPageActivity
import kotlinx.coroutines.launch

class RegisterPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterPageBinding
    private lateinit var registerViewModel: RegisterPageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize user repository
        val userRepository = UserRepository(RetrofitClient.apiService, this)

        // Initialize ViewModel with userRepository
        registerViewModel = ViewModelProvider(this, RegisterPageViewModelFactory(userRepository, this))[RegisterPageViewModel::class.java]

        val registerFirstFragment = RegisterFirstPartFragment()
        // Load the first fragment
        supportFragmentManager.beginTransaction().replace(binding.frameLayout.id, registerFirstFragment).commit()

    }

    fun onNextClicked(firstName: String, lastName: String, birthday: Int, id: Long) {
        registerViewModel.viewModelScope.launch {
            Log.d("testx", firstName + lastName + birthday + id)
            registerViewModel.onNextClicked(firstName, lastName, birthday, id)
        }
    }

    suspend fun registerUser(email: String, username: String, phoneNumber: String, password: String, confirmPassword: String): Boolean {
        Log.d("testx", email + username + phoneNumber + password + confirmPassword)
        return registerViewModel.register(email, username, phoneNumber, password, confirmPassword)
    }

    suspend fun verificationCode(verificationCode: String): Boolean {
        Log.d("testx", verificationCode)
        return registerViewModel.verificationCode(verificationCode)
    }

    fun returnLoginPage() {
        val intent = Intent(this, LoginPageActivity::class.java)
        startActivity(intent)
    }
}

class RegisterPageViewModelFactory(private val userRepository: UserRepository, private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterPageViewModel::class.java)) {
            return RegisterPageViewModel(userRepository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


