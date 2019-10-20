package com.example.kimali

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_parent_mission_list.*

class Parent_missionList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_mission_list)

        val array: Array<String> = arrayOf("청소기돌리기","설거지하기","빨래하기")
        val array2: Array<Int> = arrayOf(5000,3000,7000)

        //baseAdapter로 생성
        mission_list.adapter = HBaseAdapter(this, array, array2)

        //ArrayAdapter로 생성

        val year_Adapter = ArrayAdapter.createFromResource(this,R.array.year,android.R.layout.simple_spinner_dropdown_item)
        year_select.setAdapter(year_Adapter)

        val month_Adapter = ArrayAdapter.createFromResource(this,R.array.month,android.R.layout.simple_spinner_dropdown_item)
        month_select.setAdapter(month_Adapter)

        val day_Adapter = ArrayAdapter.createFromResource(this,R.array.day,android.R.layout.simple_spinner_dropdown_item)
        day_select.setAdapter(day_Adapter)

    }
}
