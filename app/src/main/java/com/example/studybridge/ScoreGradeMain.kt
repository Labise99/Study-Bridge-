package com.example.studybridge

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ScoreGradeMain : AppCompatActivity() {
    private lateinit var gradePeriodTextView: TextView
    private lateinit var editButton: Button
    private val REQUEST_CODE_SAVE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        StateNavRemoveActivity.hideSystemUI(window)
        setContentView(R.layout.score_grade_main)

        // nav_button1 클릭 시 ScoreMain으로 이동
        val scoreButton: ImageButton = findViewById(R.id.nav_button1)
        scoreButton.setOnClickListener {
            val intent = Intent(this, ScoreMain::class.java)
            startActivity(intent)
        }

        // nav_button1 클릭 시 ProfileMainActivity으로 이동
        val profileButton: ImageButton = findViewById(R.id.nav_button5)
        profileButton.setOnClickListener {
            val intent = Intent(this, ProfileMainActivity::class.java)
            startActivity(intent)
        }

        // nav_button1 클릭 시 StudyBridgeMainHome으로 이동
        val homeButton: ImageButton = findViewById(R.id.nav_button3)
        homeButton.setOnClickListener {
            val intent = Intent(this, StudyBridgeMainHome::class.java)
            startActivity(intent)
        }

        // nav_button1 클릭 시 MainActivity으로 이동
        val planButton: ImageButton = findViewById(R.id.nav_button4)
        planButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // UI 요소 초기화
        gradePeriodTextView = findViewById(R.id.grade_period_text_view)
        editButton = findViewById(R.id.edit_button)
        val gradeRegistrationButton = findViewById<ImageButton>(R.id.grade_registration_button)

        // 학점 등록 버튼 클릭 리스너 설정
        gradeRegistrationButton.setOnClickListener {
            val intent = Intent(this, ScoreGradeSave::class.java)
            startActivityForResult(intent, REQUEST_CODE_SAVE)
        }

        // 수정 버튼 클릭 리스너 설정
        editButton.setOnClickListener {
            val intent = Intent(this, ScoreGradeSave::class.java)
            val period = gradePeriodTextView.text.toString()
            intent.putExtra("PERIOD", period)
            startActivityForResult(intent, REQUEST_CODE_SAVE)
        }

        // 기간 텍스트뷰 클릭 리스너 설정
        gradePeriodTextView.setOnClickListener {
            val intent = Intent(this, ScoreGradeCheck::class.java)
            val period = gradePeriodTextView.text.toString()
            intent.putExtra("PERIOD", period)
            startActivity(intent)
        }
    }

    // ScoreGradeSave 액티비티에서 돌아왔을 때 호출되는 메서드
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SAVE && resultCode == Activity.RESULT_OK) {
            val period = data?.getStringExtra("PERIOD")
            gradePeriodTextView.text = period
            gradePeriodTextView.visibility = TextView.VISIBLE
            gradePeriodTextView.isClickable = true
            editButton.visibility = Button.VISIBLE
        }
    }
}