package com.example.routingproject.ui.register

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.routingproject.R
import com.example.routingproject.databinding.FragmentRegisterSecondPartBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class RegisterSecondPartFragment : Fragment() {

    private lateinit var binding: FragmentRegisterSecondPartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterSecondPartBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("CommitTransaction", "SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val username = binding.userNameEditText.text.toString()
            val phoneNumber = binding.phoneNumberEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()

            // Coroutine scope kullanarak asenkron işlemi bekleyin
            viewLifecycleOwner.lifecycleScope.launch {
                async {
                    val registerSucceed = (activity as? RegisterPageActivity)?.registerUser(email, username, phoneNumber, password, confirmPassword) ?: false
                    if (registerSucceed) {
                        val registerVerificationFragment = RegisterVerificationFragment()
                        binding.userNameEditText.visibility = View.GONE
                        binding.emailEditText.visibility = View.GONE
                        binding.phoneNumberEditText.visibility = View.GONE
                        binding.passwordEditText.visibility = View.GONE
                        binding.confirmPasswordEditText.visibility = View.GONE
                        childFragmentManager.beginTransaction().replace(R.id.constrainLayout, registerVerificationFragment).addToBackStack(null).commit()
                    } else {
                        // Show error message
                        val errorMessage = "Registration failed. Please try again."
                        print(errorMessage)
                        Log.d("testxx","basarisiz oldu.")
                    }
                }
            }
        }



    }



}
