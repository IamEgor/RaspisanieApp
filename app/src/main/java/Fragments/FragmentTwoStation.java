package Fragments;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.raspviewproj.AlarmSave;
import com.example.raspviewproj.ManagerPref;
import com.example.raspviewproj.R;
import com.example.raspviewproj.RaspActivity;

import java.util.ArrayList;

import Adapters.ListViewNedavnieAdapter;
import db.MyPrefs;
import db.MySQLiteClass;

public class FragmentTwoStation extends Fragment {

    private static final String KEY = "FragmentHistory";
    ///
    private AutoCompleteTextView EditTextFrom, EditTextTo;
    private Button ButtonShowRasp, ButtonChange;///***

    ///
    public static Fragment newInstance(int pos) {
        FragmentTwoStation fragment = new FragmentTwoStation();
        Bundle args = new Bundle();
        args.putInt(KEY, pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        EditTextFrom.setText("");
        EditTextTo.setText("");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final ViewPager con = (ViewPager) container;
        View view = inflater.inflate(R.layout.two_station, container, false);
        final ListViewNedavnieAdapter adapter = new ListViewNedavnieAdapter(this.getActivity(), ManagerPref.get(this.getActivity()).getStations());
        ListView lv = (ListView) view.findViewById(R.id.MylistView);
        lv.setAdapter(adapter);


        EditTextFrom = (AutoCompleteTextView) view.findViewById(R.id.editText_from);
        EditTextTo = (AutoCompleteTextView) view.findViewById(R.id.editText_to);
        ButtonChange = (Button) view.findViewById(R.id.button_change);///***
        ButtonShowRasp = (Button) view.findViewById(R.id.button_show_rasp);///***

        String[] data = MyPrefs.getStringPrefs(getActivity(), MyPrefs.STATION_PREFS).split(",");  // terms is a List<String>
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, data);
        EditTextFrom.setAdapter(adapter1);
        EditTextTo.setAdapter(adapter1);

        ButtonChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = "";

                temp = EditTextTo.getText().toString();
                EditTextTo.setText(EditTextFrom.getText().toString());
                EditTextFrom.setText(temp);

                EditTextFrom.dismissDropDown();
                EditTextTo.dismissDropDown();
                AlarmSave.get(getActivity()).remove();
            }
        });

        ButtonShowRasp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EditTextFrom.getText().toString().equals(null) || 
                        EditTextFrom.getText().toString().equals("") ||
                        EditTextTo.getText().toString().equals(null) ||
                        EditTextTo.getText().toString().equals("")) {
                    return;
                }

                MySQLiteClass mySQLiteClass = new MySQLiteClass(getActivity().getApplicationContext());

                mySQLiteClass.open(false);//Так сделанно потому, что я не знаю, почему на телефоне вылетает, а на эмуляторе нет
                ArrayList<String[]> list2 = mySQLiteClass. getScheduleForOneStation("Борисов");
                mySQLiteClass.close();
                for(String[] strings : list2)
                    Log.d("&&&", strings[0] + "_" + strings[1] + "_" + strings[2] + "_" + strings[3]);

                Intent i = new Intent(getActivity(), RaspActivity.class);
                i.putExtra(MyPrefs.STATION_FROM, EditTextFrom.getText().toString());
                i.putExtra(MyPrefs.STATION_TO, EditTextTo.getText().toString());
                startActivity(i);

            }
        });


        lv.setLongClickable(true);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View v,
                                    int index, long arg3) {

                TextView textView = (TextView) v.findViewById(R.id.TextViewOne);
                String text = textView.getText().toString();

                String delims = "[—]";
                String[] tokens = text.split(delims);
                Intent i = new Intent(getActivity(), RaspActivity.class);
                i.putExtra(MyPrefs.STATION_FROM, tokens[0]);
                i.putExtra(MyPrefs.STATION_TO, tokens[1]);
                startActivity(i);

            }


        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {
                final int i = index;

                Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Delete item");

                alertDialogBuilder.setMessage("Are you sure?");

                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        /////Удаление элемента из настроек и массива
                        ManagerPref.get(getActivity()).delete(i);
                        adapter.notifyDataSetChanged();
                        ////Обновление пейджера
                        con.getAdapter().notifyDataSetChanged();

                    }

                });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                    }

                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                return true;
            }
        });

        return view;

    }


}
