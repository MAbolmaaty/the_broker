package com.emupapps.the_broker.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.emupapps.the_broker.models.search.request.SearchModelRequest;
import com.emupapps.the_broker.models.search.response.SearchModelResponse;
import com.emupapps.the_broker.repositories.SearchRepository;

// Leaking
public class SearchViewModel extends ViewModel {

    private static final String TAG = SearchViewModel.class.getSimpleName();

    private SearchRepository mRepositorySearch;
    private MutableLiveData<Boolean> mLoading;
    private MutableLiveData<SearchModelResponse> mResults;
    private MutableLiveData<SearchModelRequest> mParameters = new MutableLiveData<>();
    private MutableLiveData<Boolean> mFailure;

    public void search(String category, String status, String minPrice, String maxPrice,
                       String address, String region, String district, String age,
                       String minArea, String maxArea, int regionItemPosition,
                       int districtItemPosition){

        mRepositorySearch = SearchRepository.getInstance();
        mResults = mRepositorySearch.search(category, status, minPrice, maxPrice,
                address, region, district, age, minArea, maxArea,
                regionItemPosition, districtItemPosition);
        mLoading = mRepositorySearch.loading();
        mFailure = mRepositorySearch.failure();
    }

    public LiveData<SearchModelResponse> getResults(){
        return mResults;
    }

    public LiveData<Boolean> isLoading(){
        return mLoading;
    }

    public void setParameters(SearchModelRequest parameters){
        mParameters.setValue(parameters);
    }

    public LiveData<SearchModelRequest> getParameters(){
        return mParameters;
    }

    public LiveData<Boolean> failure(){return mFailure;}
}
