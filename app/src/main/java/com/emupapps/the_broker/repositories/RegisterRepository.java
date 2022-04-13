package com.emupapps.the_broker.repositories;

import androidx.lifecycle.MutableLiveData;

import com.emupapps.the_broker.models.register.AuthenticationModelResponse;
import com.emupapps.the_broker.utils.web_service.RestClient;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterRepository {

    private static final String TAG = RegisterRepository.class.getSimpleName();

    private static RegisterRepository mInstance;
    private final MutableLiveData<Boolean> mFailure = new MutableLiveData<>();

    public static RegisterRepository getInstance() {
        if (mInstance == null) {
            mInstance = new RegisterRepository();
        }
        return mInstance;
    }

    public MutableLiveData<AuthenticationModelResponse> register(
            MutableLiveData<AuthenticationModelResponse> authentication,
            RequestBody data,
            MultipartBody.Part profilePicture) {
        Call<AuthenticationModelResponse> callRegister =
                RestClient.getInstance().getApiClient().register(data, profilePicture);
        callRegister.enqueue(new Callback<AuthenticationModelResponse>() {
            @Override
            public void onResponse(Call<AuthenticationModelResponse> call,
                                   Response<AuthenticationModelResponse> response) {
                authentication.setValue(response.body());
            }

            @Override
            public void onFailure(Call<AuthenticationModelResponse> call, Throwable t) {
                mFailure.setValue(true);
            }
        });

        return authentication;
    }

    public MutableLiveData<Boolean> failure() {
        return mFailure;
    }
}
