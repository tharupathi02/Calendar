# NIBM Mobile Application Development Final Project - Kotlin

This repository contains the final project for the Mobile Application Development module offered by the National Institute of Business Management (NIBM). The project is implemented using the Kotlin programming language and is focused on developing a mobile application with a user-friendly interface that allows users to manage their daily tasks and schedules.

This repository contains a RESTful API developed using the Calendarific API. The API provides information about worldwide holidays and observances for different countries and regions.


## Features

This App provides the following features:
-   Search holidays by country, year, and type.
-   Get information about holidays, including their names, descriptions, dates, and types.

## Technologies Used

The following technologies and tools were used to implement the project:

-   Kotlin programming language
-   Android Studio IDE
-   Volley Library
-   Calendarific API

## How to Open the Application

 1. Clone the Github repository that contains the Android project. You can do this by navigating to the repository page on Github and clicking on the "Clone or download" button, then copying the repository URL.

 2. Open Android Studio on your local machine.
    
3.  Select "File" from the menu bar, then choose "New" > "Project from Version Control" > "Git"
    
4.  In the "Clone Repository" window, paste the repository URL in the "URL" field.
    
5.  Choose the directory where you want to store the project on your local machine.
    
6.  Click the "Clone" button to start the cloning process. Android Studio will download the project from Github and import it into the IDE.

7.  Once the project is imported, you can open and run it in Android Studio as you would with any other Android project.

## API Documentation

The API documentation can be found at `https://calendarific.com`. This provides detailed information on how to use each endpoint.

## Dependencies

    dependencies{
	    // Lottie Library (Animated Icon)
	    implementation 'com.airbnb.android:lottie:3.4.0'
	    
	    // Volley Library  
	    // https://mvnrepository.com/artifact/com.android.volley/volley  
	    implementation("com.android.volley:volley:1.2.1")
	}

## Request through the Holidays API

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

## Contributors

 1. G D T Karunathilaka
 2. S D N Perera
 3. T M L M Weerasinghe
 4. N A D K K Shaminda

## License

## Screenshots

![Screenshot 1](https://lpl22.x10.mx/App%20UI/1.jpg)

![Screenshot 2](https://lpl22.x10.mx/App%20UI/2.jpg)
