package com.example.studybridge

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class StudyBridgeMainHome : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var goalLayout: LinearLayout
    private lateinit var planLayout: LinearLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.studybridge_main_home)
        enableEdgeToEdge()

        // 화면에 상태바, 네비게이션바 없게
        StateNavRemoveActivity.hideSystemUI(window)

        // 상태바, 네비게이션바 화면 클릭 시 사라짐
        findViewById<View>(R.id.main).setOnClickListener {
            StateNavRemoveActivity.hideSystemUI(window)
        }

        // 버튼을 ID로 찾기
        val startButton: ImageButton = findViewById(R.id.nav_button5)

        // 버튼에 클릭 리스너 설정
        startButton.setOnClickListener {
            // 페이지 전환하기 위한 Intent 생성
            val intent = Intent(this, ProfileMainActivity::class.java)
            // MainActivity 시작
            startActivity(intent)
            // AgreementActivity 종료 (뒤로 가기 버튼으로 돌아오지 않도록)
            finish()
        }

        // nav_button1 클릭 시 ScoreMain으로 이동
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


        dbHelper = DatabaseHelper(this)
        goalLayout = findViewById(R.id.goal_layout)
        planLayout = findViewById(R.id.plan_layout)

        loadGoals()
        loadPlans()

    }

    private fun loadGoals() {
        val cursor = dbHelper.getAllGoals()
        goalLayout.removeAllViews()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE))
                val isChecked = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IS_CHECKED)) == 1
                val goalView = createGoalOrPlanView(id, title, isChecked, true)
                goalLayout.addView(goalView)
            } while (cursor.moveToNext())
        }
        cursor.close()
    }

    private fun loadPlans() {
        val cursor = dbHelper.getAllPlans()
        planLayout.removeAllViews()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID))
                val subject = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUBJECT))
                val detail = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DETAIL)) // 추가된 부분
                val isChecked = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IS_CHECKED)) == 1
                val planView = createGoalOrPlanView(id, "$subject-$detail", isChecked, false) // 수정된 부분
                planLayout.addView(planView)
            } while (cursor.moveToNext())
        }
        cursor.close()
    }

    private fun createGoalOrPlanView(id: Int, text: String, isChecked: Boolean, isGoal: Boolean): LinearLayout {
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.HORIZONTAL

        val checkBox = CheckBox(this)
        checkBox.text = text
        checkBox.isChecked = isChecked
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isGoal) {
                dbHelper.setGoalChecked(id, isChecked) // 체크 상태를 데이터베이스에 저장
            } else {
                dbHelper.setPlanChecked(id, isChecked) // 체크 상태를 데이터베이스에 저장
            }
        }
        val checkBoxParams =
            LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        checkBox.layoutParams = checkBoxParams

        val startButton = Button(this)
        startButton.text="시작"
        startButton.setOnClickListener {
            val intent = Intent(this@StudyBridgeMainHome, StudyTime::class.java)
            startActivity(intent)
        }


        layout.addView(checkBox) // LinearLayout에 CheckBox 추가
        layout.addView(startButton)

        return layout // LinearLayout 반환
    }
}