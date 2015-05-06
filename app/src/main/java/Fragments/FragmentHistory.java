package Fragments;

import java.util.ArrayList;
import java.util.List;

import Adapters.ListViewNedavnieAdapter;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.raspisanie_proj.R;
import com.example.raspisanie_proj.rasp;

public class FragmentHistory extends Fragment {
	
	private static final String KEY ="FragmentHistory";
	
	public static Fragment newInstance(int pos){
		FragmentHistory fragment=new FragmentHistory();
		Bundle args = new Bundle();
		args.putInt(KEY, pos);
		fragment.setArguments(args);
		return fragment;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){



        final List<rasp> listmy=init();
        final ListViewNedavnieAdapter adapter = new ListViewNedavnieAdapter(this.getActivity(),listmy);
         View view = inflater.inflate(R.layout.list_nedavnie, container, false);
         final ListView lv = (ListView)view.findViewById(R.id.MylistView);


        lv.setLongClickable(true);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                                          public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                                                         int index, long arg3) {
                                              // TODO Auto-generated method stub

                                              final int i = index;

                                              Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                                              alertDialogBuilder.setTitle("Delete item");

                                              alertDialogBuilder.setMessage("Are you sure?");

                                              alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                                  public void onClick(DialogInterface dialog, int id) {

                                                      listmy.remove(i);

                                                      adapter.notifyDataSetChanged();

                                                  }

                                              });

                                              alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                                                  public void onClick(DialogInterface dialog, int id) {

                                                      dialog.cancel();

                                                  }

                                              });

                                              AlertDialog alertDialog = alertDialogBuilder.create();
                                              alertDialog.show();

                                              String str = lv.getItemAtPosition(index).toString();
                                              Toast.makeText(getActivity(), "long click : " + index, Toast.LENGTH_LONG).show();

                                              Log.d("TAG", "long click : " + index);
                                              return true;
                                          }
                                      });

             //  ListViewNedavnieAdapter adapter = new ListViewNedavnieAdapter(this.getActivity(), init());

 		lv.setAdapter(adapter);
		return view;
		
	}
	
	private List<rasp> init(){
		List<rasp> list = new ArrayList<rasp>();
        list.add(new rasp("06:00", "1 � 39 ���", "08:00", "Жодино-Южное", "Жодино"));
        list.add(new rasp("06:00", "1 � 39 ���", "08:00", "Борисов", "Барсуки"));
        list.add(new rasp("06:00", "1 � 39 ���", "08:00", "Жодино-Южное", "Жодино"));
        list.add(new rasp("06:00", "1 � 39 ���", "08:00", "Борисов", "Барсуки"));
        list.add(new rasp("06:00", "1 � 39 ���", "08:00", "Жодино-Южное", "Жодино"));
        list.add(new rasp("06:00", "1 � 39 ���", "08:00", "Борисов", "Барсуки"));
		return list;
	}

}
