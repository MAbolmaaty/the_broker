package com.emupapps.the_broker.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.emupapps.the_broker.repositories.DownloadRepository;

import okhttp3.ResponseBody;

public class DownloadViewModel extends ViewModel {

    private MutableLiveData<ResponseBody> mFile;
    private MutableLiveData<Boolean> mLoading;
    private MutableLiveData<Boolean> mFailure;

    public void download(String url){
        DownloadRepository repositoryDownload = DownloadRepository.getInstance();
        mFile = repositoryDownload.download(url);
        mLoading = repositoryDownload.loading();
        mFailure = repositoryDownload.failure();
    }

    public LiveData<ResponseBody> getFile(){
        return mFile;
    }

    public LiveData<Boolean> isLoading(){
        return mLoading;
    }

    public LiveData<Boolean> failure(){return mFailure;}
}
