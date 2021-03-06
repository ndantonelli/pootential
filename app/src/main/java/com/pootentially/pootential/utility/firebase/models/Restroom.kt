package com.pootentially.pootential.utility.firebase.models

import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import java.util.Date

/**
 * Created by nick on 2/1/18.
 */
data class Restroom(
    var id: String = "",
    var name: String = "",
    var lat: Double = 0.0,
    var lon: Double = 0.0,
    var female: Boolean = false,
    var male: Boolean = false,
    var family: Boolean = false,
    var handicap: Boolean = false,
    var changingTable: Boolean = false,
    var shower: Boolean = false,
    var waterFountain: Boolean = false,
    var airDryer: Boolean = false,
    var paperTowels: Boolean = false,
    var ambienceRating: Double = 0.0,
    var cleanlinessRating: Double = 0.0,
    var toiletPaperQualityRating: Double = 0.0,
    var overallRating: Double = 0.0,
    var numberReviews: Int = 0,
    var lastUpdated: Date? = null,
    var altitude: Double = 0.0,
    var numberStalls: Int = 0,
    var floor: String = ""
){

    private var markerOptions: MarkerOptions? = null
    var marker: Marker? = null

    fun getMarkerOptions(): MarkerOptions?{
        if (markerOptions == null) {
            markerOptions = MarkerOptions().position(LatLng(lat, lon))
        }
        return markerOptions
    }

    override fun equals(other: Any?): Boolean {
        if(other is Restroom) {
            return id == other.id
        }
        return super.equals(other)
    }
}