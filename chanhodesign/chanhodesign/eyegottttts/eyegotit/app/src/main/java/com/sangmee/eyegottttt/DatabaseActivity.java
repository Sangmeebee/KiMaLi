package com.sangmee.eyegottttt;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sangmee.eyegottttt.checkbox_listview.Choice_DatabaseActivity;
import com.sangmee.eyegottttt.checkbox_listview.Delete_DatabaseActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class DatabaseActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener mChild;

    private ListView listView;
    ArrayList<String> child_name;
    ArrayAdapter<String> arrayAdapter;
    String name;
    ImageButton button;
    SpeakVoiceActivity voiceActivity;
    ReplyVoiceActivity replyVoiceActivity;
    TextToSpeech tts;
    final int PERMISSION = 1;
    String arrayListText;
    String [] strings;


    ArrayList<String> n_sLongitude;
    ArrayList<String> n_sLatitude;
    String s_location;
    Intent intent;
    String text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        setTitle("나의 경로 리스트");



        if ( Build.VERSION.SDK_INT >= 23 ){
            // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO},PERMISSION);
        }

        //voiceActivity.text="안녕";


        listView=(ListView)findViewById(R.id.database_list);
        button=findViewById(R.id.imageButton4);
        button.setOnClickListener(voicereplyListener);
        GlideDrawableImageViewTarget gifImage=new GlideDrawableImageViewTarget(button);
        Glide.with(this).load(R.drawable.play).into(gifImage);

        child_name=new ArrayList<>();
        initDatabase();

        arrayAdapter=new ArrayAdapter<>(this,R.layout.listviewtext, new ArrayList<String>());
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(onItemClickListener);


        replyVoiceActivity = new ReplyVoiceActivity(DatabaseActivity.this, tts, "하나",null,null,arrayAdapter,listView);



        databaseReference = firebaseDatabase.getReference(); // 변경값을 확인할 child 이름
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayAdapter.clear();

                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    String name=messageData.getKey();
                    child_name.add(name);
                    arrayAdapter.add(name);

                    strings=new String[child_name.size()];

                    arrayListText=" ";
                    for(int i=0;i<child_name.size();i++){
                        strings[i]=child_name.get(i);

                        arrayListText=arrayListText.concat(i+1+"번, "+strings[i]+"  ,  ");
                    }

                }
                arrayAdapter.notifyDataSetChanged();
                listView.setSelection(arrayAdapter.getCount()-1);

                tts=new TextToSpeech(DatabaseActivity.this,DatabaseActivity.this);
                voiceActivity=new SpeakVoiceActivity(DatabaseActivity.this,tts);
                voiceActivity.text="나의 경로는 "+ arrayListText + arrayAdapter.getCount()+"개의 경로가 있습니다." +
                        "어떤 경로를 선택하시겠습니까? 번호를 말해주세요.";

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }
    @Override
    public void onInit(int status) {//TTS 보내기 위한 함수
        if(status==TextToSpeech.SUCCESS){
            int result=tts.setLanguage(Locale.KOREA);
            if(result==TextToSpeech.LANG_MISSING_DATA){
                Log.d("hyori","no tts data");
            }
            else if(result==TextToSpeech.LANG_NOT_SUPPORTED){
                Log.d("hyori","language wrong");
            }
            else{
                //mRecognizer.stopListening();
                voiceActivity.speekTTS(voiceActivity.text,tts);
            }
        }
        else{
            Log.d("hyori","failed");
        }

    }

    View.OnClickListener voicereplyListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            replyVoiceActivity.receiver();
            GlideDrawableImageViewTarget gifImage=new GlideDrawableImageViewTarget(button);
            Glide.with(DatabaseActivity.this).load(R.drawable.play2).into(gifImage);
        }
    };

    private void initDatabase() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mChild = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        databaseReference.addChildEventListener(mChild);
    }

    @Override
    protected void onDestroy() {
        //TTS 멈추기
        if(tts!=null){
            tts.stop();
            tts.shutdown();
        }
        databaseReference.removeEventListener(mChild);
        super.onDestroy();
    }

    AdapterView.OnItemClickListener onItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            s_location=adapterView.getAdapter().getItem(i).toString();
            Log.d("sangmin", s_location);
            intent=new Intent(DatabaseActivity.this, route_confirmActivity.class);
            intent.putExtra("s_location", s_location);
            startActivity(intent);
            tts.stop();
        }
    };

    //메뉴
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.switchs:

                break;
            case R.id.delete:
                intent=new Intent(DatabaseActivity.this, Delete_DatabaseActivity.class);
                startActivity(intent);

                return true;




        }
        return super.onOptionsItemSelected(item);
    }


}