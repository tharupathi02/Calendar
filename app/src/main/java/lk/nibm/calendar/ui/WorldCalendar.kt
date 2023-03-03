package lk.nibm.calendar.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
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

class WorldCalendar : AppCompatActivity() {

    private lateinit var cardBack: MaterialCardView
    private lateinit var fabFilter: FloatingActionButton
    private lateinit var recyclerViewWorldCalendar: RecyclerView
    private lateinit var txtNoHolidaysFound: TextView

    private lateinit var dialog: AlertDialog
    private lateinit var bottomSheetDialog : BottomSheetDialog

    private lateinit var holidaysList: ArrayList<HolidaysModel>

    private var countryId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_world_calendar)

        initializeComponents()

        clickListeners()

        getHolidays()

        bottomSheetDialog()

    }

    private fun bottomSheetDialog() {

        val view: View = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_filter_year_country, null)
        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(view)

        val spinnerYear = bottomSheetDialog.findViewById<Spinner>(R.id.spinnerYear)
        val spinnerMonth = bottomSheetDialog.findViewById<Spinner>(R.id.spinnerMonth)
        val spinnerCountry = bottomSheetDialog.findViewById<Spinner>(R.id.spinnerCountry)
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

        // set countries to spinner from API
        val countries = ArrayList<String>()
        val url = resources.getString(R.string.COUNTRIES_BASE_URL) + resources.getString(R.string.API_KEY)
        val resultCountries = StringRequest(Request.Method.GET, url, Response.Listener { response ->
            try {
                val jsonObject = JSONObject(response)
                val jsonObjectResponse = jsonObject.getJSONObject("response")
                val jsonArrayCountries = jsonObjectResponse.getJSONArray("countries")
                for (i in 0 until jsonArrayCountries.length()){
                    val jsonObjectCountry = jsonArrayCountries.getJSONObject(i)
                    countries.add(jsonObjectCountry.getString("country_name"))
                }
                val adapterCountry = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, countries)
                spinnerCountry?.adapter = adapterCountry
            }catch (e: Exception){
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener { error ->
            Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
        })
        Volley.newRequestQueue(this).add(resultCountries)

        spinnerCountry?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                getCountryId(spinnerCountry?.selectedItem.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@WorldCalendar, "Please select a country", Toast.LENGTH_SHORT).show()
            }

        }

        btnFilter?.setOnClickListener {
            val year = spinnerYear?.selectedItem.toString()
            val month = spinnerMonth?.selectedItem.toString()
            val monthNumber = Common.getMonthNumber(month)
            getHolidays(year, monthNumber, countryId.toString())
            bottomSheetDialog.dismiss()
        }

        btnClearFilter?.setOnClickListener {
            getHolidays()
            bottomSheetDialog.dismiss()
        }

    }

    private fun getCountryId(name: String) {
        val url = resources.getString(R.string.COUNTRIES_BASE_URL) + resources.getString(R.string.API_KEY)
        val resultCountries = StringRequest(Request.Method.GET, url, Response.Listener { response ->
            try {
                val jsonObject = JSONObject(response)
                val jsonObjectResponse = jsonObject.getJSONObject("response")
                val jsonArrayCountries = jsonObjectResponse.getJSONArray("countries")
                for (i in 0 until jsonArrayCountries.length()){
                    val jsonObjectCountry = jsonArrayCountries.getJSONObject(i)
                    if (jsonObjectCountry.getString("country_name") == name){
                        countryId = jsonObjectCountry.getString("iso-3166").toString()
                    }
                }
            }catch (e: Exception){
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener { error ->
            Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
        })
        Volley.newRequestQueue(this).add(resultCountries)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getHolidays(year: String, month: String, country: String) {
        dialog.show()

        // Clear list
        holidaysList.clear()

        val url = resources.getString(R.string.HOLIDAYS_BASE_URL) + resources.getString(R.string.API_KEY) + "&country=" + country + "&year=" + year

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
                    recyclerViewWorldCalendar.setHasFixedSize(true)
                    recyclerViewWorldCalendar.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    recyclerViewWorldCalendar.adapter = adapter
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
                recyclerViewWorldCalendar.setHasFixedSize(true)
                recyclerViewWorldCalendar.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                recyclerViewWorldCalendar.adapter = adapter
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
        recyclerViewWorldCalendar = findViewById(R.id.recyclerViewWorldCalendar)
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