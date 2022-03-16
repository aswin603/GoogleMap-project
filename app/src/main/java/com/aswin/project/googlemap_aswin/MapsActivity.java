package com.aswin.project.googlemap_aswin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.aswin.project.googlemap_aswin.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private FusedLocationProviderClient client;
    private SupportMapFragment mapFragment;
    String street = null, userName;
    MarkerOptions markerOptions;

    private double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        binding.postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });


        client = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 7);
        }


    }

    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {


                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        mMap = googleMap;
                        lat = location.getLatitude();
                        lon = location.getLongitude();
                        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(lat, lon, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        String cityName = addresses.get(0).getLocality();
                        String streetName = addresses.get(0).getAddressLine(0);
                        if (streetName != null) {
                            String s1 = streetName.substring(streetName.indexOf(", ") + 1);
                            s1.trim();
                            street = s1.split(",")[0];
                        }
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        if (street != null) {
                            markerOptions = new MarkerOptions().position(latLng).title("You are currently in " + street);
                        } else {
                            markerOptions = new MarkerOptions().position(latLng).title("Your current location");
                        }
                        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MapsActivity.this, R.raw.retro_style));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(latLng)
                                .zoom(19f)
                                .bearing(100f)
                                .tilt(75f)
                                .build();
                        final Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            }
                        }, 300);
                        googleMap.addMarker(markerOptions);
                    }

                });
            }

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 7) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MapsActivity.this, R.raw.retro_style));

    }

    @SuppressLint("RestrictedApi")
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText NameEt = new EditText(this);
        NameEt.setHint("Enter name");
        builder.setTitle("Enter you name");
        builder.setMessage("Location : " + street + "\n" + "Latitude : " + lat + "\n" + "Longitude : " + lon);
        //builder.setMessage("Enter name");
        builder.setView(NameEt, 50, 0, 50, 0);
        builder.setCancelable(false);
        builder.setPositiveButton("Post to backend", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                userName = NameEt.getText().toString();
                if (!userName.equals("")) {
                    postData();
                } else {
                    showDialog();
                    Toast.makeText(getApplicationContext(), "Add your name", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    private void postData() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://dev.d-inventions.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        DataModal modal = new DataModal(lat, lon, street, userName);

        Call<DataModal> call = retrofitAPI.createPost(modal);
        call.enqueue(new Callback<DataModal>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onResponse(@NonNull Call<DataModal> call, @NonNull Response<DataModal> response) {
                if (!response.isSuccessful()){
                    Log.e("code","code :"+response.code());
                    return;
                }
                Toast.makeText(MapsActivity.this, "Data added to API", Toast.LENGTH_SHORT).show();
                binding.progressBar.setVisibility(View.GONE);

                DataModal responseFromAPI = response.body();

                assert responseFromAPI != null;

                String responseString = "Response Code : " + response.code() + "\nstatus : " + responseFromAPI.getStatus() + "\n" + "message : " + responseFromAPI.getMessage();
                //Toast.makeText(MapsActivity.this, responseString, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MapsActivity.this,DetailsActivty.class);
                intent.putExtra("response",responseString);
                intent.putExtra("lat",lat+"");
                intent.putExtra("lon",lon+"");
                intent.putExtra("name",userName);
                intent.putExtra("location",street);
                startActivity(intent);
            }

            @Override
            public void onFailure(@NonNull Call<DataModal> call, @NonNull Throwable t) {
                Log.e("error",t.getMessage());
            }
        });

    }

}