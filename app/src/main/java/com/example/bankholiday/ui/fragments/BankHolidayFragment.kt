package com.example.bankholiday.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bankholiday.R
import com.example.bankholiday.databinding.FragmentBankHolidayBinding
import com.example.bankholiday.ui.adapters.BankHolidayAdapter
import com.example.bankholiday.ui.viewmodel.HolidayViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BankHolidayFragment : Fragment() {

    private lateinit var binding: FragmentBankHolidayBinding
    private val viewModel: HolidayViewModel by viewModels()
     private lateinit var adapter: BankHolidayAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding  = FragmentBankHolidayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getHolidaysData("2021")
        initViews()
        setObservers()
    }

    private fun setObservers() {
        viewModel.getHolidayData.observe(viewLifecycleOwner) {
            Log.d("getHolidayDataO", "${it.toString()}")
            if(it?.isEmpty() != true) {

                adapter.setData(it)
            }
        }
    }

    private fun initViews() {
         adapter = BankHolidayAdapter(requireContext())
        binding.rvBankHoliday.adapter = adapter
        binding.rvBankHoliday.layoutManager = LinearLayoutManager(requireContext())
    }

}