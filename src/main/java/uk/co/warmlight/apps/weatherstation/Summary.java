package uk.co.warmlight.apps.weatherstation;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Summary extends AppCompatActivity {

    public void processResponse(JSONObject Response) {

        final TextView timestampView = (TextView) findViewById(R.id.timestamp);
        final TextView temperatureView = (TextView) findViewById(R.id.temperature);
        final TextView pressureView = (TextView) findViewById(R.id.pressure);
        final TextView humidityView = (TextView) findViewById(R.id.humidity);
        final TextView windspeedView = (TextView) findViewById(R.id.windspeed);
        final TextView windDirectionView = (TextView) findViewById(R.id.windDirection);

        try {
            String temperature = Response.getString("temperature");
            String pressure = Response.getString("pressure");
            String humidity = Response.getString("humidity");
            String windspeed = Response.getString("windspeed");
            String windDirection = Response.getString("wind_direction");
            long unixTimestamp = Response.getLong("timestamp");
            long javaTimestamp = unixTimestamp * 1000;
            Date date = new Date(javaTimestamp);
            String timestamp = new SimpleDateFormat("dd/MM/yyyy @ kk:mm", Locale.ENGLISH).format(date);
            timestampView.setText(timestamp);
            temperatureView.setText(temperature);
            pressureView.setText(pressure);
            humidityView.setText(humidity);
            windspeedView.setText(windspeed);
            windDirectionView.setText(windDirection);
        } catch (JSONException e) {
            Log.e("WeatherStation", "Cannot parse response", e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        apiGetWeather();
    }

    public void apiGetWeather() {

        // Instantiate the request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://weather.warmlight.co.uk/api.json";

        // Request a string response from the provided URL
        JsonObjectRequest jso = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        processResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("WeatherStation", "Unable to get weather", error);
            }
        });
        queue.add(jso);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_summary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
