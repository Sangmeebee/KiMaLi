package com.example.kimali

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_parent_mission_list.*
import java.util.*

class Parent_missionList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setTheme(R.style.AppTheme_NoActionBar)
        setContentView(R.layout.activity_parent_mission_list)

        val textView1 = findViewById(R.id.year_select) as TextView
        val textView2 = findViewById(R.id.month_select) as TextView
        val textView3 = findViewById(R.id.day_select) as TextView

        var button=findViewById<View>(R.id.confirm)
        button.setOnClickListener(View.OnClickListener { view ->
            val c = Calendar.getInstance()
            val nYear = c.get(Calendar.YEAR)
            val nMon = c.get(Calendar.MONTH)
            val nDay = c.get(Calendar.DAY_OF_MONTH)

            val mDateSetListener =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    var strDate = year.toString() + "년 "
                    strDate += (monthOfYear + 1).toString() + "월 "
                    strDate += dayOfMonth.toString() + "일"

                    textView1.setText(year.toString())
                    textView2.setText((monthOfYear+1).toString())
                    textView3.setText(dayOfMonth.toString())

                    Toast.makeText(applicationContext, strDate, Toast.LENGTH_SHORT).show()
                }

            val oDialog = DatePickerDialog(
                this,
                android.R.style.Theme_DeviceDefault_Light_Dialog,
                mDateSetListener, nYear, nMon, nDay
            )
            oDialog.show()

        })

        if (intent.hasExtra("selectedString")) {
            val text = intent.getStringExtra("selectedString")
            setTitle(text)
            /* "nameKey"라는 이름의 key에 저장된 값이 있다면
               textView의 내용을 "nameKey" key에서 꺼내온 값으로 바꾼다 */

        } else {
            Toast.makeText(this, "전달된 이름이 없습니다", Toast.LENGTH_SHORT).show()
        }

        val array: Array<String> = arrayOf("청소기돌리기","설거지하기","빨래하기")
        val array2: Array<Int> = arrayOf(5000,3000,7000)

        //baseAdapter로 생성
        mission_list.adapter = HBaseAdapter(this, array, array2)
        mission_list.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String
            Toast.makeText(this, "Clicked item :"+" "+selectedItem, Toast.LENGTH_SHORT).show()
            val intent = Intent(this, parent_listview_activity::class.java)
            intent.putExtra("position", position)
            this.startActivity(intent)
        }

        //ArrayAdapter로 생성

        /*val year_Adapter = ArrayAdapter.createFromResource(this,R.array.year,android.R.layout.simple_spinner_dropdown_item)
        year_select.setAdapter(year_Adapter)

        val month_Adapter = ArrayAdapter.createFromResource(this,R.array.month,android.R.layout.simple_spinner_dropdown_item)
        month_select.setAdapter(month_Adapter)

        val day_Adapter = ArrayAdapter.createFromResource(this,R.array.day,android.R.layout.simple_spinner_dropdown_item)
        day_select.setAdapter(day_Adapter)*/

    }
}
