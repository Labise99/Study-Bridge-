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

class PlanSettingActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var subjectEditText: EditText
    private lateinit var detailEditText: EditText
    private lateinit var goalTimeTextView: TextView
    private var isEditMode = false
    private var originalPlan: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan_setting)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        subjectEditText = findViewById(R.id.subject_edit_text)
        detailEditText = findViewById(R.id.detail_edit_text)
        goalTimeTextView = findViewById(R.id.goal_time_text_view)
        val saveButton: Button = findViewById(R.id.save_button)
        val backButton: ImageButton = findViewById(R.id.back_button)
        val planpageBtn: ImageButton = findViewById(R.id.nav_button4)

        // 수정 모드인지 확인하고 초기화
        isEditMode = intent.getBooleanExtra("isEditMode", false)
        if (isEditMode) {
            originalPlan = intent.getStringExtra("planTitle")
            val parts = originalPlan?.split("-")
            if (parts != null && parts.size == 2) {
                subjectEditText.setText(parts[0].trim())
                detailEditText.setText(parts[1].trim())
            }
        }

        goalTimeTextView.setOnClickListener {
            showTimePickerDialog { time ->
                goalTimeTextView.text = time
            }
        }

        saveButton.setOnClickListener {
            savePlan()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        backButton.setOnClickListener {
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

    private fun savePlan() {
        val subject = subjectEditText.text.toString()
        val detail = detailEditText.text.toString()
        val plan = "$subject - $detail"
        val plans = sharedPreferences.getStringSet("plans", mutableSetOf())?.toMutableSet() ?: mutableSetOf()

        if (isEditMode && originalPlan != null) {
            plans.remove(originalPlan)
        }

        plans.add(plan)

        with(sharedPreferences.edit()) {
            putStringSet("plans", plans)
            putBoolean("plan_$plan", false) // 초기 체크 상태 저장
            apply()
        }
    }
}
