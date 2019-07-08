package labrador.cse.usf.edu.signin;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] names;
    private final String[] quantities;
    private final String[] ids;

    public MyListAdapter(Activity context, String[] names, String[] quantities, String[] ids) {
        super(context, R.layout.mylist, names);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.names=names;
        this.quantities=quantities;
        this.ids = ids;

    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        TextView idsText = (TextView) rowView.findViewById(R.id.ids);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);

        titleText.setText(names[position]);
        idsText.setText(ids[position]);
        subtitleText.setText(quantities[position]);

        return rowView;

    };
}
