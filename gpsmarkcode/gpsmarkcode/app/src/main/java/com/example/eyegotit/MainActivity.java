package com.example.eyegotit;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.util.FusedLocationSource;

import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, TextToSpeech.OnInitListener{
    private MapView mapView;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private LatLng coord;
    private TextToSpeech tts;
    final int PERMISSION = 1;
    Intent intent;
    SpeechRecognizer mRecognizer;
    String text;
    double lat;
    double lon;
    String locationText;

    //SST 를 하기위한 음성 함수들 관련 변수들 아직 구현되지 못함 이 관련들은 다 주석처리해놓음
//    Intent i;
//    SpeechRecognizer mRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        tts=new TextToSpeech(this,this);

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
        mapView.getMapAsync(this);

        //현재 위치 받아지는게 구현이 아직 되지 않아 네이버 지도가 설정해놓은 시청역의 위도와 경도를 임의로 삽입..
        coord = new LatLng(37.5670135, 126.9783740);
        text="어디로 가시겠습니까?";


//        //음성인식
//        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
//        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
//        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
//        mRecognizer.setRecognitionListener(listener);


    }
    //음성인식을 위한 메소드

    private RecognitionListener listener = new RecognitionListener() {
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
        public void onEndOfSpeech() { } //말하기 중지

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
                MainActivity.this.setTitle(matches.get(i));
                locationText=matches.get(i);
            }
            replyAnswer(locationText);
        }

        @Override
        public void onPartialResults(Bundle partialResults) {}

        @Override
        public void onEvent(int eventType, Bundle params) {}
    };


    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        //TTS 멈추기
        if(tts!=null){
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
        mapView.onDestroy();
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
                speekTTS();

            }
        }
        else{
            Log.d("hyori","failed");
        }

    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,  @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }
    @UiThread

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        LocationOverlay locationOverlay = naverMap.getLocationOverlay();
        locationOverlay.setVisible(true);
        locationOverlay.setPosition(new LatLng(locationOverlay.getPosition().latitude,locationOverlay.getPosition().longitude));

        //여기서 맵 소스가 보내져서 네이버 맵과 연동됨
        naverMap.setLocationSource(locationSource);
        // 검색한 위치 지도에 뜨게 하는 방법
        naverMap.setLocationTrackingMode(LocationTrackingMode.Face);

        //maker 3개,3개를 관리하기 위한 arraylist 2개
        ArrayList<Marker> arrayList1=new ArrayList<>();
        ArrayList<Marker> arrayList2=new ArrayList<>();

        Marker[] marker = new Marker[6];

        String [] pathStrings={"장충고등학교","약수역","청구역","한성대입구역","한성대학교","한성여자고등학교"};


        for (int i = 0; i < marker.length; i++) {
            getAddressLang(MainActivity.this, pathStrings[i]);

            marker[i] = new Marker();//객체 생성

            marker[i].setPosition(new LatLng(lat, lon));//marker의 위치 설정
            //latLngs[i]=new LatLng(marker[i].getPosition().latitude,marker[i].getPosition().longitude);

            //arrayList.add(new LatLng(latLngs[i].latitude,latLngs[i].longitude));
            marker[i].setMap(naverMap);//marker를 지도에 찍기

            if(i<3) {
                arrayList1.add(marker[i]);//arraylist1에다가 1,2,3번째 marker 관리
            }
            else if(i>3){
                arrayList2.add(marker[i]);//arraylist2에다가 나머지 3개 marker 관리
            }
            Log.i("hyori","afdfsd"+arrayList1.size());
        }



        /*locationOverlay.setIcon(OverlayImage.fromResource(R.drawable.mic));
        locationOverlay.setIconWidth(40);
        locationOverlay.setIconHeight(40);*/





        //현재 위치 주소(string)으로 변환된것 토스트로 찍기
        //Toast.makeText(MainActivity.this,getAddress(MainActivity.this, coord.latitude, coord.longitude),Toast.LENGTH_LONG).show();

    }
    /**
     * 위도,경도로 주소로 구하는 함수
     * @param lat
     * @param lng
     * @return 주소
     */
    public String getAddress(Context mContext, double lat, double lng) {
        String nowAddress ="현재 위치를 확인 할 수 없습니다.";
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
                    nowAddress  = currentLocationAddress;
                }
            }
        } catch (IOException e) {
            nowAddress = "주소를 가져올 수 없습니다.";
            System.out.println("주소를 가져올 수 없습니다.");
            e.printStackTrace();
        }
        return nowAddress;
    }

    /////주소를 위도, 경도로 바꿔주는 함수
    public void getAddressLang(Context mContext,String str){
        // 주소입력후 지도2버튼 클릭시 해당 위도경도값의 지도화면으로 이동
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        List<Address> list = null;
        //
        try {
            list = geocoder.getFromLocationName
                    (str, // 지역 이름
                            10); // 읽을 개수
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
        }

        if (list != null) {
            Address addr = list.get(0);
            lat = addr.getLatitude();
            lon = addr.getLongitude();
            Toast.makeText(MainActivity.this,"위도 :"+lat+" , "+"경도 : "+lon,Toast.LENGTH_LONG).show();
        }
    }
    //TTS
    private void speekTTS(){

        tts.speak(text,TextToSpeech.QUEUE_FLUSH,null);
        tts.setSpeechRate(2);

        if(tts.isSpeaking()==true) {
            tts.stop();
            mRecognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity.this);  // 새 SpeechRecognizer를 만드는 팩토리 메서드.
            mRecognizer.setRecognitionListener(listener);  // 모든 콜백을 수신하는 리스너를 설정.
            mRecognizer.startListening(intent);
        }


    }
    /////경로 찾는 함수
    private void replyAnswer(String input){
        if(!input.equals("한성대입구역")){
            text="다시 말씀해주세요";
            speekTTS();
        }
        else {
            text="경로를 찾겠습니다";
            speekTTS();
            //////////////경로 찾는 코드////////////
        }
    }

}