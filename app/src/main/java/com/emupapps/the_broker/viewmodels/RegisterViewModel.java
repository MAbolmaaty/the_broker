package com.emupapps.the_broker.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.emupapps.the_broker.models.register.RegisterModelResponse;
import com.emupapps.the_broker.repositories.RegisterRepository;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RegisterViewModel extends ViewModel {

    private MutableLiveData<RegisterModelResponse> mResult;
    private MutableLiveData<Boolean> mLoading;
    private MutableLiveData<Boolean> mFailure;

    public void register(RequestBody data, MultipartBody.Part profilePicture){

        RegisterRepository repositoryRegister = RegisterRepository.getInstance();
        mResult = repositoryRegister.register(data, profilePicture);
        mLoading = repositoryRegister.loading();
        mFailure = repositoryRegister.failure();

    }

    public LiveData<RegisterModelResponse> getResult(){
        return mResult;
    }

    public LiveData<Boolean> isLoading() {
        return mLoading;
    }

    public LiveData<Boolean> failure(){return mFailure;}
}
