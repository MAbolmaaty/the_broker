package com.emupapps.the_broker.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.emupapps.the_broker.models.document_add.AddDocumentModelResponse;
import com.emupapps.the_broker.repositories.DocumentAddRepository;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class DocumentAddViewModel extends ViewModel {

    private MutableLiveData<AddDocumentModelResponse> mNewDocument;
    private MutableLiveData<Boolean> mLoading;
    private MutableLiveData<Boolean> mFailure;

    public void addDocument(MultipartBody.Part document,
                            RequestBody userId){
        DocumentAddRepository repositoryAddDocument = DocumentAddRepository.getInstance();
        mNewDocument = repositoryAddDocument.addDocument(document, userId);
        mLoading = repositoryAddDocument.loading();
        mFailure = repositoryAddDocument.failure();
    }

    public LiveData<AddDocumentModelResponse> newDocument(){
        return mNewDocument;
    }

    public LiveData<Boolean> isLoading(){
        return mLoading;
    }

    public LiveData<Boolean> failure(){return mFailure;}
}
