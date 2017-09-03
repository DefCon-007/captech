package info.devexchanges.ratingbarlistview;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import info.devexchanges.ratingbarlistview.Utilities.ApplicationClass;

public class ListViewAdapter extends ArrayAdapter<Movie> {

    private AppCompatActivity activity;
    private List<Movie> movieList;

    public ListViewAdapter(AppCompatActivity context, int resource, List<Movie> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.movieList = objects;
    }

    @Override
    public Movie getItem(int position) {
        return movieList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_listview, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
            //holder.ratingBar.getTag(position);
        }

        holder.ratingBar.setOnRatingBarChangeListener(onRatingChangedListener(holder, position));

        holder.ratingBar.setTag(position);
        holder.ratingBar.setRating(getItem(position).getRatingStar());
        holder.movieName.setText(getItem(position).getName());

        return convertView;
    }

    private RatingBar.OnRatingBarChangeListener onRatingChangedListener(final ViewHolder holder, final int position) {
        return new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Movie item = getItem(position);
                item.setRatingStar(v);

                if (v != 0.0 ){
                    Log.d("CHECK","V not zero");
                }
                else {
                    Log.d("CHECK","v is zero");
                }


                if (v != 0.0) {
                    if (item.getInGlobalArray()){
                        //Removing item if it is present in teh global array
                        ((ApplicationClass) activity.getApplicationContext()).removeItem(item.getGlobalArrayIndex());
                        item.setInGlobalArray(false);
                    }
                    //Adding item to global array if the rating is not zero
                    item.setGlobalArrayIndex(((ApplicationClass) activity.getApplicationContext()).addItem(item));
                    item.setInGlobalArray(true);
                } else if (item.getInGlobalArray()){
                    //Removing item if it is present in teh global array
                    Log.d("REMOVE","Removing " + item.getName() + "With rating " + item.getRatingStar());
                    ((ApplicationClass) activity.getApplicationContext()).removeItem(item.getGlobalArrayIndex());
                }
//                Log.i("Adapter", "star: " + v);
            Log.i("SIZE", String.valueOf(((ApplicationClass) activity.getApplicationContext()).getSizeToSend()));
            }
        };
    }

    private static class ViewHolder {
        private RatingBar ratingBar;
        private TextView movieName;

        public ViewHolder(View view) {
            ratingBar = (RatingBar) view.findViewById(R.id.rate_img);
            movieName = (TextView) view.findViewById(R.id.text);
        }
    }
}
