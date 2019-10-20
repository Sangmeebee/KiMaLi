package com.Limchanho.computer.hw4projectLimchanho.hw3ManagerLimchanho;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.Limchanho.computer.hw4projectLimchanho.R;
import com.Limchanho.computer.hw4projectLimchanho.common.DatabaseBroker;
import com.Limchanho.computer.hw4projectLimchanho.common.Message;
import com.Limchanho.computer.hw4projectLimchanho.common.User;

import java.util.ArrayList;

public class ManagingUserLimchanhoActivity extends AppCompatActivity {

    //선언
    Intent intent;
    DatabaseBroker databaseBroker;
    ListView user_list;
    ArrayList<User> userDatabase;
    ArrayList<String> groupDatabase;
    User user_text;
    User userdata;
    int confirm;
    String rootPath = "LimchanhoDb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managing_user_limchanho);
        setTitle("사용자관리");

        //UserActivity의 listView
        user_list = (ListView) findViewById(R.id.user_list);

        databaseBroker = DatabaseBroker.createDatabaseObject(rootPath);
        databaseBroker.setUserOnDataBrokerListener(this, onUserListener);
        databaseBroker.setGroupOnDataBrokerListener(this, onGroupListener);


        //+버튼 누르면 다이얼로그 띄어주는 함수 포함한 리스너
        Button user_btn = (Button) findViewById(R.id.user_button);
        user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_dialog_show();
            }
        });


        //길게 클릭해서 텍스트 지우는 함수를 포함하는 리스너
        user_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ManagingUserLimchanhoActivity.this);
                builder.setTitle("알림");
                builder.setMessage("정말로 삭제하기 원하십니까?");
                builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                userDatabase.remove(position);
                                databaseBroker.saveUserDatabase(ManagingUserLimchanhoActivity.this,userDatabase);
                            }
                        });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
                return false;
            }
        });


    }

    // +버튼 눌러서 실행시키는 함수
    void user_dialog_show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("사용자생성");
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.add_user, null);
        builder.setView(view);
        final Spinner user_group_spinner=(Spinner)view.findViewById(R.id.user_group_spinner);
        final EditText id_edit=view.findViewById(R.id.user_id);
        final EditText pw_edit=view.findViewById(R.id.user_pw);
        ArrayAdapter<String> user_group_adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, groupDatabase);
        user_group_spinner.setAdapter(user_group_adapter);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String id=id_edit.getText().toString();
                String pw=pw_edit.getText().toString();
                String group=user_group_spinner.getSelectedItem().toString();

                User user = new User(id, pw, group);
                confirm =0;
                for(int i=0;i<userDatabase.size();i++){
                    userdata = userDatabase.get(i);
                    String a = userdata.userName;
                    if(a.equals(id)==true)
                        confirm++;
                }
                if(confirm ==0){
                    userDatabase.add(user);
                    userdata = userDatabase.get(1);
                    databaseBroker.saveUserDatabase(ManagingUserLimchanhoActivity.this, userDatabase);
                }else{
                    Message.information(ManagingUserLimchanhoActivity.this,"경고","동일한 유저가 있습니다.");
                }

            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }




    DatabaseBroker.OnDataBrokerListener onGroupListener = new DatabaseBroker.OnDataBrokerListener() {
        @Override
        public void onChange(String databaseStr) {
            groupDatabase = databaseBroker.loadGroupDatabase(ManagingUserLimchanhoActivity.this);
        }
    };



    //DatabaseBroker에 있는 loadUserDatabase 함수를 통해 데이터를 받아서 userDatabase 채우기
    DatabaseBroker.OnDataBrokerListener onUserListener = new DatabaseBroker.OnDataBrokerListener() {
        @Override
        public void onChange(String databaseStr) {
            userDatabase = databaseBroker.loadUserDatabase(ManagingUserLimchanhoActivity.this);

            MyList[] myList = new MyList[userDatabase.size()];

            for (int i = 0; i < userDatabase.size(); i++) {
                user_text = userDatabase.get(i);
                myList[i] = new MyList(user_text.userName, user_text.userPassword, user_text.userGroup);
            }
            CustomAdapter myAdapter = new CustomAdapter(ManagingUserLimchanhoActivity.this, myList);

            user_list.setAdapter(myAdapter);
        }
    };




    // 메뉴바 만들어서 각 메뉴를 눌렀을때 해당하는 엑티비티 실행시키는 곳
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
             //   intent.putExtra("groupdatabase", groupDatabase);
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
}

