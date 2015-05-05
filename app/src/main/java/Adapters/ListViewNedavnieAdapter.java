package Adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.raspisanie_proj.R;
import com.example.raspisanie_proj.rasp;

public class ListViewNedavnieAdapter extends ArrayAdapter<rasp> {
    private final Context context;
    private List<rasp> ListRasp;


    public ListViewNedavnieAdapter(Context context, List<rasp> ListRasp) {
        super(context, R.layout.list_item_nedavnie, ListRasp);
        this.context = context;
        this.ListRasp = ListRasp;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        View view = inflater.inflate(R.layout.list_item_nedavnie, parent, false);

		
		rasp rasp = getuser(position);
		
		
		TextView textView1 =(TextView) view.findViewById(R.id.TextViewOne);
		ToggleButton ToggleButton1=(ToggleButton) view.findViewById(R.id.ToggleButton_star);
		String te=rasp.getmWent()+"-"+rasp.getmCome();
		textView1.setText(te);
		
		
		return view;

    }
    private rasp getuser(int position) {
		return (rasp) ListRasp.get(position);
	}

}
