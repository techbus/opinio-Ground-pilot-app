package com.Techbus.ipinioapp;

import android.content.Intent;
import android.location.Location;
import android.os.StrictMode;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity  extends AppCompatActivity implements OnMapReadyCallback , GoogleMap.OnMarkerClickListener {

public static float jobId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Button button = (Button) findViewById(R.id.googlemaps_select_location);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, JobDetail.class);
                intent.putExtra("JobId", Float.toString(jobId));
                startActivity(intent);
            }
        });

        //Intent intent = new Intent(this, LoginActivity.class);
        //startActivity(intent);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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


    @Override
    public void onMapReady(GoogleMap map) {
String foutput="";
        Boolean valid=true;
        map.setOnMarkerClickListener(this);

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//map.setOnMarkerDragListener(onMa){};

        CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(12.9317,77.6227))
                .zoom(11)
                .bearing(0)
                .tilt(45)
                .build();

        //map.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex));
        map.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null);

        try {

            String tempUrl="http://192.168.6.165:1215/api/jobforporter/Ram";

            URL url = new URL(tempUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;

            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);

                foutput=foutput+output;
            }

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        try {
            JSONObject reader = new JSONObject(foutput);
            String status = reader.getString("status");
            if (status.equals("000"))
            {
                System.out.println("login success");
                System.out.println(reader);
                valid=true;
            }
            else
            {
                valid=false;//change here
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonArray = new JSONArray(foutput);

            int count = jsonArray.length(); // get totalCount of all jsonObjects
            for(int i=0 ; i< count; i++){   // iterate through jsonArray
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                JSONObject locationdetails = jsonObject.getJSONObject("locationdetails");
                JSONObject geometry = locationdetails.getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");
                Log.w("lat", location.getString("lat"));
                //JSONObject jsonObject1 = new JSONObject(foutput);
                jsonObject.getString("jobID");
                //jsonObject.getString("locationdetails[0].geometry[0].location[0].lat");
                Log.w("jobID", jsonObject.getString("jobID"));

                //jsonObject.getString("jobID")

               // System.out.println("jsonObject " + i + ": " + jsonObject.getString("jobID"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        map.addMarker(new MarkerOptions()
                .position(new LatLng(12.9200, 77.6500))
                .title("Panjabhi tadka").alpha(122)
                .snippet("HSR layout: Rs.30")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pack)));

        map.addMarker(new MarkerOptions()
                .position(new LatLng(12.9500, 77.6100))
                .title("big frost").alpha(123)
                .snippet("Bellandur: Rs.50").icon(BitmapDescriptorFactory.fromResource(R.drawable.pack)));

        map.addMarker(new MarkerOptions()
                .position(new LatLng(12.9650,77.5540))
                .title("biryani flames").alpha(124)
                .snippet("Koramangala: Rs.50").icon(BitmapDescriptorFactory.fromResource(R.drawable.pack)));
        map.setMyLocationEnabled(true);
        Location myLocation = map.getMyLocation();
       /* map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker arg0) {
                //arg0.remove();
                Toast.makeText(getApplicationContext()
                        , "Accepted porting at:  " + arg0.getTitle(), Toast.LENGTH_LONG)
                        .show();

                        return true;
            }
        });*/



       /* LatLng myyLatLng=new LatLng(myLocation.getLatitude(),myLocation.getLongitude());

        map.addMarker(new MarkerOptions()
                .position(myyLatLng)
                .title("Your Location")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));*/


    }

    public boolean onMarkerClick(Marker marker) {
        Log.i("GoogleMapActivity", "onMarkerClick");
        Toast.makeText(getApplicationContext(),
                "Job Selected: " + marker.getTitle(), Toast.LENGTH_LONG)
                .show();
        jobId=marker.getAlpha();
        return false;
    }



}
