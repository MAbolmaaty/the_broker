package com.emupapps.the_broker.repositories;

import androidx.lifecycle.MutableLiveData;

import com.emupapps.the_broker.models.document_delete.DeleteDocumentModelResponse;
import com.emupapps.the_broker.utils.web_service.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentDeleteRepository {

    private static DocumentDeleteRepository mInstance;
    private Call<DeleteDocumentModelResponse> mCallDeleteDocument;
    private MutableLiveData<Boolean> mLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> mFailure = new MutableLiveData<>();

    public static DocumentDeleteRepository getInstance(){
        if (mInstance == null){
            mInstance = new DocumentDeleteRepository();
        }
        return mInstance;
    }

    public MutableLiveData<DeleteDocumentModelResponse> deleteDocument(String document){
        mLoading.setValue(true);
        MutableLiveData<DeleteDocumentModelResponse> result = new MutableLiveData<>();
        mCallDeleteDocument = RestClient.getInstance().getApiClient().deleteDocument(document);
        mCallDeleteDocument.enqueue(new Callback<DeleteDocumentModelResponse>() {
            @Override
            public void onResponse(Call<DeleteDocumentModelResponse> call, Response<DeleteDocumentModelResponse> response) {
                mLoading.setValue(false);
                mFailure.setValue(false);
                if (response.body() != null){
                    result.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<DeleteDocumentModelResponse> call, Throwable t) {
                mLoading.setValue(false);
                mFailure.setValue(true);
            }
        });
        return result;
    }

    public MutableLiveData<Boolean> loading(){
        return mLoading;
    }
    public MutableLiveData<Boolean> failure(){return mFailure;}
}
