package com.emupapps.the_broker.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.emupapps.the_broker.models.real_estates_owned.RealEstatesOwnedModelResponse;
import com.emupapps.the_broker.repositories.RealEstatesOwnedRepository;

public class RealEstatesOwnedViewModel extends ViewModel {

    private MutableLiveData<RealEstatesOwnedModelResponse> mMyRealEstates;
    private MutableLiveData<Boolean> mLoading;
    private MutableLiveData<Boolean> mFailure;

    public void myRealEstates(String userId) {
        RealEstatesOwnedRepository repositoryMyRealEstates = RealEstatesOwnedRepository.getInstance();
        mMyRealEstates = repositoryMyRealEstates.myRealEstates(userId);
        mLoading = repositoryMyRealEstates.loading();
        mFailure = repositoryMyRealEstates.failure();
    }

    public LiveData<RealEstatesOwnedModelResponse> getMyRealEstates() {
        return mMyRealEstates;
    }

    public LiveData<Boolean> isLoading() {
        return mLoading;
    }

    public LiveData<Boolean> failure(){return mFailure;}
}
