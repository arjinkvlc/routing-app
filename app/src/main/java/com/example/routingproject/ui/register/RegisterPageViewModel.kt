package com.example.routingproject.ui.register

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.routingproject.data.api.UserRepository
import com.example.routingproject.data.model.ConfirmationRequest
import com.example.routingproject.data.model.ConfirmationResponse
import com.example.routingproject.data.model.RegisterRequest
import com.example.routingproject.data.model.RegisterResponse
import kotlinx.coroutines.launch

class RegisterPageViewModel(private val userRepository: UserRepository, private val context: Context) : ViewModel() {

    var firstName: String? = null
    var lastName: String? = null
    var birthday: Int? = null
    var id: Long? = null
    var registerResponse: RegisterResponse? = null
    var confirmationResponse: ConfirmationResponse? = null
    var succeeded: Boolean = false
    var data: String? = ""

    fun onNextClicked(firstName: String, lastName: String, birthday: Int, id: Long) {
        this.firstName = firstName
        this.lastName = lastName
        this.birthday = birthday
        this.id = id
    }

    suspend fun register(
        email: String,
        username: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        val registerRequest = if (firstName != null && lastName != null && birthday != null && id != null) {
            RegisterRequest(
                firstName!!,
                lastName!!,
                email,
                username,
                phoneNumber,
                id!!,
                birthday!!,
                password,
                confirmPassword
            )
        } else {
            null
        }

        if (registerRequest == null) {
            Log.e("RegisterPageViewModel", "RegisterRequest null")
            return false
        }

        return try {
            Log.d("RegisterPageViewModel", "RegisterRequest: $registerRequest")
            registerResponse = userRepository.register(registerRequest)
            succeeded = registerResponse?.succeeded ?: false
            data = registerResponse?.data
            Log.d("RegisterPageViewModel", "register succeeded: $succeeded")
            succeeded
        } catch (e: Exception) {
            Log.e("RegisterPageViewModel", "Register error: ${e.localizedMessage}")
            false
        }
    }

    fun checkRegisterOperationSucceeded(): Boolean {
        return succeeded
    }

    suspend fun verificationCode(code: String): Boolean {
        val confirmationRequest = if (data != null) {
            ConfirmationRequest(
                data!!,
                code
            )
        } else {
            null
        }

        if (confirmationRequest == null) {
            Log.e("RegisterPageViewModel", "ConfirmationRequest null")
            return false
        }

        return try {
            Log.d("RegisterPageViewModel", "ConfirmationRequest: $confirmationRequest")
            confirmationResponse = userRepository.confirmEmail(confirmationRequest)
            val succeed = confirmationResponse!!.succeeded
            Log.d("RegisterPageViewModel", "ConfirmationResponse: $confirmationResponse")
            succeed
        } catch (e: Exception) {
            Log.e("RegisterPageViewModel", "Verification error: ${e.localizedMessage}")
            Log.e("RegisterPageViewModel", "${e.message}")
            false
        }

    }
}
