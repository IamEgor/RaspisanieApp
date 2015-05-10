package com.example.raspviewproj;

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
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
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


        if (list.size() == 0)
        {
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View empty_view = inflater.inflate(R.layout.empty_list, null, false);

            TextView textViewMessage = (TextView) empty_view.findViewById(R.id.show_empty_set_tv);
            textViewMessage.setText(getResources().getString(R.string.nothing_to_show));

            setContentView(empty_view);

        } else {
            ListViewRaspAdapter adapter = new ListViewRaspAdapter(RaspOneStationActivity.this, RaspisanieContent.getListRasp(list));
            listView.setAdapter(adapter);
        }
        final Context context =this;
        listView.setLongClickable(true);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {
                // TODO Auto-generated method stub

                final View currentView = v;

                Builder alertDialogBuilder = new AlertDialog.Builder(context);
                final EditText edittext = new EditText(context);
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

                                Toast.makeText(context, "long click : " + hour + "  " + minute, Toast.LENGTH_LONG).show();

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

    }
    
    
    private void restartNotify(int hour, int minute, int edit) {

        Log.d("RESTART_NOTIFY", "11111");
        AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReciever.class);
        intent.putExtra("Edit", edit);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toggle, menu);
        return super.onCreateOptionsMenu(menu);

    }
    
    
    
    
    boolean birthSort=false;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.star:

                if(birthSort){
                    //change your view and sort it by Alphabet
                    item.setIcon(R.drawable.ic_star_not_selected);
                    item.setTitle("1");
                    birthSort=false;
                }else{
                    //change your view and sort it by Date of Birth
                    item.setIcon(R.drawable.ic_star_selected);
                    item.setTitle("2");
                    birthSort=true;
                }
                return true;

        }
        return super.onOptionsItemSelected(item);


    }
    
    @Override
    protected void onPause(){
    	super.onPause();
    	if(birthSort){
        	Intent intent = this.getIntent();
            String station1 = intent.getStringExtra(MyPrefs.STATION_SINGLE);
            ManagerPref.get(this).put(station1);
        }
    }
}
