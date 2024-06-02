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
import java.util.concurrent.TimeUnit

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
    val currentGoal = findViewById<TextView>(R.id.currentGoal)
    val totalTime = findViewById<ProgressBar>(R.id.totalTime)
    val time1 = findViewById<TextView>(R.id.time1)
    val time2 = findViewById<TextView>(R.id.time2)
    val restButton: Button = findViewById(R.id.restButton)
    private var startTime = 0L
    private var pauseOffset = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //백그라운드 상태 체크를 위한 선언
        application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
        enableEdgeToEdge()
        setContentView(R.layout.activity_study_time)

        //상단의 currentGoal에 현재 공부 내용 표기
        currentGoal.text = "tmpgoal" //tmpgoal 자리에 전달받은 공부 내용을 입력
        //학습시간 진행도 초기화
        totalTime.max = 100 //100 자리에 설정한 목표공부시간 추가
        totalTime.progress = 0
        time1.text = "00:00:00"
        time2.text = "00:00:00"
        startTime = System.currentTimeMillis()

        restButton.setOnClickListener {
            if(restButton.text == "쉬는 시간")
            {
                //공부 시간 정지 코드
                //휴식 시간 시작 코드
                //휴식 시작 타임라인DB에 추가
            }
            else
            {
                //휴식 시간 정지 코드
                //공부 시간 시작 코드
                //휴식 종료 타임라인DB에 추가
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        application.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }
}