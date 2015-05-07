package Fragments;

import java.util.ArrayList;
import java.util.List;

import Adapters.ListViewNedavnieAdapter;
import db.MyPrefs;
import db.MySQLiteClass;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.raspisanie_proj.R;
import com.example.raspisanie_proj.RaspActivity;
import com.example.raspisanie_proj.rasp;

public class FragmentTwoStation extends Fragment {

    private static final String KEY = "FragmentHistory";
    private static final String[] COUNTRIES = new String[]{
            "Belgium", "France", "Francyz", "Franchezsko", "Frank", "Friday", "Frik", "Frask", "Italy", "Germany", "Spain"
    };
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.two_station, container, false);
        ListView lv = (ListView) view.findViewById(R.id.MylistView);

        ListViewNedavnieAdapter adapter = new ListViewNedavnieAdapter(this.getActivity(), init());

        lv.setAdapter(adapter);

        EditTextFrom = (AutoCompleteTextView) view.findViewById(R.id.editText_from);
        EditTextTo = (AutoCompleteTextView) view.findViewById(R.id.editText_to);
        ButtonChange = (Button) view.findViewById(R.id.button_change);///***
        ButtonShowRasp = (Button) view.findViewById(R.id.button_show_rasp);///***
        //final RelativeLayout Relative = (RelativeLayout) view.findViewById(R.id.Relative);

        ///#
        String[] data = MyPrefs.getStringPrefs(getActivity(), MyPrefs.STATION_PREFS).split(",");  // terms is a List<String>
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, data);
        EditTextFrom.setAdapter(adapter1);
        EditTextTo.setAdapter(adapter1);
        ///#

        /*
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, COUNTRIES);
        EditTextFrom.setAdapter(adapter1);
        EditTextFrom.onCheckIsTextEditor();
        EditTextTo.setAdapter(adapter1);
        EditTextTo.onCheckIsTextEditor();



        Relative.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                EditTextFrom.setDropDownHeight(Relative.getBottom() - EditTextFrom.getBottom() + 25);
                EditTextTo.setDropDownHeight(Relative.getTop() - EditTextTo.getBottom() + 25);
            }
        });
        */
        ButtonChange.setOnClickListener(new View.OnClickListener() { ///***
            @Override
            public void onClick(View view) {
                String temp = "";

                temp = EditTextTo.getText().toString();
                EditTextTo.setText(EditTextFrom.getText().toString());
                EditTextFrom.setText(temp);

                EditTextFrom.dismissDropDown();
                EditTextTo.dismissDropDown();
            }
        });

        ButtonShowRasp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EditTextFrom.getText().toString().equals(null) || ///***
                        EditTextFrom.getText().toString().equals("") ||
                        EditTextTo.getText().toString().equals(null) ||
                        EditTextTo.getText().toString().equals("")) {
                    return;
                }
                ////////
                MySQLiteClass mySQLiteClass = new MySQLiteClass(getActivity().getApplicationContext());

                mySQLiteClass.open(false);//Так сделанно потому, что я не знаю, почему на телефоне вылетает, а на эмуляторе нет
                ArrayList<String[]> list2 = mySQLiteClass. getScheduleForOneStation("Борисов");
                mySQLiteClass.close();
                for(String[] strings : list2)
                    Log.d("&&&", strings[0] + "_" + strings[1] + "_" + strings[2] + "_" + strings[3]);
                //////////
                Intent i = new Intent(getActivity(), RaspActivity.class);
                i.putExtra(MyPrefs.STATION_FROM, EditTextFrom.getText().toString());
                i.putExtra(MyPrefs.STATION_TO, EditTextTo.getText().toString());
                startActivity(i);

            }
        });


        return view;

    }

    private List<rasp> init() {
        List<rasp> list = new ArrayList<rasp>();


        list.add(new rasp("06:00", "1 � 39 ���", "08:00", "Жодино-Южное", "Жодино"));
        list.add(new rasp("06:00", "1 � 39 ���", "08:00", "Борисов", "Барсуки"));
        //list.add(new rasp("06:00", "1 � 39 ���", "08:00", "��������", "��������"));
        //list.add(new rasp("06:00", "1 � 39 ���", "08:00", "�������", "�������-�����"));
        //list.add(new rasp("06:00", "1 � 39 ���", "08:00", "������", "��������"));
        //list.add(new rasp("06:00", "1 � 39 ���", "08:00", "��������", "�������"));
        return list;
    }

}
