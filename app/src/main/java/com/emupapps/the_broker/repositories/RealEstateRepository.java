package com.emupapps.the_broker.repositories;

import androidx.lifecycle.MutableLiveData;

import com.emupapps.the_broker.models.RealEstate;
import com.emupapps.the_broker.models.real_estate.RealEstateModelResponse;
import com.emupapps.the_broker.utils.web_service.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RealEstateRepository {

    private static final String TAG = RealEstateRepository.class.getSimpleName();

    private static RealEstateRepository instance;
    private final MutableLiveData<Boolean> mFailure = new MutableLiveData<>();

    public static RealEstateRepository getInstance(){
        if (instance == null){
            instance = new RealEstateRepository();
        }
        return instance;
    }

    public MutableLiveData<RealEstate> realEstate(
            MutableLiveData<RealEstate> realEstateDetails,
            String realEstateId){

        Call<RealEstate> callRealEstate = RestClient.getInstance().getApiClient().
                getRealEstateDetails(realEstateId);

        callRealEstate.enqueue(new Callback<RealEstate>() {
            @Override
            public void onResponse(Call<RealEstate> call,
                                   Response<RealEstate> response) {
                if (response.body() != null){
                    realEstateDetails.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<RealEstate> call, Throwable t) {
                mFailure.setValue(true);
            }
        });

        return realEstateDetails;
    }

    public MutableLiveData<Boolean> failure(){return mFailure;}
}
