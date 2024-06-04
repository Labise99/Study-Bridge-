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

class ScoreTestSave : AppCompatActivity() {

    // 테스트 기간 입력창
    private lateinit var testPeriodEditText: EditText
    private lateinit var languageGradeText: EditText
    private lateinit var languageScoreText: EditText
    private lateinit var mathGradeText: EditText
    private lateinit var mathScoreText: EditText
    private lateinit var englishGradeText: EditText
    private lateinit var englishScoreText: EditText
    private lateinit var koreanHistoryGradeText: EditText
    private lateinit var koreanHistoryScoreText: EditText
    private lateinit var scienceGradeText: EditText
    private lateinit var scienceScoreText: EditText

    // 저장 버튼
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        StateNavRemoveActivity.hideSystemUI(window)
        setContentView(R.layout.score_test_save)

        val backButton: ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }

        // 레이아웃에서 뷰 요소 찾기
        testPeriodEditText = findViewById(R.id.testperiod_edit_text)
        saveButton = findViewById(R.id.save_button)
        languageGradeText = findViewById(R.id.language_grade_text)
        languageScoreText = findViewById(R.id.language_score_text)
        mathGradeText = findViewById(R.id.math_grade_edit_text)
        mathScoreText = findViewById(R.id.math_score_text)
        englishGradeText = findViewById(R.id.english_edit_text2)
        englishScoreText = findViewById(R.id.english_score_text)
        koreanHistoryGradeText = findViewById(R.id.korean_history_grade_text)
        koreanHistoryScoreText = findViewById(R.id.korean_history_score_text)
        scienceGradeText = findViewById(R.id.science_grade_text)
        scienceScoreText = findViewById(R.id.science_score_text)

        // 전달받은 기간 정보 가져오기
        val period = intent.getStringExtra("PERIOD")
        if (period != null) {
            // 기간 정보를 입력창에 설정
            testPeriodEditText.setText(period)
        }

        // 저장 버튼 클릭 리스너 설정
        saveButton.setOnClickListener {
            val periodText = testPeriodEditText.text.toString()
            if (periodText.isNotEmpty()) {
                // 데이터 저장
                saveData()

                // 결과 인텐트에 기간 정보 추가
                val resultIntent = Intent()
                resultIntent.putExtra("PERIOD", periodText)
                // 결과 설정 및 액티비티 종료
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                // 기간이 입력되지 않은 경우 토스트 메시지 표시
                Toast.makeText(this, "시기를 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences("ScoreData", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("period", testPeriodEditText.text.toString())
            putString("languageGrade", languageGradeText.text.toString())
            putString("languageScore", languageScoreText.text.toString())
            putString("mathGrade", mathGradeText.text.toString())
            putString("mathScore", mathScoreText.text.toString())
            putString("englishGrade", englishGradeText.text.toString())
            putString("englishScore", englishScoreText.text.toString())
            putString("koreanHistoryGrade", koreanHistoryGradeText.text.toString())
            putString("koreanHistoryScore", koreanHistoryScoreText.text.toString())
            putString("scienceGrade", scienceGradeText.text.toString())
            putString("scienceScore", scienceScoreText.text.toString())
            apply()
        }
    }
}