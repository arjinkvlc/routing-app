package com.example.routingproject.ui.profile

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.routingproject.data.api.UserRepository
import com.example.routingproject.data.model.ChangePasswordRequest
import com.example.routingproject.data.model.ChangePasswordResponse

class ProfileViewModel(private val userRepository: UserRepository, private val context: Context):ViewModel() {
    suspend fun changePassword(currentPassword:String,newPassword:String,confirmPassword:String): ChangePasswordResponse {
        val changeRequest=ChangePasswordRequest(confirmPassword,newPassword,currentPassword)
        val changeResponse=userRepository.changePassword(changeRequest)
        if (changeResponse.succeeded) {
            Toast.makeText(context,"Password Changed Successfully !",Toast.LENGTH_SHORT).show()
        } else {
            Log.d("ProfileViewModel", "Password changing failed: ${changeResponse.message}")
        }

        return changeResponse
    }
}