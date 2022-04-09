package com.emupapps.the_broker.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.emupapps.the_broker.models.fav.response.FavModelResponse;
import com.emupapps.the_broker.repositories.FavoriteRepository;

public class FavoriteViewModel extends ViewModel {

    private MutableLiveData<FavModelResponse> mResult;
    private MutableLiveData<Boolean> mLoading;
    private MutableLiveData<Boolean> mFailure;

    public void favorite(String realEstateId, String userId, String locale) {
        FavoriteRepository repositoryFavorite = FavoriteRepository.getInstance();
        mResult = repositoryFavorite.favorite(realEstateId, userId, locale);
        mLoading = repositoryFavorite.loading();
        mFailure = repositoryFavorite.failure();
    }

    public LiveData<FavModelResponse> getResult() {
        return mResult;
    }

    public LiveData<Boolean> isLoading(){
        return mLoading;
    }

    public LiveData<Boolean> failure(){return mFailure;}
}
