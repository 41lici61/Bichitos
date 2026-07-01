package com.example.bichitos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.Random;

import pl.droidsonroids.gif.GifImageView;

public class EggActivity extends AppCompatActivity {
    // Views
    private GifImageView eggGif;
    private TextView timeLeftText;
    private TextView messageText;
    private Button rubButton;
    private Button tapButton;

    // Media
    private MediaPlayer backgroundMusic;
    private MediaPlayer tapSound;
    private MediaPlayer hatchSound;

    // Data
    private SharedPreferences prefs;
    private PetData petData;
    private CountDownTimer hatchTimer;
    private long hatchTimeLeft;
    private boolean isHatching = false;
    private static final long HATCH_DURATION = 300000; // 5 minutes
    private static final long RUB_REDUCTION = 30000; // 30 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verify if player has a pet
        prefs = getSharedPreferences("BichitosPrefs", Context.MODE_PRIVATE);
        String petJson = prefs.getString("petData", null);

        if (petJson != null) {
            // there is data
            PetData existingPet = new Gson().fromJson(petJson, PetData.class);

            if (existingPet.isHatched()) {
                // if it has born, go to home
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return;
            }
        }

        // if new player - REMOVED landscape forcing
        setContentView(R.layout.activity_egg);

        initViews();
        //initSounds();
        loadPetData(); // load data
        setupButtons();
        startHatchTimer();
        playBackgroundMusic();
    }

    // ... El resto del código queda IGUAL ...

    private void initViews() {
        eggGif = findViewById(R.id.eggGif);
        timeLeftText = findViewById(R.id.timeLeftText);
        messageText = findViewById(R.id.messageText);
        rubButton = findViewById(R.id.rubButton);
        tapButton = findViewById(R.id.tapButton);
    }

    private void loadPetData() {
        prefs = getSharedPreferences("BichitosPrefs", Context.MODE_PRIVATE);
        String petJson = prefs.getString("petData", null);

        if (petJson == null) {
            // New player
            petData = new PetData();
            petData.setHatchTime(System.currentTimeMillis() + HATCH_DURATION);
            savePetData();
            hatchTimeLeft = HATCH_DURATION;
        } else {
            petData = new Gson().fromJson(petJson, PetData.class);

            if (petData.isHatched()) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return;
            }

            // time left
            long currentTime = System.currentTimeMillis();
            long elapsed = currentTime - (petData.getHatchTime() - HATCH_DURATION);
            hatchTimeLeft = Math.max(0, HATCH_DURATION - elapsed);
        }
    }

    private void setupButtons() {
        rubButton.setOnClickListener(v -> {
            if (!isHatching && !petData.isHatched()) {
                playTapSound();
                reduceHatchTime(RUB_REDUCTION);
                showRubAnimation();
                updateMessage("The egg feels cozy!");
                Toast.makeText(this, "-30 seconds!", Toast.LENGTH_SHORT).show();
            }
        });

        tapButton.setOnClickListener(v -> {
            if (!isHatching && !petData.isHatched()) {
                playTapSound();
                updateTapMessage();
            }
        });
    }

    private void showRubAnimation() {
        eggGif.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(100)
                .withEndAction(() -> {
                    eggGif.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start();
                })
                .start();
    }

    private void reduceHatchTime(long reduction) {
        hatchTimeLeft = Math.max(0, hatchTimeLeft - reduction);
        updateTimeDisplay();
        savePetData();

        if (hatchTimer != null) {
            hatchTimer.cancel();
        }

        if (hatchTimeLeft <= 0) {
            startHatching();
        } else {
            startHatchTimer();
        }
    }

    private void startHatchTimer() {
        if (hatchTimer != null) {
            hatchTimer.cancel();
        }

        hatchTimer = new CountDownTimer(hatchTimeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                hatchTimeLeft = millisUntilFinished;
                updateTimeDisplay();
                if (hatchTimeLeft % 10000 == 0) {
                    updateMessage(generateRandomMessage());
                }
            }

            @Override
            public void onFinish() {
                startHatching();
            }
        }.start();
    }

    private void startHatching() {
        isHatching = true;
        rubButton.setEnabled(false);
        tapButton.setEnabled(false);

        // Change to hatching animation
        eggGif.setImageResource(R.drawable.hatching);

        // Play hatch sound
        if (hatchSound != null) {
            hatchSound.start();
        }

        // Show birth popup after 5 seconds
        new Handler().postDelayed(() -> {
            showBirthPopup();
        }, 5000);
    }

    private void showBirthPopup() {
        // Generate random gender
        String gender = new Random().nextBoolean() ? "Male" : "Female";
        petData.setGender(gender);
        petData.setHatched(true);

        // Create custom dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_birth, null);

        TextView genderText = dialogView.findViewById(R.id.genderText);
        EditText nameInput = dialogView.findViewById(R.id.nameInput);

        String genderMessage = gender.equals("Male") ? "🎉 It's a boy!" : "🎉 It's a girl!";
        genderText.setText(genderMessage);

        builder.setView(dialogView)
                .setTitle("Birth!")
                .setPositiveButton("Take Care!", (dialog, which) -> {
                    String name = nameInput.getText().toString().trim();
                    if (name.isEmpty()) {
                        name = gender.equals("Male") ? "Bichito" : "Bichita";
                    }
                    petData.setName(name);
                    savePetData();

                    // Go to Home
                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
                })
                .setCancelable(false)
                .show();
    }

    private void updateTimeDisplay() {
        long minutes = hatchTimeLeft / 60000;
        long seconds = (hatchTimeLeft % 60000) / 1000;
        String timeString = String.format("%02d:%02d", minutes, seconds);
        timeLeftText.setText(timeString);
    }

    private void updateMessage(String message) {
        messageText.setText(message);
    }

    private void updateTapMessage() {
        long minutes = hatchTimeLeft / 60000;
        long seconds = (hatchTimeLeft % 60000) / 1000;

        if (hatchTimeLeft > HATCH_DURATION * 0.8) {
            updateMessage("The egg feels warm!");
        } else if (hatchTimeLeft > HATCH_DURATION * 0.5) {
            updateMessage("You feel movement inside!");
        } else if (hatchTimeLeft > HATCH_DURATION * 0.2) {
            updateMessage("Almost ready!");
        } else if (hatchTimeLeft > 10000) {
            updateMessage("The shell is cracking!");
        } else {
            updateMessage("About to hatch!");
        }
    }

    private String generateRandomMessage() {
        String[] messages = {
                "The egg moves slightly",
                "Can you hear something inside?",
                "It's very warm!",
                "Maybe it wants to come out",
                "It feels happy!",
                "The egg is glowing",
                "Something is moving inside!",
                "It's getting ready"
        };
        return messages[new Random().nextInt(messages.length)];
    }

