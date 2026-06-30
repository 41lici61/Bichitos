package com.example.bichitos;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class PetData implements Serializable {
    @SerializedName("name")
    private String name;

    @SerializedName("gender")
    private String gender;

    @SerializedName("happiness")
    private int happiness;

    @SerializedName("hunger")
    private int hunger;

    @SerializedName("neglectPoints")
    private int neglectPoints;

    @SerializedName("carePoints")
    private int carePoints;

    @SerializedName("hatchTime")
    private long hatchTime;//when pet born

    @SerializedName("isHatched")
    private boolean isHatched;

    @SerializedName("lastUpdateTime")
    private long lastUpdateTime;

    public PetData() {
        this.happiness = 50;
        this.hunger = 50;
        this.neglectPoints = 0;
        this.carePoints = 0;
        this.isHatched = false;//still egg
        this.lastUpdateTime = System.currentTimeMillis();//actual time
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public int getHappiness() { return happiness; }
    public void setHappiness(int happiness) { this.happiness = Math.max(0, Math.min(100, happiness)); }

    public int getHunger() { return hunger; }
    public void setHunger(int hunger) { this.hunger = Math.max(0, Math.min(100, hunger)); }

    public int getNeglectPoints() { return neglectPoints; }
    public void setNeglectPoints(int neglectPoints) { this.neglectPoints = neglectPoints; }

    public int getCarePoints() { return carePoints; }
    public void setCarePoints(int carePoints) { this.carePoints = carePoints; }

    public long getHatchTime() { return hatchTime; }
    public void setHatchTime(long hatchTime) { this.hatchTime = hatchTime; }

    public boolean isHatched() { return isHatched; }
    public void setHatched(boolean hatched) { isHatched = hatched; }

    public long getLastUpdateTime() { return lastUpdateTime; }
    public void setLastUpdateTime(long lastUpdateTime) { this.lastUpdateTime = lastUpdateTime; }
}