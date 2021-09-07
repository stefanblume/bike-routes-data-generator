package de.conterra.biking.generator;

public class AdressData {
    int schluessel;
    String name;
    int rechtswert;
    int hochwert;
    String status;

    //01100,Brahmsstraße,S,406660,5751820
    public AdressData(String[] csvLine) {
        schluessel =  Integer.parseInt(csvLine[0]);
        name =  (csvLine[1]);
        status =  (csvLine[2]);
        rechtswert =  Integer.parseInt(csvLine[3]);
        hochwert =  Integer.parseInt(csvLine[4]);
    }

    public int getSchluessel() {
        return schluessel;
    }

    public void setSchluessel(int schluessel) {
        this.schluessel = schluessel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRechtswert() {
        return rechtswert;
    }

    public void setRechtswert(int rechtswert) {
        this.rechtswert = rechtswert;
    }

    public int getHochwert() {
        return hochwert;
    }

    public void setHochwert(int hochwert) {
        this.hochwert = hochwert;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
