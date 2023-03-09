package lk.nibm.calendar.Adapter

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import lk.nibm.calendar.Common.Common
import lk.nibm.calendar.Model.HolidaysModel
import lk.nibm.calendar.R
import lk.nibm.calendar.Receiver.AlarmReceiver
import java.util.*

class HolidayAdapter(var context: Context, var holidayList: List<HolidaysModel>) : RecyclerView.Adapter<HolidayAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolidayAdapter.MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.holidays, parent, false))
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: HolidayAdapter.MyViewHolder, position: Int) {

        val month = holidayList[position].holidayMonth
        val monthName = Common.getMonthName(month!!.toInt())
        //check date and get the date name
        val date = holidayList[position].holidayDate
        val dateName = Common.getDateName(date!!.toInt())

        holder.txtDate.text = StringBuilder("").append(holidayList[position].holidayDate).append(dateName).append(" ").append(monthName).append(", ").append(holidayList[position].holidayYear)
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



        if (holidayList[position].holidayName!!.contains("Poya") && holidayList[position].holidayPrimaryType!!.contains("Public Holiday")) {
            Glide.with(context).load("https://img.icons8.com/color/5200/null/dharmacakra.png").into(holder.imgHoliday)
            holder.imgHoliday.visibility = View.VISIBLE
        } else if (holidayList[position].holidayPrimaryType!!.contains("Season")) {
            Glide.with(context).load("https://img.icons8.com/color/5200/null/island-on-water.png").into(holder.imgHoliday)
            holder.imgHoliday.visibility = View.VISIBLE
        } else if (holidayList[position].holidayPrimaryType!!.contains("Observance")) {
            Glide.with(context).load("https://img.icons8.com/color/5200/null/beach-umbrella.png").into(holder.imgHoliday)
            holder.imgHoliday.visibility = View.VISIBLE
        } else if (holidayList[position].holidayPrimaryType!!.contains("Hindu")) {
            Glide.with(context).load("https://img.icons8.com/color/5200/null/pranava.png").into(holder.imgHoliday)
            holder.imgHoliday.visibility = View.VISIBLE
        } else {
            Glide.with(context).load("").into(holder.imgHoliday)
            holder.imgHoliday.visibility = View.VISIBLE
        }


        holder.cardViewHoliday.setOnClickListener {

            val view: View = LayoutInflater.from(context).inflate(R.layout.holiday_details_bottom_sheet, null)
            val bottomSheetDialog = BottomSheetDialog(context)
            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()

            val monthSelected = holidayList[position].holidayMonth
            val monthNameSelected = Common.getMonthName(monthSelected!!.toInt())

            val dateSelected = holidayList[position].holidayDate
            val dateNameSelected = Common.getDateName(dateSelected!!.toInt())

            bottomSheetDialog.findViewById<TextView>(R.id.txtHolidayName)?.text = holidayList[position].holidayName
            bottomSheetDialog.findViewById<TextView>(R.id.txtHolidayDescription)?.text = holidayList[position].holidayDescription
            bottomSheetDialog.findViewById<TextView>(R.id.txtHolidayDate)?.text = StringBuilder("").append(holidayList[position].holidayDate).append(dateNameSelected).append(" ").append(monthNameSelected).append(", ").append(holidayList[position].holidayYear)
            bottomSheetDialog.findViewById<TextView>(R.id.txtHolidayPrimary)?.text = holidayList[position].holidayPrimaryType
            bottomSheetDialog.findViewById<TextView>(R.id.txtHolidayCountry)?.text = holidayList[position].holidayCountry

        }

        createNotificationChannel()

        holder.cardViewHoliday.isLongClickable = true
        holder.cardViewHoliday.setOnLongClickListener {
            val picker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Set Reminder for ${holidayList[position].holidayName}")
                .build()

            picker.show((context as AppCompatActivity).supportFragmentManager, "android")
            picker.addOnPositiveButtonClickListener{
                // Check 12 hour format or 24 hour format and convert it to 12 hour format and set it to text view
                setAlarm(picker.hour, picker.minute, holidayList[position].holidayName!!, holidayList[position].holidayDate!!.toInt(), holidayList[position].holidayMonth!!.toInt(), holidayList[position].holidayYear!!.toInt())
            }
            true
        }

        holder.cardViewHoliday.animation = AnimationUtils.loadAnimation(context, R.anim.top_anim)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setAlarm(hour: Int, minute: Int, holidayName: String, date: Int, month: Int, year: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, date)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, FLAG_IMMUTABLE)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)

        Snackbar.make((context as AppCompatActivity).findViewById(android.R.id.content), "Reminder set Success for $holidayName", Snackbar.LENGTH_LONG)
            .setAction("Cancel") {
                alarmManager.cancel(pendingIntent)
                Snackbar.make((context as AppCompatActivity).findViewById(android.R.id.content), "Reminder Cancelled", Snackbar.LENGTH_SHORT).setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE).show()
            }
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
            .show()
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
        val cardViewHolidayType: MaterialCardView = itemView.findViewById(R.id.cardViewHolidayType)
        val imgHoliday: ImageView = itemView.findViewById(R.id.imgHoliday)
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Remainder"
            val description = "Remainder"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("android", name, importance)
            channel.description = description

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}