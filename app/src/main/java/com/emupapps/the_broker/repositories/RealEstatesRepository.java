package com.emupapps.the_broker.repositories;

import androidx.lifecycle.MutableLiveData;

import com.emupapps.the_broker.models.RealEstate;
import com.emupapps.the_broker.models.real_estates.request.RealEstatesModelRequest;
import com.emupapps.the_broker.models.real_estates.response.RealEstatesModelResponse;
import com.emupapps.the_broker.utils.web_service.RestClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RealEstatesRepository {

    private static final String TAG = RealEstatesRepository.class.getSimpleName();

    private static RealEstatesRepository instance;
    private Call<List<RealEstate>> mCallRealEstates;
    private MutableLiveData<Boolean> mFailure = new MutableLiveData<>();

    public static RealEstatesRepository getInstance(){
        if (instance == null){
            instance = new RealEstatesRepository();
        }
        return instance;
    }

    public MutableLiveData<List<RealEstate>> getRealEstates(
            MutableLiveData<List<RealEstate>> realEstates){

        mCallRealEstates = RestClient.getInstance().getApiClient().getRealEstates();
        mCallRealEstates.enqueue(new Callback<List<RealEstate>>() {
            @Override
            public void onResponse(Call<List<RealEstate>> call, Response<List<RealEstate>> response) {
                mFailure.setValue(false);
                if (response.body() != null){
                    realEstates.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<RealEstate>> call, Throwable t) {
                mFailure.setValue(true);
            }
        });
        return realEstates;
    }

    public MutableLiveData<Boolean> failure(){return mFailure;}
}
