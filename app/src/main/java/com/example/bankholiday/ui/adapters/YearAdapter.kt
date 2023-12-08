package com.example.bankholiday.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bankholiday.R
import com.example.bankholiday.databinding.ItemYearsBinding

class YearAdapter(
    private val ctx: Context,
    private val years: List<String>,
    private val initSetPosition: Int
) :
    RecyclerView.Adapter<YearAdapter.YearViewHolder>() {
    var yearClickCallBack: ((String) -> Unit)? = null
    private var selectedYearPosition = initSetPosition

    // Load regular and bold fonts
    private val poppinsBold = ResourcesCompat.getFont(ctx, R.font.poppins_bold)
    private val poppinsRegular = ResourcesCompat.getFont(ctx, R.font.poppins_regular)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearViewHolder {
        return YearViewHolder(
            ItemYearsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return years.size
    }

    override fun onBindViewHolder(holder: YearViewHolder, position: Int) {
        holder.bind(years[position], position)
    }


    inner class YearViewHolder(val binding: ItemYearsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String, pos: Int) {

            binding.textYear.apply {
                typeface = if (pos == selectedYearPosition) poppinsBold else poppinsRegular
                text = item
            }
            setAnimation(binding.rootView)
            binding.dashView.visibility =
                if (selectedYearPosition == pos) View.VISIBLE else View.INVISIBLE

            binding.textYear.setOnClickListener {
                if(selectedYearPosition != pos){
                    selectedYearPosition = pos
                    yearClickCallBack?.invoke(item)
                    notifyDataSetChanged()
                }
            }
        }

        private fun setAnimation(itemView: View) {

            val animation = AnimationUtils.loadAnimation(ctx, android.R.anim.fade_out)
            itemView.animation = animation
        }
    }
}