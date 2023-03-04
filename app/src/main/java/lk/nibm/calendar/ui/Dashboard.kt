package lk.nibm.calendar.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.card.MaterialCardView
import lk.nibm.calendar.Adapter.HolidaysInMonthAdapter
import lk.nibm.calendar.Common.Common
import lk.nibm.calendar.Model.HolidaysModel
import lk.nibm.calendar.R
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class Dashboard : AppCompatActivity() {

    private lateinit var txtDate: TextView
    private lateinit var txtMonth: TextView
    private lateinit var txtYear: TextView
    private lateinit var txtHour: TextView
    private lateinit var txtMin: TextView
    private lateinit var txtAMPM: TextView
    private lateinit var txtDay: TextView
    private lateinit var txtTodayDate: TextView
    private lateinit var txtHolidaysInThisMonth: TextView
    private lateinit var txtHoliday: TextView
    private lateinit var recyclerViewHolidayInThisMonth: RecyclerView
    private lateinit var cardBoBackYears: MaterialCardView
    private lateinit var cardWorldCalendar: MaterialCardView

    private lateinit var dialog: AlertDialog

    private lateinit var holidaysInThisMonth: ArrayList<HolidaysModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        initializeComponents()

        getDateTime()

        getHolidaysInThisMonth()

        clickListeners()

    }

    private fun clickListeners() {
        cardBoBackYears.setOnClickListener {
            val intent = Intent(this, GoBackYears::class.java)
            startActivity(intent)
        }
        cardWorldCalendar.setOnClickListener {
            val intent = Intent(this, WorldCalendar::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getHolidaysInThisMonth() {

        dialog.show()

        val url = resources.getString(R.string.HOLIDAYS_BASE_URL) + resources.getString(R.string.API_KEY) + "&country=LK&year=" + txtYear.text.toString()

        val result = StringRequest(Request.Method.GET, url, Response.Listener { response ->

            try {
                val jsonObject = JSONObject(response)
                val jsonObjectResponse = jsonObject.getJSONObject("response")
                val jsonArrayHolidays = jsonObjectResponse.getJSONArray("holidays")

                // Check holidays in this month
                val currentMonth = SimpleDateFormat("M",Locale.getDefault()).format(Date())
                val currentDay = SimpleDateFormat("d",Locale.getDefault()).format(Date())
                val longMonth = SimpleDateFormat("MMMM",Locale.getDefault()).format(Date())
                for (i in 0 until jsonArrayHolidays.length()){
                    val jsonObjectHolidayList = jsonArrayHolidays.getJSONObject(i)
                    val date = jsonObjectHolidayList.getJSONObject("date")
                    val dateTime = date.getJSONObject("datetime")
                    val month = dateTime.getString("month")
                    if (month == currentMonth){
                        val holidays = HolidaysModel()
                        holidays.holidayName = jsonObjectHolidayList.getString("name")
                        holidays.holidayDescription = jsonObjectHolidayList.getString("description")
                        holidays.holidayDate = dateTime.getString("day")
                        holidays.holidayMonth = longMonth
                        holidays.holidayYear = dateTime.getString("year")
                        holidays.holidayPrimaryType = jsonObjectHolidayList.getString("primary_type")
                        holidays.holidayCountry = jsonObjectHolidayList.getJSONObject("country").getString("name")
                        holidaysInThisMonth.add(holidays)
                    }
                    if (currentDay == dateTime.getString("day")){
                        txtHoliday.text = jsonObjectHolidayList.getString("name")
                        Common.HOLIDAY_NAME = jsonObjectHolidayList.getString("name")
                    } else{
                        txtHoliday.text = "No Holiday Today"
                        Common.HOLIDAY_NAME = "No Holiday Today"
                    }
                }

                // Pass holidays to adapter
                val adapter = HolidaysInMonthAdapter(this, holidaysInThisMonth)
                recyclerViewHolidayInThisMonth.setHasFixedSize(true)
                recyclerViewHolidayInThisMonth.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                recyclerViewHolidayInThisMonth.adapter = adapter
                adapter.notifyDataSetChanged()
                dialog.dismiss()

            }catch (e: Exception){
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

        }, Response.ErrorListener {error ->
            Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        })

        Volley.newRequestQueue(this).add(result)

    }

    private fun getDateTime() {
        SimpleDateFormat("yyyy-MMM-dd hh:mm:ss a", Locale.getDefault()).format(Date())

        val currentYear = SimpleDateFormat("yyyy",Locale.getDefault()).format(Date())
        val currentMonth = SimpleDateFormat("MMM",Locale.getDefault()).format(Date())
        val currentDay = SimpleDateFormat("dd",Locale.getDefault()).format(Date())
        val day = SimpleDateFormat("EEEE",Locale.getDefault()).format(Date())

        val currentHour = SimpleDateFormat("hh",Locale.getDefault()).format(Date())
        val currentMin = StringBuilder(":").append(SimpleDateFormat("mm",Locale.getDefault()).format(Date()))
        val currentAMPM = SimpleDateFormat("a",Locale.getDefault()).format(Date())

        txtDate.text = currentDay
        txtMonth.text = currentMonth
        txtYear.text = currentYear
        txtDay.text = day

        txtHour.text = currentHour
        txtMin.text = currentMin
        txtAMPM.text = currentAMPM

        // Set full date
        val longMonth = SimpleDateFormat("MMMM",Locale.getDefault()).format(Date())
        txtTodayDate.text = StringBuilder().append(currentDay).append(" ").append(longMonth).append(" ").append(currentYear)

        txtHolidaysInThisMonth.text = "Holidays in $longMonth"

    }

    private fun initializeComponents() {
        txtDate = findViewById(R.id.txtDate)
        txtMonth = findViewById(R.id.txtMonth)
        txtYear = findViewById(R.id.txtYear)
        txtHour = findViewById(R.id.txtHour)
        txtMin = findViewById(R.id.txtMin)
        txtAMPM = findViewById(R.id.txtAMPM)
        txtDay = findViewById(R.id.txtDay)
        txtTodayDate = findViewById(R.id.txtTodayDate)
        txtHolidaysInThisMonth = findViewById(R.id.txtHolidaysInThisMonth)
        txtHoliday = findViewById(R.id.txtHoliday)
        recyclerViewHolidayInThisMonth = findViewById(R.id.recyclerViewHolidayInThisMonth)
        cardWorldCalendar = findViewById(R.id.cardWorldCalendar)
        cardBoBackYears = findViewById(R.id.cardBoBackYears)

        holidaysInThisMonth = arrayListOf<HolidaysModel>()

        AlertDialog.Builder(this).apply {
            setCancelable(false)
            setView(R.layout.progress_dialog)
        }.create().also {
            dialog = it
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        }

    }
}