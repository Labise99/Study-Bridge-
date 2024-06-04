package com.example.studybridge

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class PlanSettingActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var subjectEditText: EditText
    private lateinit var detailEditText: EditText
    private lateinit var goalTimeTextView: TextView
    private var isEditMode = false
    private var originalSubject: String? = null
    private var originalDetail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan_setting)

        dbHelper = DatabaseHelper(this)

        subjectEditText = findViewById(R.id.subject_edit_text)
        detailEditText = findViewById(R.id.detail_edit_text)
        goalTimeTextView = findViewById(R.id.goal_time_text_view)
        val saveButton: Button = findViewById(R.id.save_button)
        val backButton: ImageButton = findViewById(R.id.back_button)

        // 수정 모드인지 확인하고 초기화
        isEditMode = intent.getBooleanExtra("isEditMode", false)
        if (isEditMode) {
            originalSubject = intent.getStringExtra("planSubject")
            originalDetail = intent.getStringExtra("planDetail")
            subjectEditText.setText(originalSubject)
            detailEditText.setText(originalDetail)
            // 추가로 기존 goalTime을 설정해야 할 경우 여기에 작성
        }
        //화면에 상태바,네비게이션바 없게
        StateNavRemoveActivity.hideSystemUI(window)

        // 상태바, 네비게이션바 화면클릭시 사라짐
        findViewById<View>(R.id.main).setOnClickListener {
            StateNavRemoveActivity.hideSystemUI(window)
        }

        goalTimeTextView.setOnClickListener {
            showTimePickerDialog { time ->
                goalTimeTextView.text = time
            }
        }

        saveButton.setOnClickListener {
            savePlan()
            val intent = Intent(this, MainActivity::class.java)
            setResult(RESULT_OK, intent)
            finish()
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun showTimePickerDialog(onTimeSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            val time = String.format("%02d:%02d", selectedHour, selectedMinute)
            onTimeSelected(time)
        }, hour, minute, true)

        timePickerDialog.show()
    }

    private fun savePlan() {
        val subject = subjectEditText.text.toString()
        val detail = detailEditText.text.toString()
        val goalTime = goalTimeTextView.text.toString()
        val plan = "$subject-$detail"

        if (isEditMode && originalSubject != null && originalDetail != null) {
            // 기존 계획 업데이트 로직을 추가할 수 있습니다.
            val cursor = dbHelper.getAllPlans()
            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUBJECT)) == originalSubject
                        && cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DETAIL)) == originalDetail) {
                        val id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID))
                        dbHelper.updatePlan(id, subject, detail, goalTime, false)  // 기본적으로 체크되지 않은 상태로 저장
                        break
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()
        } else {
            dbHelper.addPlan(subject, detail, goalTime, false)  // 기본적으로 체크되지 않은 상태로 저장
        }

        Toast.makeText(this, "계획이 저장되었습니다", Toast.LENGTH_SHORT).show()
    }
}