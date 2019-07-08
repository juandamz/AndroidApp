package labrador.cse.usf.edu.signin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class createAccount extends AppCompatActivity {


    private static final String TAG = createAccount.class.getName();
    private Button btnRequest;
    private Button signUp;

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private TextView mTextView;
    String validate;

    String emailString;
    String passwordString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        btnRequest = (Button) findViewById(R.id.buttonRequest);
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAndRequestResponse();
            }
        });
    }

    private void sendAndRequestResponse() {

        EditText email = (EditText) findViewById(R.id.email);
        emailString = email.getText().toString();
        EditText password = (EditText) findViewById(R.id.password);
        passwordString = password.getText().toString();
        EditText password2 = (EditText) findViewById(R.id.password2);
        String password2String = password2.getText().toString();

        if(passwordString.equals(password2String))
        {
            String url = "http://www.juandavidmora.com/androidApp/login_validation.php?mobile=app&email=" + emailString + "&pass=" + passwordString + "&action_form=sign_up";

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
                    Log.i(TAG, "Error :" + error.toString());
                }
            });

            mRequestQueue.add(mStringRequest);

        }
        else{
            Toast.makeText(getApplicationContext(),"Passwords do not match. Try again", Toast.LENGTH_LONG).show();//display the response on screen
        }
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
                    sendMessage(userID);
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
    public void sendMessage(int userId) {
        //Save username and password
        SharedPreferences.Editor editor = getSharedPreferences("user_info", MODE_PRIVATE).edit();
        editor.putString("name", emailString);
        editor.putString("pass", passwordString);
        editor.apply();

        Intent intent = new Intent( getApplicationContext() , Inventory.class);
        Bundle bundle = new Bundle();
        bundle.putInt("UserId", userId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}