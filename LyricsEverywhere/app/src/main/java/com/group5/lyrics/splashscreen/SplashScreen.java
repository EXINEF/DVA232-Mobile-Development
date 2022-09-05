package com.group5.lyrics.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.group5.lyrics.MainActivity;

public class SplashScreen extends AppCompatActivity {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        startActivity(new Intent(this, MainActivity.class));
    }
}
