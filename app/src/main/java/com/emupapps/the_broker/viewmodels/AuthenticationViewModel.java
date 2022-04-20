package com.emupapps.the_broker.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.emupapps.the_broker.models.register.AuthenticationModelResponse;
import com.emupapps.the_broker.repositories.LoginRepository;
import com.emupapps.the_broker.repositories.RegisterRepository;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AuthenticationViewModel extends ViewModel {
    private final MutableLiveData<AuthenticationModelResponse> mAuthentication =
            new MutableLiveData<>();
    private MutableLiveData<Boolean> mFailure;

    public void register(RequestBody data, MultipartBody.Part profilePicture){
        RegisterRepository registerRepository = RegisterRepository.getInstance();
        registerRepository.register(mAuthentication, data, profilePicture);
        mFailure = registerRepository.failure();
    }

    public void login(String identifier, String password){
        LoginRepository loginRepository = LoginRepository.getInstance();
        loginRepository.login(mAuthentication, identifier, password);
        mFailure = loginRepository.failure();
    }

    public LiveData<AuthenticationModelResponse> getResult() {
        return mAuthentication;
    }

    public void setResult(String jwt){
        mAuthentication.setValue(new AuthenticationModelResponse(jwt));
    }

    public void logout(){
        mAuthentication.setValue(null);
    }

    public LiveData<Boolean> failure(){return mFailure;}
}
