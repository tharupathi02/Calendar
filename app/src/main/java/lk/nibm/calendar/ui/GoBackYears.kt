package lk.nibm.calendar.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import lk.nibm.calendar.Adapter.HolidayAdapter
import lk.nibm.calendar.Common.Common
import lk.nibm.calendar.Model.HolidaysModel
import lk.nibm.calendar.R
import org.json.JSONObject
import java.util.*

class GoBackYears : AppCompatActivity() {

    private lateinit var cardBack: MaterialCardView
    private lateinit var fabFilter: FloatingActionButton
    private lateinit var recyclerViewBackYears: RecyclerView
    private lateinit var txtNoHolidaysFound: TextView

    private lateinit var dialog: AlertDialog
    private lateinit var bottomSheetDialog : BottomSheetDialog

    private lateinit var holidaysList: ArrayList<HolidaysModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_go_back_years)

        initializeComponents()

        clickListeners()

        getHolidays()

        bottomSheetDialog()

    }

    private fun bottomSheetDialog() {

        val view: View = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_filter_year, null)
        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(view)

        val spinnerYear = bottomSheetDialog.findViewById<Spinner>(R.id.spinnerYear)
        val spinnerMonth = bottomSheetDialog.findViewById<Spinner>(R.id.spinnerMonth)
        val btnFilter = bottomSheetDialog.findViewById<Button>(R.id.btnFilter)
        val btnClearFilter = bottomSheetDialog.findViewById<Button>(R.id.btnClearFilter)

        // set passed 10 years to spinner
        val years = ArrayList<String>()
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        for (i in 0..10){
            years.add((currentYear - i).toString())
        }
        val adapterYear = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, years)
        spinnerYear?.adapter = adapterYear

        // set months to spinner
        val months = ArrayList<String>()
        months.add("All Months")
        months.add("January")
        months.add("February")
        months.add("March")
        months.add("April")
        months.add("May")
        months.add("June")
        months.add("July")
        months.add("August")
        months.add("September")
        months.add("October")
        months.add("November")
        months.add("December")
        val adapterMonth = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, months)
        spinnerMonth?.adapter = adapterMonth

        btnFilter?.setOnClickListener {
            val year = spinnerYear?.selectedItem.toString()
            val month = spinnerMonth?.selectedItem.toString()
            val monthNumber = Common.getMonthNumber(month)
            getHolidays(year, monthNumber)
            bottomSheetDialog.dismiss()
        }

        btnClearFilter?.setOnClickListener {
            getHolidays()
            bottomSheetDialog.dismiss()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getHolidays(year: String, month: String) {
        dialog.show()

        // Clear list
        holidaysList.clear()

        val url = resources.getString(R.string.HOLIDAYS_BASE_URL) + resources.getString(R.string.API_KEY) + "&country=LK&year=" + year

        val result = StringRequest(Request.Method.GET, url, Response.Listener { response ->
            try {
                val jsonObject = JSONObject(response)
                if (jsonObject != null){
                    val jsonObjectResponse = jsonObject.getJSONObject("response")
                    val jsonArrayHolidays = jsonObjectResponse.getJSONArray("holidays")
                    for (i in 0 until jsonArrayHolidays.length()){
                        val jsonObjectHolidayList = jsonArrayHolidays.getJSONObject(i)
                        val date = jsonObjectHolidayList.getJSONObject("date")
                        val dateTime = date.getJSONObject("datetime")
                        val holidays = HolidaysModel()
                        if (dateTime.getString("month") == month){
                            holidays.holidayName = jsonObjectHolidayList.getString("name")
                            holidays.holidayDescription = jsonObjectHolidayList.getString("description")
                            holidays.holidayDate = dateTime.getString("day")
                            holidays.holidayMonth = dateTime.getString("month")
                            holidays.holidayYear = dateTime.getString("year")
                            holidays.holidayPrimaryType = jsonObjectHolidayList.getString("primary_type")
                            holidays.holidayCountry = jsonObjectHolidayList.getJSONObject("country").getString("name")
                            holidaysList.add(holidays)
                        } else if (month == "0"){
                            holidays.holidayName = jsonObjectHolidayList.getString("name")
                            holidays.holidayDescription = jsonObjectHolidayList.getString("description")
                            holidays.holidayDate = dateTime.getString("day")
                            holidays.holidayMonth = dateTime.getString("month")
                            holidays.holidayYear = dateTime.getString("year")
                            holidays.holidayPrimaryType = jsonObjectHolidayList.getString("primary_type")
                            holidays.holidayCountry = jsonObjectHolidayList.getJSONObject("country").getString("name")
                            holidaysList.add(holidays)
                        }
                    }
                    // Pass holidays to adapter
                    val adapter = HolidayAdapter(this, holidaysList)
                    recyclerViewBackYears.setHasFixedSize(true)
                    recyclerViewBackYears.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    recyclerViewBackYears.adapter = adapter
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                } else {
                    holidaysList.clear()
                    txtNoHolidaysFound.visibility = View.VISIBLE
                    dialog.dismiss()
                }

            }catch (e: Exception){
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

        }, Response.ErrorListener { error ->
            Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        })

        Volley.newRequestQueue(this).add(result)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getHolidays() {

        dialog.show()

        val url = resources.getString(R.string.HOLIDAYS_BASE_URL) + resources.getString(R.string.API_KEY) + "&country=LK&year=2023"

        val result = StringRequest(Request.Method.GET, url, Response.Listener { response ->
            try {
                val jsonObject = JSONObject(response)
                val jsonObjectResponse = jsonObject.getJSONObject("response")
                val jsonArrayHolidays = jsonObjectResponse.getJSONArray("holidays")

                for (i in 0 until jsonArrayHolidays.length()){
                    val jsonObjectHolidayList = jsonArrayHolidays.getJSONObject(i)
                    val date = jsonObjectHolidayList.getJSONObject("date")
                    val dateTime = date.getJSONObject("datetime")
                    val holidays = HolidaysModel()
                    holidays.holidayName = jsonObjectHolidayList.getString("name")
                    holidays.holidayDescription = jsonObjectHolidayList.getString("description")
                    holidays.holidayDate = dateTime.getString("day")
                    holidays.holidayMonth = dateTime.getString("month")
                    holidays.holidayYear = dateTime.getString("year")
                    holidays.holidayPrimaryType = jsonObjectHolidayList.getString("primary_type")
                    holidays.holidayCountry = jsonObjectHolidayList.getJSONObject("country").getString("name")
                    holidaysList.add(holidays)
                }

                // Pass holidays to adapter
                val adapter = HolidayAdapter(this, holidaysList)
                recyclerViewBackYears.setHasFixedSize(true)
                recyclerViewBackYears.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                recyclerViewBackYears.adapter = adapter
                adapter.notifyDataSetChanged()
                dialog.dismiss()

            }catch (e: Exception){
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

        }, Response.ErrorListener { error ->
            Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        })

        Volley.newRequestQueue(this).add(result)

    }

    private fun clickListeners() {
        cardBack.setOnClickListener {
            onBackPressed()
        }

        fabFilter.setOnClickListener {
            bottomSheetDialog.show()
        }
    }

    private fun initializeComponents() {
        cardBack = findViewById(R.id.cardBack)
        fabFilter = findViewById(R.id.fabFilter)
        recyclerViewBackYears = findViewById(R.id.recyclerViewBackYears)
        txtNoHolidaysFound = findViewById(R.id.txtNoHolidaysFound)

        holidaysList = arrayListOf<HolidaysModel>()

        AlertDialog.Builder(this).apply {
            setCancelable(false)
            setView(R.layout.progress_dialog)
        }.create().also {
            dialog = it
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }
}