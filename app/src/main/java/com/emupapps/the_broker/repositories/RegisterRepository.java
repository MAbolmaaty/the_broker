package com.emupapps.the_broker.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.emupapps.the_broker.models.register.RegisterModelResponse;
import com.emupapps.the_broker.utils.web_service.RestClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterRepository {

    private static final String TAG = RegisterRepository.class.getSimpleName();

    private static RegisterRepository mInstance;
    private Call<RegisterModelResponse> mCallRegister;
    private MutableLiveData<Boolean> mLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> mFailure = new MutableLiveData<>();

    public static RegisterRepository getInstance() {
        if (mInstance == null) {
            mInstance = new RegisterRepository();
        }
        return mInstance;
    }

    public MutableLiveData<RegisterModelResponse> register(RequestBody data,
                                                           MultipartBody.Part profilePicture) {

        mLoading.setValue(true);
        MutableLiveData<RegisterModelResponse> result = new MutableLiveData<>();
        mCallRegister = RestClient.getInstance().getApiClient().register(data, profilePicture);
        Log.d(TAG, "register start");
        mCallRegister.enqueue(new Callback<RegisterModelResponse>() {
            @Override
            public void onResponse(Call<RegisterModelResponse> call,
                                   Response<RegisterModelResponse> response) {
                Log.d(TAG, "onResponse");
                Log.d(TAG, "response.code() : " + response.code());
                mLoading.setValue(false);
                mFailure.setValue(false);
                if (response.body() != null) {
                    result.setValue(response.body());
                } else if (response.errorBody() != null){
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("message");
                        String key = jsonObject.getString("key");
                        //result.setValue(new RegisterModelResponse(message, key));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    mFailure.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<RegisterModelResponse> call, Throwable t) {
                Log.d(TAG, "on failure");
                mLoading.setValue(false);
                mFailure.setValue(true);
            }
        });
        return result;
    }

    public MutableLiveData<Boolean> loading(){
        return mLoading;
    }
    public MutableLiveData<Boolean> failure(){return mFailure;}
}
