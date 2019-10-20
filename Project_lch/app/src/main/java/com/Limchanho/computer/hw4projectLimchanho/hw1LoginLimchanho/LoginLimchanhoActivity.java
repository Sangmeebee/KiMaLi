package com.Limchanho.computer.hw4projectLimchanho.hw1LoginLimchanho;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.Limchanho.computer.hw4projectLimchanho.R;
import com.Limchanho.computer.hw4projectLimchanho.common.Message;
import com.Limchanho.computer.hw4projectLimchanho.common.User;
import com.Limchanho.computer.hw4projectLimchanho.hw2BookingLimchanho.BookingLimchanhoActivity;
import com.Limchanho.computer.hw4projectLimchanho.hw3ManagerLimchanho.ManagingGroupLimchanhoActivity;
import com.Limchanho.computer.hw4projectLimchanho.common.DatabaseBroker;
import com.Limchanho.computer.hw4projectLimchanho.hw3ManagerLimchanho.ManagingUserLimchanhoActivity;

import java.util.ArrayList;

public class LoginLimchanhoActivity extends AppCompatActivity {
    ArrayList<String> groupDatabase;
    ArrayList<User> userDatabase;
    DatabaseBroker databaseBroker;
    Spinner spinner;
    User user[];
    User correct_user;
    Button enter_button, password_change_btn, close_btn, enter_btn, newuser_btn;
    EditText user_text;
    EditText password;
    RadioButton manager_button, user_button;
    Intent intent;
    String[] user_login;
    String rootPath;
    User userdata;
    int confirm;
    int count;
    int check1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_limchanho);

        RadioGroup group=(RadioGroup)findViewById(R.id.radio_group);
        manager_button=(RadioButton)findViewById(R.id.manager_radioButton);
        user_button=(RadioButton)findViewById(R.id.user_radioButton);
        user_text=findViewById(R.id.user_id);
        password=findViewById(R.id.user_password);


        newuser_btn = (Button)findViewById(R.id.newuser_button);
        password_change_btn=(Button)findViewById(R.id.change_password);
        close_btn=(Button)findViewById(R.id.exit_button);
        enter_btn=(Button)findViewById(R.id.enter_button);

        spinner = findViewById(R.id.spinner);
        spinner.setEnabled(false);
        enter_button = findViewById(R.id.enter_button);
        enter_button.setOnClickListener(onClickListener);
        enter_button.setEnabled(false);
        rootPath="LimchanhoDb";
        user_login = new String[3];


        //사용자, 관리자 라디오버튼 이벤트
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.user_radioButton:
                        user_text.setEnabled(true);
                        user_text.requestFocus();
                        password.setText("");
                        break;
                    case R.id.manager_radioButton:
                        user_text.setText("root");
                        user_text.setEnabled(false);
                        password.requestFocus();
                        password.setText("");
                        break;
                }
            }
        });


        // 회원가입 버튼
        newuser_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_user();
            }

        });

        //끝내기버튼 이벤트
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //확인버튼
        enter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_login[0] = user_text.getText().toString();
                user_login[1] = password.getText().toString();
                user_login[2] = spinner.getSelectedItem().toString();

                user = new User[userDatabase.size()];
                for (int i = 0; i < userDatabase.size(); i++) {
                    user[i] = new User(userDatabase.get(i).toString());
                }


                if (manager_button.isChecked()) {
                    if (user[0].isMeByName(user_login[0]) && user[0].isMeByPassword(user_login[1])) {
                        intent = new Intent(LoginLimchanhoActivity.this, ManagingGroupLimchanhoActivity.class);
                        intent.putExtra("rootPath", rootPath);
                        startActivity(intent);
                    }
                    if (user[0].isMeByName(user_login[0]) && !user[0].isMeByPassword(user_login[1])) {
                        Message.information(LoginLimchanhoActivity.this, "경고", "비밀번호가 불일치합니다.");
                    }
                    if (!user[0].isMeByName(user_login[0]) && user[0].isMeByPassword(user_login[1])) {
                        Message.information(LoginLimchanhoActivity.this, "경고", "등록되지 않은 사용자입니다.");
                    }
                }
                check1 = 0;
                if (user_button.isChecked()) {
                    for (int i = 1; i < userDatabase.size(); i++) {
                        if (user[i].isMeByName(user_login[0]) && user[i].isMeByPassword(user_login[1]) && user[i].isMeByGroup(user_login[2])) {
                            intent = new Intent(LoginLimchanhoActivity.this, BookingLimchanhoActivity.class);
                            intent.putExtra("userName", user[i].userName);
                            intent.putExtra("userGroup", user[i].userGroup);
                            intent.putExtra("rootPath", rootPath);
                            startActivity(intent);
                            check1 = 1;
                        }
                    }
                    if (check1 == 0) {
                        for (int i = 1; i < userDatabase.size(); i++) {
                            if (user[i].isMeByName(user_login[0])) {
                                if (user[i].isMeByPassword(user_login[1])) {
                                    Message.information(LoginLimchanhoActivity.this, "경고", "소속그룹이 불일치합니다.");
                                    check1 = 1;
                                } else {
                                    Message.information(LoginLimchanhoActivity.this, "경고", "비밀번호가 불일치합니다.");
                                    check1 = 1;
                                }
                            }
                        }
                    }
                    if (check1 == 0) {
                        Message.information(LoginLimchanhoActivity.this, "경고", "아이디가 불일치합니다.");
                    }
                }
            }
            });

        //비밀번호 변경 버튼
        password_change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_login[0]=user_text.getText().toString();
                user_login[1]=password.getText().toString();
                user_login[2]=spinner.getSelectedItem().toString();

                user=new User[userDatabase.size()];
                for(int i=0; i<userDatabase.size(); i++){
                    user[i]=new User(userDatabase.get(i).toString());
                }
                if(manager_button.isChecked()) {
                    if (user[0].isMeByName(user_login[0]) && user[0].isMeByPassword(user_login[1])) {
                        correct_user=user[0];
                        password_change_dialog_show();
                    }

                    else if(user[0].isMeByName(user_login[0])&&!user[0].isMeByPassword(user_login[1])){
                        Message.information(LoginLimchanhoActivity.this, "경고", "비밀번호가 불일치합니다.");
                    }
                    else {
                        Message.information(LoginLimchanhoActivity.this, "경고", "등록되지 않은 사용자입니다.");
                    }
                }

                if(user_button.isChecked()) {
                    for (int i = 1; i < userDatabase.size(); i++) {
                        if (user[i].isMeByName(user_login[0]) && user[i].isMeByPassword(user_login[1]) && user[i].isMeByGroup(user_login[2])) {
                            correct_user=user[i];
                            password_change_dialog_show();
                        }
                        else if (user[i].isMeByName(user_login[0]) && user[i].isMeByPassword(user_login[1])) {
                            Message.information(LoginLimchanhoActivity.this, "경고", "소속그룹이 불일치합니다.");
                        }
                        else if (user[i].isMeByName(user_login[0]) && user[i].isMeByGroup(user_login[2])) {
                            Message.information(LoginLimchanhoActivity.this, "경고", "비밀번호가 불일치합니다.");
                        }
                        else if (user[i].isMeByPassword(user_login[1]) && user[i].isMeByGroup(user_login[2])) {
                            Message.information(LoginLimchanhoActivity.this, "경고", "등록되지 않은 사용자입니다.");
                        }
                        else if (user[i].isMeByName(user_login[0])) {
                            Message.information(LoginLimchanhoActivity.this, "경고", "비밀번호가 불일치합니다.");
                        }
                    }
                }
            }
        });



        //databaseBroker 인스턴스 생성
        databaseBroker = DatabaseBroker.createDatabaseObject(rootPath);
        databaseBroker.setGroupOnDataBrokerListener(this, onGroupListener);
        databaseBroker.setUserOnDataBrokerListener(this, onUserListener);

        //preference
        if(savedInstanceState == null) {
            SharedPreferences prefs = getSharedPreferences("save",0);

            String id = prefs.getString("id","");
            String pw = prefs.getString("pw","");
            boolean manager = prefs.getBoolean("manager",false);
            boolean user = prefs.getBoolean("user",false);
            user_text.setText(id);
            password.setText(pw);
            manager_button.setChecked(manager);
            user_button.setChecked(user);


        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences prefs = getSharedPreferences("save",0);
        SharedPreferences.Editor editor = prefs.edit();
        String id = user_text.getText().toString();
        String pw = password.getText().toString();
        boolean manager = manager_button.isChecked();
        boolean user = user_button.isChecked();

        editor.putString("id",id);
        editor.putString("pw",pw);
        editor.putBoolean("manager",manager);
        editor.putBoolean("user",user);
        editor.apply();
    }


    void create_user() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("사용자생성");
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.add_user, null);
        builder.setView(view);
        final Spinner user_group_spinner=(Spinner)view.findViewById(R.id.user_group_spinner);
        final EditText id_edit=view.findViewById(R.id.user_id);
        final EditText pw_edit=view.findViewById(R.id.user_pw);
        groupDatabase = databaseBroker.loadGroupDatabase(LoginLimchanhoActivity.this);
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
                    databaseBroker.saveUserDatabase(LoginLimchanhoActivity.this, userDatabase);
                }else{
                    Message.information(LoginLimchanhoActivity.this,"경고","동일한 유저가 있습니다.");
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




    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int selected = spinner.getSelectedItemPosition();
            Toast.makeText(LoginLimchanhoActivity.this, groupDatabase.get(selected), Toast.LENGTH_SHORT).show();

        }
    };

    //그룹데이터베이스 데이터를 불러온 후 스피너에 어댑터형식으로 세팅하기
    DatabaseBroker.OnDataBrokerListener onGroupListener = new DatabaseBroker.OnDataBrokerListener() {
        @Override
        public void onChange(String databaseStr) {
            groupDatabase = databaseBroker.loadGroupDatabase(LoginLimchanhoActivity.this);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(LoginLimchanhoActivity.this,
                    android.R.layout.simple_list_item_1, groupDatabase);
            spinner.setAdapter(adapter);
            spinner.setEnabled(true);
            enter_button.setEnabled(true);

        }
    };

    //유저데이터베이스 데이터를 불러오기
    DatabaseBroker.OnDataBrokerListener onUserListener = new DatabaseBroker.OnDataBrokerListener() {
        @Override
        public void onChange(String databaseStr) {
            userDatabase = databaseBroker.loadUserDatabase(LoginLimchanhoActivity.this);
        }
    };


    void password_change_dialog_show() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("'"+correct_user.userName+"'의 비밀번호 변경");
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_login_limchanho, null);
        builder.setView(view);

        //Dialog의 EditText를 불러옴(EditText에 쓴 text를 얻기 위해)
        final EditText new_pw = (EditText) view.findViewById(R.id.new_pw);
        final EditText new_pw_enter = (EditText) view.findViewById(R.id.new_pw_enter);

        //Dialog의 확인 버튼을 클릭 했을 때 이벤트
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String new_password = new_pw.getText().toString();
                        String new_password_enter = new_pw_enter.getText().toString();
                        if(new_password.equals(new_password_enter)){
                            for(int i=0;i<userDatabase.size();i++) {
                                User element = userDatabase.get(i);
                                if(element.userName.equals(correct_user.userName)){
                                    element.userPassword=new_password;
                                }
                            }
                        }
                        else{
                            Message.information(LoginLimchanhoActivity.this, "경고", "비밀번호가 동일하지 않습니다.");
                        }
                        //groupDatabase에 deitText에 쓴 text를 저장한다.

                        databaseBroker.saveUserDatabase(LoginLimchanhoActivity.this,userDatabase);

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
}
