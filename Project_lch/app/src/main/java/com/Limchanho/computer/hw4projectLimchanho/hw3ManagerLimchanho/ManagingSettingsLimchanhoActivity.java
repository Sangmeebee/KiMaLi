package com.Limchanho.computer.hw4projectLimchanho.hw3ManagerLimchanho;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.Limchanho.computer.hw4projectLimchanho.R;
import com.Limchanho.computer.hw4projectLimchanho.common.DatabaseBroker;
import com.Limchanho.computer.hw4projectLimchanho.common.Message;
import com.Limchanho.computer.hw4projectLimchanho.common.Settings;

import java.util.ArrayList;

public class ManagingSettingsLimchanhoActivity extends AppCompatActivity {

    ArrayList<String> groupDatabase;
    Intent intent;
    Spinner spinner1;
    Spinner spinner2;
    DatabaseBroker databaseBroker;
    Settings settingsDatabase;
    String timelist[] = {"00:30","01:00","01:30","02:00","02:30","03:00","03:30","04:00","04:30","05:00","05:30","06:00"
            ,"06:30","07:00","07:30","08:00","08:30","09:00","09:30","10:00","10:30"};
    String rootPath = "LimchanhoDb";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managing_settings_limchanho);
        setTitle("설정관리");

        databaseBroker = DatabaseBroker.createDatabaseObject(rootPath);
        databaseBroker.setSettingsOnDataBrokerListener(this,settinglistener);

        spinner1 = findViewById(R.id.spinner1);
        spinner1.setEnabled(false);
        spinner2 = findViewById(R.id.spinner2);
        spinner2.setEnabled(false);
        /*
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ManagingSettingsLimchanhoActivity.this,
                android.R.layout.simple_list_item_1, timelist);
        spinner1.setAdapter(adapter);
        spinner1.setSelection((settingsDatabase.maxContinueBookingSlots - 1));
        spinner1.setEnabled(true);
        spinner2.setAdapter(adapter);
        spinner2.setSelection((settingsDatabase.maxTotalBookingSlots - 1));
        spinner2.setEnabled(true);
        */

        // 스피너1에서 아이템 선택 시 해당하는 아이템에 맞는 정보를 settingdatabase에 저장하는 방법
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > settingsDatabase.maxTotalBookingSlots - 1) {
                    Message.information(ManagingSettingsLimchanhoActivity.this, "에러",
                            "총 선택 수 보다 연속 선택 수가 높습니다. 다시 선택해주세요");
                    spinner1.setSelection((settingsDatabase.maxContinueBookingSlots - 1));
                } else {
                    settingsDatabase.maxContinueBookingSlots = position + 1;
                    databaseBroker.saveSettingsDatabase(ManagingSettingsLimchanhoActivity.this, settingsDatabase);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 스피너2에서 아이템 선택 시 해당하는 아이템에 맞는 정보를 settingdatabase에 저장하는 방법
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < settingsDatabase.maxContinueBookingSlots - 1) {
                    Message.information(ManagingSettingsLimchanhoActivity.this, "에러",
                            "연속 선택 수 보다 총 선택 수가 작습니다. 다시 선택해주세요");
                    spinner2.setSelection((settingsDatabase.maxTotalBookingSlots - 1));
                }
                settingsDatabase.maxTotalBookingSlots = position + 1;
                databaseBroker.saveSettingsDatabase(ManagingSettingsLimchanhoActivity.this, settingsDatabase);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    // 메뉴 만든 곳
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.group_screen:
                intent = new Intent(this, ManagingGroupLimchanhoActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.user_screen:
                intent = new Intent(this, ManagingUserLimchanhoActivity.class);
            //    intent.putExtra("groupdatabase", groupDatabase);
                startActivity(intent);
                finish();
                return true;
            case R.id.setting_screen:
                intent = new Intent(this, ManagingSettingsLimchanhoActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //세팅데이터베이스 데이터를 불러오기
        DatabaseBroker.OnDataBrokerListener settinglistener = new DatabaseBroker.OnDataBrokerListener()  {
        @Override
        public void onChange(String databaseStr) {
            settingsDatabase = databaseBroker.loadSettingsDatabase(ManagingSettingsLimchanhoActivity.this);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(ManagingSettingsLimchanhoActivity.this,
                    android.R.layout.simple_list_item_1, timelist);
            spinner1.setAdapter(adapter);
            spinner1.setSelection((settingsDatabase.maxContinueBookingSlots - 1));
            spinner1.setEnabled(true);
            spinner2.setAdapter(adapter);
            spinner2.setSelection((settingsDatabase.maxTotalBookingSlots - 1));
            spinner2.setEnabled(true);
        }
    };
}
