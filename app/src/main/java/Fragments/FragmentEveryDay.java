package Fragments;

import java.util.ArrayList;
import java.util.Calendar;

import Adapters.ListViewRaspAdapter;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raspviewproj.NotificationReciever;
import com.example.raspviewproj.R;
import com.example.raspviewproj.RaspisanieContent;

import db.MyPrefs;
import db.MySQLiteClass;

public class FragmentEveryDay extends Fragment {

    private static final String KEY = "FragmentHistory2";

    public static Fragment newInstance(int pos) {
        FragmentEveryDay fragment = new FragmentEveryDay();
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

        if (list2.size() == 0) {
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
                final EditText edittext = new EditText(getActivity());
                edittext.setHint("0-60 минут");
                alertDialogBuilder.setTitle("Напоминалочка");
                alertDialogBuilder.setMessage("За сколько минут напомнить");

                alertDialogBuilder.setView(edittext);
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {

                                String YouEditTextValue = edittext.getText().toString();
                                TextView Text = (TextView) currentView.findViewById(R.id.textView_Out);

                                int minute = getMinute(Text);
                                int hour = getHour(Text);
                                int edit = Integer.parseInt(YouEditTextValue);

                                minute = minute - edit;

                                if (minute < 0) {
                                    hour = hour - 1;
                                    minute = 60 + minute;
                                }

                                restartNotify(hour, minute, edit);

                                Toast.makeText(getActivity(), "long click : " + hour + "  " + minute, Toast.LENGTH_LONG).show();

                            }

                        }
                );

                alertDialogBuilder.setNegativeButton("No",
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

    private int getHour(TextView Text) {

        String TimeView = (String) Text.getText();
        int start = 0;
        int end = 2;

        char[] buf = new char[end - start];
        TimeView.getChars(start, end, buf, 0);
        String s = new String(buf);
        int hour = Integer.parseInt(s);

        return hour;
    }

    private int getMinute(TextView Text) {

        String TimeView = (String) Text.getText();
        int start = 0;
        int end = 2;

        int start_minute = 3;
        int end_minute = 5;

        char[] buf = new char[end - start];
        TimeView.getChars(start_minute, end_minute, buf, 0);
        String s1 = new String(buf);
        int Minute = Integer.parseInt(s1);


        return Minute;
    }

    private void restartNotify(int hour, int minute, int edit) {

        Log.d("RESTART_NOTIFY", "11111");
        AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), NotificationReciever.class);
        intent.putExtra("Edit", edit);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),
                0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar calendar = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();

        calendar.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

}
