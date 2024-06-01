package com.example.routingproject.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.routingproject.ChangePasswordDialogFragment
import com.example.routingproject.data.api.RetrofitClient
import com.example.routingproject.data.api.UserRepository
import com.example.routingproject.data.model.AddTripRequest
import com.example.routingproject.databinding.FragmentProfileBinding
import com.example.routingproject.ui.login.LoginPageActivity
import com.example.routingproject.ui.login.LoginPageViewModel
import com.example.routingproject.ui.login.LoginPageViewModelFactory
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(), ChangePasswordDialogFragment.ChangePasswordDialogListener {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var profileViewModel: ProfileViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val sharedPref = activity?.getSharedPreferences("user_prefs",0)
        val username = sharedPref?.getString("username", "Unknown")
        val email = sharedPref?.getString("email", "Unknown")

        val userRepository = UserRepository(RetrofitClient.apiService,requireContext())
        profileViewModel = ViewModelProvider(
            this,
            ProfileViewModelFactory(userRepository, requireContext())
        )[ProfileViewModel::class.java]

        binding.usernametextView.text = ""+binding.usernametextView.text+"$username"
        binding.emailtextView.text=""+binding.emailtextView.text+"$email"
        binding.logOutButton.setOnClickListener{
            val intent=Intent(requireActivity(),LoginPageActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
            Toast.makeText(requireActivity(),"Log out successfully !",Toast.LENGTH_SHORT).show()
        }
        binding.changePasswordButton.setOnClickListener{
                val dialog = ChangePasswordDialogFragment()
                dialog.setListener(this)
                dialog.show(childFragmentManager, "com.example.routingproject.ChangePasswordDialogFragment")
        }

        return binding.root
    }

    override fun onDialogPositiveClick(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ) {
        lifecycleScope.launch {
            if(!currentPassword.equals(null) && !newPassword.equals(null) && !confirmPassword.equals(null)){
                try {
                    profileViewModel.changePassword(currentPassword, newPassword, confirmPassword)
                    Log.d("testchange", "Şifre değiştirme işlemi başarılı.")
                }catch (e: Exception){
                    Toast.makeText(requireContext(), "Password info cannot be null", Toast.LENGTH_LONG).show()
                }
            }

        }
    }
}
class ProfileViewModelFactory(private val userRepository: UserRepository, private val context: Context) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(userRepository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
