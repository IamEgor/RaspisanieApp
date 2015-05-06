package Fragments;

import java.util.ArrayList;
import java.util.List;

import Adapters.ListViewRaspAdapter;
import db.MyPrefs;
import db.MySQLiteClass;



import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.TextView;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.raspisanie_proj.NotificationReciever;
import com.example.raspisanie_proj.R;
import com.example.raspisanie_proj.rasp;

public class FragmentToday extends Fragment {

    private static final String KEY = "FragmentHistory2";

    public static Fragment newInstance(int pos) {
        FragmentToday fragment = new FragmentToday();
        Bundle args = new Bundle();
        args.putInt(KEY, pos);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_today, container, false);
        ListView lv = (ListView) view.findViewById(R.id.MylistView);

        Intent intent = getActivity().getIntent();
        String station1 = intent.getStringExtra(MyPrefs.STATION_FROM);
        String station2 = intent.getStringExtra(MyPrefs.STATION_TO);

        MySQLiteClass mySQLiteClass = new MySQLiteClass(getActivity().getApplicationContext());

        mySQLiteClass.open(false);//Так сделанно потому, что я не знаю, почему на телефоне вылетает, а на эмуляторе нет
        ArrayList<String[]> list2 = mySQLiteClass.getScheduleNow(station1, station2);
        mySQLiteClass.close();



        lv.setLongClickable(true);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {
                // TODO Auto-generated method stub

                final View currentView = v;

                Builder alertDialogBuilder = new AlertDialog.Builder(
                        getActivity());
                alertDialogBuilder.setTitle("Delete item");
                alertDialogBuilder.setMessage("Are you sure?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {

                                restartNotify();
                                TextView Text = (TextView) currentView.findViewById(R.id.textViewComeOut);
                                Toast.makeText(getActivity(),"long click : " + Text.getText(),Toast.LENGTH_LONG).show();

                            }

                        });

                alertDialogBuilder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {

                            }

                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                return true;
            }
        });





        ListViewRaspAdapter adapter = new ListViewRaspAdapter(getActivity(), rasp.getListRasp(list2));
        lv.setAdapter(adapter);

        return view;

    }


    private void restartNotify() {

        Log.d("RESTART_NOTIFY","11111");
        AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), NotificationReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),
                0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 4000,
                pendingIntent);
    }

}
