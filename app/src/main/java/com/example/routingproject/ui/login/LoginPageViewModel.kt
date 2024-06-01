package com.example.routingproject.ui.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.routingproject.SharedPreferencesHelper
import com.example.routingproject.data.api.UserRepository
import com.example.routingproject.data.model.LoginRequest
import com.example.routingproject.data.model.LoginResponse

class LoginPageViewModel(private val userRepository: UserRepository, private val context: Context) : ViewModel() {

    var jwToken: String? = null
    var refreshToken: String? = null
    var userId: String? = null

    suspend fun authenticate(loginKey: String, password: String): LoginResponse {
        val loginRequest = LoginRequest(
            loginKey,
            password
        )

        val loginResponse = userRepository.authenticate(loginRequest)

        if (loginResponse.succeeded) {
            jwToken = loginResponse.data.jwToken
            refreshToken = loginResponse.data.refreshToken
            userId = loginResponse.data.id
            saveTokens(jwToken, refreshToken,userId)
        } else {
            Log.d("LoginPageViewModel", "Authentication failed: ${loginResponse.message}")
        }

        return loginResponse
    }

    private fun saveTokens(jwToken: String?, refreshToken: String?, id: String?) {
        val sharedPreferencesHelper = SharedPreferencesHelper(context)
        sharedPreferencesHelper.saveJwtToken(jwToken ?: "")
        sharedPreferencesHelper.saveRefreshToken(refreshToken ?: "")
        sharedPreferencesHelper.saveUserId(id ?: "")
    }
}