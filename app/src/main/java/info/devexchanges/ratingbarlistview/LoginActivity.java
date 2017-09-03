package info.devexchanges.ratingbarlistview;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ayush on 14/01/17.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "LoginActivity";
    public final static String PREFS_NAME = "CAPTECHAPP";
    ImageView logo;
    TextView register;
    EditText email,pass ;
    EditText f_name,l_name,password,re_password,contact_no,email_id;
    Button login ;  //creating button variables
    ProgressDialog dialog;
    String server_ip,url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        homepageSession();
        initialiseVariables();  //Initialising Variables
        initialiseListeners();  //Initialising Listeners
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (TextUtils.isEmpty(email.getText().toString())) {
//                    Toast.makeText(ActivityLogin.this, getString(R.string.activity_login_noemail), Toast.LENGTH_SHORT).show();
//                } else if (TextUtils.isEmpty(pass.getText().toString())) {
//                    Toast.makeText(ActivityLogin.this,getString(R.string.activity_login_nopass), Toast.LENGTH_SHORT).show();
//                } else {
//                    dialog.setMessage(getString(R.string.activity_login_login_dialog));
//                    dialog.show();
//                    sendData();
//                }
//            }
//        });


    }

    private void homepageSession(){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
//Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
        boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);

        if(hasLoggedIn)
        {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }

    private void initialiseVariables() {
        server_ip = getString(R.string.server_ip);
        email = (EditText) findViewById(R.id.et_login_email);
        pass = (EditText) findViewById(R.id.et_login_pass);
        login = (Button) findViewById(R.id.btn_login) ;
        register = (TextView) findViewById(R.id.tv_register);
        dialog= new ProgressDialog(LoginActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        url = server_ip.concat("/login");
    }

    private void initialiseListeners(){
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    public void sendData() {
        Log.d(TAG, "Sending data opened in LoginActivity");
        RequestParams params = new RequestParams();
        params.put("email", email.getText().toString());
        params.put("pass", pass.getText().toString());

//        params.setUseJsonStreamer(true);

//        params.put("first_name", f_name.getText().toString());


        AsyncHttpClient client = new AsyncHttpClient();


        AsyncHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

//                String str = new String(bytes, "UTF-8");
                try {
                    String login_status = response.getString("status");
                    Log.d(TAG, response.toString());
                    switch (login_status){
                        case "1" :
                            dialog.dismiss();
                            Log.d(TAG, " Login Success");

                            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0); // 0 - for private mode
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putBoolean("hasLoggedIn", true);
                            editor.putString("email",email.getText().toString());
                            editor.commit();
                            Intent i =new Intent(LoginActivity.this, MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.putExtra("isHomepageFirstTimeOpened",true);
                            startActivity(i);
                            finish();
                            Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                            break;
                        case "noemail" :
                            dialog.dismiss();
                            Toast.makeText(LoginActivity.this, getString(R.string.activity_login_wrongemail), Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Login fail : Wrong Email");
                            break ;
                        case "0" :
                            dialog.dismiss();
                            Toast.makeText(LoginActivity.this, getString(R.string.activity_login_wrongpass), Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Login Fail : Wrong Password");
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                response.getInt("status");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d(TAG,"Send data Fail");
                dialog.dismiss();
                Toast.makeText(LoginActivity.this, getString(R.string.activity_login_senddatafail), Toast.LENGTH_SHORT).show();
            }
        };


        client.post(url, params, responseHandler);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_login :
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Toast.makeText(LoginActivity.this, getString(R.string.activity_login_noemail), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(pass.getText().toString())) {
                    Toast.makeText(LoginActivity.this,getString(R.string.activity_login_nopass), Toast.LENGTH_SHORT).show();
                } else {
                    dialog.setMessage(getString(R.string.activity_login_login_dialog));
                    dialog.show();
                    sendData();
                }
                break;
            case R.id.tv_register :
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
                break;

        }

    }
}