//    private void initSounds() {
//        try {
//            backgroundMusic = MediaPlayer.create(this, R.raw.egg_background_music);
//            if (backgroundMusic != null) {
//                backgroundMusic.setLooping(true);
//                backgroundMusic.setVolume(0.5f, 0.5f);
//            }
//
//            tapSound = MediaPlayer.create(this, R.raw.tap_sound);
//            hatchSound = MediaPlayer.create(this, R.raw.hatch_sound);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void playBackgroundMusic() {
        if (backgroundMusic != null && !backgroundMusic.isPlaying()) {
            backgroundMusic.start();
        }
    }

    private void playTapSound() {
        if (tapSound != null) {
            tapSound.start();
        }
    }

    private void savePetData() {
        String petJson = new Gson().toJson(petData);
        prefs.edit().putString("petData", petJson).apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        savePetData();
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        playBackgroundMusic();
        updateStats();
    }

    private void updateStats() {
        if (petData == null) return;

        long currentTime = System.currentTimeMillis();
        long timeSinceLastUpdate = currentTime - petData.getLastUpdateTime();

        if (timeSinceLastUpdate > 60000) {
            int minutesPassed = (int) (timeSinceLastUpdate / 60000);
            petData.setHappiness(petData.getHappiness() - (minutesPassed * 2));
            petData.setHunger(petData.getHunger() + (minutesPassed * 3));

            if (petData.getHappiness() < 20 || petData.getHunger() > 80) {
                petData.setNeglectPoints(petData.getNeglectPoints() + minutesPassed);
            }

            petData.setLastUpdateTime(currentTime);
            savePetData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (backgroundMusic != null) {
            backgroundMusic.release();
            backgroundMusic = null;
        }
        if (tapSound != null) {
            tapSound.release();
            tapSound = null;
        }
        if (hatchSound != null) {
            hatchSound.release();
            hatchSound = null;
        }
        if (hatchTimer != null) {
            hatchTimer.cancel();
        }
    }
}