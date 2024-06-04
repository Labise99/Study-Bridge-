package com.example.studybridge

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AgreementActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.agreement)  // agreement.xml 레이아웃을 설정합니다.
        StateNavRemoveActivity.hideSystemUI(window)

        // 버튼을 ID로 찾기
        val startButton: Button = findViewById(R.id.agree_start_button)

        // 버튼에 클릭 리스너 설정
        startButton.setOnClickListener {
            // MainActivity로 전환하기 위한 Intent 생성
            val intent = Intent(this, StudyBridgeMainHome::class.java)
            // MainActivity 시작
            startActivity(intent)
            // AgreementActivity 종료 (뒤로 가기 버튼으로 돌아오지 않도록)
            finish()
        }

    }
}
