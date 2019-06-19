package labrador.cse.usf.edu.projectbull;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONObject;

public class MainActivity extends ActionBarActivity {
    String finalweather;

    @Overrideprotected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.text1);
        String lat = "28.0546";
        String lon = "-82.4131";
        // Create a new JsonObjectRequest.
        JsonObjectRequest request = new JsonObjectRequest("http://api.wunderground.com/api/4d59f686ea764287/conditions/q/" + lat + "," + lon + ".json", null,
                new Response.Listener<JSONObject>() {
                    @Overridepublic
                    void onResponse(JSONObject response) {
                        //mTextView.setText(response.toString());
                        finalweather = parseAndNotify(response.toString());
                        mTextView.setText(finalweather);
                    }
                },

                new Response.ErrorListener() {
                    @Overridepublic
                    void onErrorResponse(VolleyError error) {
                        mTextView.setText(error.toString());
                    }
                }
        );
        // With the request created, simply add it to our Application's RequestQueue
        VolleyApplication.getInstance().getRequestQueue().add(request);
    }


    private TextViewmTextView;

    private String parseAndNotify(String theResult) {

        JSONTokener theTokener = new JSONTokener(theResult);
        JSONObject theWeatherResult;

        try {
            theWeatherResult = (JSONObject) theTokener.nextValue();
            if (theWeatherResult != null) {
                JSONObject curWeather = theWeatherResult.getJSONObject("current_observation");
                JSONObject curCity = curWeather.getJSONObject("observation_location");
                String temperature = curWeather.getString("temperature_string");
                String weather = curWeather.getString("weather");
                String city = curCity.getString("full");

                return "The weather at " + city + " is " + weather + ", with " + temperature + " temperature";
            }
        } catch (JSONException e) {
            // Log.e(INNER_TAG, e.getMessage());
        }
        return null;
    }
}
