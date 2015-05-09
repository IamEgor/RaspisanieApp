package com.example.raspisanie_proj;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import Adapters.ListViewRaspAdapter;
import db.MyPrefs;
import db.MySQLiteClass;

/**
 * Created by Егор on 07.05.2015.
 */
public class RaspOneStationActivity extends FragmentActivity {
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_today);

        listView = (ListView) findViewById(R.id.MylistView);

        Intent intent =getIntent();
        String station = intent.getStringExtra(MyPrefs.STATION_SINGLE);

        MySQLiteClass mySQLiteClass = new MySQLiteClass(RaspOneStationActivity.this);
        mySQLiteClass.open(false);//Так сделанно потому, что я не знаю, почему на телефоне вылетает, а на эмуляторе нет
        ArrayList<String[]> list = mySQLiteClass.getScheduleForOneStation(station);
        mySQLiteClass.close();


        if (list.get(0).length == 1)///***
        {
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View empty_view = inflater.inflate(R.layout.empty_list, null, false);

            TextView textViewMessage = (TextView) empty_view.findViewById(R.id.show_empty_set_tv);
            textViewMessage.setText(getResources().getString(R.string.nothing_to_show));

            setContentView(empty_view);////svsdvsdvsdv
        } else {
            ListViewRaspAdapter adapter = new ListViewRaspAdapter(RaspOneStationActivity.this, rasp.getListRasp(list));
            listView.setAdapter(adapter);
        }

    }
}
