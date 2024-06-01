package com.example.routingproject

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.routingproject.data.api.RetrofitClient
import com.example.routingproject.data.api.UserRepository
import com.example.routingproject.ui.login.LoginPageViewModel
import com.example.routingproject.ui.login.LoginPageViewModelFactory
import com.example.routingproject.ui.profile.ProfileFragment
import com.example.routingproject.ui.profile.ProfileViewModel
import kotlinx.coroutines.launch

class ChangePasswordDialogFragment: DialogFragment() {
    private var listener: ChangePasswordDialogListener? = null
    private lateinit var currentPasswordEditText: EditText
    private lateinit var newPasswordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())

        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.change_password_dialog, null)
        builder.setView(dialogView)

        currentPasswordEditText = dialogView.findViewById(R.id.currentPasswordEditText)
        newPasswordEditText = dialogView.findViewById(R.id.newPasswordEditText)
        confirmPasswordEditText = dialogView.findViewById(R.id.confirmNewPasswordEditText)

        builder.setPositiveButton("Confirm") { dialog, _ ->
            val currentPasswordInput = currentPasswordEditText.text.toString()
            val newPasswordInput = newPasswordEditText.text.toString()
            val confirmPasswordInput = confirmPasswordEditText.text.toString()
            listener?.onDialogPositiveClick(currentPasswordInput, newPasswordInput, confirmPasswordInput)
            Log.d("testchange", "$currentPasswordInput $confirmPasswordInput")
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        return builder.create()
    }

    fun setListener(listener: ProfileFragment) {
        this.listener = listener
    }

    interface ChangePasswordDialogListener {
        fun onDialogPositiveClick(currentPassword: String, newPassword: String, confirmPassword: String)
    }
}
