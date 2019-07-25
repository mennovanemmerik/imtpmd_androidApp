package com.example.imtpmd;

public class publiek {

    private String wLijst = "standaart Wlijst";

    public String getWlijst(){
        return wLijst;
    }

    public void setWlijst(String newWlijst){
        this.wLijst = newWlijst;
        wLijst=newWlijst.toString();
        wLijst=newWlijst;
    }
}
