package com.johnvincent.testlocator.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.johnvincent.testlocator.R;
import com.johnvincent.testlocator.adapter.NearbyAdapter;
import com.johnvincent.testlocator.model.JsonResponse;
import com.johnvincent.testlocator.network.RESTClient;
import com.johnvincent.testlocator.util.GPSTracker;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TextView tvError;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private NearbyAdapter adapter;
    private List<JsonResponse> jsonResponseList = new ArrayList<>();
    private SwipeRefreshLayout pullToRefresh;
    private LocationManager mLocationManager;
    private double mLat = 0, mLon = 0;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    private GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // call initUI function
        initUI();

        // check permission for security
        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will
                // execute every time, else your else part will work
            } else {
                getLocation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Get user's location
     */
    private void getLocation(){
        gps = new GPSTracker(MainActivity.this);

        // check if GPS enabled
        if(gps.canGetLocation()){
            // get location and set to loadListing method
            mLat = gps.getLatitude();
            mLon = gps.getLongitude();
            loadListings(mLat, mLon);
        }else{
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    /**
    * Initialize UI components, TextViews, RecyclerView etc...
    */
    private void initUI(){

        toolbar = (Toolbar)findViewById(R.id.layout_toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        adapter = new NearbyAdapter(jsonResponseList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        tvError = (TextView)findViewById(R.id.tv_error);
        pullToRefresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadListings(mLat,mLon);
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    /**
     * Load nearby hospitals
     * @params: Latitude, Longitude
     */
    private void loadListings(double lat, double lon){
        //display loading dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.dialog_message));
        progressDialog.show();

        RESTClient.getInstance().getApiService()
                .getNearMeList(lat, lon)
                .enqueue(new Callback<List<JsonResponse>>() {
                    @Override
                    public void onResponse(Call<List<JsonResponse>> call, Response<List<JsonResponse>> response) {
                        progressDialog.dismiss();
                        if(response.isSuccessful()){
                            // Load the JSON data to List and add to adapter
                            jsonResponseList = response.body();
                            adapter = new NearbyAdapter(jsonResponseList);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }else{
                            // if the server returns data, display error message
                            tvError.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<JsonResponse>> call, Throwable t) {
                        progressDialog.dismiss();
                        // display error message if there is critical error
                        tvError.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_CODE_PERMISSION :
                getLocation();
                break;

        }
    }
}
