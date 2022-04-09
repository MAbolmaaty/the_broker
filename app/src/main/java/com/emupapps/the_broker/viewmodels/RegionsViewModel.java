package com.emupapps.the_broker.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.emupapps.the_broker.models.regions.RegionsModelResponse;
import com.emupapps.the_broker.repositories.RegionsRepository;

// Leaking
public class RegionsViewModel extends ViewModel {

    private MutableLiveData<RegionsModelResponse> mRegions;
    private MutableLiveData<Boolean> mLoading;
    private MutableLiveData<Boolean> mFailure;

    public void regions(String locale){
        RegionsRepository repositoryRegions = RegionsRepository.getInstance();
        mRegions = repositoryRegions.getRegions(locale);
        mLoading = repositoryRegions.loading();
        mFailure = repositoryRegions.failure();
    }

    public LiveData<RegionsModelResponse> getRegions(){
        return mRegions;
    }

    public LiveData<Boolean> isLoading(){
        return mLoading;
    }

    public LiveData<Boolean> failure(){return mFailure;}
}
