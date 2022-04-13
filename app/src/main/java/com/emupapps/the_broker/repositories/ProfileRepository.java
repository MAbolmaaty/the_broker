package com.emupapps.the_broker.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.emupapps.the_broker.models.ProfileModelResponse;
import com.emupapps.the_broker.utils.web_service.RestClient;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {
    private static final String TAG = ProfileRepository.class.getSimpleName();
    private static ProfileRepository instance;

    public static ProfileRepository getInstance() {
        if (instance == null) {
            instance = new ProfileRepository();
        }
        return instance;
    }

    public MutableLiveData<ProfileModelResponse> profile(
            MutableLiveData<ProfileModelResponse> profile,
            String authorization) {
        Call<ProfileModelResponse> callProfile =
                RestClient.getInstance().getApiClient().profile("Bearer " +
                        authorization);
        callProfile.enqueue(new Callback<ProfileModelResponse>() {
            @Override
            public void onResponse(Call<ProfileModelResponse> call,
                                   Response<ProfileModelResponse> response) {
                profile.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ProfileModelResponse> call, Throwable t) {
                Log.d(TAG, "onFailure");
            }
        });
        return profile;
    }
}
