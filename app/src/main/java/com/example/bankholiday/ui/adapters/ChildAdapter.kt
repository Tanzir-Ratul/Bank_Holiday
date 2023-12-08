package com.example.bankholiday.ui.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.bankholiday.R
import com.example.bankholiday.api.models.MonthWithHolidays
import com.example.bankholiday.databinding.ItemHolidays2ndRvBinding
import com.example.bankholiday.utils.formatDate
import java.util.Random

class ChildAdapter(
    val ctx: Context,
    private val holidayItems: List<MonthWithHolidays.HolidayItem>?
) :
    RecyclerView.Adapter<ChildAdapter.ChildViewHolder>() {

    private val colorList = listOf("#FFF0DE", "#E6FEF4", "#E6EDFE", "#FBE6FE", "#FEE6E6", "#D8FDFB")
    private  var color: Int? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        return ChildViewHolder(
            ItemHolidays2ndRvBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return holidayItems?.size ?: 0
    }

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holidayItems?.get(position)?.let { holder.bind(it) }
    }

    inner class ChildViewHolder(private val binding: ItemHolidays2ndRvBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MonthWithHolidays.HolidayItem) {

            binding.dateTextView.text = formatDate(item.date,"yyyy-MM-dd")
            binding.nameTextView.text = item.name
            setRandomBackgroundColor(binding.cardView)

            setAnimation(itemView)

        }
        private fun setAnimation(itemView: View) {

            val animation = AnimationUtils.loadAnimation(ctx, android.R.anim.slide_in_left)
            itemView.animation = animation
        }
        private fun setRandomBackgroundColor(cardView: CardView) {
            val randomColor = colorList.random()
            color = Color.parseColor(randomColor)
            cardView.setCardBackgroundColor(color?: R.color.white)
        }

        private fun generateRandomColor(): Int {
            val random = Random()
            return Color.argb(50, random.nextInt(50), random.nextInt(50), random.nextInt(50))
        }
    }
}