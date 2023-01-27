package com.example.discoverpic.model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CountryModel {
    final public static CountryModel instance = new CountryModel();

    final String BASE_URL = "https://countriesnow.space/api/v0.1/";
    Retrofit retrofit;
    CountryApi countryApi;

    private CountryModel(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        countryApi = retrofit.create(CountryApi.class);
    }

    public LiveData<List<Country>> getCountriesAndCities(){
        MutableLiveData<List<Country>> data = new MutableLiveData<>();
        Call<CountrySearchResult> call = countryApi.getCountriesAndCities();
        call.enqueue(new Callback<CountrySearchResult>() {
            @Override
            public void onResponse(Call<CountrySearchResult> call, Response<CountrySearchResult> response) {
                if (response.isSuccessful()){
                    CountrySearchResult res = response.body();
                    data.setValue(res.getData());
                }else{
                    Log.d("TAG","----- getCountriesAndCities response error");
                }
            }

            @Override
            public void onFailure(Call<CountrySearchResult> call, Throwable t) {
                Log.d("TAG","----- getCountriesAndCities fail");
            }
        });
        return data;
    }
    
    
}
