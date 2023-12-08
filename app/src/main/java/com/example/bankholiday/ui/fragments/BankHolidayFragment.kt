package com.example.bankholiday.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bankholiday.databinding.FragmentBankHolidayBinding
import com.example.bankholiday.ui.adapters.BankHolidayAdapter
import com.example.bankholiday.ui.adapters.YearAdapter
import com.example.bankholiday.ui.viewmodel.HolidayViewModel
import com.example.bankholiday.utils.handleUnsuccessfulAPI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BankHolidayFragment : Fragment() {

    private lateinit var binding: FragmentBankHolidayBinding
    private val viewModel: HolidayViewModel by viewModels()
    private lateinit var bankHolidayAdapter: BankHolidayAdapter
    private lateinit var yearAdapter: YearAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBankHolidayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callHolidaysAPI("2023")
        initViews()
        setOnClickListener()
        setObservers()
    }

    private fun setOnClickListener() {

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
      yearAdapter.yearClickCallBack ={
          if(it.isNotEmpty()){
              binding.rvBankHoliday.visibility = View.GONE
              binding.yearRV.visibility = View.GONE
              callHolidaysAPI(it)
          }
      }
    }

    private fun callHolidaysAPI(year:String?){
        viewModel.getHolidaysData(year?.trim())
    }
    private fun setObservers() {
        viewModel.getHolidayData.observe(viewLifecycleOwner) {
            if (it?.isNotEmpty() == true) {
                bankHolidayAdapter.setData(it)
                binding.rvBankHoliday.scrollToPosition(0)
                binding.yearRV.visibility = View.VISIBLE
                binding.rvBankHoliday.visibility = View.VISIBLE
                binding.noDataFoundLL.visibility = View.GONE
            }else{
                binding.yearRV.visibility = View.GONE
                binding.rvBankHoliday.visibility = View.GONE
                binding.noDataFoundLL.visibility = View.VISIBLE
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.shimmer.visibility = View.VISIBLE
                binding.shimmer.startShimmer()
            } else {
                binding.shimmer.visibility = View.GONE
                binding.shimmer.stopShimmer()
            }
        }

        viewModel.messageCode.observe(viewLifecycleOwner){code->
            if(code != null){
                handleUnsuccessfulAPI(requireContext(),code)
            }
        }
    }

    private fun initViews() {
        //adapter init
        bankHolidayAdapter = BankHolidayAdapter(requireContext())
        binding.rvBankHoliday.adapter = bankHolidayAdapter
        binding.rvBankHoliday.layoutManager = LinearLayoutManager(requireContext())

        //year adapter init
        binding.yearRV.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val years = (2005..2024).map { it.toString() }
        yearAdapter = YearAdapter(requireContext(), years, years.indexOf("2023"))
        binding.yearRV.adapter = yearAdapter
        // Scroll to the initially selected position
        binding.yearRV.scrollToPosition(years.indexOf("2023"))

    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.resetLiveDate()
    }
}