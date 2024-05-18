package com.example.study_bridge_capstone

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// 24.05.18 일단 긁어오기만 한 코드. 앱에 맞게 수정해야 하니 사용하지 말것.
class DBHelper(context: Context?,name:String?,factory:SQLiteDatabase.CursorFactory?,version: Int)
    : SQLiteOpenHelper(context,name,factory,version) {

    class DBHelper(context: Context?,name:String?,factory:SQLiteDatabase.CursorFactory?,version: Int)
        : SQLiteOpenHelper(context,name,factory,version) {

        //매개변수는 DB instance
        override fun onCreate(db: SQLiteDatabase?) {
            //쿼리문. 존재하지 않는다면 테이블을 생성하라는 말
            //시퀀스
            //컬럼명
            var sql : String = " CREATE TABLE IF NOT EXISTS MYTABLE( " +
                    "    SEQ INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "    TXT TEXT)  "
            db?.execSQL(sql)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            var sql : String = " DROP TABLE IF EXISTS MYTABLE "
            db?.execSQL(sql)
            onCreate(db)
        }

        fun insert(db: SQLiteDatabase,txt:String){
            var sql = " INSERT INTO MYTABLE(TXT) " +
                    " VALUES('${txt}')"

            db.execSQL(sql)

        }

        fun select(db: SQLiteDatabase, txt:String) : String?{
            var sql = " SELECT * FROM MYTABLE " +
                    "   WHERE TXT='${txt}' "
            var result = db.rawQuery(sql, null)

            var str:String? = ""
            while (result.moveToNext()){
                str += "번호 :" +result.getString(result.getColumnIndex("SEQ")) + "\n" +
                        "데이터 :" + result.getString(result.getColumnIndex("TXT"))
            }

            return str
        }

        fun delete(db:SQLiteDatabase,txt:String){
            val sql = " DELETE FROM MYTABLE " +
                    " WHERE TXT='${txt}' "

            db.execSQL(sql)
        }


    }
}