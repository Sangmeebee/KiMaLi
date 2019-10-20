package com.Limchanho.computer.hw4projectLimchanho.hw3ManagerLimchanho;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.Limchanho.computer.hw4projectLimchanho.R;


public class CustomAdapter extends BaseAdapter {


    Context context;
    MyList[] myList;

    public CustomAdapter(Context context, MyList[] myList) {
        this.context = context;
        this.myList = myList;
    }

    @Override
    public int getCount() {
        return myList.length;
    }

    @Override
    public Object getItem(int position) {
        return myList[position];
    }

    @Override
    public long getItemId(int position) {
        return (long)position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        convertView=layoutInflater.inflate(R.layout.listitem_userdatabase, null);
        MyList my_list=myList[position];
        TextView textViewId=convertView.findViewById(R.id.Id);
        TextView textViewPw=convertView.findViewById(R.id.Pw);
        TextView textViewGroup=convertView.findViewById(R.id.Group);

        textViewId.setText(my_list.id);
        textViewPw.setText(my_list.pw);
        textViewGroup.setText(my_list.group);
        return convertView;
    }
}
