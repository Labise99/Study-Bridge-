package com.example.studybridge

import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate

class ScoreGradeCheck : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        StateNavRemoveActivity.hideSystemUI(window)
        setContentView(R.layout.score_grade_check)

        val graphContainer = findViewById<LinearLayout>(R.id.grade_graph_container)
        val scoreDetails = findViewById<TextView>(R.id.grade_score_details)

        // 인텐트로부터 기간 정보 가져오기
        val period = intent.getStringExtra("PERIOD")

        // 그래프와 세부 정보를 위한 모의 데이터, 실제 데이터로 교체 필요
        val grades = mapOf("수강 학점" to 18, "전공 학점" to 12, "교양 학점" to 6, "성적 정보" to 3.5)
        val scores = mapOf("수강 학점" to 18, "전공 학점" to 12, "교양 학점" to 6, "성적 정보" to 3.5)

        // 바 차트 설정
        val barChart = BarChart(this)
        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()
        var i = 0

        // 점수를 바 차트에 추가
        for ((subject, score) in scores) {
            entries.add(BarEntry(i.toFloat(), score.toFloat()))
            labels.add(subject)
            i++
        }

        // 데이터셋 설정
        val dataSet = BarDataSet(entries, "Grades")
        dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        val barData = BarData(dataSet)
        barData.barWidth = 0.9f // 바의 너비를 설정
        barChart.data = barData
        barChart.setFitBars(true) // 바가 차트 내부에 적절히 맞추어지도록 설정
        barChart.description.isEnabled = false
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.xAxis.granularity = 1f
        barChart.xAxis.setDrawGridLines(false) // x축의 그리드 라인을 제거하여 더 깔끔하게 보이도록 함
        barChart.xAxis.labelRotationAngle = 45f // 레이블이 겹치지 않도록 각도 조정
        barChart.axisLeft.axisMinimum = 0f
        barChart.axisLeft.axisMaximum = 100f
        barChart.axisRight.isEnabled = false
        barChart.invalidate() // 차트를 다시 그립니다.

        // 그래프를 컨테이너에 추가
        graphContainer.addView(barChart, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        ))

        // 세부 정보 텍스트뷰 설정
        val details = StringBuilder()
        details.append("기간: $period\n\n")
        for ((subject, grade) in grades) {
            details.append("$subject: $grade\n")
        }
        scoreDetails.text = details.toString()
    }
}