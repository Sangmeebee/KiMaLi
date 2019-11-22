package com.example.kimali

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast

class Parent_firstView : AppCompatActivity() {
    val array: Array<String> = arrayOf("아들1","아들2","아들3","딸1","딸2")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_NoActionBar)
        setContentView(R.layout.activity_parent_first_view)

        val listview = findViewById(R.id.child_list) as ListView
        val adapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1,array)

        listview.adapter = adapter
        listview.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String
            Toast.makeText(this, "Clicked item :"+" "+selectedItem,Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Parent_missionList::class.java)
            intent.putExtra("selectedString", selectedItem)
            this.startActivity(intent)
        }

    }
}
