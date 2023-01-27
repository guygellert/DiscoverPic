package com.example.discoverpic.model;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CountryApi {
    @GET("countries/")
    Call<CountrySearchResult> getCountriesAndCities();
}
