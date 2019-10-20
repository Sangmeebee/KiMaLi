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

import com.Limchanho.computer.hw4projectLimchanho.R;
import com.Limchanho.computer.hw4projectLimchanho.common.DatabaseBroker;
import com.Limchanho.computer.hw4projectLimchanho.common.Message;
import com.Limchanho.computer.hw4projectLimchanho.common.User;

import java.util.ArrayList;

public class ManagingGroupLimchanhoActivity extends AppCompatActivity {

    ArrayList<String> groupDatabase;
    ArrayList<User> userDatabase;
    DatabaseBroker databaseBroker;
    ListView list;
    Intent intent;
    String strText;
    String rootPath = "LimchanhoDb";
    User userdata;
    int confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managing_group_limchanho);
        setTitle("그룹관리");

        list = (ListView) findViewById(R.id.list);

        databaseBroker = DatabaseBroker.createDatabaseObject(rootPath);
        databaseBroker.setGroupOnDataBrokerListener(this, onGroupListener);
        databaseBroker.setUserOnDataBrokerListener(this, onUserListener);

        // + 버튼 클릭시 발생될 이벤트 처리
        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_show();
            }
        });

        //리스트뷰 아이템롱클릭시 발생될 이벤트 처리
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                strText = (String) parent.getItemAtPosition(position);
                longclick_show();
                return false;
            }
        });
    }

    // + 버튼 눌렀을때 실행되는 곳
    void add_show() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("그룹생성");
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.add_group, null);
        builder.setView(view);

        final EditText editText = (EditText) view.findViewById(R.id.edittext);

        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newgroup = editText.getText().toString();
                confirm=0;
                for(int i=0;i<groupDatabase.size();i++) {
                    String element = groupDatabase.get(i).toString();
                    if (element.equals(newgroup) == true)
                        confirm++;
                }
                if(confirm==0){
                    groupDatabase.add(newgroup);
                    databaseBroker.saveGroupDatabase(ManagingGroupLimchanhoActivity.this,groupDatabase);
                }else{
                    Message.information(ManagingGroupLimchanhoActivity.this,"경고","동일한 그룹이 있습니다.");
                }


            }
        });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }
    // 리스트뷰 아이템 클릭시 삭제할건지 묻는 알트다이얼로그
    void longclick_show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알림");
        builder.setMessage("'" + strText + "' 그룹을 정말로 삭제하기 원하십니까?");

        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i=userDatabase.size()-1;i>0;i--){
                            if(strText.equals(userDatabase.get(i).userGroup)){//현재 있는 사용자의 그룹들 중 제거한 그룹이 있는지 체크
                                userDatabase.remove(i);

                                databaseBroker.saveUserDatabase(ManagingGroupLimchanhoActivity.this,userDatabase);
                            }
                        }

                        groupDatabase.remove(strText);
                        databaseBroker.saveGroupDatabase(ManagingGroupLimchanhoActivity.this,groupDatabase);

                    }
                });

        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }





    // 데이터베이스에서 그룹데이터베이스 정보 읽어와서 리스트에 띄우는 곳
        DatabaseBroker.OnDataBrokerListener onGroupListener = new DatabaseBroker.OnDataBrokerListener() {
            @Override
            public void onChange(String databaseStr) {
                groupDatabase = databaseBroker.loadGroupDatabase(ManagingGroupLimchanhoActivity.this);
                userDatabase = databaseBroker.loadUserDatabase(ManagingGroupLimchanhoActivity.this);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ManagingGroupLimchanhoActivity.this,
                        android.R.layout.simple_list_item_1, groupDatabase);
                list.setAdapter(adapter);

            }
        };

    DatabaseBroker.OnDataBrokerListener onUserListener = new DatabaseBroker.OnDataBrokerListener() {
        @Override
        public void onChange(String databaseStr) {
            userDatabase = databaseBroker.loadUserDatabase(ManagingGroupLimchanhoActivity.this) ;
        }
    };


    //메뉴 부분 구현
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
