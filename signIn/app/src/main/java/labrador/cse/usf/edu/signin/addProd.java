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

public class addProd extends AppCompatActivity {

    TextInputEditText prodName;
    TextInputEditText qty;
    TextInputEditText units;
    TextInputEditText low;

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private static final String TAG = addProd.class.getName();
    int userID;

    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prod);

        prodName = findViewById(R.id.prod_name);
        qty = findViewById(R.id.qty);
        units = findViewById(R.id.units);
        low = findViewById(R.id.low);
        add = findViewById(R.id.Update);
        SharedPreferences prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        userID = prefs.getInt("UserId", 0);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlCall();
            }
        });

    }

    void sqlCall(){

        final String nameS = prodName.getText().toString();
        final String qtyS = qty.getText().toString();
        final String lowS = low.getText().toString();
        final String unitS = units.getText().toString();

        if(nameS == "" || qtyS == "" || lowS == "" || unitS == ""){
            Toast.makeText(getApplicationContext(),"Please fill in all fields :", Toast.LENGTH_LONG).show();//display the response on screen
        }
        else{
            String url = "http://juandavidmora.com/androidApp/jsonProducts.php?userId="+ userID +"&action=add&Product_Name=" + nameS + "&Product_qty=" + qtyS + "&Prod_low_limit=" + lowS + "&Prod_unit=" + unitS;

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





}
