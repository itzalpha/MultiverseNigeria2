package com.example.multiversenigeria2.me;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import com.example.multiversenigeria2.databinding.ActivityCommunityGuidelinesBinding;
import java.util.Objects;

public class CommunityGuidelines extends AppCompatActivity {

    ActivityCommunityGuidelinesBinding binding ;
    public String filename = "community.txt";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommunityGuidelinesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.loadUrl("file:///android_asset/" + filename);

    }
}