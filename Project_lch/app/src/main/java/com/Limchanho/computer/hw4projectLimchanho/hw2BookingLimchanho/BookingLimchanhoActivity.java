package com.Limchanho.computer.hw4projectLimchanho.hw2BookingLimchanho;



import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.Limchanho.computer.hw4projectLimchanho.common.DatabaseBroker;
import com.Limchanho.computer.hw4projectLimchanho.common.FilebaseBroker;
import com.Limchanho.computer.hw4projectLimchanho.common.Message;
import com.Limchanho.computer.hw4projectLimchanho.common.Settings;

import java.util.ArrayList;
import java.util.Calendar;

public class BookingLimchanhoActivity extends AppCompatActivity {

    Context context;
    private ArrayList<String> mData = new ArrayList<>();

    String[] bookingDatabase = null;
    Settings settingsDatabase = null;
    String userName;
    String userGroup;

    DatabaseBroker databaseBroker;
    Settings settings;
    String rootPath ="LimchanhoDb";
    Intent intent;
    View.OnClickListener onClickListener;

    LinearLayout linearLayout;
    BookingDrawer bookingDrawer;
    int maxSlots = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;


        linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        setContentView(linearLayout, layoutParams);

        intent=getIntent();
        rootPath=intent.getStringExtra("rootPath");
        databaseBroker = DatabaseBroker.createDatabaseObject(rootPath);
        userName=intent.getStringExtra("userName");
        userGroup=intent.getStringExtra("userGroup");
        setTitle("부킹 "+userGroup);
        //부킹데이터베이스 데이터를 불러오기
        databaseBroker.setBookingOnDataBrokerListener(this, userGroup, new DatabaseBroker.OnDataBrokerListener() {
            @Override
            public void onChange(String databaseStr) {
                bookingDatabase = databaseBroker.loadBookingDatabase(BookingLimchanhoActivity.this ,userGroup);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    //세로방향 (기본) 일 때

                    drawBase();
                } else {
                    //핸드폰이 가로방향이 되었을 때.
                    new_drawBase();
                }
                bookingDrawer = new BookingDrawer();
                bookingDrawer.start();
            }
        });

        databaseBroker.setSettingsOnDataBrokerListener(this, new DatabaseBroker.OnDataBrokerListener() {
            @Override
            public void onChange(String databaseStr) {
                settingsDatabase = databaseBroker.loadSettingsDatabase(BookingLimchanhoActivity.this);

                onClickListener = new View.OnClickListener() {
                    public void onClick(View view) {
                        MyTextView textView = (MyTextView) view;

                        if(bookingDatabase[textView.index] == "") {
                            int k = 0;
                            for(int i=0;i<maxSlots;i++){
                                if(bookingDatabase[i].equals(userName)){
                                    k++;
                                }
                            }
                            if(settingsDatabase.maxTotalBookingSlots == k){
                                Message.information(BookingLimchanhoActivity.this,"오류","최대슬롯 초과");
                            }else {
                                int j = 0;
                                for(int i=(textView.index-settingsDatabase.maxContinueBookingSlots);i<=(textView.index+settingsDatabase.maxContinueBookingSlots);i++){
                                    if(bookingDatabase[i].equals(userName) || i == textView.index){
                                        j++;
                                        if(j==(settingsDatabase.maxContinueBookingSlots+1))
                                            break;
                                    }else {
                                        j = 0;
                                    }
                                }
                                if(j<settingsDatabase.maxContinueBookingSlots+1){
                                    bookingDatabase[textView.index] = userName;
                                    databaseBroker.saveBookingDatabase(BookingLimchanhoActivity.this,userGroup,bookingDatabase);
                                }else {
                                    Message.information(BookingLimchanhoActivity.this,"오류","연속슬롯 초과");
                                }
                            }

                        }else {
                            if(bookingDatabase[textView.index].equals(userName)) {
                                bookingDatabase[textView.index] = "";
                                databaseBroker.saveBookingDatabase(BookingLimchanhoActivity.this,userGroup,bookingDatabase);
                            } else {
                                Message.information(BookingLimchanhoActivity.this,"오류","이미 다른 사용자가 예약중입니다.");
                            }
                        }
                    }

                };
            }
        });



    }


    void drawBase() {
        linearLayout.removeAllViews();
        for (int i = 0; i < maxSlots/2; i++) {

            LinearLayout outerLayout = new LinearLayout(context);
            outerLayout.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams outerLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            outerLayout.setWeightSum(2); //WeightSum(2) = 반반씩 단, 자식의 너비가 0dp여야함.
            linearLayout.addView(outerLayout, outerLp);
            for (int j = 0; j < 2; j++) {

                MyTextView textView = new MyTextView(context);

                //현재시간 받아서 저장하는것
                Calendar cal = Calendar.getInstance();
                int hournow = cal.get(Calendar.HOUR_OF_DAY);
                int minnow = cal.get(Calendar.MINUTE);

                //현재시간이랑 테이블시간이랑 비교해서 회색으로칠하고 온클릭리스너 안넣어주는것
                if (i<hournow||i==hournow&&j*30<minnow)
                    textView.setBackgroundColor(Color.GRAY);
                if(i<hournow||i==hournow&&j*30<minnow){}
                else
                    textView.setOnClickListener(onClickListener);

                //00:00 나타내기
                String hour = String.format("%02d", i);
                String min = String.format("%02d", j * 30);

                //시간+부킹정보
                textView.index = i * 2 + j;
                if (bookingDatabase[i * 2 + j] == null)
                    textView.setText(hour + ":" + min);
                else
                    textView.setText(hour + ":" + min + "        " + bookingDatabase[i * 2 + j]);

                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                //textView에 padding 설정
                textView.setPadding(45, 0, 0, 0);
                //textView에 margin 설정
                layoutParams.setMargins(10,4,10,4);
                outerLayout.addView(textView, layoutParams);
            }
        }
    }

    void new_drawBase() {
        linearLayout.removeAllViews();

        for (int i = 0; i < maxSlots/4+1; i++) {

            LinearLayout outerLayout = new LinearLayout(context);
            outerLayout.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams outerLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            outerLayout.setWeightSum(4); //WeightSum(2) = 반반씩 단, 자식의 너비가 0dp여야함.
            linearLayout.addView(outerLayout, outerLp);
            for (int j = 0; j < 4; j++) {

                MyTextView textView = new MyTextView(context);

                //현재시간 받아서 저장하는것
                Calendar cal = Calendar.getInstance();
                int hournow = cal.get(Calendar.HOUR_OF_DAY);
                int minnow = cal.get(Calendar.MINUTE);

                //현재시간이랑 테이블시간이랑 비교해서 회색으로칠하고 온클릭리스너 안넣어주는것
                String hour = null;
                String min = null;
                //00:00 나타내기
                if (j == 0 || j == 1) {
                    hour = String.format("%02d", i * 2);
                    min = String.format("%02d", j * 30);
                } else if (j == 2 || j == 3) {
                    hour = String.format("%02d", i * 2 + 1);
                    min = String.format("%02d", ((j * 30) - 60));
                }

                if (Integer.parseInt(hour)<hournow||Integer.parseInt(hour)==hournow&&Integer.parseInt(min)<minnow)
                    textView.setBackgroundColor(Color.GRAY);
                if(Integer.parseInt(hour)<hournow||Integer.parseInt(hour)==hournow&&Integer.parseInt(min)<minnow){}
                else
                    textView.setOnClickListener(onClickListener);


                //시간+부킹정보
                textView.index = i * 4 + j;
                if(i!=12) {
                    if (bookingDatabase[i * 4 + j] == null)
                        textView.setText(hour + ":" + min);
                    else
                        textView.setText(hour + ":" + min + "        " + bookingDatabase[i * 4 + j]);
                }else {
                    if (j == 0 || j == 1) {
                        if (bookingDatabase[i * 4 + j] == null)
                            textView.setText(hour + ":" + min);
                        else
                            textView.setText(hour + ":" + min + "        " + bookingDatabase[i * 4 + j]);
                    }
                    else if (j == 2 || j == 3) {
                        textView.setText("");
                        textView.setOnClickListener(null);
                    }
                }

                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                //textView에 padding 설정
                textView.setPadding(45, 0, 0, 0);
                //textView에 margin 설정
                layoutParams.setMargins(10,4,10,4);
                outerLayout.addView(textView, layoutParams);
            }
        }
    }

    // 클릭하면 접속자 이름 집어넣어야됨    index값 불러올수 없어서 밑에처럼 상속



    class MyTextView extends android.support.v7.widget.AppCompatTextView {
        int index;

        public MyTextView(Context context) {
            super(context);
        }
    }

    /* 이부분이 thread라서 시간이 지나면 회색되고 작동안하게 하는거 팁준 부분
      linearLayout.removeAllViews(); 이거 화면 지우는거 참고*/

    class BookingDrawer extends Thread {
        boolean isRun = true;

        @Override
        public void run() {
            super.run();
            while (isRun) {

                try {
                    sleep(1000);
                } catch (InterruptedException e) {

                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                            //세로방향 (기본) 일 때

                            drawBase();
                        } else {
                            //핸드폰이 가로방향이 되었을 때.
                            new_drawBase();
                        }
                    }
                });

            }
        }
    }
}
