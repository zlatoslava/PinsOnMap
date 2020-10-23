package com.example.pinsonmap

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

    private val FILTER_ACTIVITY_REQUEST_CODE = 0
    private lateinit var map: GoogleMap
    private lateinit var pins: List<Pin>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        parseJson()
    }

    private fun parseJson() {           //parse json file from local directory into list of pins
        val jsonFileString = getJsonDataFromAsset(applicationContext, "pins.json")
        val pinsResponse = Gson().fromJson(jsonFileString, PinsResponse::class.java)
        pins = pinsResponse.pins
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.show_services -> {
            startFilterActivity()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun startFilterActivity() {
        val intent = Intent(this, FilterActivity::class.java)
        startActivityForResult(intent, FILTER_ACTIVITY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        map.clear()

        if (requestCode == FILTER_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val stackOfServices = data!!.getSerializableExtra("stackOfServices") as ArrayDeque<*>           //Get list of services which were marked from FilterActivity
                while (!stackOfServices.isEmpty()) {
                    showServices(stackOfServices.pop() as String)
                }
            } else {
                showDialogNoServicesWereMarked()
            }
        }
    }

    private fun showServices(s: String) {           // Create pins on map if such service was marked
        pins.filter { it.service == s }.forEach {
            val pin = LatLng(it.coordinates.lat, it.coordinates.lng)
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                it.coordinates.lat,
                it.coordinates.lng
            )
            map.addMarker(           //add marker on map with title "type of service" and exact coordinates
                MarkerOptions()
                    .position(pin)
                    .title(it.service)
                    .snippet(snippet)
            )
        }
        map.moveCamera(           //Move camera on map to convenient view of city
            CameraUpdateFactory.newLatLngZoom(
                LatLng(55.751244, 37.618423),           // Moscow coordinates
                11f
            )
        )
    }

    private fun showDialogNoServicesWereMarked() {
        val builder = AlertDialog.Builder(this).apply {
            setTitle("Error")
            setMessage("No services were marked!\n\n" + "Please mark some service and try again.")
            setNeutralButton("Ok") { dialog, which ->
                startFilterActivity()
            }
        }
        builder.create().show()
    }
}
