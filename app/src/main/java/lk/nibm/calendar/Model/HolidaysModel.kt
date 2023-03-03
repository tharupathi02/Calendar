package lk.nibm.calendar.Model

class HolidaysModel {

    var holidayName: String? = null
    var holidayDescription : String? = null
    var holidayCountry : String? = null
    var holidayCountryId : String? = null
    var holidayDate : String? = null
    var holidayMonth : String? = null
    var holidayYear : String? = null
    var holidayType : String? = null
    var holidayPrimaryType : String? = null
    var holidayCanonicalURL : String? = null
    var holidayLocation : String? = null
    var holidayStates : String? = null

    constructor()

    constructor(
        holidayName: String?,
        holidayDescription: String?,
        holidayCountry: String?,
        holidayCountryId: String?,
        holidayDate: String?,
        holidayMonth: String?,
        holidayYear: String?,
        holidayType: String?,
        holidayPrimaryType: String?,
        holidayCanonicalURL: String?,
        holidayLocation: String?,
        holidayStates: String?
    ) {
        this.holidayName = holidayName
        this.holidayDescription = holidayDescription
        this.holidayCountry = holidayCountry
        this.holidayCountryId = holidayCountryId
        this.holidayDate = holidayDate
        this.holidayMonth = holidayMonth
        this.holidayYear = holidayYear
        this.holidayType = holidayType
        this.holidayPrimaryType = holidayPrimaryType
        this.holidayCanonicalURL = holidayCanonicalURL
        this.holidayLocation = holidayLocation
        this.holidayStates = holidayStates
    }



}