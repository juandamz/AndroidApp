package labrador.cse.usf.edu.signin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONObject;

public class  MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private Button btnRequest;
    private Button signUp;

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private TextView mTextView;
    String validate;

    TextInputEditText username;
    TextInputEditText passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnRequest = (Button) findViewById(R.id.buttonRequest);
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                sendAndRequestResponse();
            }
        });

        signUp = findViewById(R.id.signUp);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), createAccount.class));
            }
        });

        username = (TextInputEditText) findViewById(R.id.email);
        passwordText = (TextInputEditText) findViewById(R.id.password);
        SharedPreferences prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        String restoredEmail = prefs.getString("name", null);
        String restoredPass = prefs.getString("pass", null);
        if(restoredEmail != null) {
            String email = prefs.getString("name", "No name defined");
            username.setText(email);
        }
        if(restoredPass != null) {
            String password = prefs.getString("pass", "No name defined");
            passwordText.setText(password);
        }


    }

    private void sendAndRequestResponse() {

        EditText email = (EditText) findViewById(R.id.email);
        final String emailString = email.getText().toString();
        EditText password = (EditText) findViewById(R.id.password);
        final String passwordString = password.getText().toString();

        String url = "http://www.juandavidmora.com/androidApp/login_validation.php?mobile=app&email=" + emailString + "&pass=" + passwordString + "&action_form=sign_in";

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                validation(response, emailString, passwordString);

                //Toast.makeText(getApplicationContext(),"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG,"Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);
    }


    private void validation(String theResult, String email, String password){

        JSONTokener theTokener = new JSONTokener(theResult);
        JSONArray loginInfo;

        try{
            loginInfo = (JSONArray) theTokener.nextValue();
            if(loginInfo != null){
                JSONObject user = loginInfo.getJSONObject(0);
                int userID = user.getInt("UserID");

                if(userID > 0){
                    sendMessage(userID, email, password);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Try again", Toast.LENGTH_LONG).show();//display the response on screen
                }
            }

        }catch (JSONException e){
            // Log.e(INNER_TAG, e.getMessage());
        }
    }

    /* Called when the user clicks the Send button */
    public void sendMessage(int userId, String email, String password) {
        //Save username and password
        SharedPreferences.Editor editor = getSharedPreferences("user_info", MODE_PRIVATE).edit();
        editor.putString("name", email);
        editor.putString("pass", password);
        editor.putInt("UserId", userId);
        editor.apply();

        Intent intent = new Intent( getApplicationContext() , Inventory.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


}
