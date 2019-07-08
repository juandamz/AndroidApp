package labrador.cse.usf.edu.signin;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.widget.AdapterView.OnItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;



public class Inventory extends AppCompatActivity implements SensorEventListener {

    int userID;

    //new json handling
    private JsonArrayRequest JsonArrayRequest;
    private Context mContext;

    //json data handling
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private static final String TAG = Inventory.class.getName();
    Gson gson = new Gson();

    //accelerometer
    private SensorManager sensorManager;
    Sensor accelerometer;

    //list
    ListView listView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home: //inv
                    getList(userID, "view");
                    return true;
                case R.id.navigation_dashboard: //low
                    getList(userID, "low");
                    return true;
                case R.id.navigation_notifications: //add
                    //mTextMessage.setText(R.string.title_notifications);
                    startActivity(new Intent(getApplicationContext(),addProd.class));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        userID = prefs.getInt("UserId", 0);
        getList(userID, "view");

        setContentView(R.layout.activity_inventory);

        listView = (ListView) findViewById(R.id.listview);

        //accelerometer
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(Inventory.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_scan:
                startActivity(new Intent(getApplicationContext(), Scanner.class));
                return true;
            case R.id.action_logout:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                return true;
            default:return super.onOptionsItemSelected(item);
        }
    }

    private void getList(int userID, String action) {


        String url = "http://juandavidmora.com/androidApp/jsonProducts.php?action=" + action + "&userId=" + userID;

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //createList(response);
                //Toast.makeText(getApplicationContext(),"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen

                JSONTokener theTokener = new JSONTokener(response);
                JSONArray productNames;

                try {
                    productNames = (JSONArray) theTokener.nextValue();
                    JSONObject user = productNames.getJSONObject(0);
                    int n = productNames.length();

                    String[] names = new String[n];
                    String[] qtys = new String[n];
                    final String[] ids = new String[n];

                    for(int i=0; i < productNames.length() ; i++) {
                        user = productNames.getJSONObject(i);
                        String name = user.getString("Prod_name");
                        String qty = user.getString("Prod_qty");
                        String id = user.getString("Prod_id");
                        names[i] = name;
                        qtys[i] = qty;
                        ids[i] = id;
                    }
                    MyListAdapter adapter = new MyListAdapter(Inventory.this , names, qtys, ids);
                    listView = (ListView)findViewById(R.id.listview);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                            final String prod_id = ids[position];
                            SharedPreferences.Editor editor = getSharedPreferences("user_info", MODE_PRIVATE).edit();
                            editor.putString("prod_id", prod_id);
                            editor.apply();
                            startActivity(new Intent(getApplicationContext(),EditProduct.class));
                        }
                    });

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


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        //sensorEvent.values[0] is X, [1] is Y, [2] is Z
        if(sensorEvent.values[0] > 20){

            SharedPreferences prefs = getSharedPreferences("user_info", MODE_PRIVATE);
            String restoredText = prefs.getString("name", null);
            if(restoredText != null)
            {
                finish();
            }

        }

    }

}
