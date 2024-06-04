package com.example.studybridge

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class GoalSettingActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var titleEditText: EditText
    private lateinit var goalTimeTextView: TextView
    private lateinit var startTimeTextView: TextView
    private var isEditMode = false
    private var originalTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_setting)
        enableEdgeToEdge()

        //화면에 상태바,네비게이션바 없게
        StateNavRemoveActivity.hideSystemUI(window)

        // 상태바, 네비게이션바 화면클릭시 사라짐
        findViewById<View>(R.id.goal).setOnClickListener {
            StateNavRemoveActivity.hideSystemUI(window)
        }

        dbHelper = DatabaseHelper(this)

        titleEditText = findViewById(R.id.title_edit_text)
        goalTimeTextView = findViewById(R.id.goal_time_text_view)
        startTimeTextView = findViewById(R.id.start_time_text_view)
        val saveButton: Button = findViewById(R.id.save_button)
        val backButton: ImageButton = findViewById(R.id.back_button)
        val planpageBtn: ImageButton = findViewById(R.id.nav_button4)

        // 수정 모드인지 확인하고 초기화
        isEditMode = intent.getBooleanExtra("isEditMode", false)
        if (isEditMode) {
            originalTitle = intent.getStringExtra("goalTitle")
            titleEditText.setText(originalTitle)
            // 추가로 기존 goalTime 및 startTime을 설정해야 할 경우 여기에 작성
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

        backButton.setOnClickListener {
            finish()
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
        val goalTime = goalTimeTextView.text.toString()
        val startTime = startTimeTextView.text.toString()

        if (isEditMode && originalTitle != null) {
            // 기존 목표 업데이트 로직을 추가할 수 있습니다.
            val cursor = dbHelper.getAllGoals()
            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE)) == originalTitle) {
                        val id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID))
                        dbHelper.updateGoal(id, title, goalTime, startTime, false)  // 기본적으로 체크되지 않은 상태로 저장
                        break
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()
        } else {
            dbHelper.addGoal(title, goalTime, startTime, false)  // 기본적으로 체크되지 않은 상태로 저장
        }

        Toast.makeText(this, "목표가 저장되었습니다", Toast.LENGTH_SHORT).show()
    }
}