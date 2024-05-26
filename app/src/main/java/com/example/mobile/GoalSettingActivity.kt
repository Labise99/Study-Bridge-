package com.example.mobile

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class GoalSettingActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var titleEditText: EditText
    private lateinit var goalTimeTextView: TextView
    private lateinit var startTimeTextView: TextView
    private var isEditMode = false
    private var originalTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_setting)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        titleEditText = findViewById(R.id.title_edit_text)
        goalTimeTextView = findViewById(R.id.goal_time_text_view)
        startTimeTextView = findViewById(R.id.start_time_text_view)
        val saveButton: Button = findViewById(R.id.save_button)
        val BackButton: ImageButton = findViewById(R.id.back_button)
        val planpageBtn: ImageButton = findViewById(R.id.nav_button4)

        // 수정 모드인지 확인하고 초기화
        isEditMode = intent.getBooleanExtra("isEditMode", false)
        if (isEditMode) {
            originalTitle = intent.getStringExtra("goalTitle")
            titleEditText.setText(originalTitle)
        }

        goalTimeTextView.setOnClickListener {
            showTimePickerDialog { time ->
                goalTimeTextView.text = time
            }
        }

        startTimeTextView.setOnClickListener {
            showTimePickerDialog { time ->
                startTimeTextView.text = time
            }
        }

        saveButton.setOnClickListener {
            saveGoal()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        BackButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        planpageBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showTimePickerDialog(onTimeSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
            val time = String.format("%02d:%02d", selectedHour, selectedMinute)
            onTimeSelected(time)
        }, hour, minute, true)

        timePickerDialog.show()
    }

    private fun saveGoal() {
        val title = titleEditText.text.toString()
        val goals = sharedPreferences.getStringSet("goals", mutableSetOf())?.toMutableSet() ?: mutableSetOf()

        if (isEditMode && originalTitle != null) {
            goals.remove(originalTitle)
        }

        goals.add(title)

        with(sharedPreferences.edit()) {
            putStringSet("goals", goals)
            putBoolean("goal_$title", false) // 초기 체크 상태 저장
            apply()
        }
    }
}
