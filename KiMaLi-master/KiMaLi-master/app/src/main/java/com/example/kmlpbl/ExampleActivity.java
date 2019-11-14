package com.example.kmlpbl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExampleActivity extends AppCompatActivity {


    private DatabaseReference databaseReference;
    ArrayList<String> outer_title = new ArrayList<>();
    ArrayList<String> outer_explan = new ArrayList<>();
    ArrayList<Integer> outer_price = new ArrayList<>();
    ArrayList<String> upper_title = new ArrayList<>();
    ArrayList<String> upper_explan = new ArrayList<>();
    ArrayList<Integer> upper_price = new ArrayList<>();
    ArrayList<String> lower_title = new ArrayList<>();
    ArrayList<String> lower_explan = new ArrayList<>();
    ArrayList<Integer> lower_price = new ArrayList<>();
    ArrayList<String> skirt_title = new ArrayList<>();
    ArrayList<String> skirt_explan = new ArrayList<>();
    ArrayList<Integer> skirt_price = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        //아우터 데이터 가져오기
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Query recentPostsQuery = databaseReference.child("all").child("outer");
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    String key=messageData.getKey();
                    String o_title = messageData.child("product_name").getValue().toString();
                    outer_title.add(o_title);
                    Log.d("sangmin", o_title);

                    Integer o_price = Integer.parseInt(messageData.child("price").getValue().toString());
                    outer_price.add(o_price);
                    Log.d("sangmin", ""+o_price);

                    String o_explan = messageData.child("explan").getValue().toString();
                    outer_explan.add(o_explan);
                    Log.d("sangmin", o_explan);


                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //상의 데이터 가져오기
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Query recentPostsQuery2 = databaseReference.child("all").child("upper");
        recentPostsQuery2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    String u_title = messageData.child("product_name").getValue().toString();
                    upper_title.add(u_title);
                    Log.d("sangmin", u_title);

                    Integer u_price = Integer.parseInt(messageData.child("price").getValue().toString());
                    upper_price.add(u_price);
                    Log.d("sangmin", ""+u_price);

                    String u_explan = messageData.child("explan").getValue().toString();
                    upper_explan.add(u_explan);
                    Log.d("sangmin", u_explan);


                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        databaseReference = FirebaseDatabase.getInstance().getReference();
        Query recentPostsQuery3 = databaseReference.child("all").child("lower");
        recentPostsQuery3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    String l_title = messageData.child("product_name").getValue().toString();
                    lower_title.add(l_title);
                    Log.d("sangmin", l_title);

                    Integer l_price = Integer.parseInt(messageData.child("price").getValue().toString());
                    lower_price.add(l_price);
                    Log.d("sangmin", ""+l_price);

                    String l_explan = messageData.child("explan").getValue().toString();
                    lower_explan.add(l_explan);
                    Log.d("sangmin", l_explan);


                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        databaseReference = FirebaseDatabase.getInstance().getReference();
        Query recentPostsQuery4 = databaseReference.child("all").child("skirt");
        recentPostsQuery4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    String s_title = messageData.child("product_name").getValue().toString();
                    skirt_title.add(s_title);
                    Log.d("sangmin", s_title);

                    Integer s_price = Integer.parseInt(messageData.child("price").getValue().toString());
                    skirt_price.add(s_price);
                    Log.d("sangmin", ""+s_price);

                    String s_explan = messageData.child("explan").getValue().toString();
                    skirt_explan.add(s_explan);
                    Log.d("sangmin", s_explan);


                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
