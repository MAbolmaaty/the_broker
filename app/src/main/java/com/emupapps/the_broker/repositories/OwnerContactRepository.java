package com.emupapps.the_broker.repositories;

import androidx.lifecycle.MutableLiveData;

import com.emupapps.the_broker.models.owner_contact.request.ContactOwnerModelRequest;
import com.emupapps.the_broker.models.owner_contact.response.ContactOwnerModelResponse;
import com.emupapps.the_broker.utils.web_service.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnerContactRepository {

    private static OwnerContactRepository instance;
    private Call<ContactOwnerModelResponse> mCallContactOwner;
    private MutableLiveData<Boolean> mLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> mFailure = new MutableLiveData<>();

    public static OwnerContactRepository getInstance(){
        if (instance == null){
            instance = new OwnerContactRepository();
        }
        return instance;
    }

    public MutableLiveData<ContactOwnerModelResponse> contactOwner(String message, String ownerId, String locale){

        mLoading.setValue(true);
        MutableLiveData<ContactOwnerModelResponse> result = new MutableLiveData<>();
        mCallContactOwner = RestClient.getInstance().getApiClient()
                .contactOwner(new ContactOwnerModelRequest(message, ownerId, locale));
        mCallContactOwner.enqueue(new Callback<ContactOwnerModelResponse>() {
            @Override
            public void onResponse(Call<ContactOwnerModelResponse> call, Response<ContactOwnerModelResponse> response) {
                mLoading.setValue(false);
                mFailure.setValue(false);
                if (response.body() != null){
                    result.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ContactOwnerModelResponse> call, Throwable t) {
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
