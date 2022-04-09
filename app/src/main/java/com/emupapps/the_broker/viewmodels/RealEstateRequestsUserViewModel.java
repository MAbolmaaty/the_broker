package com.emupapps.the_broker.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.emupapps.the_broker.models.real_estate_requests_user.RealEstateRequestsUserModelResponse;
import com.emupapps.the_broker.repositories.RealEstateRequestsUserRepository;

public class RealEstateRequestsUserViewModel extends ViewModel {

    private MutableLiveData<RealEstateRequestsUserModelResponse> mRequests;
    private MutableLiveData<Boolean> mLoading;
    private MutableLiveData<Boolean> mFailure;

    public void realEstateRequests(String realEstateId){
        RealEstateRequestsUserRepository repositoryRealEstateRequests = RealEstateRequestsUserRepository.getInstance();
        mRequests = repositoryRealEstateRequests.realEstateRequests(realEstateId);
        mLoading = repositoryRealEstateRequests.loading();
        mFailure = repositoryRealEstateRequests.failure();
    }

    public LiveData<RealEstateRequestsUserModelResponse> getRealEstateRequests(){
        return mRequests;
    }

    public LiveData<Boolean> isLoading(){
        return mLoading;
    }

    public LiveData<Boolean> failure(){return mFailure;}
}
