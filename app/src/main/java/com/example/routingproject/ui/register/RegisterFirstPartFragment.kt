package com.example.routingproject.ui.register

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.routingproject.R
import com.example.routingproject.databinding.FragmentRegisterFirstPartBinding
import com.example.routingproject.ui.register.RegisterPageActivity

class RegisterFirstPartFragment : Fragment() {
    private var _binding: FragmentRegisterFirstPartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterFirstPartBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("CommitTransaction")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nextButton.setOnClickListener {
            val firstName = binding.firstNameEditText.text.toString()
            val lastName = binding.lastNameEditText.text.toString()
            val birthday = binding.birthdayEditText.text.toString().toIntOrNull() ?: 0
            val id = binding.idNumberEditText.text.toString()


            // Pass data to the activity
            (activity as? RegisterPageActivity)?.onNextClicked(firstName, lastName, birthday, id.toLong())


            val registerSecondFragment = RegisterSecondPartFragment()
            binding.idNumberEditText.visibility=View.GONE
            binding.firstNameEditText.visibility=View.GONE
            binding.lastNameEditText.visibility=View.GONE
            binding.birthdayEditText.visibility=View.GONE
            binding.nextButton.visibility=View.GONE
            childFragmentManager.beginTransaction().replace(R.id.layout,registerSecondFragment).addToBackStack(null).commit()
            // Navigate to the second part fragment
            /*
            findNavController().navigate(RegisterFirstPartFragmentDirections.actionRegisterFirstPartFragmentToRegisterSecondPartFragment())*/
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}