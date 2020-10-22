package com.example.pinsonmap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.pinsonmap.models.Pin
import com.example.pinsonmap.models.PinsResponse
import com.example.pinsonmap.utils.getJsonDataFromAsset

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var pins: List<Pin>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        parseJson()
    }

    fun parseJson() {
        val jsonFileString = getJsonDataFromAsset(applicationContext, "pins.json")

        val gson = Gson()
        val pinsResponse = gson.fromJson(jsonFileString, PinsResponse::class.java)
        pins = pinsResponse.pins
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        pins.filter { it.service.equals("c") }.forEach{
            val pin = LatLng(it.coordinates.lat, it.coordinates.lng)
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                it.coordinates.lat,
                it.coordinates.lng
            )
            map.addMarker(MarkerOptions()
                .position(pin)
                .title(it.service)
                .snippet(snippet))
        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(55.751244, 37.618423), 11f))

    }
}
