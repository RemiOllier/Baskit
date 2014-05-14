package SQLite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.copiercoller.baskit.app.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Remi on 12/05/2014.
 */

public class ListeAdapter extends ArrayAdapter<HashMap<String, String>> {

    private Context context;
    private ArrayList<HashMap<String, String>> data;
    private int viewId;

    private LayoutInflater mInflater;

    public ListeAdapter(Context c, ArrayList<HashMap<String, String>> d) {
        super(c, R.layout.row_layout, d);

        this.context = c;
        //this.viewId = textViewResourceId;
        this.data = d;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    /*
     * We are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Assign the view we are converting to a local variable
        View vi = convertView;
        Holder holder;

        if (convertView == null) {

            // Inflate the view since it does not exist
            if (vi == null) {
                mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                vi = mInflater.inflate(R.layout.row_layout, null);
            }

            holder = new Holder();
            holder.textView_nom = (TextView) vi.findViewById(R.id.label_liste);
            holder.textView_date = (TextView) vi.findViewById(R.id.date_modif);

            holder.imageView = (ImageView) vi.findViewById(R.id.icon);

            vi.setTag(holder);

        } else {
            holder = (Holder) vi.getTag();
        }

        HashMap<String, String> currentData = new HashMap<String, String>();
        currentData = data.get(position);

        if (currentData != null) {
            holder.textView_nom.setText(currentData.get("nom"));
            holder.textView_date.setText(currentData.get("date"));
        }

        return vi;
    }

    /** View holder for the views we need access to */
    private static class Holder {
        public TextView textView_nom;
        public TextView textView_date;
        public ImageView imageView;
    }

}