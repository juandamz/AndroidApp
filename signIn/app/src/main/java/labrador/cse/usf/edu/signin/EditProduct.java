package labrador.cse.usf.edu.signin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class EditProduct extends AppCompatActivity {

    TextInputEditText prodName;
    TextInputEditText qty;
    TextInputEditText units;
    TextInputEditText low;

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private static final String TAG = EditProduct.class.getName();
    int userID;
    String prod_id;

    Button update;
    Button remove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        prodName = findViewById(R.id.prod_name);
        qty = findViewById(R.id.qty);
        units = findViewById(R.id.units);
        low = findViewById(R.id.low);
        update = findViewById(R.id.Update);
        remove = findViewById(R.id.Remove);
        SharedPreferences prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        userID = prefs.getInt("UserId", 0);
        prod_id = prefs.getString("prod_id", "try again");

        getProdInfo();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateSqlCall();
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoveSqlCall();
            }
        });

    }


    void getProdInfo(){
        SharedPreferences prefs = getSharedPreferences("user_info", MODE_PRIVATE);

        String url = "http://juandavidmora.com/androidApp/jsonProducts.php?action=viewProd&prodId=" + prod_id;

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONTokener theTokener = new JSONTokener(response);
                JSONArray prodInfo;

                try{
                    prodInfo = (JSONArray) theTokener.nextValue();
                    if(prodInfo != null){
                        JSONObject user = prodInfo.getJSONObject(0);

                        String prod_Name = user.getString("Prod_name");
                        String Prod_qty = user.getString("Prod_qty");
                        String Prod_low_limit = user.getString("Prod_low_limit");
                        String Prod_unit = user.getString("Prod_unit");
                        prodName.setText(prod_Name);
                        qty.setText(Prod_qty);
                        units.setText(Prod_unit);
                        low.setText(Prod_low_limit);

                    }

                }catch (JSONException e){
                    // Log.e(INNER_TAG, e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG,"Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);
    }

    void RemoveSqlCall(){

        final String nameS = prodName.getText().toString();
        final String qtyS = qty.getText().toString();
        final String lowS = low.getText().toString();
        final String unitS = units.getText().toString();

        if(nameS == "" || qtyS == "" || lowS == "" || unitS == ""){
            Toast.makeText(getApplicationContext(),"Please fill in all fields :", Toast.LENGTH_LONG).show();//display the response on screen
        }
        String url = "http://juandavidmora.com/androidApp/jsonProducts.php?userId=" + userID +"&action=delete&Product_ID=" + prod_id ;

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                startActivity(new Intent(getApplicationContext(), Inventory.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG,"Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);

    }
    void UpdateSqlCall(){

        final String nameS = prodName.getText().toString();
        final String qtyS = qty.getText().toString();
        final String lowS = low.getText().toString();
        final String unitS = units.getText().toString();

        if(nameS == "" || qtyS == "" || lowS == "" || unitS == ""){
            Toast.makeText(getApplicationContext(),"Please fill in all fields :", Toast.LENGTH_LONG).show();//display the response on screen
        }
            String url = "http://juandavidmora.com/androidApp/jsonProducts.php?userId="+ userID +"&action=update&Product_Name=" + nameS + "&Product_qty=" + qtyS + "&Prod_low_limit=" + lowS + "&Prod_unit=" + unitS + "&Product_ID=" + prod_id ;

            //RequestQueue initialized
            mRequestQueue = Volley.newRequestQueue(this);

            //String Request initialized
            mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    startActivity(new Intent(getApplicationContext(), Inventory.class));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i(TAG,"Error :" + error.toString());
                }
            });

            mRequestQueue.add(mStringRequest);
        }

    }
