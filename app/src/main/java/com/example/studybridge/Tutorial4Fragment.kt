package com.example.studybridge

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.Fragment

class Tutorial4Fragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Enable edge to edge here
        activity?.enableEdgeToEdge()

        return inflater.inflate(R.layout.tutorial_4, container, false)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 상태바와 네비게이션 바 숨기기
        activity?.window?.let { StateNavRemoveActivity.hideSystemUI(it) }
        // imageView2 클릭 리스너 설정
        view.findViewById<ImageView>(R.id.imageView2).setOnClickListener {
            // AgreementActivity로 이동
            val intent = Intent(activity, AgreementActivity::class.java)
            startActivity(intent)
        }
    }
}
