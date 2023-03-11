![Logo](https://www.nibm.lk/wp-content/themes/nibm-theme-child/images/main-logo.svg)
# NIBM Mobile Application Development Final Project - Kotlin

This repository contains the final project for the Mobile Application Development module offered by the National Institute of Business Management (NIBM). The project is implemented using the Kotlin programming language and is focused on developing a mobile application with a user-friendly interface.

This repository contains a RESTful API developed using the Calendarific API. The API provides information about worldwide holidays and observances for different countries and regions.


## Table of Content

* [NIBM Mobile Application Development Final Project - Kotlin](https://github.com/tharupathi02/Calendar#nibm-mobile-application-development-final-project---kotlin)
    * [Documentation](https://github.com/tharupathi02/Calendar#documentation)
    * [Features](https://github.com/tharupathi02/Calendar#features)
    * [Technologies Used](https://github.com/tharupathi02/Calendar#technologies-used)
    * [How to Open the Application](https://github.com/tharupathi02/Calendar#how-to-open-the-application)
    * [Dependencies](https://github.com/tharupathi02/Calendar#dependencies)
    * [API Documentation](https://github.com/tharupathi02/Calendar#api-documentation)
        * [API Reference](https://github.com/tharupathi02/Calendar#api-reference)
            * [Get all items](https://github.com/tharupathi02/Calendar#get-all-items)
            * [An Example Request for All Holidays in the United States for 2019](https://github.com/tharupathi02/Calendar#an-example-request-for-all-holidays-in-the-united-states-for-2019)
            * [API response that executes successfully](https://github.com/tharupathi02/Calendar#api-response-that-executes-successfully)
        * [Request through the Holidays API](https://github.com/tharupathi02/Calendar#request-through-the-holidays-api)
    * [Contributors](https://github.com/tharupathi02/Calendar#contributors)
    * [License](https://github.com/tharupathi02/Calendar#license)
    * [Screenshots](https://github.com/tharupathi02/Calendar#screenshots)
    

## Documentation

[Documentation](https://nibm-my.sharepoint.com/personal/cohdse221f-024_student_nibm_lk/Documents/MAD%20Assessment%204%201.pdf?login_hint=COHDSE221F-024%40student.nibm.lk)
<a href="https://nibm-my.sharepoint.com/personal/cohdse221f-024_student_nibm_lk/Documents/MAD%20Assessment%204%201.pdf?login_hint=COHDSE221F-024%40student.nibm.lk" target="_blank">Documentation</a>


## Features

This App provides the following features:
-   Search holidays by country, year, and type.
-   Get information about holidays, including their names, descriptions, dates, and types.

## Technologies Used

The following technologies and tools were used to implement the project:

-   Kotlin Programming Language
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

## Dependencies

```bash
dependencies{
	// Lottie Library (Animated Icon)
	implementation 'com.airbnb.android:lottie:3.4.0'

	// Volley Library  
	// https://mvnrepository.com/artifact/com.android.volley/volley  
	implementation("com.android.volley:volley:1.2.1")

	// Glide Library for Kotlin
	// https://mvnrepository.com/artifact/com.github.bumptech.glide/glide 
	implementation("com.github.bumptech.glide:glide:4.14.2")
}
```
## API Documentation

The API documentation can be found at `https://calendarific.com`. This provides detailed information on how to use each endpoint.
	
### API Reference

#### Get all items

```bash
  /api/v2/holidays?api_key=<API_KEY>&country=<COUNTRY_CODE>&year=<YEAR>
```

| Parameter | Type     | Description                				|
| :-------- | :------- | :------------------------------------------|
| `api_key` | `string` | **Required**. Your API key 				|
| `country` | `string` | Your Country Code (US, UK, AU, LK) 		|
| `year` 	| `string` | The year you want to return the Holidays 	|

#### An Example Request for All Holidays in the United States for 2019

```bash
  https://calendarific.com/api/v2/holidays?&api_key=baa9dc110aa712sd3a9fa2a3dwb6c01d4c875950dc32vs&country=US&year=2019
```

#### API response that executes successfully

```http
  {
    "meta": {
        "code": 200
    },
    "response": {
        "holidays": [
            {
                "name": "Name of holiday goes here",
                "description": "Description of holiday goes here",
                "date": {
                    "iso": "2018-12-31",
                    "datetime": {
                        "year": 2018,
                        "month": 12,
                        "day": 31
                    }
                },
                "type": [
                    "Type of Observance goes here"
                ]
            }
        ]
    }
}
```


### Request through the Holidays API

```bash
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
```

## Contributors

 1. G D T Karunathilaka
 2. S D N Perera
 3. T M L M Weerasinghe
 4. N A D K K Shaminda

## License

## Screenshots

![Screenshot 1](https://lpl22.x10.mx/App%20UI/1.jpg)

![Screenshot 2](https://lpl22.x10.mx/App%20UI/2.jpg)
