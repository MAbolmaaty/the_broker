package com.emupapps.the_broker.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.emupapps.the_broker.models.notifications.NotificationsModelResponse;
import com.emupapps.the_broker.repositories.NotificationsRepository;

public class NotificationsViewModel extends ViewModel {

    private MutableLiveData<NotificationsModelResponse> mNotifications;
    private MutableLiveData<Boolean> mLoading;
    private MutableLiveData<Boolean> mFailure;

    public void notifications(String userId){
        NotificationsRepository repositoryNotifications = NotificationsRepository.getInstance();
        mNotifications = repositoryNotifications.getNotifications(userId);
        mLoading = repositoryNotifications.loading();
        mFailure = repositoryNotifications.failure();
    }

    public LiveData<NotificationsModelResponse> getNotifications(){
        return mNotifications;
    }

    public LiveData<Boolean> isLoading(){
        return mLoading;
    }

    public LiveData<Boolean> failure(){return mFailure;}
}
