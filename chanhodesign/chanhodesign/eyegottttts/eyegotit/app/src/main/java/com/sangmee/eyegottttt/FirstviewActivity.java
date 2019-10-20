package com.sangmee.eyegottttt;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

public class FirstviewActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{
    final int PERMISSION = 1;
    TextToSpeech tts;
    SpeakVoiceActivity voiceActivity;
    ReplyVoiceActivity replyVoiceActivity;
    Intent intent;
    TextView textview1, textview2, textview3, textview4;
    LinearLayout linearLayout;
    final int MOVE_HAND=350;//얼마나 밀었을때
    float sx,sy,ssx,ssy;//시작지점


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // style 다른거 쓸라면 이렇게 해야됨.
        setTheme(R.style.nomenubar);

        setContentView(R.layout.activity_firstview);

        linearLayout = findViewById(R.id.linearlayout);
        textview1 = findViewById(R.id.textView1);
        textview2 = findViewById(R.id.textView2);
        textview3 = findViewById(R.id.textView3);
        textview4 = findViewById(R.id.textView4);
        final ImageView iv = (ImageView)findViewById(R.id.imageView);
        iv.setVisibility(View.INVISIBLE);

        Handler delayHandler = new Handler();

        // textview.animate().getDuration()


        textview1.setText("안녕하세요");
        textview2.setText("eye got it 입니다.\n어떤 기능을 사용할건가요?\n");
        textview3.setText("1. 등록해둔 경로\n2. 새로운 경로\n");
        textview4.setText("(1번 : 오른쪽 드래그)\n(2번 : 왼쪽 드래그)");
        textview1.animate().alpha(1f).setDuration(2000);

// 딜레이 거는 방법 밑에 있는 숫자로 조정 가능
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 이 부분이 alpha0으로 둔것을 천천히 나타나게 하는 부분
                textview2.animate().alpha(1f).setDuration(2000);
            }
        },900);

        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                textview3.animate().alpha(1f).setDuration(2000);
            }
        },3000);

        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                textview4.animate().alpha(1f).setDuration(2000);
            }
        },6000);




        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent e) {
                if(e.getAction() == MotionEvent.ACTION_DOWN){
                    sx = e.getRawX();
                    sy = e.getRawY();
                    iv.setX(sx);
                    iv.setY(sy);
                    iv.setVisibility(View.VISIBLE);
                }
                if(e.getAction() == MotionEvent.ACTION_MOVE){
                    ssx = e.getRawX();
                    ssy = e.getRawY();
                    iv.setX(ssx);
                    iv.setY(ssy);
                }
                else if(e.getAction() == MotionEvent.ACTION_UP){
                    float diffxx = sx-e.getRawX();
                    float diffyy = sy - e.getRawY();
                    iv.setVisibility(View.INVISIBLE);
                    if(Math.abs(diffxx)>Math.abs(diffyy)){
                        if(diffxx>MOVE_HAND) {
                            Intent intent=new Intent(FirstviewActivity.this, MainActivity.class);
                            startActivity(intent);
                            tts.stop();
                        }
                        else if (diffxx<-MOVE_HAND) {
                            Intent intent=new Intent(FirstviewActivity.this, DatabaseActivity.class);
                            startActivity(intent);
                            tts.stop();
                        }
                    }
                    else {
                        if (diffyy > MOVE_HAND){
                            //"아래에서 위로"
                        }
                        else if (diffyy < -MOVE_HAND){
                            //"위에서 아래로"
                        }
                    }
                }
                return true;
            }
        });


        if (Build.VERSION.SDK_INT >= 23) {
            // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO}, PERMISSION);
        }
        tts = new TextToSpeech(this, this);
        voiceActivity = new SpeakVoiceActivity(FirstviewActivity.this, tts);

        voiceActivity.text = "안녕하세요 eye got it 입니다.... 어떤 기능을 사용할건가요?1번 등록해둔 경로..  2번 새로운 경로...  1번 선택 시 오른쪽 드래그... 2번 선택 시 왼쪽 드래그..를 하세요";
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
        }
    };

    View.OnClickListener fordisablemanListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(FirstviewActivity.this, DatabaseActivity.class);
            startActivity(intent);
            tts.stop();
        }
    };
    View.OnClickListener forablemanListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(FirstviewActivity.this, MainActivity.class);
            startActivity(intent);
            tts.stop();
        }
    };





}