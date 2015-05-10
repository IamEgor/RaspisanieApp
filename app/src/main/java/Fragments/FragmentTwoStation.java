package Fragments;

import Adapters.ListViewNedavnieAdapter;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.example.raspviewproj.ManagerPref;
import com.example.raspviewproj.R;
import com.example.raspviewproj.RaspActivity;

import db.MyPrefs;

public class FragmentTwoStation extends Fragment {

    private static final String KEY = "FragmentHistory";

    private AutoCompleteTextView editTextFrom, editTextTo;
    private Button buttonShowRasp, buttonChange;

    public static Fragment newInstance(int pos) {
        FragmentTwoStation fragment = new FragmentTwoStation();
        Bundle args = new Bundle();
        args.putInt(KEY, pos);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.two_station, container, false);
        final ListViewNedavnieAdapter adapter = new ListViewNedavnieAdapter(this.getActivity(), ManagerPref.get(this.getActivity()).getStations());
        ListView lv = (ListView) view.findViewById(R.id.MylistView);
        lv.setAdapter(adapter);


        editTextFrom = (AutoCompleteTextView) view.findViewById(R.id.editText_from);
        editTextTo = (AutoCompleteTextView) view.findViewById(R.id.editText_to);
        buttonChange = (Button) view.findViewById(R.id.button_change);
        buttonShowRasp = (Button) view.findViewById(R.id.button_show_rasp);

        String[] data = MyPrefs.getStringPrefs(getActivity(), MyPrefs.STATION_PREFS).split(",");  // terms is a List<String>
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, data);
        editTextFrom.setAdapter(adapter1);
        editTextTo.setAdapter(adapter1);

        buttonChange.setOnClickListener(new View.OnClickListener() { ///***
            @Override
            public void onClick(View view) {
                String temp = "";

                temp = editTextTo.getText().toString();
                editTextTo.setText(editTextFrom.getText().toString());
                editTextFrom.setText(temp);

                editTextFrom.dismissDropDown();
                editTextTo.dismissDropDown();
            }
        });

        buttonShowRasp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextFrom.getText().toString().equals(null) ||
                        editTextFrom.getText().toString().equals("") ||
                        editTextTo.getText().toString().equals(null) ||
                        editTextTo.getText().toString().equals("")) {
                    return;
                }

                Intent i = new Intent(getActivity(), RaspActivity.class);
                i.putExtra(MyPrefs.STATION_FROM, editTextFrom.getText().toString().trim());
                i.putExtra(MyPrefs.STATION_TO, editTextTo.getText().toString().trim());
                startActivity(i);

            }
        });


        lv.setLongClickable(true);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View v,
                                    int index, long arg3) {

                TextView textView = (TextView) v.findViewById(R.id.TextViewOne);
                String text = textView.getText().toString();
                Log.d("(((", text);
                String delims = "[-]";
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
                        //  con.getAdapter().notifyDataSetChanged();

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

    @Override
    public void onResume() {
        super.onResume();
        editTextFrom.dismissDropDown();
        editTextTo.dismissDropDown();
    }
}
