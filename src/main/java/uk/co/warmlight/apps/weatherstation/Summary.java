package uk.co.warmlight.apps.weatherstation;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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

        Log.i("WeatherStation", "Refreshing");

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
                apiGetWeather();
            }
        });

        apiGetWeather();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

}
