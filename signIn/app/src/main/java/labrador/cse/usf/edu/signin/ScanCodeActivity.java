package labrador.cse.usf.edu.signin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    CharSequence  upc;
    ZXingScannerView ScannerView;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private static final String TAG = "ScanCodeActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScannerView = new ZXingScannerView(this);
        setContentView(ScannerView);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void handleResult(final Result result) {

        upc = result.getText();
        //Scanner.resultTextView.setText(upc);
        String url = "https://api.upcitemdb.com/prod/trial/lookup?upc=" + upc;

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                //   mTextView = (TextView) findViewById(R.id.validateResponse);
                // validation(response);
                //   mTextView.setText(validate);

                JSONTokener theTokener = new JSONTokener(response);
                JSONObject item;

                try{
                    //Scanner.resultTextView.setText(response);
                    item = (JSONObject) theTokener.nextValue();
                    if(item!=null){

                        //JSONObject items = item.getJSONObject("offers");
                        JSONObject name = item.getJSONArray("items").getJSONObject(0);
                        String title = name.getString("title");
                        Scanner.resultTextView.setText(title);

                        JSONObject price = item.getJSONArray("items").getJSONObject(0);
                        String price2 = name.getString("lowest_recorded_price");
                        Scanner.resultTextView2.setText(price2);




                    }
                }catch (JSONException e){
                    // Log.e(INNER_TAG, e.getMessage());
                }
                //Toast.makeText(getApplicationContext(),"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG,"Error :" + error.toString());

            }

        });


        mRequestQueue.add(mStringRequest);
        onBackPressed();
    }

    @Override
    protected void onPause(){
        super.onPause();

        ScannerView.stopCamera();
    }

    @Override
    protected void onResume(){
        super.onResume();
        ScannerView.setResultHandler(this);
        ScannerView.startCamera();
    }
}