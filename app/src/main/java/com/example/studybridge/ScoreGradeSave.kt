package com.example.studybridge

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ScoreGradeSave : AppCompatActivity() {
    private lateinit var gradePeriodEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var languageGradeText: EditText
    private lateinit var mathGradeText: EditText
    private lateinit var englishGradeText: EditText
    private lateinit var koreanHistoryGradeText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        StateNavRemoveActivity.hideSystemUI(window)
        setContentView(R.layout.score_grade_save)

        val backButton: ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }

        // UI 요소 초기화
        gradePeriodEditText = findViewById(R.id.testperiod_edit_text)
        saveButton = findViewById(R.id.save_button)
        languageGradeText = findViewById(R.id.language_grade_text)
        mathGradeText = findViewById(R.id.math_grade_edit_text)
        englishGradeText = findViewById(R.id.english_edit_text2)
        koreanHistoryGradeText = findViewById(R.id.korean_history_grade_text)

        // 전달받은 기간 정보를 설정
        val period = intent.getStringExtra("PERIOD")
        if (period != null) {
            gradePeriodEditText.setText(period)
        }

        // 저장 버튼 클릭 리스너 설정
        saveButton.setOnClickListener {
            val periodText = gradePeriodEditText.text.toString()
            if (periodText.isNotEmpty()) {
                saveData()
                val resultIntent = Intent()
                resultIntent.putExtra("PERIOD", periodText)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "시기를 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences("ScoreGradeData", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("period", gradePeriodEditText.text.toString())
            putString("languageGrade", languageGradeText.text.toString())
            putString("mathGrade", mathGradeText.text.toString())
            putString("englishGrade", englishGradeText.text.toString())
            putString("koreanHistoryGrade", koreanHistoryGradeText.text.toString())
            apply()
        }
    }
}