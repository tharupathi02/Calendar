package lk.nibm.calendar.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import lk.nibm.calendar.Common.Common
import lk.nibm.calendar.Model.HolidaysModel
import lk.nibm.calendar.R

class HolidayAdapter(var context: Context, var holidayList: List<HolidaysModel>) : RecyclerView.Adapter<HolidayAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolidayAdapter.MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.holidays, parent, false))
    }

    override fun onBindViewHolder(holder: HolidayAdapter.MyViewHolder, position: Int) {

        val month = holidayList[position].holidayMonth
        val monthName = Common.getMonthName(month!!.toInt())

        holder.txtDate.text = StringBuilder(holidayList[position].holidayDate).append(" ").append(monthName).append(" ").append(holidayList[position].holidayYear)
        holder.txtHoliday.text = holidayList[position].holidayName
        holder.txtTodayType.text = holidayList[position].holidayPrimaryType
        holder.txtCountry.text = holidayList[position].holidayCountry

        // change background color of the card view four by four
        if (position % 3 == 0) {
            holder.cardViewHoliday.setCardBackgroundColor(context.resources.getColor(R.color.color6))
            holder.txtDate.setTextColor(context.resources.getColor(R.color.white))
            holder.txtHoliday.setTextColor(context.resources.getColor(R.color.white))
            holder.txtTodayType.setTextColor(context.resources.getColor(R.color.white))
            holder.txtCountry.setTextColor(context.resources.getColor(R.color.white))
        } else if (position % 3 == 1) {
            holder.cardViewHoliday.setCardBackgroundColor(context.resources.getColor(R.color.color1))
            holder.txtDate.setTextColor(context.resources.getColor(R.color.white))
            holder.txtHoliday.setTextColor(context.resources.getColor(R.color.white))
            holder.txtTodayType.setTextColor(context.resources.getColor(R.color.white))
            holder.txtCountry.setTextColor(context.resources.getColor(R.color.white))
        } else if (position % 3 == 2) {
            holder.cardViewHoliday.setCardBackgroundColor(context.resources.getColor(R.color.color8))
            holder.txtDate.setTextColor(context.resources.getColor(R.color.white))
            holder.txtHoliday.setTextColor(context.resources.getColor(R.color.white))
            holder.txtTodayType.setTextColor(context.resources.getColor(R.color.white))
            holder.txtCountry.setTextColor(context.resources.getColor(R.color.white))
        }

        holder.cardViewHoliday!!.animation = AnimationUtils.loadAnimation(context, R.anim.top_anim)

    }

    override fun getItemCount(): Int {
        return holidayList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtDate: TextView = itemView.findViewById(R.id.txtDate)
        val txtHoliday: TextView = itemView.findViewById(R.id.txtHoliday)
        val txtTodayType: TextView = itemView.findViewById(R.id.txtTodayType)
        val txtCountry: TextView = itemView.findViewById(R.id.txtCountry)
        val cardViewHoliday: MaterialCardView = itemView.findViewById(R.id.cardViewHoliday)
    }
}