package com.example.kmlpbl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import androidx.core.content.ContextCompat
import com.google.firebase.database.*
import java.util.*

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener

import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.Button
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    internal var user_list = ArrayList<User>()
    internal var users = arrayOfNulls<User>(50)
    var mDatabase: DatabaseReference? = null

    private var databaseReference: DatabaseReference? = null
    internal var outer_title = ArrayList<String>()
    internal var outer_explan = ArrayList<String>()
    internal var outer_price = ArrayList<Int>()
    internal var upper_title = ArrayList<String>()
    internal var upper_explan = ArrayList<String>()
    internal var upper_price = ArrayList<Int>()
    internal var lower_title = ArrayList<String>()
    internal var lower_explan = ArrayList<String>()
    internal var lower_price = ArrayList<Int>()
    internal var skirt_title = ArrayList<String>()
    internal var skirt_explan = ArrayList<String>()
    internal var skirt_price = ArrayList<Int>()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firebase=FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        // Write a string to node "disconnectmessage" when this client loses connection
        FirebaseDatabase.getInstance().getReference("disconnectmessage").onDisconnect().setValue("I disconnected!")


        mDatabase = FirebaseDatabase.getInstance().reference

        //아우터 데이터 가져오기
        databaseReference = FirebaseDatabase.getInstance().reference
        val recentPostsQuery = databaseReference!!.child("all").child("outer")
        recentPostsQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {


                for (messageData in dataSnapshot.children) {
                    val key = messageData.key
                    val o_title = messageData.child("product_name").value!!.toString()
                    outer_title.add(o_title)


                    val o_price = Integer.parseInt(messageData.child("price").value!!.toString())
                    outer_price.add(o_price)


                    val o_explan = messageData.child("explan").value!!.toString()
                    outer_explan.add(o_explan)



                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })


        //상의 데이터 가져오기
        databaseReference = FirebaseDatabase.getInstance().reference
        val recentPostsQuery2 = databaseReference!!.child("all").child("upper")
        recentPostsQuery2.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {


                for (messageData in dataSnapshot.children) {
                    val u_title = messageData.child("product_name").value!!.toString()
                    upper_title.add(u_title)


                    val u_price = Integer.parseInt(messageData.child("price").value!!.toString())
                    upper_price.add(u_price)


                    val u_explan = messageData.child("explan").value!!.toString()
                    upper_explan.add(u_explan)



                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })


        databaseReference = FirebaseDatabase.getInstance().reference
        val recentPostsQuery3 = databaseReference!!.child("all").child("lower")
        recentPostsQuery3.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {


                for (messageData in dataSnapshot.children) {
                    val l_title = messageData.child("product_name").value!!.toString()
                    lower_title.add(l_title)


                    val l_price = Integer.parseInt(messageData.child("price").value!!.toString())
                    lower_price.add(l_price)


                    val l_explan = messageData.child("explan").value!!.toString()
                    lower_explan.add(l_explan)



                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })



        databaseReference = FirebaseDatabase.getInstance().reference
        val recentPostsQuery4 = databaseReference!!.child("all").child("skirt")
        recentPostsQuery4.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {


                for (messageData in dataSnapshot.children) {
                    val s_title = messageData.child("product_name").value!!.toString()
                    skirt_title.add(s_title)


                    val s_price = Integer.parseInt(messageData.child("price").value!!.toString())
                    skirt_price.add(s_price)


                    val s_explan = messageData.child("explan").value!!.toString()
                    skirt_explan.add(s_explan)



                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })





        val listview: ListView
        val adapter: ListViewAdapter

        // adapter 생성
        adapter = ListViewAdapter()

        val all : Button
        val outer : Button
        val top : Button
        val bottom : Button
        val skirt : Button

        // 버튼에 연결
        all = findViewById<View>(R.id.btn_1) as Button
        outer = findViewById<View>(R.id.btn_2) as Button
        top = findViewById<View>(R.id.btn_3) as Button
        bottom = findViewById<View>(R.id.btn_4) as Button
        skirt = findViewById<View>(R.id.btn_5) as Button


        // 버튼 온클릭 리스너 달기
        all.setOnClickListener {
            Toast.makeText(this@MainActivity, "all", Toast.LENGTH_SHORT).show()
            adapter.clearItem()
            for (i in 0..2) {
                adapter.addItem(
                    ContextCompat.getDrawable(this, R.drawable.outer)!!,
                    outer_title[i], outer_price[i].toString()
                )
            }

            for (i in 0..2) {
                adapter.addItem(
                    ContextCompat.getDrawable(this, R.drawable.cloth3)!!,
                    upper_title[i], upper_price[i].toString()
                )
            }

            for (i in 0..2) {
                adapter.addItem(
                    ContextCompat.getDrawable(this, R.drawable.bottom)!!,
                    lower_title[i], lower_price[i].toString()
                )
            }

            for (i in 0..2) {
                adapter.addItem(
                    ContextCompat.getDrawable(this, R.drawable.skirt)!!,
                    skirt_title[i], skirt_price[i].toString()
                )
            }

            adapter.notifyDataSetChanged()
        }
        outer.setOnClickListener {
            Toast.makeText(this@MainActivity, "outer", Toast.LENGTH_SHORT).show()
            adapter.clearItem()
            for (i in 0..2) {
                adapter.addItem(
                    ContextCompat.getDrawable(this, R.drawable.outer)!!,
                    outer_title[i], outer_price[i].toString()
                )
            }

            adapter.notifyDataSetChanged()
        }
        top.setOnClickListener {
            Toast.makeText(this@MainActivity, "top", Toast.LENGTH_SHORT).show()
            adapter.clearItem()
            for (i in 0..2) {
                adapter.addItem(
                    ContextCompat.getDrawable(this, R.drawable.cloth3)!!,
                    upper_title[i], upper_price[i].toString()
                )
            }

            adapter.notifyDataSetChanged()
        }
        bottom.setOnClickListener {
            Toast.makeText(this@MainActivity, "bottom", Toast.LENGTH_SHORT).show()
            adapter.clearItem()
            for (i in 0..2) {
                adapter.addItem(
                    ContextCompat.getDrawable(this, R.drawable.bottom)!!,
                    lower_title[i], lower_price[i].toString()
                )
            }

            adapter.notifyDataSetChanged()
        }
        skirt.setOnClickListener {
            Toast.makeText(this@MainActivity, "skirt", Toast.LENGTH_SHORT).show()
            adapter.clearItem()
            for (i in 0..2) {
                adapter.addItem(
                    ContextCompat.getDrawable(this, R.drawable.skirt)!!,
                    skirt_title[i], skirt_price[i].toString()
                )
            }

            adapter.notifyDataSetChanged()
        }



        // 리스트뷰 참조 및 Adapter달기
        listview = findViewById<View>(R.id.mainListView) as ListView
        listview.adapter = adapter

        adapter.addItem(
            ContextCompat.getDrawable(this, R.drawable.cloth3)!!,
            "맨투맨", "맨투맨1이다"
        )
        adapter.addItem(
            ContextCompat.getDrawable(this, R.drawable.cloth3)!!,
            "셔츠", "셔츠이다"
        )
        adapter.addItem(
            ContextCompat.getDrawable(this, R.drawable.cloth3)!!,
            "맨투맨2", "맨투맨2이다"
        )



       // for (s in outer_title)
         //   Log.d("sangmin", s)



    }



    private fun writeNewUser() {
        var cloth_id: String

        users[0] = User("패딩", 80000, "품절임박")
        users[1] = User("롱패딩", 88000, "패피라면 입어야 할 ")
        users[2] = User("오리털패딩", 100000, "100% 오리털")

        users[3] = User("무지 티", 10000, "간편한 옷입니다")
        users[4] = User("나시 티", 3000, "시원합니다")
        users[5] = User("박스 티", 20000, "도전 패션피플")

        users[6] = User("데님 진", 29000, "인기상품")
        users[7] = User("찢청바지", 28000, "초인기상품")
        users[8] = User("벙거지 바지", 30000, "초초인기상품")


        users[9] = User("청스커트", 329000, "인기상품")
        users[10] = User("레깅스커트", 328000, "초인기상품")
        users[11] = User("골덴스커트", 40000, "초초인기상품")

        for (i in 0..2) {
            cloth_id = "00$i"
            mDatabase!!.child("all").child("outer").child(cloth_id).setValue(users[i])
        }

        for (i in 3..5) {
            cloth_id = "00$i"
            mDatabase!!.child("all").child("upper").child(cloth_id).setValue(users[i])
        }

        for (i in 6..8) {
            cloth_id = "00$i"
            mDatabase!!.child("all").child("lower").child(cloth_id).setValue(users[i])
        }

        for (i in 9..11) {
            cloth_id = "00$i"
            mDatabase!!.child("all").child("skirt").child(cloth_id).setValue(users[i])
        }


    }

}
