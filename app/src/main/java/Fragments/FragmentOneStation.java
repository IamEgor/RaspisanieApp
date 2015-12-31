package Fragments;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.raspviewproj.ManagerPref;
import com.example.raspviewproj.R;
import com.example.raspviewproj.RaspOneStationActivity;

import Adapters.ListViewNedavnieAdapter;
import db.MyPrefs;

public class FragmentOneStation extends Fragment {


    private static final String KEY = "FragmentHistory";
    private AutoCompleteTextView editTextFrom;///***
    private Button buttonShowRasp;

    public static Fragment newInstance(int pos) {
        FragmentOneStation fragment = new FragmentOneStation();
        Bundle args = new Bundle();
        args.putInt(KEY, pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        editTextFrom.setText("");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewPager con = (ViewPager) container;
        View view = inflater.inflate(R.layout.one_station, container, false);
        ListView lv = (ListView) view.findViewById(R.id.MylistView);

        final ListViewNedavnieAdapter adapter = new ListViewNedavnieAdapter(this.getActivity(), ManagerPref.get(this.getActivity()).getStations_one());

        lv.setAdapter(adapter);
        editTextFrom = (AutoCompleteTextView) view.findViewById(R.id.editText_one);///***
        buttonShowRasp = (Button) view.findViewById(R.id.button_show_rasp);///***

        String[] data = MyPrefs.getStringPrefs(getActivity(), MyPrefs.STATION_PREFS).split(","); ///***
        ArrayAdapter<String> edTextAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, data);///***
        editTextFrom.setAdapter(edTextAdapter);///***

        buttonShowRasp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), RaspOneStationActivity.class);
                i.putExtra(MyPrefs.STATION_SINGLE, editTextFrom.getText().toString());
                startActivity(i);
            }
        });


        lv.setLongClickable(true);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View v,
                                    int index, long arg3) {

                TextView textView = (TextView) v.findViewById(R.id.TextViewOne);
                String text = textView.getText().toString();


                Intent i = new Intent(getActivity(), RaspOneStationActivity.class);
                i.putExtra(MyPrefs.STATION_SINGLE, text);

                startActivity(i);

            }


        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {
                final int i = index;

                Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Удаление");

                alertDialogBuilder.setMessage("Удалить элемент?");

                alertDialogBuilder.setPositiveButton("Да", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        /////Удаление элемента из настроек и массива
                        ManagerPref.get(getActivity()).delete_one(i);
                        adapter.notifyDataSetChanged();
                        ////Обновление пейджера
                        con.getAdapter().notifyDataSetChanged();


                    }

                });

                alertDialogBuilder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {

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