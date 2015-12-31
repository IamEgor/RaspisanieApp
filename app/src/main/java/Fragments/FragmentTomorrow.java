package Fragments;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.raspviewproj.AlarmSave;
import com.example.raspviewproj.DateTimePicker;
import com.example.raspviewproj.DateTimePicker.DateWatcher;
import com.example.raspviewproj.MainActivity;
import com.example.raspviewproj.NotificationReciever;
import com.example.raspviewproj.R;
import com.example.raspviewproj.RaspisanieContent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Adapters.ListViewRaspAdapter;
import db.MyPrefs;
import db.MySQLiteClass;

public class FragmentTomorrow extends Fragment {

    private static final String KEY = "FragmentHistory2";
    private String mCity;

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
        mCity = station1 + " - " + station2;
        MySQLiteClass mySQLiteClass = new MySQLiteClass(getActivity().getApplicationContext());

        mySQLiteClass.open(false);//Так сделанно потому, что я не знаю, почему на телефоне вылетает, а на эмуляторе нет
        ArrayList<String[]> list2 = mySQLiteClass.getScheduleEveryDay(station1, station2);
        mySQLiteClass.close();

        if (list2.size() == 0)///***
        {
            View empty_view = inflater.inflate(R.layout.empty_list, null, false);

            TextView textViewMessage = (TextView) empty_view.findViewById(R.id.show_empty_set_tv);
            textViewMessage.setText(getResources().getString(R.string.nothing_to_show));

            view = empty_view;
        } else {
            ListViewRaspAdapter adapter = new ListViewRaspAdapter(getActivity(), RaspisanieContent.getListRasp(list2));
            lv.setAdapter(adapter);
        }

        lv.setLongClickable(true);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {
                // TODO Auto-generated method stub

                final View currentView = v;

                Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                final RelativeLayout mDateTimeDialogView = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.dialog, null);
                // Grab widget instance
                final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView.findViewById(R.id.DateTimePicker);
                DateWatcher s = null;
                mDateTimePicker.setDateChangedListener(s);
                alertDialogBuilder.setView(mDateTimeDialogView);

                alertDialogBuilder.setTitle("Добавить напоминание");


                alertDialogBuilder.setPositiveButton("Да",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {

                                int mYear = mDateTimePicker.getYear();
                                int mMonth = mDateTimePicker.getMonth();
                                int mDay = mDateTimePicker.getDay();
                                int mHour = mDateTimePicker.getHour();
                                int mMinute = mDateTimePicker.getMinute();

                                TextView Text = (TextView) currentView.findViewById(R.id.textView_Out);
                                String timeLeave = Text.getText().toString();


                                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.ms").format(new Date());
                                Uri uri = Uri.parse(date);

                                String temp = new String(mCity);

                                MakeAlarm(mYear, mMonth, mDay, mHour, mMinute, uri, timeLeave, temp);

                            }
                        }
                );

                alertDialogBuilder.setNegativeButton("Нет",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {

                            }

                        }
                );

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                return true;
            }
        });
        
        
        
        
        return view;

    }

    private void MakeAlarm(int year, int month, int day, int hour, int minute, Uri uri, String timeLeave, String City) {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Intent intent = new Intent(MainActivity.getAppContext(), NotificationReciever.class);
        intent.setData(uri);

        PendingIntent sender = PendingIntent.getBroadcast(
                MainActivity.getAppContext(), 0, intent, 0);

        AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);

        String timeAlarm = year + "/" + month + "/" + day + " в " + hour + ":" + minute;

        AlarmSave.get(getActivity()).put(uri.toString(), City, timeLeave, timeAlarm);
     
        
    	
    }

}
