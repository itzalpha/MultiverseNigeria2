package com.example.multiversenigeria2.me;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.multiversenigeria2.databinding.ActivityAboutShareVerseBinding;

public class AboutShareVerse extends AppCompatActivity {

    ActivityAboutShareVerseBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityAboutShareVerseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
    }

}