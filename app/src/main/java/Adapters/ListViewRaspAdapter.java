package Adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.raspviewproj.R;
import com.example.raspviewproj.RaspisanieContent;

public class ListViewRaspAdapter extends ArrayAdapter<RaspisanieContent> {

    private final Context context;
    private List<RaspisanieContent> listRaspisanieContent;

    public ListViewRaspAdapter(Context context, List<RaspisanieContent> listRaspisanieContent) {
        super(context, R.layout.list_item_rasp, listRaspisanieContent);
        this.context = context;
        this.listRaspisanieContent = listRaspisanieContent;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.list_item_rasp, parent, false);


        RaspisanieContent RaspisanieContent = getuser(position);

        TextView textViewOut = (TextView) view.findViewById(R.id.textView_Out);
        TextView textViewIn = (TextView) view.findViewById(R.id.textView_In);
        TextView textViewLong = (TextView) view.findViewById(R.id.textView_long);
        TextView textViewComeOut = (TextView) view.findViewById(R.id.textViewComeOut);
        textViewOut.setText(RaspisanieContent.getmTimeOut());
        textViewIn.setText(RaspisanieContent.getmTimeIn());
        textViewLong.setText(RaspisanieContent.getmTimeLong());
        String te = RaspisanieContent.getmWent() + "-" + RaspisanieContent.getmCome();
        textViewComeOut.setText(te);

        return view;

    }

    private RaspisanieContent getuser(int position) {
        return (RaspisanieContent) listRaspisanieContent.get(position);
    }
}
