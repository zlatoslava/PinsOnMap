package com.example.pinsonmap

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_filter_activity.*
import java.util.ArrayDeque


class FilterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_activity)

        setListeners()
    }

    private fun setListeners() {
        button_show.setOnClickListener {
            sendResultOfActivity()
            finish()
        }
    }

    private fun sendResultOfActivity() {
        intent = Intent(this, MapsActivity::class.java)

        val stackOfServices = getListOfServices()
        if (stackOfServices.isEmpty()) {
            setResult(Activity.RESULT_CANCELED, intent)
        } else {
            intent.putExtra("stackOfServices", stackOfServices)
            setResult(Activity.RESULT_OK, intent)
        }
    }

    private fun getListOfServices(): ArrayDeque<String> {
        val stackOfServices = ArrayDeque<String>()

        if (checkBoxA.isChecked) stackOfServices.push("a")
        if (checkBoxB.isChecked) stackOfServices.push("b")
        if (checkBoxC.isChecked) stackOfServices.push("c")

        return stackOfServices
    }
}
