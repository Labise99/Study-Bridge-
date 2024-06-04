package com.example.studybridge

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var goalLayout: LinearLayout
    private lateinit var planLayout: LinearLayout

    // Calendar 관련 변수 추가
    private lateinit var yearSpinner: Spinner
    private lateinit var monthSpinner: Spinner
    private lateinit var daysContainer: LinearLayout
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button

    private val calendar: Calendar = Calendar.getInstance()
    private val koreanWeekDays = arrayOf("일", "월", "화", "수", "목", "금", "토")
    private var startDayOffset = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

        // nav_button1 클릭 시 homeMain으로 이동
        val homeButton: ImageButton = findViewById(R.id.nav_button3)
        homeButton.setOnClickListener {
            val intent = Intent(this, StudyBridgeMainHome::class.java)
            startActivity(intent)
        }

        dbHelper = DatabaseHelper(this)
        goalLayout = findViewById(R.id.goal_layout)
        planLayout = findViewById(R.id.plan_layout)

        val goalButton: ImageButton = findViewById(R.id.goal_button)
        val planButton: ImageButton = findViewById(R.id.plan_button)
        val planpageBtn: ImageButton = findViewById(R.id.nav_button4)

        goalButton.setOnClickListener {
            val intent = Intent(this, GoalSettingActivity::class.java)
            startActivity(intent)
        }

        planButton.setOnClickListener {
            val intent = Intent(this, PlanSettingActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_PLAN_SETTING)
        }

        planpageBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        loadGoals()
        loadPlans()

        // Calendar 관련 코드 추가
        yearSpinner = findViewById(R.id.yearSpinner)
        monthSpinner = findViewById(R.id.monthSpinner)
        daysContainer = findViewById(R.id.daysContainer)
        prevButton = findViewById(R.id.prevButton)
        nextButton = findViewById(R.id.nextButton)

        val years = (2020..2030).toList().map { it.toString() }
        val yearAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
        yearSpinner.adapter = yearAdapter
        yearSpinner.setSelection(years.indexOf(calendar.get(Calendar.YEAR).toString()))

        val months = resources.getStringArray(R.array.months)
        val monthAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, months)
        monthSpinner.adapter = monthAdapter
        monthSpinner.setSelection(calendar.get(Calendar.MONTH))

        calendar.set(Calendar.DAY_OF_MONTH, 1)
        updateDays()

        prevButton.setOnClickListener {
            startDayOffset -= 5
            updateDays()
        }

        nextButton.setOnClickListener {
            startDayOffset += 5
            updateDays()
        }

        monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                calendar.set(Calendar.MONTH, position)
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                startDayOffset = 0
                updateDays()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                calendar.set(Calendar.YEAR, years[position].toInt())
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                startDayOffset = 0
                updateDays()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
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
        val checkBoxParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        checkBox.layoutParams = checkBoxParams

        val editButton = Button(this)
        editButton.text = "수정"
        editButton.setOnClickListener {
            val intent = if (isGoal) {
                Intent(this, GoalSettingActivity::class.java).apply {
                    putExtra("isEditMode", true)
                    putExtra("goalTitle", text)
                }
            } else {
                Intent(this, PlanSettingActivity::class.java).apply {
                    putExtra("isEditMode", true)
                    putExtra("planSubject", text.split("-")[0]) // 수정된 부분
                    putExtra("planDetail", text.split("-")[1])  // 수정된 부분
                }
            }
            startActivityForResult(intent, REQUEST_CODE_PLAN_SETTING)
        }

        val deleteButton = Button(this)
        deleteButton.text = "삭제"
        deleteButton.setOnClickListener {
            if (isGoal) {
                removeGoal(id)
            } else {
                removePlan(id)
            }
            loadGoals()
            loadPlans()
        }

        val buttonLayout = LinearLayout(this)
        buttonLayout.orientation = LinearLayout.HORIZONTAL
        buttonLayout.addView(editButton)
        buttonLayout.addView(deleteButton)

        layout.addView(checkBox)
        layout.addView(buttonLayout)

        return layout
    }

    private fun removeGoal(id: Int) {
        dbHelper.deleteGoal(id)
    }

    private fun removePlan(id: Int) {
        dbHelper.deletePlan(id)
    }

    private fun updateDays() {
        daysContainer.removeAllViews()
        val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val startDay = 1 + startDayOffset
        val daysToShow = mutableListOf<Int>()

        for (i in 0 until 5) {
            val day = startDay + i
            if (day in 1..maxDay) {
                daysToShow.add(day)
            }
        }

        for (day in daysToShow) {
            val dayButton = Button(this)
            dayButton.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
            dayButton.text = "$day ${koreanWeekDays[dayOfWeek]}"
            dayButton.setOnClickListener {
                Toast.makeText(this, "Clicked on day $day", Toast.LENGTH_SHORT).show()
            }
            daysContainer.addView(dayButton)
        }

        prevButton.isEnabled = startDayOffset > 0
        nextButton.isEnabled = startDay + 5 <= maxDay
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PLAN_SETTING && resultCode == RESULT_OK) {
            val plan = data?.getStringExtra("plan")
            plan?.let {
                val planTextView = TextView(this)
                planTextView.text = it
                planLayout.addView(planTextView)
            }
            loadPlans() // 수정된 계획을 다시 로드하도록 추가
        }
    }

    companion object {
        const val REQUEST_CODE_PLAN_SETTING = 1
    }
}