package com.example.bichitos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class WelcomeActivity extends AppCompatActivity {

    private ImageView eggImage;
    private ImageView englishEggImage;
    private ImageView spanishEggImage;
    private ImageView musicButton;
    private GifImageView musicAnim;
    private boolean languajeSelected=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);
        SharedPreferences prefs = getSharedPreferences("BichitosPrefs", MODE_PRIVATE);
        String petJson = prefs.getString("petData",null);

        if(petJson!=null){//if no data
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

        eggImage=findViewById(R.id.eggImage);
        englishEggImage=findViewById(R.id.englishEggImage);
        spanishEggImage=findViewById(R.id.spanishEggImage);
        musicButton=findViewById(R.id.musicButton);
        musicAnim=findViewById(R.id.musicAnim);
    }

    public void clickOnSpanish(View view){
        eggImage.setVisibility(View.INVISIBLE);
        englishEggImage.setVisibility(View.INVISIBLE);
        spanishEggImage.setVisibility(View.VISIBLE);
        languajeSelected=true;
    }

    public void clickOnEnglish(View view){
        eggImage.setVisibility(View.INVISIBLE);
        englishEggImage.setVisibility(View.VISIBLE);
        spanishEggImage.setVisibility(View.INVISIBLE);
        languajeSelected=true;
    }

    public void clickOnMusic(View view){
        musicButton.setVisibility(View.INVISIBLE);
        musicAnim.setVisibility(View.VISIBLE);
        GifDrawable gifDrawable = (GifDrawable) musicAnim.getDrawable();
        gifDrawable.setLoopCount(1);
        gifDrawable.seekTo(0);
        gifDrawable.start();
        gifDrawable.setLoopCount(1);
        musicAnim.postDelayed(() -> {
            musicAnim.setVisibility(View.INVISIBLE);
            musicButton.setVisibility(View.VISIBLE);
        }, gifDrawable.getDuration());
    }

    public void clickOK(View view){
        if(languajeSelected){
            Intent intent = new Intent(this, EggActivity.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, "Select a language", Toast.LENGTH_SHORT).show();
        }

    }
}