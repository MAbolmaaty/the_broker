package com.emupapps.the_broker.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.emupapps.the_broker.models.payment_cards.PaymentCardsModelResponse;
import com.emupapps.the_broker.repositories.PaymentCardsRepository;

public class PaymentCardsViewModel extends ViewModel {

    private MutableLiveData<PaymentCardsModelResponse> mPaymentCards;
    private MutableLiveData<Boolean> mLoading;
    private MutableLiveData<Boolean> mFailure;

    public void paymentCards(String userId){
        PaymentCardsRepository repositoryPaymentCards = PaymentCardsRepository.getInstance();
        mPaymentCards = repositoryPaymentCards.getPaymentCards(userId);
        mLoading = repositoryPaymentCards.loading();
        mFailure = repositoryPaymentCards.failure();
    }

    public LiveData<PaymentCardsModelResponse> getPaymentCards(){
        return mPaymentCards;
    }

    public LiveData<Boolean> isLoading(){
        return mLoading;
    }

    public LiveData<Boolean> failure(){return mFailure;}
}
