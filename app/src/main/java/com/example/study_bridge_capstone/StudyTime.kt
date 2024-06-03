package com.example.study_bridge_capstone

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.TimeUnit
import kotlin.math.max

class StudyTime : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var prefs: SharedPreferences
    private var isResting = false
    private var studyTime = 0L
    private var restTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study_time)

        val time1 = findViewById<TextView>(R.id.time1)
        val time2 = findViewById<TextView>(R.id.time2)
        val restButton = findViewById<Button>(R.id.restButton)

        prefs = getSharedPreferences("StudyTimePrefs", Context.MODE_PRIVATE)
        prefs.edit().clear().apply() // Clear SharedPreferences

        studyTime = 0L
        restTime = 0L

        val goalTimeMillis = TimeUnit.MINUTES.toMillis(60)
        time1.text = formatTime(goalTimeMillis)

        restButton.setOnClickListener {
            isResting = !isResting
            if (isResting) {
                restButton.text = "휴식 종료"
                studyTime += System.currentTimeMillis() - prefs.getLong("startTime", 0)
                prefs.edit().putLong("startTime", System.currentTimeMillis()).apply()
            } else {
                restButton.text = "휴식 시작"
                restTime += System.currentTimeMillis() - prefs.getLong("startTime", 0)
                prefs.edit().putLong("startTime", System.currentTimeMillis()).apply()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val pauseTime = prefs.getLong("pauseTime", 0)
        val backgroundTime = prefs.getLong("backgroundTime", 0)
        if (pauseTime != 0L) {
            val now = System.currentTimeMillis()
            if (isResting) {
                restTime += now - pauseTime - backgroundTime
            } else {
                studyTime += now - pauseTime - backgroundTime
            }
            prefs.edit().remove("pauseTime").apply()
            prefs.edit().remove("backgroundTime").apply()
        }
        prefs.edit().putLong("startTime", System.currentTimeMillis()).apply()
        handler.post(timerRunnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
        val now = System.currentTimeMillis()
        if (isResting) {
            restTime += now - prefs.getLong("startTime", 0)
        } else {
            studyTime += now - prefs.getLong("startTime", 0)
        }
        prefs.edit().putLong("studyTime", studyTime).apply()
        prefs.edit().putLong("restTime", restTime).apply()
        prefs.edit().putLong("pauseTime", now).apply()
        prefs.edit().putLong("backgroundTime", now - prefs.getLong("startTime", 0)).apply()
    }

    private val timerRunnable = object : Runnable {
        override fun run() {
            val startTime = prefs.getLong("startTime", 0)
            val goalTimeMillis = TimeUnit.MINUTES.toMillis(60)
            val now = System.currentTimeMillis()

            if (isResting) {
                val elapsedTime = restTime + (now - startTime)
                findViewById<TextView>(R.id.time2).text = formatTime(elapsedTime)
            } else {
                val elapsedTime = studyTime + (now - startTime)
                val remainingTime = max(0, goalTimeMillis - elapsedTime)
                findViewById<TextView>(R.id.time1).text = formatTime(remainingTime)
            }

            handler.postDelayed(this, 1000)
        }
    }

    private fun formatTime(millis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}