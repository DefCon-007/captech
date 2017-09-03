package info.devexchanges.ratingbarlistview;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;


import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import me.grantland.widget.AutofitHelper;

public class ResultActivity extends AppCompatActivity {

    SharedPreferences loginSettings;
    SharedPreferences.Editor logineditor ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        loginSettings = getApplicationContext().getSharedPreferences(LoginActivity.PREFS_NAME, getApplicationContext().MODE_PRIVATE);
        logineditor = loginSettings.edit();
//        JSONObject finalList = new JSONObject();
        ArrayList<Movie> finalList = new ArrayList<Movie>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            JSONObject jsonObj = new JSONObject(getIntent().getStringExtra("response"));
            Iterator<String> keysIterator = jsonObj.keys();
            while (keysIterator.hasNext())
            {
                String keyStr = (String)keysIterator.next();
                String valueStr = jsonObj.getString(keyStr);
                Movie movieDetails = readData(keyStr);
                movieDetails.setRatingStar(Float.parseFloat(valueStr));
//                String movieName = movieDetails.getMovieName();
                finalList.add(movieDetails);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        ScrollView sv = (ScrollView) findViewById(R.id.SVContainer);
        LinearLayout container = new LinearLayout(this);
        container.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        container.setOrientation(LinearLayout.VERTICAL);
//        Iterator<String> keysIterator = finalList.keys();
//        while (keysIterator.hasNext())
        Integer i=1;
        for (final Movie mov : finalList)
        {
                String keyStr = mov.getName();
                String valueStr = String.valueOf(mov.getRatingStar());


                LinearLayout newll = new LinearLayout(this);
                newll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
                newll.setOrientation(LinearLayout.VERTICAL);

            //Add Rating Bar
            SimpleRatingBar ratebar = (SimpleRatingBar) View.inflate(this, R.layout.rating_bar, null);
            Log.d("PRINT",valueStr);
            ratebar.setRating(Float.parseFloat(String.valueOf(valueStr)));
            ratebar.setFillColor(getResources().getColor(R.color.star));
            ratebar.setBorderColor(getResources().getColor(R.color.star));
            ratebar.setStarCornerRadius((float) 5.0);
                i = i +1;
                // Add textview 1
                TextView textView1 = (TextView) View.inflate(this, R.layout.text_view, null);;
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params1.gravity = Gravity.CENTER_HORIZONTAL;
                textView1.setLayoutParams(params1);
                textView1.setText(keyStr);
            textView1.setTextColor(getResources().getColor(R.color.resultText));
//                textView1.setBackgroundColor(0xff66ff66); // hex color 0xAARRGGBB
//                textView1.setGravity(Gravity.CENTER_HORIZONTAL);
                textView1.setTextSize(30);
                textView1.setTextColor(getResources().getColor(R.color.iron));
//                textView1.setTextAppearance(R.style.text);
                AutofitHelper.create(textView1);
                newll.addView(textView1);


//                Drawable stars = ratebar.getProgressDrawable();
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                stars.setTint(getResources().getColor(R.color.star));
//            }
            newll.addView(ratebar);


                newll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mov.getUrl())));
                    }
                });

            View v = new View(this);
            v.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    5
            ));
            v.setBackgroundColor(Color.parseColor("#B3B3B3"));

                container.addView(newll);
            container.addView(v);



        }
        sv.addView(container);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.homepage_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.logout :
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private Movie readData(String id) {
        AssetManager assetManager = getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open("movies.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            try {
                String csvLine;
                while ((csvLine = reader.readLine()) != null) {
                    String[] row = csvLine.split(",");
                    if (id.equalsIgnoreCase(row[0])) {
                        Movie movieData = new Movie();
                        movieData.setMovieId(row[0]);
                        movieData.setName(row[1]);
                        movieData.setGeners(row[2]);
                        movieData.setUrl(row[3]);
                        return movieData;
                    }


//                    listDataHeader.add(row[1]);
//                    resultList.add(row);
                }
            } catch (IOException ex) {
                throw new RuntimeException("Error in reading CSV file: " + ex);
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException("Error while closing input stream: " + e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        for (data obj : moviesData) {
//            Log.d("OUTPUT", "Name : " + obj.getMovieName() + " / ID : " + obj.getMovieId());
//        }
//    Log.d("CHECK",resultList.toString());
        return null;
    }

    public void logout (){
        logineditor.putBoolean("hasLoggedIn", false);
        logineditor.remove("email");
        logineditor.apply();
        logineditor.commit();
        Intent i = new Intent(ResultActivity.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

}
