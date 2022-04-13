package com.emupapps.the_broker.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.emupapps.the_broker.models.ProfileModelResponse;
import com.emupapps.the_broker.repositories.ProfileRepository;

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<ProfileModelResponse> mProfile = new MutableLiveData<>();

    public void userInfo(String authorization){
        ProfileRepository profileRepository = ProfileRepository.getInstance();
        profileRepository.profile(mProfile, authorization);
    }

    public LiveData<ProfileModelResponse> getResult(){
        return mProfile;
    }

}
