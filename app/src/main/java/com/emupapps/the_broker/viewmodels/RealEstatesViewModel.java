package com.emupapps.the_broker.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.emupapps.the_broker.models.RealEstate;
import com.emupapps.the_broker.models.real_estates.response.RealEstatesModelResponse;
import com.emupapps.the_broker.repositories.RealEstatesRepository;

import java.util.List;

// Leaking
public class RealEstatesViewModel extends ViewModel {

    private final MutableLiveData<List<RealEstate>> mRealEstates = new MutableLiveData<>();
    private MutableLiveData<String> realEstate = new MutableLiveData<>();
    private MutableLiveData<Boolean> mMapFragment = new MutableLiveData<>();
    private MutableLiveData<Boolean> mFailure;

    public void realEstates() {
        RealEstatesRepository repositoryRealEstates = RealEstatesRepository.getInstance();
        repositoryRealEstates.getRealEstates(mRealEstates);
        mFailure = repositoryRealEstates.failure();
    }

    public LiveData<List<RealEstate>> getRealEstates() {
        return mRealEstates;
    }

    public void select(String realEstateId){
        realEstate.setValue(realEstateId);
    }

    public LiveData<String> getSelectedId(){
        return realEstate;
    }

    public void startMapFragment(boolean start){
        mMapFragment.setValue(start);
    }

    public MutableLiveData<Boolean> isMapFragment(){
        return mMapFragment;
    }

    public LiveData<Boolean> failure(){return mFailure;}
}
