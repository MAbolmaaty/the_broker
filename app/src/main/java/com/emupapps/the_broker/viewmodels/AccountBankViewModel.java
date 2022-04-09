package com.emupapps.the_broker.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.emupapps.the_broker.models.account_bank.BankAccountModelResponse;
import com.emupapps.the_broker.repositories.AccountBankRepository;

public class AccountBankViewModel extends ViewModel {

    private MutableLiveData<BankAccountModelResponse> mBankAccount;
    private MutableLiveData<Boolean> mLoading;
    private MutableLiveData<Boolean> mFailure;

    public void bankAccount(String realEstateId){
        AccountBankRepository repositoryBankAccount = AccountBankRepository.getInstance();
        mBankAccount = repositoryBankAccount.getBankAccount(realEstateId);
        mLoading = repositoryBankAccount.loading();
        mFailure = repositoryBankAccount.failure();
    }

    public LiveData<BankAccountModelResponse> getBankAccount(){
        return mBankAccount;
    }

    public LiveData<Boolean> isLoading(){
        return mLoading;
    }

    public LiveData<Boolean> failure(){return mFailure;}
}
