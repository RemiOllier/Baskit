package SQLite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.copiercoller.baskit.app.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Remi on 15/05/2014.
 */
public class ProduitsAdapter extends ArrayAdapter<HashMap<String, String>> {

    private Context context;
    private ArrayList<HashMap<String, String>> data;
    private int viewId;

    private LayoutInflater mInflater;

    public ProduitsAdapter(Context c, ArrayList<HashMap<String, String>> d) {
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
        View view = convertView;
        Holder holder;

        if (convertView == null) {

            // Inflate the view since it does not exist
            if (view == null) {
                mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = mInflater.inflate(R.layout.row_layout_produit, null);
            }

            holder = new Holder();
            holder.textView_nom_produit = (TextView) view.findViewById(R.id.tv_label_produit);
            holder.textView_id_produit = (TextView) view.findViewById(R.id.tv_id_produit);

            view.setTag(holder);

        } else {
            holder = (Holder) view.getTag();
        }

        HashMap<String, String> currentData = new HashMap<String, String>();
        currentData = data.get(position);

        if (currentData != null) {
            holder.textView_nom_produit.setText(currentData.get("nom_produit"));
            holder.textView_id_produit.setText(currentData.get("tv_id_produit"));
        }

        return view;
    }

    private static class Holder {

        public TextView textView_nom_produit;
        public TextView textView_id_produit;

        //public ImageView blabla;
    }

}