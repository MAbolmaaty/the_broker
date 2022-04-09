package com.emupapps.the_broker.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.emupapps.the_broker.models.favorites.FavoritesModelResponse;
import com.emupapps.the_broker.repositories.FavoritesRepository;

public class FavoritesViewModel extends ViewModel {

    private MutableLiveData<FavoritesModelResponse> mFavorites;
    private MutableLiveData<Boolean> mLoading;
    private MutableLiveData<Boolean> mFailure;

    public void favorites(String userId){
        FavoritesRepository repositoryFavorites = FavoritesRepository.getInstance();
        mFavorites = repositoryFavorites.favorites(userId);
        mLoading = repositoryFavorites.loading();
        mFailure = repositoryFavorites.failure();
    }

    public LiveData<FavoritesModelResponse> getFavorites(){
        return mFavorites;
    }

    public LiveData<Boolean> isLoading(){
        return mLoading;
    }

    public LiveData<Boolean> failure(){return mFailure;}
}
