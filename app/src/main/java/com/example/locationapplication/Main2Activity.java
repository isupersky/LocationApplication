package com.example.locationapplication;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Main2Activity extends AppCompatActivity implements LocationListener{
    private  RequestQueue mQueue;
    Button getLocationBtn;
    TextView locationText;
    double lat,lon;
    public String imgId;
    LocationManager locationManager;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        getLocationBtn = (Button)findViewById(R.id.getLocationBtn);
        locationText = (TextView)findViewById(R.id.locationText);
        mQueue = Volley.newRequestQueue(this);

        getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();

                // String weatherUrl = "http://api.openweathermap.org/data/2.5/weather?lat="+22+"&lon="+75+"&units=metric&APPID=2bf54d7c3d8918fa21a85b6dcc979c9a";
                //jsonParse(weatherUrl);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });

        View v= navigationView.getHeaderView(0);
        img = v.findViewById(R.id.imageViewHeader);

        String imgUrl= "http://openweathermap.org/img/w/" + imgId+ ".png";
        //  img = findViewById(R.id.imageViewHeader);
        Glide.with(this).load(imgUrl).into(img);



    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);

            //new try
            Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
            onLocationChanged(location);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        lat =location.getLatitude();
        lon= location.getLongitude();
        String weatherUrl = "http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&units=metric&APPID=2bf54d7c3d8918fa21a85b6dcc979c9a";
        locationText.setText("Current Location: " + lat + ", " + lon);

        // Toast.makeText(this,""+lat+ lon,Toast.LENGTH_SHORT).show();

        jsonParse(weatherUrl);

    }
    private void jsonParse(String url) {
        //String url = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd000001f0af90f3616349b86afb5082c578bf37&format=json&filters[state]=Gujarat";
        // Toast.makeText(getApplicationContext(),"yaha aa rha hai 1",Toast.LENGTH_SHORT).show();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Toast.makeText(getApplicationContext(),"yaha aa rha hai 2",Toast.LENGTH_SHORT).show();
                            JSONArray jsonArray = response.getJSONArray("weather");
                            JSONObject main = response.getJSONObject("main");
                            //Toast.makeText(this, ""+jsonArray.length(), Toast.LENGTH_SHORT).show();
                            // Toast.makeText(getApplicationContext(),""+jsonArray.length(),Toast.LENGTH_SHORT).show();

                            // for(int i = 0; i<jsonArray.length() ; i++){
                            //JSONObject record = jsonArray.getJSONObject(i);
                            String temp = main.getString("temp");
                            Toast.makeText(getApplicationContext(), "Temperature : "+ temp, Toast.LENGTH_SHORT).show();
                            String pressure = main.getString("pressure");
                            Toast.makeText(getApplicationContext(), "Pressure : "+ pressure, Toast.LENGTH_SHORT).show();

                            JSONObject Weatherobject =jsonArray.getJSONObject(0) ;
                            imgId = Weatherobject.getString("icon");

                            Toast.makeText(getApplicationContext(), "id : "+ Weatherobject.getString("icon"), Toast.LENGTH_SHORT).show();


                            //String temp = main.getString("timestamp");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), ""+ error, Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(request);
    }


    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(Main2Activity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }


}
