package uk.co.warmlight.apps.weatherstation;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
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

            doToast("Fetched latest data");
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
                apiGetWeather("Updating data...");
            }
        });

        apiGetWeather("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        apiGetWeather("Fetching data...");
    }

    public void apiGetWeather(String status) {

        doToast(status);

        // Instantiate the request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://weather.warmlight.co.uk/api.json";

        // Request a JSON object response from the provided URL
        JsonObjectRequest jso = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        processResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // No internet connection
                if(error instanceof NoConnectionError) {
                    doToast("Unable to contact server, check your internet connection");
                } else {
                    // Something else went wrong
                    Log.e("WeatherStation", "Unable to get weather", error);
                    doToast("Unable to get weather data");
                }
            }
        });
        queue.add(jso);
    }

    public void doToast(String message) {

        if (message.length() > 0) {
            final Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
        }

    }

}
