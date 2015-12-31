package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.raspviewproj.R;
import com.example.raspviewproj.RaspisanieContent;

import java.util.List;

public class ListViewRaspAdapter extends ArrayAdapter<RaspisanieContent> {

    private final Context context;
    private List<RaspisanieContent> ListRasp;

    public ListViewRaspAdapter(Context context, List<RaspisanieContent> ListRasp) {
        super(context, R.layout.list_item_rasp, ListRasp);
        this.context = context;
        this.ListRasp = ListRasp;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.list_item_rasp, parent, false);

        RaspisanieContent rasp = getuser(position);

        TextView textViewOut = (TextView) view.findViewById(R.id.textView_Out);
        TextView textViewIn = (TextView) view.findViewById(R.id.textView_In);
        TextView textViewLong = (TextView) view.findViewById(R.id.textView_long);
        TextView textViewComeOut = (TextView) view.findViewById(R.id.textViewComeOut);
        textViewOut.setText(rasp.getmTimeOut());
        textViewIn.setText(rasp.getmTimeIn());
        textViewLong.setText(rasp.getmTimeLong());
        String te = rasp.getmWent() + "-" + rasp.getmCome();
        textViewComeOut.setText(te);


        return view;
    }

    private RaspisanieContent getuser(int position) {
        return (RaspisanieContent) ListRasp.get(position);
    }
}
