package com.example.bichitos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);
        SharedPreferences prefs = getSharedPreferences("BichitosPrefs", MODE_PRIVATE);
        String petJson = prefs.getString("petData",null);

        if(petJson!=null){
            PetData petData = new Gson().fromJson(petJson, PetData.class);
            Intent intent;
            if(petData.isHatched()){
                intent = new Intent(this, HomeActivity.class);
            }else{
                intent=new Intent(this, EggActivity.class);
            }
            startActivity(intent);
            finish();
        }
    }
}