package info.devexchanges.ratingbarlistview;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import info.devexchanges.ratingbarlistview.Utilities.movieListAdapter;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MainActivity extends AppCompatActivity {

    private static final String TAG="MainActivity";
    private Integer flag = 0 ;
    private ListView listView,movieListView;
    private Button submitButton;
    private ArrayAdapter<Movie> adapter,movieListAdapter;
    private ArrayList<Movie> arrayList,ratingList;
    private AppCompatActivity appCompatActivity;
    private String server_ip,url;
    private ProgressDialog dialog;
    private AlertDialog webViewDialog;
    SharedPreferences loginSettings;
    SharedPreferences.Editor logineditor ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginSettings = getApplicationContext().getSharedPreferences(LoginActivity.PREFS_NAME, getApplicationContext().MODE_PRIVATE);
        logineditor = loginSettings.edit();
        movieListView = (ListView) findViewById(R.id.lv_movieList);
        listView = (ListView)findViewById(R.id.list_view);
        submitButton = (Button) findViewById(R.id.submit);
        server_ip = getString(R.string.server_ip);
        url = server_ip.concat("/readmovies");
        dialog= new ProgressDialog(MainActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Please wait...");

        ratingList = new ArrayList<>();
        appCompatActivity = this;
        readData();

        movieListView.setLongClickable(true);
        listView.setLongClickable(true);

        movieListAdapter = new movieListAdapter(this,R.layout.item_movielist,arrayList);
        movieListView.setAdapter(movieListAdapter);
        listView.setAdapter(adapter);
//
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//                                           int pos, long id) {
//                ratingList.remove(pos);
//                adapter = new ListViewAdapter(appCompatActivity, R.layout.item_listview, ratingList);
//                listView.setAdapter(adapter);
//                setButtonState();
//                return true;
//            }
//        });

        movieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("CHECK","In on click");
                Movie movie = (Movie) adapterView.getAdapter().getItem(i);
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Title here");

                WebView wv = new WebView(MainActivity.this);
                wv.loadUrl(movie.getUrl());
                wv.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);

                        return true;
                    }
                });

                alert.setView(wv);
                alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });

        movieListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                if (ratingList.size() == 5 ){
                    //Show snackbar to rate
                    return true;
                }


                Movie movie = (Movie) arg0.getAdapter().getItem(pos);
                if (!(ifListHas(ratingList,movie.getMovieId()))){
                ratingList.add(movie);}
                else {
                    //Show snack bar for duplicate movie

                }
                adapter = new ListViewAdapter(appCompatActivity, R.layout.item_listview, ratingList);
                listView.setAdapter(adapter);
                setButtonState();
                return true;
            }
        });


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"Submit button clicked");
                dialog.show();
                sendData(ratingList);
            }
        });
//
//        movieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//            }
//        });

    }

    private AdapterView.OnItemClickListener onItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


//                final Dialog dialog = new Dialog(MainActivity.this);
//                dialog.setContentView(R.layout.layout_dialog);
//                dialog.setTitle("Movie details");
//
//                TextView name = (TextView) dialog.findViewById(R.id.movie_name);
//                TextView country = (TextView) dialog.findViewById(R.id.country);
//                TextView starRate = (TextView) dialog.findViewById(R.id.rate);
//
//                Movie movie = (Movie) parent.getAdapter().getItem(position);
//                name.setText("Movie name: " + movie.getName());
//                country.setText("URL: " + movie.getUrl());
//                starRate.setText("Your rate: " + movie.getRatingStar());
//
//                WebView wv = (WebView) dialog.findViewById(R.id.wv);
//                wv.loadUrl(movie.getUrl());
//
//                dialog.show();
            }
        };
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

    public void logout (){
        logineditor.putBoolean("hasLoggedIn", false);
        logineditor.remove("email");
        logineditor.apply();
        logineditor.commit();
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private void readData() {
        arrayList = new ArrayList<>();
        AssetManager assetManager = getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open("movies.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            try {
                String csvLine;
                while ((csvLine = reader.readLine()) != null) {
                    String[] row = csvLine.split(",");
                    Movie movieData = new Movie();
                    movieData.setMovieId(row[0]);
                    movieData.setName(row[1]);
                    movieData.setGeners(row[2]);
                    movieData.setUrl(row[3]);
                    arrayList.add(movieData);

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
    }

    private Boolean ifListHas(ArrayList<Movie> list,String movieId){
        for (Movie item : list){
            if (item.getMovieId().equalsIgnoreCase(movieId)){
                return true;
            }
        }
        return false;
    }

    private void setButtonState(){
        if (ratingList.size() == 5){
            submitButton.setEnabled(true);
        }
        else {
            submitButton.setEnabled(false);
        }
    }

    public void sendData(ArrayList<Movie> dataset) {
        Log.d("HomePage", "Sending data opened in LoginActivity");
        RequestParams params = new RequestParams();
        for (Movie item : dataset) {
            params.put(item.getMovieId(),item.getRatingStar());
        }
//        params.setUseJsonStreamer(true);

//        params.put("first_name", f_name.getText().toString());


        AsyncHttpClient client = new AsyncHttpClient();


        AsyncHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

//                String str = new String(bytes, "UTF-8");

                Log.d("RESP",String.valueOf(statusCode));
                Log.d("RESP",response.toString());
                Intent i = new Intent(MainActivity.this,ResultActivity.class);
                i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("response",response.toString());
                dialog.dismiss();
                getApplicationContext().startActivity(i);
//                response.getInt("status");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                dialog.dismiss();
//                Toast.makeText(HomepageActivity.this, "Unknown Error occured", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(int i , Header[] h, Throwable t, JSONObject j){
                super.onFailure(i,h,t,j);
                dialog.dismiss();
            }
        };


        client.post(url, params, responseHandler);
    }
}