package com.example.kimali

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class select_child_activity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_NoActionBar)
        setContentView(R.layout.activity_select_childlistview)
    }
}