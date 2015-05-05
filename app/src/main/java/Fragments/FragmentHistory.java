package Fragments;

import java.util.ArrayList;
import java.util.List;

import Adapters.ListViewNedavnieAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

         View view = inflater.inflate(R.layout.list_nedavnie, container, false);
         ListView lv = (ListView)view.findViewById(R.id.MylistView);

 		
 		ListViewNedavnieAdapter adapter = new ListViewNedavnieAdapter(this.getActivity(),init());

 		lv.setAdapter(adapter);
		return view;
		
	}
	
	private List<rasp> init(){
		List<rasp> list = new ArrayList<rasp>();
		list.add(new rasp("06:00","1 � 39 ���","08:00","������-�����","�����(�������� ��������)"));
		list.add(new rasp("06:00","1 � 39 ���","08:00","�������","���������"));
		list.add(new rasp("06:00","1 � 39 ���","08:00","��������","��������"));
		list.add(new rasp("06:00","1 � 39 ���","08:00","�������","�������-�����"));
		list.add(new rasp("06:00","1 � 39 ���","08:00","������","��������"));
		list.add(new rasp("06:00","1 � 39 ���","08:00","��������","�������"));	
		return list;
	}

}
