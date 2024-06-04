package com.example.studybridge

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class TutorialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        // Tutorial1Fragment를 추가합니다.
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, Tutorial1Fragment())
            .commit()
    }
}
