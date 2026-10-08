package com.example.bichitos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

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
    private VideoView introVideo;

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

        introVideo=findViewById(R.id.introVideo);
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

    public void clickOK(View view) {
        if (!languajeSelected) {
            Toast.makeText(this, "Select a language", Toast.LENGTH_SHORT).show();
            return;
        }

        FrameLayout videoContainer = findViewById(R.id.videoContainer);
        videoContainer.setVisibility(View.VISIBLE);

        String path = "android.resource://" + getPackageName() + "/" + R.raw.transition;
        introVideo.setVideoURI(Uri.parse(path));

        introVideo.setOnPreparedListener(mp -> {
            int videoWidth = mp.getVideoWidth();
            int videoHeight = mp.getVideoHeight();
            float videoRatio = (float) videoWidth / videoHeight;

            int containerWidth = videoContainer.getWidth();
            int containerHeight = videoContainer.getHeight();
            float containerRatio = (float) containerWidth / containerHeight;

            ViewGroup.LayoutParams params = introVideo.getLayoutParams();

            if (videoRatio > containerRatio) {
                params.height = containerHeight;
                params.width = (int) (containerHeight * videoRatio);
            } else {
                params.width = containerWidth;
                params.height = (int) (containerWidth / videoRatio);
            }

            introVideo.setLayoutParams(params);
            introVideo.start();
        });

        introVideo.setOnCompletionListener(mp -> {
            Intent intent = new Intent(this, EggActivity.class);
            startActivity(intent);
            finish();
        });
    }
}