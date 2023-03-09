package lk.nibm.calendar.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.card.MaterialCardView
import lk.nibm.calendar.Adapter.HolidaysInMonthAdapter
import lk.nibm.calendar.Common.Common
import lk.nibm.calendar.Model.HolidaysModel
import lk.nibm.calendar.R
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class Dashboard : AppCompatActivity() {

    private lateinit var txtYear: TextView
    private lateinit var txtHolidaysInThisMonth: TextView
    private lateinit var txtHoliday: TextView
    private lateinit var recyclerViewHolidayInThisMonth: RecyclerView
    private lateinit var cardBoBackYears: MaterialCardView
    private lateinit var cardWorldCalendar: MaterialCardView
    private lateinit var imgHoliday: ImageView
    private lateinit var txtLocationCountry: TextView

    private lateinit var dialog: AlertDialog

    private lateinit var holidaysInThisMonth: ArrayList<HolidaysModel>

    // Locations
    private lateinit var fusedLocation: FusedLocationProviderClient
    var isPermissionGranted: Boolean = false
    private val LOCATION_REQUEST_CODE = 100

    private var countryId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        initializeComponents()

        fusedLocation = LocationServices.getFusedLocationProviderClient(this)
        checkLocationPermission()

        getDateTime()

        clickListeners()

    }

    @SuppressLint("MissingPermission")
    private fun getCountry() {
        dialog.show()
        if (isPermissionGranted){
            val locationResult = fusedLocation.lastLocation
            locationResult.addOnCompleteListener(this){location ->
                if (location.isSuccessful) {
                    val lastLocation = location.result
                    val geocoder = Geocoder(this, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(lastLocation!!.latitude, lastLocation.longitude, 1)
                    val country = addresses?.get(0)!!.countryName
                    txtLocationCountry.text = country
                    if (country != null) {
                        getCountryId(country)
                    } else {
                        getHolidaysInThisMonth("LK")
                        dialog.dismiss()
                    }
                } else {
                    getHolidaysInThisMonth("LK")
                    dialog.dismiss()
                }
            }
        } else {
            txtLocationCountry.text = "No Location Found !"
            getHolidaysInThisMonth("LK")
            dialog.dismiss()
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
                        getHolidaysInThisMonth(countryId!!)
                    }
                }
            }catch (e: Exception){
                Toast.makeText(this, "" + e.message, Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }, Response.ErrorListener { error ->
            Toast.makeText(this, "" + error.message, Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        })
        Volley.newRequestQueue(this).add(resultCountries)
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
    private fun getHolidaysInThisMonth(country: String) {
        val url = resources.getString(R.string.HOLIDAYS_BASE_URL) + resources.getString(R.string.API_KEY) + "&country=" + country + "&year=" + SimpleDateFormat("yyyy",Locale.getDefault()).format(Date())

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
                        if (currentDay == dateTime.getString("day")){
                            txtHoliday.text = jsonObjectHolidayList.getString("name")
                            Common.HOLIDAY_NAME = jsonObjectHolidayList.getString("name")
                            if (jsonObjectHolidayList.getString("name").contains("Poya")) {
                                Glide.with(this).load("https://img.icons8.com/color/5200/null/dharmacakra.png").into(imgHoliday)
                                imgHoliday.visibility = View.VISIBLE
                            } else {
                                imgHoliday.visibility = View.GONE
                            }
                        }
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
                Toast.makeText(this, "" + e.message.toString(), Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

        }, Response.ErrorListener {error ->
            Toast.makeText(this, "" + error.message.toString(), Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        })

        Volley.newRequestQueue(this).add(result)

    }

    private fun getDateTime() {
        val longMonth = SimpleDateFormat("MMMM",Locale.getDefault()).format(Date())
        txtHolidaysInThisMonth.text = "Holidays in $longMonth"
    }

    private fun initializeComponents() {
        txtYear = findViewById(R.id.txtYear)
        txtHolidaysInThisMonth = findViewById(R.id.txtHolidaysInThisMonth)
        txtHoliday = findViewById(R.id.txtHoliday)
        recyclerViewHolidayInThisMonth = findViewById(R.id.recyclerViewHolidayInThisMonth)
        cardWorldCalendar = findViewById(R.id.cardWorldCalendar)
        cardBoBackYears = findViewById(R.id.cardBoBackYears)
        imgHoliday = findViewById(R.id.imgHoliday)
        txtLocationCountry = findViewById(R.id.txtLocationCountry)

        holidaysInThisMonth = arrayListOf<HolidaysModel>()

        AlertDialog.Builder(this).apply {
            setCancelable(false)
            setView(R.layout.progress_dialog)
        }.create().also {
            dialog = it
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        }

    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_REQUEST_CODE)
        } else{
            isPermissionGranted = true
            if (isPermissionGranted){
                getCountry()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        isPermissionGranted = false
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isPermissionGranted = true
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}