package com.example.timezoneconvertor

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var editTextTime: TextInputEditText
    private lateinit var spinnerOriginalTimeZones: Spinner
    private lateinit var spinnerTargetTimeZones: Spinner
    private lateinit var buttonConvert: Button
    private lateinit var textViewOriginalTime: TextView
    private lateinit var textViewConvertedTime: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextTime = findViewById(R.id.editTextTime)
        spinnerOriginalTimeZones = findViewById(R.id.spinnerOriginalTimeZones)
        spinnerTargetTimeZones = findViewById(R.id.spinnerTargetTimeZones)
        buttonConvert = findViewById(R.id.buttonConvert)
        textViewOriginalTime = findViewById(R.id.textViewOriginalTime)
        textViewConvertedTime = findViewById(R.id.textViewConvertedTime)

        setupSpinners()

        // Set onClickListener for the Convert button
        buttonConvert.setOnClickListener {
            convertTime()
        }
    }

    private fun setupSpinners() {
        val timeZones = listOf("GMT", "UTC", "PST (-08:00)", "EST (-05:00)", "IST (+05:30)") // Correct offsets
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, timeZones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerOriginalTimeZones.adapter = adapter
        spinnerTargetTimeZones.adapter = adapter
    }

    private fun convertTime() {
        val inputTime = editTextTime.text.toString().trim()

        if (inputTime.isEmpty()) {
            editTextTime.error = "Please enter a valid time"
            return
        }

        val originalTimeZone = getTimeZoneId(spinnerOriginalTimeZones.selectedItem as String)
        val targetTimeZone = getTimeZoneId(spinnerTargetTimeZones.selectedItem as String)

        try {
            val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            inputFormat.isLenient = false
            inputFormat.timeZone = TimeZone.getTimeZone(originalTimeZone)
            val date = inputFormat.parse(inputTime)

            val outputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            outputFormat.timeZone = TimeZone.getTimeZone(targetTimeZone)
            val convertedTime = outputFormat.format(date)

            textViewOriginalTime.text = "Original Time: $inputTime ($originalTimeZone)"
            textViewConvertedTime.text = "Converted Time: $convertedTime ($targetTimeZone)"
        } catch (e: Exception) {
            editTextTime.error = "Invalid time format"
        }
    }

    private fun getTimeZoneId(displayName: String): String {
        return when (displayName) {
            "PST (-08:00)" -> "GMT-08:00"
            "EST (-05:00)" -> "GMT-05:00"
            "IST (+05:30)" -> "GMT+05:30"
            else -> displayName
        }
    }
}