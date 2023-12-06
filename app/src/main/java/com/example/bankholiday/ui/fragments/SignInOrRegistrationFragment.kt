package com.example.bankholiday.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bankholiday.R
import com.example.bankholiday.databinding.FragmentSignInOrRegistrationBinding
import com.example.bankholiday.session.SessionManager
import com.example.bankholiday.ui.viewmodel.LogInOrRegistrationViewModel
import com.example.bankholiday.utils.animateVisibility
import com.example.bankholiday.utils.isValidEmail
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInOrRegistrationFragment : Fragment() {

    private var binding: FragmentSignInOrRegistrationBinding? = null
    private val viewModel: LogInOrRegistrationViewModel by viewModels()

    @Inject
    lateinit var session: SessionManager

    private var trackWhichButton = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignInOrRegistrationBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setOnClickListener()
        setObserver()

    }

    private fun setObserver() {

        viewModel.apply {

            registrationResponse.observe(viewLifecycleOwner) {
                if (it != null) {
                    if (!it.token.isNullOrEmpty()) {
                        session.accessToken = it.token
                        findNavController().navigate(R.id.action_signInOrRegistrationFragment_to_bankHolidayFragment)
                    }
                } else if(messageCode.value!=null && messageCode.value != 200 && messageCode.value != 402) {
                    Toast.makeText(requireContext(), "Invalid request", Toast.LENGTH_SHORT)
                        .show()
                }else if(messageCode.value!=null && messageCode.value == 402){
                    Toast.makeText(requireContext(), "The email has already been taken", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            loginResponse.observe(viewLifecycleOwner) {
                Log.d("responseCodeFr", "re ${messageCode.value}")
                if (it != null) {
                    if (!it.token.isNullOrEmpty()) {
                        findNavController().navigate(R.id.action_signInOrRegistrationFragment_to_bankHolidayFragment)
                    }
                } else if(messageCode.value == 401) {
                    Toast.makeText(requireContext(), "Unauthorized", Toast.LENGTH_SHORT)
                        .show()
                }

            }

            isLoading.observe(viewLifecycleOwner) {
                if (it) binding?.progressBar?.visibility = View.VISIBLE
                else binding?.progressBar?.visibility = View.GONE
            }
        }
    }

    private fun registrationApiCall() {

        viewModel.registerUser(
            name = binding?.nameET?.text.toString(),
            email = binding?.emailET?.text.toString(),
            password = binding?.passwordET?.text.toString()
        )
    }


    private fun initView() {
        //init login view
        binding?.view1?.visibility = View.VISIBLE
        binding?.view2?.visibility = View.GONE
        animateVisibility(binding?.nameET!!, false)
        animateVisibility(binding?.termsAndConditionsTV!!, false)
        binding?.getStartedNowTV?.text = "Welcome Back"
        binding?.enterCredentialsTV?.text = "Sign in to continue"
        trackWhichButton = true
    }

    private fun setOnClickListener() {

        binding?.btnSignIn?.setOnClickListener {

            if (binding?.emailET?.text.toString().isEmpty()) {
                binding?.emailET?.error = "Please enter email"
                return@setOnClickListener
            } else if (!isValidEmail(binding?.emailET?.text.toString().trim())) {
                binding?.emailET?.error = "Invalid Email Address"
                return@setOnClickListener
            } else if (binding?.passwordET?.text.toString().isEmpty()) {
                binding?.passwordET?.error = "Please enter password"
                return@setOnClickListener
            } else if (binding?.passwordET?.text.toString().length < 6) {
                binding?.passwordET?.error = "Password must be at least 6 characters"
                return@setOnClickListener

            } else
                if (!trackWhichButton) {
                    if (binding?.nameET?.text.toString().isEmpty()) {
                        binding?.nameET?.error = "Please enter name"
                        return@setOnClickListener
                    }
                    registrationApiCall()
                } else {
                    loginApiCall()
                }

        }

        binding?.loginCL?.setOnClickListener {
            binding?.view1?.visibility = View.VISIBLE
            binding?.view2?.visibility = View.GONE
            animateVisibility(binding?.nameET!!, false)
            animateVisibility(binding?.termsAndConditionsTV!!, false)
            binding?.getStartedNowTV?.text = "Welcome Back"
            binding?.enterCredentialsTV?.text = "Sign in to continue"
            trackWhichButton = true

        }

        binding?.signupCL?.setOnClickListener {
            animateVisibility(binding?.nameET!!, true)
            animateVisibility(binding?.termsAndConditionsTV!!, true)
            binding?.view1?.visibility = View.GONE
            binding?.view2?.visibility = View.VISIBLE
            binding?.getStartedNowTV?.text = "Get Started Now"
            binding?.enterCredentialsTV?.text = "Enter your credentials to access your \naccount"
            trackWhichButton = false
        }

    }

    private fun loginApiCall() {
        viewModel.loginUser(
            userName = binding?.emailET?.text.toString().trim(),
            password = binding?.passwordET?.text.toString().trim()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.resetValues()
        viewModel.loginResponse.removeObservers(viewLifecycleOwner)
        viewModel.registrationResponse.removeObservers(viewLifecycleOwner)

    }
}