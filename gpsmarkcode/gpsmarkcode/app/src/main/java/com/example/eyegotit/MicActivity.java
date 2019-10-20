package com.example.eyegotit;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MicActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private String text;
    Intent intent;
    private TextToSpeech tts;
    SpeechRecognizer mRecognizer;
    final int PERMISSION = 1;
    String toastText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mic);
        if ( Build.VERSION.SDK_INT >= 23 ){
            // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO},PERMISSION);
        }

        //사용자에게 음성을 요구하고 음성 인식기를 통해 전송하는 활동을 시작함.
        intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        //음성인식을 위한 음성 인식기의 의도에 사용되는 여분의 키 설정
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getPackageName());
        //음성을 번역할 언어 설정.
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");

        tts=new TextToSpeech(MicActivity.this,this);

        text = "현재 위치" + getAddress(MicActivity.this, 37.5670135, 126.9783740) + "맞나요?";
        speekTTS();

        ImageButton btn = findViewById(R.id.imageButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiver();
            }
        });
    }
    @Override
    protected void onDestroy() {
        //TTS 멈추기
        if(tts!=null){
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
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
                speekTTS();
            }
        }
        else{
            Log.d("hyori","failed");
        }
    }

    public String getAddress(Context mContext, double lat, double lng) {
        String nowAddress = "현재 위치를 확인 할 수 없습니다.";
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        List<Address> address;
        try {
            if (geocoder != null) {
                //세번째 파라미터는 좌표에 대해 주소를 리턴 받는 갯수로
                //한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 최대갯수 설정
                address = geocoder.getFromLocation(lat, lng, 1);

                if (address != null && address.size() > 0) {
                    // 주소 받아오기
                    String currentLocationAddress = address.get(0).getAddressLine(0).toString();
                    nowAddress = currentLocationAddress;
                }
            }
        } catch (IOException e) {
            nowAddress = "주소를 가져올 수 없습니다.";
            System.out.println("주소를 가져올 수 없습니다.");
            e.printStackTrace();
        }
        return nowAddress;
    }

    private void speekTTS(){
        tts.speak(text, TextToSpeech.QUEUE_FLUSH,null);
        tts.setSpeechRate(2);
    }

    private void replyAnswer(String input){
        if(input.equals("네")||input.equals("내")||input.equals("네네")){
            //Toast.makeText(MicActivity.this,"현재위치 맞대요",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MicActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else if(input.equals("아니오")||input.equals("아니요")){
            text="다시 말씀해주세요";
            speekTTS();
        }
    }

    private String receiver(){
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(MicActivity.this);  // 새 SpeechRecognizer를 만드는 팩토리 메서드.
        mRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) { //음성인식 시작하는것을 알림.
                Toast.makeText(getApplicationContext(),"음성인식을 시작합니다.",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBeginningOfSpeech() {} //말하기 시작

            @Override
            public void onRmsChanged(float rmsdB) {} //입력받는 소리의 크기를 알려줌.

            @Override
            public void onBufferReceived(byte[] buffer) {} //사용자가 말한 단어들을 buffer에 담음.

            @Override
            public void onEndOfSpeech() {} //말하기 중지

            //음성인식 에러처리 부분
            @Override
            public void onError(int error) {
                String message;

                switch (error) {
                    case SpeechRecognizer.ERROR_AUDIO:
                        message = "오디오 에러";
                        break;
                    case SpeechRecognizer.ERROR_CLIENT:
                        message = "클라이언트 에러";
                        break;
                    case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                        message = "퍼미션 없음";
                        break;
                    case SpeechRecognizer.ERROR_NETWORK:
                        message = "네트워크 에러";
                        break;
                    case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                        message = "네트웍 타임아웃";
                        break;
                    case SpeechRecognizer.ERROR_NO_MATCH:
                        message = "찾을 수 없음";
                        break;
                    case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                        message = "RECOGNIZER가 바쁨";
                        break;
                    case SpeechRecognizer.ERROR_SERVER:
                        message = "서버가 이상함";
                        break;
                    case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                        message = "말하는 시간초과";
                        break;
                    default:
                        message = "알 수 없는 오류임";
                        break;
                }

                Toast.makeText(getApplicationContext(), "에러가 발생하였습니다. : " + message,Toast.LENGTH_SHORT).show();
            }

            //음성인식 결과 출력
            @Override
            public void onResults(Bundle results) {
                // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줍니다.
                ArrayList<String> matches =
                        results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                for(int i = 0; i < matches.size() ; i++){
                    toastText=matches.get(i);
                }
                Toast.makeText(MicActivity.this,toastText,Toast.LENGTH_LONG).show();
                replyAnswer(toastText);
            }

            @Override
            public void onPartialResults(Bundle partialResults) {}

            @Override
            public void onEvent(int eventType, Bundle params) {}
        });

        mRecognizer.startListening(intent);
        return toastText;
    }

}