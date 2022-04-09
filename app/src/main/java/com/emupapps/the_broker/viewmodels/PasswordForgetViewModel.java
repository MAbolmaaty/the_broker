package com.emupapps.the_broker.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.emupapps.the_broker.models.password_forget.response.ForgetPasswordModelResponse;
import com.emupapps.the_broker.repositories.PasswordForgetRepository;

// Leaking
public class PasswordForgetViewModel extends ViewModel {

    private MutableLiveData<ForgetPasswordModelResponse> mResult;
    private MutableLiveData<Boolean> mLoading;
    private MutableLiveData<Boolean> mFailure;

    public void forgetPassword(String email, String locale){
        PasswordForgetRepository repositoryForgetPassword = PasswordForgetRepository.getInstance();
        mResult = repositoryForgetPassword.forgetPassword(email, locale);
        mLoading = repositoryForgetPassword.loading();
        mFailure = repositoryForgetPassword.failure();
    }

    public LiveData<ForgetPasswordModelResponse> getResult(){
        return mResult;
    }

    public LiveData<Boolean> isLoading(){
        return mLoading;
    }

    public LiveData<Boolean> failure(){return mFailure;}
}
