package com.example.imtpmd;

import com.google.gson.annotations.SerializedName;

public class Civilization {
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;
    @SerializedName("expansion")
    public String expansion;
    @SerializedName("army_type")
    public String armyType;

    public String getName(){
        return this.name;
    }


}
