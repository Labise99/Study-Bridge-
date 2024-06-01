package com.example.study_bridge_capstone

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class StudyTime : AppCompatActivity() {
    //앱 생명주기 감지를 위한 activityLifecycleCallbacks 호출
    private val activityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityResumed(activity: Activity) {
            //DB에 딴짓 종료 이벤트로 저장
        }

        override fun onActivityPaused(activity: Activity) {
            //DB에 딴짓 시작 이벤트로 저장
        }

        // 컴파일 에러를 피하기 위한 선언. 건들지 말 것.
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
        override fun onActivityStarted(activity: Activity) {}
        override fun onActivityStopped(activity: Activity) {}
        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
        override fun onActivityDestroyed(activity: Activity) {}
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //백그라운드 상태 체크를 위한 선언
        application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
        enableEdgeToEdge()
        setContentView(R.layout.activity_study_time)

        //상단의 currentGoal에 현재 공부 내용 표기
        val currentGoal = findViewById<TextView>(R.id.currentGoal)
        currentGoal.text = "tmpgoal" //tmpgoal 자리에 전달받은 공부 내용을 입력
        val totalTime = findViewById<ProgressBar>(R.id.totalTime)
        totalTime.max = 100 //

    }

    override fun onDestroy() {
        super.onDestroy()
        application.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }


}