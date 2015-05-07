package Fragments;

import java.util.ArrayList;


import Adapters.ListViewRaspAdapter;
import db.MyPrefs;
import db.MySQLiteClass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.raspisanie_proj.R;
import com.example.raspisanie_proj.rasp;

public class FragmentTomorrow extends Fragment {

    private static final String KEY = "FragmentHistory2";

    public static Fragment newInstance(int pos) {
        FragmentTomorrow fragment = new FragmentTomorrow();
        Bundle args = new Bundle();
        args.putInt(KEY, pos);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_tomorrow, container, false);
        ListView lv = (ListView) view.findViewById(R.id.MylistView);

        Intent intent = getActivity().getIntent();
        String station1 = intent.getStringExtra(MyPrefs.STATION_FROM);
        String station2 = intent.getStringExtra(MyPrefs.STATION_TO);

        MySQLiteClass mySQLiteClass = new MySQLiteClass(getActivity().getApplicationContext());

        mySQLiteClass.open(false);//Так сделанно потому, что я не знаю, почему на телефоне вылетает, а на эмуляторе нет
        ArrayList<String[]> list2 = mySQLiteClass.getScheduleEveryDay(station1, station2);
        mySQLiteClass.close();

        if (list2.get(0).length == 1)///***
        {
            View empty_view = inflater.inflate(R.layout.empty_list, null, false);

            TextView textViewMessage = (TextView) empty_view.findViewById(R.id.show_empty_set_tv);
            textViewMessage.setText(getResources().getString(R.string.nothing_to_show));

            view = empty_view;
        } else {
            ListViewRaspAdapter adapter = new ListViewRaspAdapter(getActivity(), rasp.getListRasp(list2));
            lv.setAdapter(adapter);
        }
        return view;

    }

}
