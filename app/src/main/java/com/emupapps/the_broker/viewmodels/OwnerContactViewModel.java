package com.emupapps.the_broker.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.emupapps.the_broker.models.owner_contact.response.ContactOwnerModelResponse;
import com.emupapps.the_broker.repositories.OwnerContactRepository;

public class OwnerContactViewModel extends ViewModel {

    private MutableLiveData<Boolean> mLoading;
    private MutableLiveData<ContactOwnerModelResponse> mResult;
    private MutableLiveData<Boolean> mFailure;

    public void contactOwner(String message, String ownerId, String locale){
        OwnerContactRepository repositoryContactOwner = OwnerContactRepository.getInstance();
        mResult = repositoryContactOwner.contactOwner(message, ownerId, locale);
        mLoading = repositoryContactOwner.loading();
        mFailure = repositoryContactOwner.failure();
    }

    public LiveData<ContactOwnerModelResponse> getResult(){
        return mResult;
    }

    public LiveData<Boolean> isLoading(){
        return mLoading;
    }

    public LiveData<Boolean> failure(){return mFailure;}
}
