package com.example.pinsonmap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.pinsonmap.models.Pin
import com.example.pinsonmap.models.PinsResponse
import com.example.pinsonmap.utils.getJsonDataFromAsset
import com.google.gson.Gson

class MapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        parseJson()
    }

    fun parseJson() {
        val jsonFileString = getJsonDataFromAsset(applicationContext, "pins.json")

        val gson = Gson()
        val pinsResponse = gson.fromJson(jsonFileString, PinsResponse::class.java)
        val services: List<String> = pinsResponse.services
        val pins: List<Pin> = pinsResponse.pins
    }
}
