package com.emupapps.the_broker.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.emupapps.the_broker.models.register.AuthenticationModelResponse;
import com.emupapps.the_broker.repositories.LoginRepository;

import okhttp3.RequestBody;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<AuthenticationModelResponse> mAuthentication;
    private MutableLiveData<Boolean> mFailure;

    public void login(RequestBody identifier, RequestBody password){
        LoginRepository loginRepository = LoginRepository.getInstance();
        mFailure = loginRepository.failure();
    }

    public LiveData<AuthenticationModelResponse> getResult() {
        return mAuthentication;
    }

    public LiveData<Boolean> failure(){return mFailure;}
}
