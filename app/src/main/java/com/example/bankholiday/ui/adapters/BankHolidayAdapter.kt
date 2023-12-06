package com.example.bankholiday.ui.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bankholiday.api.models.MonthWithHolidays
import com.example.bankholiday.databinding.ItemHolidaysBinding
import java.util.Random

class BankHolidayAdapter(val ctx: Context) :
    RecyclerView.Adapter<BankHolidayAdapter.BankHolidayViewHolder>() {

    private var data: MutableList<MonthWithHolidays>? = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankHolidayViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHolidaysBinding.inflate(inflater, parent, false)
        return BankHolidayViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: BankHolidayViewHolder, position: Int) {
        val item = data?.get(position)
        holder.bind(item)
    }


    inner class BankHolidayViewHolder(private val binding: ItemHolidaysBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.childRV.layoutManager =
                LinearLayoutManager(binding.root.context, RecyclerView.VERTICAL, false)
        }

        fun bind(item: MonthWithHolidays?) {

            binding.monthTV.text = item?.monthName
            binding.childRV.visibility =
                if (item?.holidayItems?.isNotEmpty() == true) View.VISIBLE else View.GONE

            val adapter = ChildAdapter(binding.root.context, item?.holidayItems)
            binding.childRV.adapter = adapter

        }


    }

    fun setData(data: MutableList<MonthWithHolidays>?) {
        this.data?.clear()
        data?.let { this.data?.addAll(it) }
        notifyDataSetChanged()

    }


}
