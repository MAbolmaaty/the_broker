package com.emupapps.the_broker.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.emupapps.the_broker.models.login.request.LoginModelRequest;
import com.emupapps.the_broker.models.login.response.LoginModelResponse;
import com.emupapps.the_broker.models.register.AuthenticationModelResponse;
import com.emupapps.the_broker.utils.web_service.RestClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {

    private static final String TAG = LoginRepository.class.getSimpleName();

    private static LoginRepository instance;
    private final MutableLiveData<Boolean> mFailure = new MutableLiveData<>();

    public static LoginRepository getInstance() {
        if (instance == null) {
            instance = new LoginRepository();
        }
        return instance;
    }

    public MutableLiveData<AuthenticationModelResponse> login(
            MutableLiveData<AuthenticationModelResponse> authentication,
            String identifier,
            String password) {
        Call<AuthenticationModelResponse> callLogin =
                RestClient.getInstance().getApiClient().login(identifier, password);
        callLogin.enqueue(new Callback<AuthenticationModelResponse>() {
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

    public MutableLiveData<Boolean> failure(){return mFailure;}
}
