package com.example.bankholiday.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import com.example.bankholiday.R
import com.example.bankholiday.api.models.MonthWithHolidays
import com.example.bankholiday.databinding.ItemHolidaysBinding

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

            fun bind(item: MonthWithHolidays?) {
                Log.d("onBindViewHolder", "${item.toString()}")

                //binding.monthTV.text = item?.monthName
                // Set visibility based on holidays
                binding.holidaysContainer.visibility =
                    if (item?.holidayItems?.isNotEmpty() == true) View.VISIBLE else View.GONE

                // Set holiday details dynamically
                val holidaysContainer = binding.holidaysContainer
                holidaysContainer.removeAllViews()

                if (item != null && item.holidayItems.isNotEmpty()) {
                    item.holidayItems.forEach { holiday ->
                        val marginBetweenViews = ctx.resources.getDimensionPixelSize(R.dimen.margin_between_views)
                        val holidayView = LayoutInflater.from(binding.root.context)
                            .inflate(R.layout.item_holidays, holidaysContainer, false)

                        val layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )

                        // Set margin to the bottom of the view
                        layoutParams.bottomMargin = marginBetweenViews

                        holidayView.layoutParams = layoutParams
                        val dateTextView = holidayView.findViewById<TextView>(R.id.dateTextView)
                        val nameTextView = holidayView.findViewById<TextView>(R.id.nameTextView)
                        val descriptionTextView = holidayView.findViewById<TextView>(R.id.descriptionTextView)

                        dateTextView.text = holiday.date
                        nameTextView.text = holiday.name
                        descriptionTextView.text = holiday.description

                        holidaysContainer.addView(holidayView)

                    }
                }
            }
        }

        fun setData(data: MutableList<MonthWithHolidays>?) {
            this.data?.clear()
            data?.let { this.data?.addAll(it) }
            notifyDataSetChanged()

        }


    }
