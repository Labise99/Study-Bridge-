package com.example.studybridge
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.activity.enableEdgeToEdge
import android.widget.ImageView


class Tutorial1Fragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Enable edge to edge here
        activity?.enableEdgeToEdge()

        return inflater.inflate(R.layout.tutorial_1, container, false)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 상태바와 네비게이션 바 숨기기
        activity?.window?.let { StateNavRemoveActivity.hideSystemUI(it) }
        // imageView2 클릭 리스너 설정
        view.findViewById<ImageView>(R.id.imageView2).setOnClickListener {
            // Tutorial2Fragment로 전환
            val fragment = Tutorial2Fragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null) // 뒤로가기 스택에 추가하여 뒤로 가기 버튼으로 전환을 취소할 수 있게 함
                .commit()
        }
    }
}
