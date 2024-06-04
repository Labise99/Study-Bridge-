package com.example.studybridge

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.studybridge.R
import com.example.studybridge.ScoreGradeMain
import com.example.studybridge.ScoreTestMain
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ScoreMain : AppCompatActivity() {
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var btnAnalysis: Button
    private lateinit var btnMockTest: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        StateNavRemoveActivity.hideSystemUI(window)


        setContentView(R.layout.score_main)

        // UI 요소 초기화
        floatingActionButton = findViewById(R.id.floatingActionButton4)
        btnAnalysis = findViewById(R.id.btn_analysis)
        btnMockTest = findViewById(R.id.btn_mock_test)

        // 플로팅 액션 버튼 클릭 리스너 설정
        floatingActionButton.setOnClickListener {
            toggleButtonsVisibility() // 버튼 가시성 토글
        }

        // btnAnalysis 클릭 시 ScoreGradeMain 액티비티로 이동
        btnAnalysis.setOnClickListener {
            val intent = Intent(this, ScoreGradeMain::class.java)
            startActivity(intent)
        }

        // btnMockTest 클릭 시 ScoreTestMain 액티비티로 이동
        btnMockTest.setOnClickListener {
            val intent = Intent(this, ScoreTestMain::class.java)
            startActivity(intent)
        }
        val scoreButton: ImageButton = findViewById(R.id.nav_button1)
        scoreButton.setOnClickListener {
            val intent = Intent(this, ScoreMain::class.java)
            startActivity(intent)
        }

        // nav_button1 클릭 시 ScoreMain으로 이동
        val profileButton: ImageButton = findViewById(R.id.nav_button5)
        profileButton.setOnClickListener {
            val intent = Intent(this, ProfileMainActivity::class.java)
            startActivity(intent)
        }

        // nav_button1 클릭 시 homeMain으로 이동
        val homeButton: ImageButton = findViewById(R.id.nav_button3)
        homeButton.setOnClickListener {
            val intent = Intent(this, StudyBridgeMainHome::class.java)
            startActivity(intent)
        }

        // nav_button1 클릭 시 ScoreMain으로 이동
        val planButton: ImageButton = findViewById(R.id.nav_button4)
        planButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    // 플로팅 액션 버튼 클릭 시 버튼 가시성 토글
    private fun toggleButtonsVisibility() {
        if (btnAnalysis.visibility == View.GONE && btnMockTest.visibility == View.GONE) {
            btnAnalysis.visibility = View.VISIBLE
            btnMockTest.visibility = View.VISIBLE
        } else {
            btnAnalysis.visibility = View.GONE
            btnMockTest.visibility = View.GONE
        }
    }
}