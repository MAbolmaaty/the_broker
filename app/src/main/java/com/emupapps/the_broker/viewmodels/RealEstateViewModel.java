package com.emupapps.the_broker.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.emupapps.the_broker.models.RealEstate;
import com.emupapps.the_broker.models.real_estate.RealEstateModelResponse;
import com.emupapps.the_broker.repositories.RealEstateRepository;

// Leaking
public class RealEstateViewModel extends ViewModel {

    private final MutableLiveData<RealEstate> mRealEstateDetails = new MutableLiveData<>();;
    private MutableLiveData<Boolean> mFailure;

    public void realEstate(String realEstateId) {
        RealEstateRepository repositoryRealEstate = RealEstateRepository.getInstance();
        repositoryRealEstate.realEstate(mRealEstateDetails, realEstateId);
        mFailure = repositoryRealEstate.failure();
    }

    public LiveData<RealEstate> getRealEstateDetails() {
        return mRealEstateDetails;
    }

    public LiveData<Boolean> failure(){return mFailure;}
}
