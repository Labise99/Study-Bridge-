package com.example.studybridge

import android.content.Context
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

class ScoreTestCheck : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        StateNavRemoveActivity.hideSystemUI(window)
        setContentView(R.layout.score_test_check)

        val graphContainer = findViewById<LinearLayout>(R.id.graph_container)
        val scoreDetails = findViewById<TextView>(R.id.score_details)

        // 인텐트로부터 기간 정보 가져오기
        val period = intent.getStringExtra("PERIOD")

        // 저장된 데이터 불러오기
        val sharedPreferences = getSharedPreferences("ScoreData", Context.MODE_PRIVATE)
        val languageScore = sharedPreferences.getString("languageScore", "0")?.toFloat() ?: 0f
        val mathScore = sharedPreferences.getString("mathScore", "0")?.toFloat() ?: 0f
        val englishScore = sharedPreferences.getString("englishScore", "0")?.toFloat() ?: 0f
        val koreanHistoryScore = sharedPreferences.getString("koreanHistoryScore", "0")?.toFloat() ?: 0f
        val scienceScore = sharedPreferences.getString("scienceScore", "0")?.toFloat() ?: 0f

        // 그래프와 세부 정보를 위한 데이터
        val scores = mapOf(
            "국어" to languageScore,
            "수학" to mathScore,
            "영어" to englishScore,
            "한국사" to koreanHistoryScore,
            "과학" to scienceScore
        )

        // 바 차트 설정
        val barChart = BarChart(this)
        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()
        var i = 0

        // 점수를 바 차트에 추가
        for ((subject, score) in scores) {
            entries.add(BarEntry(i.toFloat(), score))
            labels.add(subject)
            i++
        }

        // 데이터셋 설정
        val dataSet = BarDataSet(entries, "Scores")
        dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        val barData = BarData(dataSet)
        barChart.data = barData
        barChart.description.isEnabled = false
        barChart.setFitBars(true)
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.xAxis.granularity = 1f
        barChart.xAxis.setDrawGridLines(false)
        barChart.xAxis.labelRotationAngle = 45f
        barChart.axisLeft.axisMinimum = 0f
        barChart.axisLeft.axisMaximum = 100f
        barChart.axisRight.isEnabled = false
        barChart.invalidate()

        // 그래프를 컨테이너에 추가
        graphContainer.addView(barChart, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        ))

        // 세부 정보 텍스트뷰 설정
        val details = """
            기간: $period
            
            국어: 점수 ${scores["국어"]}
            수학: 점수 ${scores["수학"]}
            영어: 점수 ${scores["영어"]}
            한국사: 점수 ${scores["한국사"]}
            과학: 점수 ${scores["과학"]}
        """.trimIndent()

        scoreDetails.text = details
    }
}