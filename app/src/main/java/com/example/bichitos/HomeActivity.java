package com.example.bichitos;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

public class HomeActivity extends AppCompatActivity {
    private TextView petNameText;
    private TextView petInfoText;
    private SharedPreferences prefs;
    private PetData petData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        loadPetData();
        displayPetInfo();
    }

    private void initViews() {
        petNameText = findViewById(R.id.petNameText);
        petInfoText = findViewById(R.id.petInfoText);
    }

    private void loadPetData() {
        prefs = getSharedPreferences("BichitosPrefs", MODE_PRIVATE);
        String petJson = prefs.getString("petData", null);

        if (petJson != null) {
            petData = new Gson().fromJson(petJson, PetData.class);
        }
    }

    private void displayPetInfo() {
        if (petData != null) {
            String petName = getString(R.string.pet_name_display, petData.getName());
            petNameText.setText(petName);

            String petInfo = getString(R.string.pet_info_display,
                    petData.getGender(),
                    petData.getHappiness(),
                    petData.getHunger(),
                    petData.getCarePoints(),
                    petData.getNeglectPoints()
            );
            petInfoText.setText(petInfo);
        }
    }
}