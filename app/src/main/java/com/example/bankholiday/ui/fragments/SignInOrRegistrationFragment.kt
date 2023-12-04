package com.example.bankholiday.ui.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.adapters.ViewBindingAdapter.setOnClick
import com.example.bankholiday.R
import com.example.bankholiday.databinding.FragmentSignInOrRegistrationBinding
import com.example.bankholiday.utils.animateVisibility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInOrRegistrationFragment : Fragment() {

    private var binding: FragmentSignInOrRegistrationBinding? = null

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
    }

    private fun initView() {

    }

    private fun setOnClickListener() {
        binding?.btnSignIn?.setOnClickListener {

        }

        binding?.loginCL?.setOnClickListener {
            binding?.view1?.visibility = View.VISIBLE
            binding?.view2?.visibility = View.GONE
            animateVisibility(binding?.emailET!!, false)
            binding?.getStartedNowTV?.text = "Welcome Back"
            binding?.enterCredentialsTV?.text = "Sign in to continue"


        }

        binding?.signupCL?.setOnClickListener {
            animateVisibility(binding?.emailET!!, true)
            binding?.view1?.visibility = View.GONE
            binding?.view2?.visibility = View.VISIBLE
            binding?.getStartedNowTV?.text = "Get Started Now"
            binding?.enterCredentialsTV?.text = "Enter your credentials to access your \naccount"
        }

    }
}