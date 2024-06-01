package com.example.study_bridge_capstone

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DBNAME, null, DBVERSION) { //SQLiteOpenHelper를 상속받음

    companion object { //복수 접근을 방지하기 위한 싱글턴 패턴. DB의 모든 접근은 DBHelper를 통해서 이루어진다.
        const val DBNAME = "study_bridge.db" //db의 이름
        const val DBVERSION = 0 //db 버전. 예시를 따라가기 위한 변수

        //Double Checked Locking
        @Volatile
        private var instance: DBHelper? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(DBHelper::class.java) {
                instance ?: DBHelper(context).also {
                    instance = it
                }
            }
    }

    override fun onCreate(db: SQLiteDatabase?) { //테이블 생성
        val createQuery :String = "create table test (" +
                "id integer primary key autoincrement, " +
                "txt text); "
        db?.execSQL(createQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) { //테이블 업데이트
        if(oldVersion != newVersion) { //테이블이 옛날 버전일 때
            db?.execSQL("drop table if exists timeline") //기존 테이블 드랍 후
            onCreate(db) //새로 테이블 생성. 이때 기존 데이터는 모두 날아간다.
        }
    }

    fun createData(db: SQLiteDatabase?, tableName: String, member: String) { //데이터 삽입 함수
        val createQuery: String = "insert into $tableName" + //어느 테이블에
                "values ( $member );" //어떤 내용을 삽입할 것인가
        db?.execSQL(createQuery)
    }

    fun readData(db: SQLiteDatabase?, tableName: String, target: String, searchQuery: String) { //데이터 읽기
        val createQuery = "select $target from $tableName " + //어느 테이블의 어떤 애트리뷰트를
                "where $searchQuery );" //어떤 조건으로 검색할 것인가
        db?.execSQL(createQuery)
    }

    fun updateData(db: SQLiteDatabase?, tableName: String, editQuery: String, searchQuery: String) { //데이터 수정
        val createQuery = "update $tableName " + //어떤 테이블의 내용을
                "set $editQuery " + //어떻게 수정할 것인가
                "where $searchQuery );" //어떤 조건에 해당하는 튜플을 수정하라 것인가
        db?.execSQL(createQuery)
    }

    fun deleteData(db: SQLiteDatabase?, tableName: String, searchQuery: String) { //데이터 삭제
        val createQuery = "delete from $tableName " + //어느 테이블의
                "where $searchQuery );" //어떤 조건에 해당하는 튜플을 삭제할 것인가
        db?.execSQL(createQuery)
    }

}