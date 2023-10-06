package com.example.wetterapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout homeRL;
    private ProgressBar loadingPB;
    private TextView cityNameTV, temperatureTV, conditionTV;
    private RecyclerView weatherRV;
    private TextInputEditText cityEdt;
    private ImageView backIV, iconIV, searchIV;
    private ArrayList<WeatherRVModal> weatherRVModalArrayList;
    private WeatherRVAdapter weatherRVAdapter;
    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;
    private String cityName;
    private Object Volley;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.activity_main);
        homeRL =findViewById(R.id.idRLHome);
        loadingPB =findViewById(R.id.idPBLoading);
        cityNameTV =findViewById(R.id.idTVCityName);
        temperatureTV =findViewById(R.id.idTVTemperature);
        conditionTV =findViewById(R.id.idTVCondition);
        weatherRV =findViewById(R.id.idRvWeather);
        cityEdt =findViewById(R.id.idEdtCity);
        backIV = findViewById(R.id.idIVBlack);
        iconIV =findViewById(R.id.idIVIcon);
        searchIV = findViewById(R.id.idIVSearch);

        weatherRVModalArrayList = new ArrayList<>();
        weatherRVAdapter = new WeatherRVAdapter(this, weatherRVModalArrayList);

        weatherRV.setAdapter(weatherRVAdapter);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        cityName = getCityName(location.getLongitude(), location.getLatitude());
        getWeatherInfo(cityName);

       searchIV.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String city = cityEdt.getText().toString();
               if(city.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter your city", Toast.LENGTH_SHORT).show();

               }
               else{
                   cityNameTV.setText(cityName);
                   getWeatherInfo(city);
               }
           }
       });

     }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CODE)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Permission granted..", Toast.LENGTH_SHORT).show();
            }else
            {
                Toast.makeText(this, " Pleae provide the permssion", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }

    private String getCityName(double longitude, double latitude)
     {
         String cityName = "Not found";
         Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
         try {
             List<Address> addresses= gcd.getFromLocation(latitude,longitude, 10);
             for(Address adr: addresses)
             {
                 if(adr !=null)
                 {
                     String city = adr.getLocality();
                     if(city !=null && !city.equals(""))
                     {
                         cityName = city;
                     }
                     else{
                         Log.d("TAG", "CITY NOT FOUNT!");
                         Toast.makeText(this, "User City not Found!", Toast.LENGTH_SHORT).show();
                     }
                 }
             }

         }catch (IOException e)
         {
             e.printStackTrace();
         }
         return cityName;
     }
    private void getWeatherInfo(String cityName)
    {
        String url = "http://api.weatherapi.com/v1/forecast.json?key=20c8b866511145b28c122706221007&q="+cityName+ "&days=1&aqi=no&alerts=no";
        cityNameTV.setText(cityName);

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);



    }
}