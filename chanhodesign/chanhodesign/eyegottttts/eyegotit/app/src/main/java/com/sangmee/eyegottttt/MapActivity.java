package com.sangmee.eyegottttt;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Align;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.LocationButtonView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private DatabaseReference databaseReference;
    Intent intent;
    String s_location;
    ArrayList<Double> longitude_list= new ArrayList<>();
    ArrayList<Double> latitude_list= new ArrayList<>();
    ArrayList<LatLng> latlng_list=new ArrayList<>();
    Location[] saved_location=new Location[50];
    ArrayList<Location> location_list=new ArrayList<>();

    //지도 변수
    // NaverMap API 3.0
    private MapView mapView;
    private LocationButtonView locationButtonView;

    double longitude;
    double latitude;

    NaverMap naverMap;
    ArrayList<Marker> arrayList1=new ArrayList<>();

    // FusedLocationSource (Google)
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //맵 변수
        mapView = findViewById(R.id.main_map_view);
        mapView.onCreate(savedInstanceState);
        naverMapBasicSettings();
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            return;
        }

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                100, // 통지사이의 최소 시간간격 (miliSecond)
                1, // 통지사이의 최소 변경거리 (m)
                mLocationListener);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                100, // 통지사이의 최소 시간간격 (miliSecond)
                1, // 통지사이의 최소 변경거리 (m)
                mLocationListener);
        // Toast.makeText(practice.this,(int)longtitude,Toast.LENGTH_SHORT).show();

        //  lm.removeUpdates(mLocationListener);
        //여기까지


    }

    //맵 부분
    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.
            longitude = location.getLongitude(); //경도
            latitude = location.getLatitude();   //위도

            for(int i=0; i<longitude_list.size(); i++) {
                //지점들의 각 로케이션 객채 생성
                saved_location[i] = new Location("point" + i);
                saved_location[i].setLatitude(latitude_list.get(i));
                saved_location[i].setLongitude(longitude_list.get(i));
                location_list.add(saved_location[i]);

            }

            for(int i=0; i<location_list.size(); i++){
                //현재위치와 지점간의 거리가 2m이하일때
                if(location.distanceTo(location_list.get(i))<=2){
                    Toast.makeText(MapActivity.this, "도착", Toast.LENGTH_SHORT).show();
                }
            }


        }

        public void onProviderDisabled(String provider) {

        }

        public void onProviderEnabled(String provider) {
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

    };


    public void naverMapBasicSettings() {
        mapView.getMapAsync(this);

        //내위치 버튼
        locationButtonView = findViewById(R.id.locationbuttonview);
        // 내위치 찾기 위한 source
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onMapReady(@NonNull final NaverMap naverMap) {
        //naverMap.getUiSettings().setLocationButtonEnabled(true);
        this.naverMap = naverMap;
        //naverMap 크기
        /*
        LocationOverlay locationOverlay = naverMap.getLocationOverlay();
        Log.d("sangmin", locationOverlay.getPosition().toString());
        locationOverlay.setVisible(true);
        locationOverlay.setCircleRadius(100);
        locationOverlay.setCircleOutlineWidth(10);
        locationOverlay.setCircleOutlineColor(Color.BLACK);
        */
        locationButtonView.setMap(naverMap);

        // Location Change Listener을 사용하기 위한 FusedLocationSource 설정
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.NoFollow);
        //longtitude = locationSource.getLastLocation().getLongitude();
        //latitude = locationSource.getLastLocation().getLatitude();

        intent = getIntent();
        s_location = intent.getStringExtra("s_location");

        databaseReference = FirebaseDatabase.getInstance().getReference(s_location); // 변경값을 확인할 child 이름
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                Log.d("sangminkey", key);
                longitude_list.clear();
                latitude_list.clear();
                long n=dataSnapshot.getChildrenCount();
                Log.d("sangconfirm", ""+n);

                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    String data_key = messageData.getKey();
                    Log.d("sangmin", data_key);

                    String sLongitude = messageData.child("sLongitude").getValue().toString();
                    String sLatitude = messageData.child("sLatitude").getValue().toString();
                    Log.d("sangmin", sLongitude);
                    Log.d("sangmin", sLatitude);
                    double d_longitude = Double.parseDouble(sLongitude);
                    double d_latitude = Double.parseDouble(sLatitude);
                    longitude_list.add(new Double(d_longitude));
                    latitude_list.add(new Double(d_latitude));

                    Marker marker = new Marker();
                    latlng_list.add(new LatLng(d_latitude, d_longitude));
                    marker.setPosition((new LatLng(d_latitude, d_longitude)));

                    if (data_key.equals("지점0")) {

                        marker.setCaptionText(s_location + " 출발지");
                        marker.setCaptionTextSize(16);
                        marker.setCaptionColor(Color.BLUE);
                        marker.setCaptionAlign(Align.Top);
                        marker.setIconTintColor(Color.RED);
                    } else if (data_key.equals("지점"+(n-1))) {

                        marker.setCaptionText(s_location + " 도착지");
                        marker.setCaptionTextSize(16);
                        marker.setCaptionColor(Color.BLUE);
                        marker.setCaptionAlign(Align.Top);
                        marker.setIconTintColor(Color.BLUE);
                    } else {
                        marker.setCaptionText(data_key);
                    }
                    marker.setMap(naverMap);

                }


                //경로선 그리는 코드
                PathOverlay path = new PathOverlay();
                List<LatLng> coords = new ArrayList<>();
                for(int j=0; j<latlng_list.size(); j++){
                    Collections.addAll(coords, latlng_list.get(j));
                }

                path.setCoords(coords);
                path.setMap(naverMap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


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
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}
