package com.example.studybridge

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProfileMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.profile_main)

        //화면에 상태바,네비게이션바 없게
        StateNavRemoveActivity.hideSystemUI(window)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profile_home)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, 0, 0, 0)
            insets
        }
        // Spinner 초기화
        val spinner: Spinner = findViewById(R.id.spinner_identity)

        // 어댑터 생성 및 설정
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.identity_array,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // 상태바, 네비게이션바 화면클릭시 사라짐
        findViewById<View>(R.id.profile_home).setOnClickListener {
            StateNavRemoveActivity.hideSystemUI(window)
        }


        // 저장 버튼 클릭 시 알림 표시
        val submitButton: Button = findViewById(R.id.submitButton)
        submitButton.setOnClickListener {
            showSaveNotification()
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


    // 저장되었습니다 알림 표시 메서드
    private fun showSaveNotification() {
        // 알림을 생성하고 표시합니다. 여기서는 간단한 Toast 메시지를 사용하겠습니다.
        Toast.makeText(this, "저장되었습니다", Toast.LENGTH_SHORT).show()
    }

}

//        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
//        bottomNavigationView.setOnItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.nav_score -> {
//                    // Home 선택됨
//                    // TODO: 점수 페이지로 이동
//                    true
//                }
//                R.id.nav_statistics -> {
//                    // Search 선택됨
//                    // TODO: 통계 페이지로 이동
//                    true
//                }
//                R.id.nav_home -> {
//                    // Search 선택됨
//                    // TODO: 홈 페이지로 이동
//                    true
//                }
//                R.id.nav_planner -> {
//                    // Search 선택됨
//                    // TODO: 공부계획 페이지로 이동
//                    true
//                }
//                R.id.nav_profile -> {
//                    // Profile 선택됨
//                    // 현재 페이지가 프로필 페이지이므로 아무 동작도 하지 않음
//                    true
//                }
//                else -> false
//            }
//        }