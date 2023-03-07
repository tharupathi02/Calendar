package lk.nibm.calendar.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import lk.nibm.calendar.Common.Common
import lk.nibm.calendar.Model.HolidaysModel
import lk.nibm.calendar.R

class HolidaysInMonthAdapter(var context: Context, var holidayList: List<HolidaysModel>) : RecyclerView.Adapter<HolidaysInMonthAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.holidays_in_month, parent, false))
    }

    override fun getItemCount(): Int {
        return holidayList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val date = holidayList[position].holidayDate
        val dateName = Common.getDateName(date!!.toInt())

        holder.txtDate.text = StringBuilder("").append(holidayList[position].holidayDate).append(dateName).append(" ").append(holidayList[position].holidayMonth).append(", ").append(holidayList[position].holidayYear)
        holder.txtHoliday.text = holidayList[position].holidayName
        holder.txtTodayType.text = holidayList[position].holidayPrimaryType

        if (position % 2 == 0) {
            holder.cardViewHoliday.setCardBackgroundColor(context.resources.getColor(R.color.color6))
            holder.txtDate.setTextColor(context.resources.getColor(R.color.white))
            holder.txtHoliday.setTextColor(context.resources.getColor(R.color.white))
            holder.txtTodayType.setTextColor(context.resources.getColor(R.color.white))
        } else {
            holder.cardViewHoliday.setCardBackgroundColor(context.resources.getColor(R.color.color8))
            holder.txtDate.setTextColor(context.resources.getColor(R.color.white))
            holder.txtHoliday.setTextColor(context.resources.getColor(R.color.white))
            holder.txtTodayType.setTextColor(context.resources.getColor(R.color.white))
        }

        // Check the holiday type and set the Icon
        if (holidayList[position].holidayPrimaryType == "National Holiday") {
            holder.cardViewHolidayType.setCardBackgroundColor(context.resources.getColor(R.color.holiday_color1))
        } else if (holidayList[position].holidayPrimaryType == "State Holiday") {
            holder.cardViewHolidayType.setCardBackgroundColor(context.resources.getColor(R.color.holiday_color2))
        } else if (holidayList[position].holidayPrimaryType == "Observance") {
            holder.cardViewHolidayType.setCardBackgroundColor(context.resources.getColor(R.color.holiday_color3))
        } else if (holidayList[position].holidayPrimaryType == "Public Holiday") {
            holder.cardViewHolidayType.setCardBackgroundColor(context.resources.getColor(R.color.holiday_color4))
        } else if (holidayList[position].holidayPrimaryType == "Season") {
            holder.cardViewHolidayType.setCardBackgroundColor(context.resources.getColor(R.color.holiday_color5))
        } else {
            holder.cardViewHolidayType.setCardBackgroundColor(context.resources.getColor(R.color.holiday_color6))
        }

        holder.cardViewHoliday.setOnClickListener {

            val view: View = LayoutInflater.from(context).inflate(R.layout.holiday_details_bottom_sheet, null)
            val bottomSheetDialog = BottomSheetDialog(context)
            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()

            val dateSelected = holidayList[position].holidayDate
            val dateNameSelected = Common.getDateName(dateSelected!!.toInt())

            bottomSheetDialog.findViewById<TextView>(R.id.txtHolidayName)?.text = holidayList[position].holidayName
            bottomSheetDialog.findViewById<TextView>(R.id.txtHolidayDescription)?.text = holidayList[position].holidayDescription
            bottomSheetDialog.findViewById<TextView>(R.id.txtHolidayDate)?.text = StringBuilder("").append(holidayList[position].holidayDate).append(dateNameSelected).append(" ").append(holidayList[position].holidayMonth).append(", ").append(holidayList[position].holidayYear)
            bottomSheetDialog.findViewById<TextView>(R.id.txtHolidayPrimary)?.text = holidayList[position].holidayPrimaryType
            bottomSheetDialog.findViewById<TextView>(R.id.txtHolidayCountry)?.text = holidayList[position].holidayCountry

        }

        // Check if Holiday Name contains only "Poya Day" and set the Icon
        val string = holidayList[position].holidayName
        val checkString = "Poya Day"
        if (holidayList[position].holidayName == "Poya Day" || string!!.contains(checkString)) {
            Glide.with(context).load("https://img.icons8.com/color/5200/null/dharmacakra.png").into(holder.imgHoliday)
            holder.imgHoliday.visibility = View.VISIBLE
        } else {
            holder.imgHoliday.visibility = View.GONE
        }

        holder.cardViewHoliday!!.animation = AnimationUtils.loadAnimation(context, R.anim.top_anim)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtDate: TextView = itemView.findViewById(R.id.txtDate)
        val txtHoliday: TextView = itemView.findViewById(R.id.txtHoliday)
        val txtTodayType: TextView = itemView.findViewById(R.id.txtTodayType)
        val cardViewHoliday: MaterialCardView = itemView.findViewById(R.id.cardViewHoliday)
        val cardViewHolidayType: MaterialCardView = itemView.findViewById(R.id.cardViewHolidayType)
        val imgHoliday: ImageView = itemView.findViewById(R.id.imgHoliday)
    }

}