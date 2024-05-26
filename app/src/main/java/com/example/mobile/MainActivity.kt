package com.example.mobile

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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
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

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
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
            startActivity(intent)
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
        val goals = sharedPreferences.getStringSet("goals", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        goalLayout.removeAllViews()
        for (goal in goals) {
            val goalView = createGoalOrPlanView(goal, true)
            goalLayout.addView(goalView)
        }
    }

    private fun loadPlans() {
        val plans = sharedPreferences.getStringSet("plans", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        planLayout.removeAllViews()
        for (plan in plans) {
            val planView = createGoalOrPlanView(plan, false)
            planLayout.addView(planView)
        }
    }

    private fun createGoalOrPlanView(text: String, isGoal: Boolean): LinearLayout {
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.HORIZONTAL

        val checkBox = CheckBox(this)
        checkBox.text = text
        checkBox.isChecked = isChecked(text)
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            setChecked(text, isChecked)
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
                    putExtra("plan", text)
                }
            }
            startActivity(intent)
        }

        val deleteButton = Button(this)
        deleteButton.text = "삭제"
        deleteButton.setOnClickListener {
            if (isGoal) {
                removeGoal(text)
            } else {
                removePlan(text)
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

    private fun setChecked(text: String, isChecked: Boolean) {
        val checkedItems = sharedPreferences.getStringSet("checkedItems", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        if (isChecked) {
            checkedItems.add(text)
        } else {
            checkedItems.remove(text)
        }
        with(sharedPreferences.edit()) {
            putStringSet("checkedItems", checkedItems)
            apply()
        }
    }

    private fun isChecked(text: String): Boolean {
        val checkedItems = sharedPreferences.getStringSet("checkedItems", mutableSetOf()) ?: mutableSetOf()
        return checkedItems.contains(text)
    }

    private fun removeGoal(goal: String) {
        val goals = sharedPreferences.getStringSet("goals", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        goals.remove(goal)
        with(sharedPreferences.edit()) {
            putStringSet("goals", goals)
            apply()
        }
    }

    private fun removePlan(plan: String) {
        val plans = sharedPreferences.getStringSet("plans", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        plans.remove(plan)
        with(sharedPreferences.edit()) {
            putStringSet("plans", plans)
            apply()
        }
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
}