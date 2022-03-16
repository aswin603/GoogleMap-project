package com.aswin.project.googlemap_aswin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.aswin.project.googlemap_aswin.databinding.ActivityDetailsBinding;
import com.aswin.project.googlemap_aswin.databinding.ActivityMapsBinding;

public class DetailsActivty extends AppCompatActivity {

    private ActivityDetailsBinding binding;
    String latitude,longitude,location,userName,response;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        latitude = intent.getStringExtra("lat");
        longitude = intent.getStringExtra("lon");
        location = intent.getStringExtra("location");
        userName = intent.getStringExtra("name");
        response = intent.getStringExtra("response");

        binding.latTxt.setText("Latitude : "+latitude);
        binding.lonTxt.setText("Longitude : "+longitude);
        binding.nameTxt.setText("Username :"+userName);
        binding.responseTxt.setText("Response : "+response);
        binding.locationTxt.setText("Location : "+location);






    }
}