package com.example.mobile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var goalLayout: LinearLayout
    private lateinit var planLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        goalLayout = findViewById(R.id.goal_layout)
        planLayout = findViewById(R.id.plan_layout)

        val goalButton: ImageButton = findViewById(R.id.goal_button)
        val planButton: ImageButton = findViewById(R.id.plan_button)
        val planpageBtn: ImageButton = findViewById(R.id.nav_button4)

        // goal_button 클릭 시 GoalSettingActivity로 이동
        goalButton.setOnClickListener {
            val intent = Intent(this, GoalSettingActivity::class.java)
            startActivity(intent)
        }

        // plan_button 클릭 시 PlanSettingActivity로 이동
        planButton.setOnClickListener {
            val intent = Intent(this, PlanSettingActivity::class.java)
            startActivity(intent)
        }

        planpageBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // 저장된 목표와 학습 계획을 로드
        loadGoals()
        loadPlans()
    }

    private fun loadGoals() {
        val goals = sharedPreferences.getStringSet("goals", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        goalLayout.removeAllViews()
        for (goal in goals) {
            addGoalCheckBox(goal)
        }
    }

    private fun loadPlans() {
        val plans = sharedPreferences.getStringSet("plans", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        planLayout.removeAllViews()
        for (plan in plans) {
            addPlanCheckBox(plan)
        }
    }

    private fun addGoalCheckBox(goal: String) {
        val checkBox = CheckBox(this)
        checkBox.text = goal
        checkBox.id = View.generateViewId()

        // Load the checked state
        val isChecked = sharedPreferences.getBoolean("goal_$goal", false)
        checkBox.isChecked = isChecked

        // Save the checked state when changed
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            with(sharedPreferences.edit()) {
                putBoolean("goal_$goal", isChecked)
                apply()
            }
        }

        val editButton = Button(this).apply {
            text = "Edit"
            id = View.generateViewId()
            setOnClickListener {
                editGoal(goal)
            }
        }

        val deleteButton = Button(this).apply {
            text = "Delete"
            id = View.generateViewId()
            setOnClickListener {
                removeGoal(goal)
                loadGoals()
            }
        }

        val itemLayout = RelativeLayout(this)
        val checkBoxParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            addRule(RelativeLayout.ALIGN_PARENT_START)
            addRule(RelativeLayout.CENTER_VERTICAL)
        }

        val editButtonParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            addRule(RelativeLayout.ALIGN_PARENT_END)
            addRule(RelativeLayout.CENTER_VERTICAL)
        }

        val deleteButtonParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            addRule(RelativeLayout.LEFT_OF, editButton.id)
            addRule(RelativeLayout.CENTER_VERTICAL)
            marginEnd = 8 // 버튼 간의 간격
        }

        itemLayout.addView(checkBox, checkBoxParams)
        itemLayout.addView(editButton, editButtonParams)
        itemLayout.addView(deleteButton, deleteButtonParams)
        goalLayout.addView(itemLayout)
    }

    private fun addPlanCheckBox(plan: String) {
        val checkBox = CheckBox(this)
        checkBox.text = plan
        checkBox.id = View.generateViewId()

        // Load the checked state
        val isChecked = sharedPreferences.getBoolean("plan_$plan", false)
        checkBox.isChecked = isChecked

        // Save the checked state when changed
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            with(sharedPreferences.edit()) {
                putBoolean("plan_$plan", isChecked)
                apply()
            }
        }

        val editButton = Button(this).apply {
            text = "수정"
            id = View.generateViewId()
            setOnClickListener {
                editPlan(plan)
            }
        }

        val deleteButton = Button(this).apply {
            text = "삭제"
            id = View.generateViewId()
            setOnClickListener {
                removePlan(plan)
                loadPlans()
            }
        }

        val itemLayout = RelativeLayout(this)
        val checkBoxParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            addRule(RelativeLayout.ALIGN_PARENT_START)
            addRule(RelativeLayout.CENTER_VERTICAL)
        }

        val editButtonParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            addRule(RelativeLayout.ALIGN_PARENT_END)
            addRule(RelativeLayout.CENTER_VERTICAL)
        }

        val deleteButtonParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            addRule(RelativeLayout.LEFT_OF, editButton.id)
            addRule(RelativeLayout.CENTER_VERTICAL)
            marginEnd = 8 // 버튼 간의 간격
        }

        itemLayout.addView(checkBox, checkBoxParams)
        itemLayout.addView(editButton, editButtonParams)
        itemLayout.addView(deleteButton, deleteButtonParams)
        planLayout.addView(itemLayout)
    }

    private fun editGoal(goal: String) {
        val intent = Intent(this, GoalSettingActivity::class.java)
        intent.putExtra("isEditMode", true)
        intent.putExtra("goalTitle", goal)
        startActivity(intent)
    }

    private fun editPlan(plan: String) {
        val intent = Intent(this, PlanSettingActivity::class.java)
        intent.putExtra("isEditMode", true)
        intent.putExtra("planTitle", plan)
        startActivity(intent)
    }

    private fun removeGoal(goal: String) {
        val goals = sharedPreferences.getStringSet("goals", setOf())?.toMutableSet() ?: mutableSetOf()
        goals.remove(goal)

        with(sharedPreferences.edit()) {
            putStringSet("goals", goals)
            remove("goal_$goal") // Remove the saved state
            apply()
        }
    }

    private fun removePlan(plan: String) {
        val plans = sharedPreferences.getStringSet("plans", setOf())?.toMutableSet() ?: mutableSetOf()
        plans.remove(plan)

        with(sharedPreferences.edit()) {
            putStringSet("plans", plans)
            remove("plan_$plan") // Remove the saved state
            apply()
        }
    }

    override fun onResume() {
        super.onResume()
        loadGoals()
        loadPlans()
    }
}
