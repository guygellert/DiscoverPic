package com.example.discoverpic.model;

public class Country {
    String iso2;
    String iso3;
    String country;
    String[] cities;

    public String getIso2() {
        return iso2;
    }

    public String getIso3() {
        return iso3;
    }

    public String getCountry() {
        return country;
    }

    public String[] getCities() {
        return cities;
    }

    public void setIso2(String iso2) {
        this.iso2 = iso2;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCities(String[] cities) {
        this.cities = cities;
    }
}
