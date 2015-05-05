package Adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.raspisanie_proj.R;
import com.example.raspisanie_proj.rasp;

public class ListViewRaspAdapter extends ArrayAdapter<rasp> {
	
	 private final Context context;
	 private List<rasp> ListRasp;

	 public ListViewRaspAdapter(Context context, List<rasp> ListRasp) {
	        super(context, R.layout.list_item_rasp, ListRasp);
	        this.context = context;
	        this.ListRasp = ListRasp;
	    }
	 
	 @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        
	        View view = inflater.inflate(R.layout.list_item_rasp, parent, false);

			
			rasp rasp = getuser(position);
			
			TextView textViewOut =(TextView) view.findViewById(R.id.textView_Out);
			TextView textViewIn =(TextView) view.findViewById(R.id.textView_In);
			TextView textViewLong =(TextView) view.findViewById(R.id.textView_long);
			TextView textViewComeOut=(TextView) view.findViewById(R.id.textViewComeOut);
			textViewOut.setText(rasp.getmTimeOut());
			textViewIn.setText(rasp.getmTimeIn());
			textViewLong.setText(rasp.getmTimeLong());
			String te=rasp.getmWent()+"-"+rasp.getmCome();
			textViewComeOut.setText(te);
			
			return view;

	    }
	    private rasp getuser(int position) {
			return (rasp) ListRasp.get(position);
		}
}
