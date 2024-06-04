package com.example.studybridge

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "studyPlanner.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_GOALS = "goals"
        const val TABLE_PLANS = "plans"
        const val COLUMN_ID = "_id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DETAIL = "detail"
        const val COLUMN_GOAL_TIME = "goal_time"
        const val COLUMN_START_TIME = "start_time"
        const val COLUMN_IS_CHECKED = "is_checked"
        const val COLUMN_SUBJECT = "subject"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_GOALS_TABLE = ("CREATE TABLE $TABLE_GOALS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_TITLE TEXT,"
                + "$COLUMN_GOAL_TIME TEXT,"
                + "$COLUMN_START_TIME TEXT,"
                + "$COLUMN_IS_CHECKED INTEGER" + ")")

        val CREATE_PLANS_TABLE = ("CREATE TABLE $TABLE_PLANS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_SUBJECT TEXT,"
                + "$COLUMN_DETAIL TEXT,"
                + "$COLUMN_GOAL_TIME TEXT,"
                + "$COLUMN_IS_CHECKED INTEGER" + ")")

        db?.execSQL(CREATE_GOALS_TABLE)
        db?.execSQL(CREATE_PLANS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_GOALS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PLANS")
        onCreate(db)
    }

    fun addGoal(title: String, goalTime: String, startTime: String, isChecked: Boolean) {
        val values = ContentValues()
        values.put(COLUMN_TITLE, title)
        values.put(COLUMN_GOAL_TIME, goalTime)
        values.put(COLUMN_START_TIME, startTime)
        values.put(COLUMN_IS_CHECKED, if (isChecked) 1 else 0)

        val db = this.writableDatabase
        db.insert(TABLE_GOALS, null, values)
        db.close()
    }

    fun addPlan(subject: String, detail: String, goalTime: String, isChecked: Boolean) {
        val values = ContentValues()
        values.put(COLUMN_SUBJECT, subject)
        values.put(COLUMN_DETAIL, detail)
        values.put(COLUMN_GOAL_TIME, goalTime)
        values.put(COLUMN_IS_CHECKED, if (isChecked) 1 else 0)

        val db = this.writableDatabase
        db.insert(TABLE_PLANS, null, values)
        db.close()
    }

    fun getAllGoals(): Cursor {
        val db = this.readableDatabase
        return db.query(TABLE_GOALS, null, null, null, null, null, null)
    }

    fun getAllPlans(): Cursor {
        val db = this.readableDatabase
        return db.query(TABLE_PLANS, null, null, null, null, null, null)
    }

    fun updateGoal(id: Int, title: String, goalTime: String, startTime: String, isChecked: Boolean): Int {
        val values = ContentValues()
        values.put(COLUMN_TITLE, title)
        values.put(COLUMN_GOAL_TIME, goalTime)
        values.put(COLUMN_START_TIME, startTime)
        values.put(COLUMN_IS_CHECKED, if (isChecked) 1 else 0)

        val db = this.writableDatabase
        return db.update(TABLE_GOALS, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun updatePlan(id: Int, subject: String, detail: String, goalTime: String, isChecked: Boolean): Int {
        val values = ContentValues()
        values.put(COLUMN_SUBJECT, subject)
        values.put(COLUMN_DETAIL, detail)
        values.put(COLUMN_GOAL_TIME, goalTime)
        values.put(COLUMN_IS_CHECKED, if (isChecked) 1 else 0)

        val db = this.writableDatabase
        return db.update(TABLE_PLANS, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun deleteGoal(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_GOALS, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    fun deletePlan(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_PLANS, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    fun setGoalChecked(id: Int, isChecked: Boolean) {
        val values = ContentValues()
        values.put(COLUMN_IS_CHECKED, if (isChecked) 1 else 0)

        val db = this.writableDatabase
        db.update(TABLE_GOALS, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    fun setPlanChecked(id: Int, isChecked: Boolean) {
        val values = ContentValues()
        values.put(COLUMN_IS_CHECKED, if (isChecked) 1 else 0)

        val db = this.writableDatabase
        db.update(TABLE_PLANS, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }
}