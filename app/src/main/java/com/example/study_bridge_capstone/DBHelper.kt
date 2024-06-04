package com.example.study_bridge_capstone

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// DBHelper 클래스: SQLite 데이터베이스를 관리하는 클래스
class DBHelper(context: Context) : SQLiteOpenHelper(context, "database.db", null, 1) {

    // 데이터베이스가 처음 생성될 때 호출되는 메소드
    override fun onCreate(db: SQLiteDatabase) {
        val createProfileTableSql = """
        CREATE TABLE profile (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nickname TEXT,
            goal TEXT,
            identity TEXT
        )
    """.trimIndent()

        val createGradeDataTableSql = """
        CREATE TABLE grade_data (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            semester TEXT,
            subject TEXT,
            total_credit REAL,
            liberal_credit REAL,
            major_credit REAL,
            grade REAL
        )
    """.trimIndent()

        val createExamDataTableSql = """
        CREATE TABLE exam_data (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            date TEXT,
            subject TEXT,
            grade INTEGER,
            raw INTEGER
        )
    """.trimIndent()

        val createStudyPlanTableSql = """
        CREATE TABLE study_plan (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT,
            time TEXT
        )
    """.trimIndent()

        val createTodayStudyTableSql = """
        CREATE TABLE today_study (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            date TEXT,
            pid INTEGER,
            is_checked INTEGER,
            study_time TEXT,
            rest_time TEXT,
            waste_time TEXT,
            FOREIGN KEY(pid) REFERENCES study_plan(id)
        )
    """.trimIndent()

        val createGoalTableSql = """
        CREATE TABLE goal (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT,
            time TEXT
        )
    """.trimIndent()

        val createTodayGoalTableSql = """
        CREATE TABLE today_goal (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            date TEXT,
            pid INTEGER,
            is_checked INTEGER,
            work_time TEXT,
            rest_time TEXT,
            waste_time TEXT,
            FOREIGN KEY(pid) REFERENCES goal(id)
        )
    """.trimIndent()

        db.execSQL(createProfileTableSql)
        db.execSQL(createGradeDataTableSql)
        db.execSQL(createExamDataTableSql)
        db.execSQL(createStudyPlanTableSql)
        db.execSQL(createTodayStudyTableSql)
        db.execSQL(createGoalTableSql)
        db.execSQL(createTodayGoalTableSql)
    }

    // 데이터베이스가 업그레이드되어야 할 때 호출되는 메소드
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS profile")
        db.execSQL("DROP TABLE IF EXISTS timeline")
        db.execSQL("DROP TABLE IF EXISTS grade_data")
        db.execSQL("DROP TABLE IF EXISTS exam_data")
        db.execSQL("DROP TABLE IF EXISTS study_plan")
        db.execSQL("DROP TABLE IF EXISTS calendar")
        onCreate(db)
    }

    // 데이터 삽입을 위한 메소드
    // tableName: 데이터를 삽입할 테이블의 이름
    // data: 삽입할 데이터. 키는 컬럼 이름, 값은 해당 컬럼에 삽입할 값
    fun insert(tableName: String, data: Map<String, Any>) {
        val values = ContentValues().apply {
            for ((key, value) in data) {
                when (value) {
                    is String -> put(key, value)
                    is Int -> put(key, value)
                    is Long -> put(key, value)
                    is Float -> put(key, value)
                    is Boolean -> put(key, value)
                    else -> throw IllegalArgumentException("Unsupported data type")
                }
            }
        }
        writableDatabase.insert(tableName, null, values)
    }

    // 데이터 조회를 위한 메소드
    // tableName: 데이터를 조회할 테이블의 이름
    // selection: 조회할 데이터의 조건. SQL의 WHERE 절에 해당
    // selectionArgs: selection에 ?가 포함된 경우, ?를 대체할 값들
    fun read(tableName: String, selection: String?, selectionArgs: Array<String>?): Cursor {
        return readableDatabase.query(tableName, null, selection, selectionArgs, null, null, null)
    }

    // 데이터 업데이트를 위한 메소드
    // tableName: 데이터를 업데이트할 테이블의 이름
    // data: 업데이트할 데이터. 키는 컬럼 이름, 값은 해당 컬럼에 업데이트할 값
    // whereClause: 업데이트할 데이터의 조건. SQL의 WHERE 절에 해당
    // whereArgs: whereClause에 ?가 포함된 경우, ?를 대체할 값들
    fun update(tableName: String, data: Map<String, Any>, whereClause: String, whereArgs: Array<String>) {
        val values = ContentValues().apply {
            for ((key, value) in data) {
                when (value) {
                    is String -> put(key, value)
                    is Int -> put(key, value)
                    is Long -> put(key, value)
                    is Float -> put(key, value)
                    is Boolean -> put(key, value)
                    else -> throw IllegalArgumentException("Unsupported data type")
                }
            }
        }
        writableDatabase.update(tableName, values, whereClause, whereArgs)
    }

    // 데이터 삭제를 위한 메소드
    // tableName: 데이터를 삭제할 테이블의 이름
    // whereClause: 삭제할 데이터의 조건. SQL의 WHERE 절에 해당
    // whereArgs: whereClause에 ?가 포함된 경우, ?를 대체할 값들
    fun delete(tableName: String, whereClause: String, whereArgs: Array<String>) {
        writableDatabase.delete(tableName, whereClause, whereArgs)
    }
}