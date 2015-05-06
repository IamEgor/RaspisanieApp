package com.example.raspisanie_proj;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import db.MyPrefs;

/**
 * Created by Егор on 29.04.2015.
 */
public class MenuActivity extends Activity {
    public static final String UPDATE_STATUS        =   "update";
    public static final String STATUS_WILL_UPDATE   =   "will_update";
    public static final String STATUS_NOW_UPDATING  =   "now_updating";
    public static final String STATUS_NOT_UPDATING  =   "not_updating";
    private boolean isUpdateBtnClicked;
    private Button button;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);

        textView = (TextView) findViewById(R.id.TextView_LastUpdate);
        button = (Button) findViewById(R.id.button_update);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()) {
                    isUpdateBtnClicked = true;

                    button.setEnabled(!isUpdateBtnClicked);
                    textView.setText(R.string.updating_sentence);

                    MyPrefs.setPrefs(MenuActivity.this, UPDATE_STATUS, STATUS_WILL_UPDATE);//!
                }
                else
                    Toast.makeText(MenuActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_LONG).show();

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MenuActiv","onResume");

        String updateStatus = MyPrefs.getStringPrefs(MenuActivity.this, UPDATE_STATUS);
        String updateDate = MyPrefs.getStringPrefs(MenuActivity.this, MyPrefs.UPDATE_DATE);
        textView.append(updateDate);
        Log.d("UPDATE_STATUS", updateStatus);
        if(updateStatus.equals(STATUS_WILL_UPDATE) || updateStatus.equals(STATUS_NOW_UPDATING)){
            textView.setText(R.string.updating_sentence);
            button.setEnabled(false);
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MenuActiv","onPause");
        finish();
    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
