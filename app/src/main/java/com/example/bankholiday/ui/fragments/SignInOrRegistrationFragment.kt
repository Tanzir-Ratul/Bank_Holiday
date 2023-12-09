package com.example.bankholiday.ui.fragments

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bankholiday.R
import com.example.bankholiday.databinding.FragmentSignInOrRegistrationBinding
import com.example.bankholiday.session.SessionManager
import com.example.bankholiday.ui.viewmodel.LogInOrRegistrationViewModel
import com.example.bankholiday.utils.animateVisibility
import com.example.bankholiday.utils.handleUnsuccessfulAPI
import com.example.bankholiday.utils.hideKeyboard
import com.example.bankholiday.utils.isValidEmail
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInOrRegistrationFragment : Fragment() {
    @Inject
    lateinit var session: SessionManager
    private var binding: FragmentSignInOrRegistrationBinding? = null
    private val viewModel: LogInOrRegistrationViewModel by viewModels()


    // Load regular and bold fonts
    private var poppinsBold: Typeface? = null
    private var poppinsMedium: Typeface? = null
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
                } else if (messageCode.value != null && messageCode.value != 200 && messageCode.value != 402) {
                    Toast.makeText(requireContext(), "Invalid request", Toast.LENGTH_SHORT)
                        .show()
                } else if (messageCode.value != null && messageCode.value == 402) {
                    Toast.makeText(
                        requireContext(),
                        "The email has already been taken",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

            loginResponse.observe(viewLifecycleOwner) {
                Log.d("responseCodeFr", "re ${messageCode.value}")
                if (it != null) {
                    if (!it.token.isNullOrEmpty()) {
                        findNavController().navigate(R.id.action_signInOrRegistrationFragment_to_bankHolidayFragment)
                    }
                } else if (messageCode.value == 401) {
                    Toast.makeText(requireContext(), "Unauthorized", Toast.LENGTH_SHORT)
                        .show()
                }

            }

            messageCode.observe(viewLifecycleOwner) { code ->
                if (code != null) {
                    handleUnsuccessfulAPI(requireContext(), code)
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
        val fullText = "By pressing ‘Submit’ you aggress \n\t\t\t\tto our terms & conditions"
        val spannableString = SpannableString(fullText)

        val termsStart = fullText.indexOf("our terms")
        val termsEnd = termsStart + "our terms & conditions".length

        spannableString.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.termsAndCondition
                )
            ),
            termsStart,
            termsEnd,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding?.termsAndConditionsTV?.text = spannableString

        poppinsBold = ResourcesCompat.getFont(requireContext(), R.font.poppins_bold)
        poppinsMedium = ResourcesCompat.getFont(requireContext(), R.font.poppins_medium)

        // Handle back button press
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitConfirmationDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


        //init login view
        binding?.view1?.visibility = View.VISIBLE
        binding?.view2?.visibility = View.GONE
        animateVisibility(binding?.nameET!!, false)
        binding?.loginTV?.typeface = poppinsBold
        binding?.signupTV?.typeface = poppinsMedium
        animateVisibility(binding?.termsAndConditionsTV!!, false)
        binding?.getStartedNowTV?.text = "Welcome Back"
        binding?.enterCredentialsTV?.text = "Sign in to continue"
        trackWhichButton = true
    }

    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Exit App")
            .setCancelable(true)
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { _, _ ->
                requireActivity().finish()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun setOnClickListener() {

        binding?.btnSignIn?.setOnClickListener {
            hideKeyboard()
            if (!trackWhichButton) {
                if (binding?.nameET?.text.toString().isEmpty()) {
                    binding?.nameET?.error = "Please enter name"
                    return@setOnClickListener
                }
            }
            if (binding?.emailET?.text.toString().isEmpty()) {
                binding?.emailET?.error = "Please enter email"
                return@setOnClickListener
            }
            if (!isValidEmail(binding?.emailET?.text.toString().trim())) {
                binding?.emailET?.error = "Invalid Email Address"
                return@setOnClickListener
            }
            if (binding?.passwordET?.text.toString().isEmpty()) {
                binding?.passwordET?.error = "Please enter password"
                return@setOnClickListener
            }
            if (binding?.passwordET?.text.toString().length < 6) {
                binding?.passwordET?.error = "Password must be at least 6 characters"
                return@setOnClickListener

            }

            if (!trackWhichButton) {
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
            binding?.loginTV?.typeface = poppinsBold
            binding?.signupTV?.typeface = poppinsMedium
        }

        binding?.signupCL?.setOnClickListener {
            animateVisibility(binding?.nameET!!, true)
            animateVisibility(binding?.termsAndConditionsTV!!, true)
            binding?.view1?.visibility = View.GONE
            binding?.view2?.visibility = View.VISIBLE
            binding?.getStartedNowTV?.text = "Get Started Now"
            binding?.enterCredentialsTV?.text = "Enter your credentials to access your \naccount"
            trackWhichButton = false
            binding?.loginTV?.typeface = poppinsMedium
            binding?.signupTV?.typeface = poppinsBold
        }

    }

    private fun loginApiCall() {
        viewModel.loginUser(
            userName = binding?.emailET?.text.toString().trim(),
            password = binding?.passwordET?.text.toString()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.resetValues()
        viewModel.loginResponse.removeObservers(viewLifecycleOwner)
        viewModel.registrationResponse.removeObservers(viewLifecycleOwner)

    }
}