package labrador.cse.usf.edu.signin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private TextView mTextView;
    String validate;

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
    }

    private void sendAndRequestResponse() {

        EditText email = (EditText) findViewById(R.id.email);
        String emailString = email.getText().toString();
        EditText password = (EditText) findViewById(R.id.password);
        String passwordString = password.getText().toString();

        String url = "http://www.juandavidmora.com/androidApp/login_validation.php?mobile=app&email=" + emailString + "&pass=" + passwordString + "&action_form=sign_in";

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


             //   mTextView = (TextView) findViewById(R.id.validateResponse);
                validation(response);
             //   mTextView.setText(validate);

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


    private void validation(String theResult){

        JSONTokener theTokener = new JSONTokener(theResult);
        JSONArray loginInfo;

        try{
            loginInfo = (JSONArray) theTokener.nextValue();
            if(loginInfo != null){
                JSONObject user = loginInfo.getJSONObject(0);
                int userID = user.getInt("UserID");
//
                if(userID > 0){
                    startActivity(new Intent(getApplicationContext(), Inventory.class));
                }
                else{
                    Toast.makeText(getApplicationContext(),"Try again", Toast.LENGTH_LONG).show();//display the response on screen
                }
            }

        }catch (JSONException e){
            // Log.e(INNER_TAG, e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


}
