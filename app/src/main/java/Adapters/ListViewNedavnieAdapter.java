package Adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.raspviewproj.R;

public class ListViewNedavnieAdapter extends ArrayAdapter<String> {
    private final Context context;
    private ArrayList<String> ListRasp = new ArrayList<String>();


    public ListViewNedavnieAdapter(Context context, ArrayList<String> ListRasp) {
        super(context, R.layout.list_item_nedavnie, ListRasp);
        this.context = context;
        this.ListRasp = ListRasp;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.list_item_nedavnie, parent, false);

        TextView textView1 = (TextView) view.findViewById(R.id.TextViewOne);
        textView1.setText(ListRasp.get(position));


        return view;

    }


}
