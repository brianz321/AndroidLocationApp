package com.zeff.brian.locationfinder;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void getLocation(View view)
    {
        String api_key = "AIzaSyBRZl3FHhiEQMJjpxFMrNKpbT1lmQwXUQk";
        EditText text = (EditText)findViewById(R.id.locationText);
        String location = text.getText().toString();
        String httpurl = "https://maps.googleapis.com/maps/api/geocode/json?address=";
        location = location.replace(' ','+');
        httpurl = httpurl + location + "&key=" + api_key;
        getLatitudeLongitude(httpurl);
    }

    public void getLatitudeLongitude(String httpURL){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, httpURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        findLatitudeLongitude(response);// parse through JSON here
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {}
                });
        queue.add(stringRequest);
    }

    public void findLatitudeLongitude(String response){
        Intent intent = new Intent(this, DisplayMapActivity.class);
        try{
            JSONObject json = new JSONObject(response);
            JSONArray jsonArray = json.getJSONArray("results");
            double latitude = jsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
            double longitude = jsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
            intent.putExtra("Latitude",latitude);
            intent.putExtra("Longitude", longitude);
            startActivity(intent);
        }
        catch (JSONException e){
            e.getLocalizedMessage();
        }
    }
}
