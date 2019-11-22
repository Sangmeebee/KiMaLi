package com.example.kimali

import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class parent_listview_activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_NoActionBar)
        setContentView(R.layout.activity_parent_listview)

        val textView = findViewById(R.id.textView3) as TextView
        textView.setText("수학의 정석 집합단원 풀기                   보상 : 1000원")
    }
}