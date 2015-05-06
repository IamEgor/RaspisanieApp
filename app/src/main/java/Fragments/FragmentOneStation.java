package Fragments;

import java.util.ArrayList;
import java.util.List;

import com.example.raspisanie_proj.R;
import com.example.raspisanie_proj.rasp;

import Adapters.ListViewNedavnieAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class FragmentOneStation extends Fragment {
	
	
private static final String KEY ="FragmentHistory";
	
	public static Fragment newInstance(int pos){
		FragmentOneStation fragment=new FragmentOneStation();
		Bundle args = new Bundle();
		args.putInt(KEY, pos);
		fragment.setArguments(args);
		return fragment;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

         View view = inflater.inflate(R.layout.one_station, container, false);
         ListView lv = (ListView)view.findViewById(R.id.MylistView);

 		// ���������� ������ ���� String
 		ListViewNedavnieAdapter adapter = new ListViewNedavnieAdapter(this.getActivity(),init());
 		// ���������� ������� ������
 		lv.setAdapter(adapter);
 		EditText EditTextFrom=(EditText)view.findViewById(R.id.editText_one);
 		Button ButtonShowRasp=(Button)view.findViewById(R.id.button_show_rasp);
 		
		return view;

}
	private List<rasp> init(){
		List<rasp> list = new ArrayList<rasp>();


        list.add(new rasp("06:00", "1 � 39 ���", "08:00", "Жодино-Южное", "Жодино"));
        list.add(new rasp("06:00", "1 � 39 ���", "08:00", "Борисов", "Барсуки"));
		/*list.add(new rasp("06:00","1 � 39 ���","08:00","��������","��������"));
		list.add(new rasp("06:00","1 � 39 ���","08:00","�������","�������-�����"));
		list.add(new rasp("06:00","1 � 39 ���","08:00","������","��������"));
		list.add(new rasp("06:00","1 � 39 ���","08:00","��������","�������"));	*/
		return list;
	}
	
	
	
}