package com.example.study_bridge_capstone

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.TimeUnit
import kotlin.math.max

class StudyTime : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper()) // 메인 스레드에서 실행할 핸들러
    private lateinit var prefs: SharedPreferences // 설정값을 저장할 SharedPreferences
    private var isResting = false // 현재 휴식 중인지 여부
    private var studyTime = 0L // 공부한 시간
    private var restTime = 0L // 휴식한 시간

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study_time)

        val time1 = findViewById<TextView>(R.id.time1)
        val time2 = findViewById<TextView>(R.id.time2)
        val restButton = findViewById<Button>(R.id.restButton)

        // SharedPreferences 초기화
        prefs = getSharedPreferences("StudyTimePrefs", Context.MODE_PRIVATE)
        prefs.edit().clear().apply() // SharedPreferences 값 초기화

        studyTime = 0L
        restTime = 0L

        val goalTimeMillis = TimeUnit.MINUTES.toMillis(60) // 목표 시간을 밀리초로 변환
        time1.text = formatTime(goalTimeMillis) // 목표 시간을 텍스트뷰에 표시

        // 휴식 버튼 클릭 이벤트
        restButton.setOnClickListener {
            isResting = !isResting // 휴식 상태 토글
            if (isResting) {
                restButton.text = "휴식 종료"
                studyTime += System.currentTimeMillis() - prefs.getLong("startTime", 0) // 공부 시간 업데이트
                prefs.edit().putLong("startTime", System.currentTimeMillis()).apply() // 시작 시간 업데이트
            } else {
                restButton.text = "휴식 시작"
                restTime += System.currentTimeMillis() - prefs.getLong("startTime", 0) // 휴식 시간 업데이트
                prefs.edit().putLong("startTime", System.currentTimeMillis()).apply() // 시작 시간 업데이트
            }
        }
    }

    // 앱이 활성화될 때
    override fun onResume() {
        super.onResume()
        val pauseTime = prefs.getLong("pauseTime", 0) // 일시정지 시간 가져오기
        val backgroundTime = prefs.getLong("backgroundTime", 0) // 백그라운드 시간 가져오기
        if (pauseTime != 0L) {
            val now = System.currentTimeMillis()
            if (isResting) {
                restTime += now - pauseTime - backgroundTime // 휴식 시간 업데이트
            } else {
                studyTime += now - pauseTime - backgroundTime // 공부 시간 업데이트
            }
            prefs.edit().remove("pauseTime").apply() // 일시정지 시간 삭제
            prefs.edit().remove("backgroundTime").apply() // 백그라운드 시간 삭제
        }
        prefs.edit().putLong("startTime", System.currentTimeMillis()).apply() // 시작 시간 업데이트
        handler.post(timerRunnable) // 타이머 시작
    }

    // 앱이 비활성화될 때
    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null) // 모든 콜백과 메시지 삭제
        val now = System.currentTimeMillis()
        if (isResting) {
            restTime += now - prefs.getLong("startTime", 0) // 휴식 시간 업데이트
        } else {
            studyTime += now - prefs.getLong("startTime", 0) // 공부 시간 업데이트
        }
        prefs.edit().putLong("studyTime", studyTime).apply() // 공부 시간 저장
        prefs.edit().putLong("restTime", restTime).apply() // 휴식 시간 저장
        prefs.edit().putLong("pauseTime", now).apply() // 일시정지 시간 저장
        prefs.edit().putLong("backgroundTime", now - prefs.getLong("startTime", 0)).apply() // 백그라운드 시간 저장
    }

    // 타이머 Runnable
    private val timerRunnable = object : Runnable {
        override fun run() {
            val startTime = prefs.getLong("startTime", 0) // 시작 시간 가져오기
            val goalTimeMillis = TimeUnit.MINUTES.toMillis(60) // 목표 시간을 밀리초로 변환
            val now = System.currentTimeMillis()

            if (isResting) {
                val elapsedTime = restTime + (now - startTime) // 경과 시간 계산
                findViewById<TextView>(R.id.time2).text = formatTime(elapsedTime) // 경과 시간 표시
            } else {
                val elapsedTime = studyTime + (now - startTime) // 경과 시간 계산
                val remainingTime = max(0, goalTimeMillis - elapsedTime) // 남은 시간 계산
                findViewById<TextView>(R.id.time1).text = formatTime(remainingTime) // 남은 시간 표시
            }

            handler.postDelayed(this, 1000) // 1초 후에 다시 실행
        }
    }

    // 시간 포맷 함수
    private fun formatTime(millis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(millis) // 시간으로 변환
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60 // 분으로 변환
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60 // 초로 변환
        return String.format("%02d:%02d:%02d", hours, minutes, seconds) // 포맷에 맞게 변환
    }
}