package com.example.routingproject.ui.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.routingproject.databinding.FragmentRegisterVerification2Binding
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class RegisterVerificationFragment : Fragment() {
    private lateinit var binding: FragmentRegisterVerification2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterVerification2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.verifyButton.setOnClickListener {
            val code = binding.verificationCodeEditText.text.toString()

            Log.d("code", code)

            viewLifecycleOwner.lifecycleScope.launch {
                async {
                    val verificationSucceeded =
                        (activity as? RegisterPageActivity)?.verificationCode(code)
                    if (verificationSucceeded == true) {
                        (activity as? RegisterPageActivity)?.returnLoginPage()
                    } else {
                        // Show error message
                        val errorMessage = "Verification failed. Please try again."
                        // Görsel olarak hata mesajı gösterme
                        binding.verificationCodeEditText.error = errorMessage
                        binding.verificationCodeEditText.requestFocus()
                    }
                }

            }
        }
    }
}