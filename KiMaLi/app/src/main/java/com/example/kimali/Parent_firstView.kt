package com.example.kimali

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class Parent_firstView : AppCompatActivity() {
    val array: Array<String> = arrayOf("아들1","아들2","아들3","딸1","딸2")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_first_view)

        val listview = findViewById(R.id.child_list) as ListView
        val adapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1,array)

        listview.adapter = adapter


    }
}
