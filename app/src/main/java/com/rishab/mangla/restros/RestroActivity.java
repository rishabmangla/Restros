package com.rishab.mangla.restros;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rishab.mangla.restros.adater.CustomListAdapter;
import com.rishab.mangla.restros.app.AppController;
import com.rishab.mangla.restros.model.Restro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

public class RestroActivity extends Activity {
    // Log tag
    private static final String TAG = RestroActivity.class.getSimpleName();

    // GPSTracker class
    GPSTracker gps;

    // Restros json url
    private static final String url = "http://staging.couponapitest.com/task_data.txt";
    private ProgressDialog pDialog;
    private List<Restro> restroList = new ArrayList<Restro>();
    private ListView listView;
    private CustomListAdapter adapter;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restro);

        gps = new GPSTracker(RestroActivity.this);



        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, restroList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

//        // changing action bar color
//        getActionBar().setBackgroundDrawable(
//                new ColorDrawable(Color.parseColor("#1E90FF")));

    }

    @Override
    protected void onResume() {
        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // \n is for new line
//            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            Log.i("rishab","Latitude : " + latitude + " longitude " + longitude);

            getResponse(latitude, longitude);
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        super.onResume();
    }

    private void getResponse(final double latitude, final double longitude){

        // Creating volley request obj
        JsonObjectRequest restroReq = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        Log.i("rishab", "response : " + response.toString());

                        try {
                            JSONObject data = response.getJSONObject("data");
                            for(int i = 0; i < 36; ++i){
                                if(!data.has(String.valueOf(i)))
                                    continue;
                                JSONObject obj = data.getJSONObject(String.valueOf(i));

                                Restro restro = new Restro();
                                restro.setName(obj.getString("OutletName"));
                                restro.setThumbnailUrl(obj.getString("LogoURL"));
                                restro.setLocation(obj.getString("NeighbourhoodName"));

                                float[] dist = new float[2];
                                Location.distanceBetween(latitude, longitude, obj.getDouble("Latitude"),  obj.getDouble("Longitude"), dist);
                                restro.setDistance(dist[0]);

                                // Genre is json array
                                JSONArray cuisineArry = obj.getJSONArray("Categories");
                                ArrayList<String> cuisine = new ArrayList<String>();
                                for (int j = 0; j < cuisineArry.length(); j++) {
                                    JSONObject category = (JSONObject) cuisineArry.get(j);
                                    if(category.getString("CategoryType").equals("Cuisine") || category.getString("CategoryType").equals("TypeOfRestaurant"))
                                        cuisine.add(category.getString("Name"));
                                }
                                restro.setCuisine(cuisine);

                                // adding restro to restro array
                                restroList.add(restro);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("mangla", "error : " + e);
                            Toast.makeText(RestroActivity.this, "Check your network connection.", Toast.LENGTH_SHORT).show();
                        }

                        Collections.sort(restroList, new Comparator<Restro>() {
                            @Override
                            public int compare(Restro lhs, Restro rhs) {
                                return (int) (lhs.getDistance() - rhs.getDistance());
                            }
                        });
//                        // notifying list adapter about data changes
//                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.i("mangla", "volleyerrorr : " + error);
                Toast.makeText(RestroActivity.this, "Check your network connection.", Toast.LENGTH_SHORT).show();
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(restroReq);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_restro, menu);
        return true;
    }

}
