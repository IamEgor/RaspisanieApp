package com.example.raspisanie_proj;

import Adapters.MyPagerAdapter;
import db.MyPrefs;
import db.MySQLiteClass;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MainActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.main);

        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        pager.setCurrentItem(1);
        PagerTabStrip strip = (PagerTabStrip) findViewById(R.id.pagerTabStrip);
        strip.setTabIndicatorColor(0x0060b7);
        strip.setMinimumHeight(7);


    }


    @Override
    protected void onResume() {
        super.onResume();

        Log.d("MainActiv", "onResume");
        String updateStatus = MyPrefs.getStringPrefs(MainActivity.this, MenuActivity.UPDATE_STATUS);
        Log.d("MainActiv", "updateStatus = " + updateStatus);
        if (updateStatus.equals(MenuActivity.STATUS_UPDATING)) {
            //MyPrefs.setPrefs(MainActivity.this, MenuActivity.UPDATE_DB, false);
            new MyTask().execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("onOptionsItemSelected", "onOptionsItemSelected");
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            makeNotification("Загрузка началась", "Обновление базы началось");
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Void doInBackground(Void... params) {

            MySQLiteClass mySQLiteClass = new MySQLiteClass(MainActivity.this);
            mySQLiteClass.fillingTest(getResources());

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //добавить массив из sharedPrefs в editText
            //MyPrefs.setPrefs(MainActivity.this, MenuActivity.UPDATE_DB, false);

            MyPrefs.setPrefs(MainActivity.this, MenuActivity.UPDATE_STATUS, MenuActivity.STATUS_NOT_UPDATING);



            MyPrefs.setPrefs(MainActivity.this, MyPrefs.UPDATE_DATE, MySQLiteClass.getCurrentTime());
            makeNotification("Загрузка закончилась", "Обновление базы завершено");
            setProgressBarIndeterminateVisibility(false);

            ///#
            String[] data = MyPrefs.getStringPrefs(MainActivity.this, MyPrefs.STATION_PREFS).split(",");  // terms is a List<String>
            ArrayAdapter<String> adapter = new ArrayAdapter<String>((MainActivity.this), android.R.layout.simple_list_item_1, data);
            ((AutoCompleteTextView) findViewById(R.id.editText_from)).setAdapter(adapter);
            ((AutoCompleteTextView) findViewById(R.id.editText_to)).setAdapter(adapter);
            Log.d("###", "setAdapters");
            ///#

        }
    }

    public void makeNotification(String title, String messageText) {
        final int NOTIFY_ID = 101;

        Context context = getApplicationContext();

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Resources res = context.getResources();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.parovoz)
                        // большая картинка
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.parovoz_small))
                        //.setTicker(res.getString(R.string.warning)) // текст в строке состояния

                .setTicker(title)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                        //.setContentTitle(res.getString(R.string.notifytitle)) // Заголовок уведомления
                .setContentTitle(res.getString(R.string.app_name))
                        //.setContentText(res.getString(R.string.notifytext))
                .setContentText(title); // Текст уведомленимя

        // Notification notification = builder.getNotification(); // до API 16
        Notification notification = builder.getNotification();//build();

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, notification);
    }


}
