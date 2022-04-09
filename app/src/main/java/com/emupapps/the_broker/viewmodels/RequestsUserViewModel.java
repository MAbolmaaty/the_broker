package com.emupapps.the_broker.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.emupapps.the_broker.models.requests_user.UserRequestsModelResponse;
import com.emupapps.the_broker.repositories.RequestsUserRepository;

// Leaking
public class RequestsUserViewModel extends ViewModel {

    private MutableLiveData<UserRequestsModelResponse> mUserRequests;
    private MutableLiveData<Boolean> mLoading;
    private MutableLiveData<Boolean> mFailure;

    public void userRequests(String userId){
        RequestsUserRepository repositoryUserRequests = RequestsUserRepository.getInstance();
        mUserRequests = repositoryUserRequests.userRequests(userId);
        mLoading = repositoryUserRequests.loading();
        mFailure = repositoryUserRequests.failure();
    }

    public LiveData<UserRequestsModelResponse> getUserRequests(){
        return mUserRequests;
    }

    public LiveData<Boolean> isLoading() {
        return mLoading;
    }

    public LiveData<Boolean> failure(){return mFailure;}
}
