package com.emupapps.the_broker.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.emupapps.the_broker.models.auction_bid.response.AuctionBidModelResponse;
import com.emupapps.the_broker.repositories.AuctionBidRepository;

public class AuctionBidViewModel extends ViewModel {

    private MutableLiveData<AuctionBidModelResponse> mResult;
    private MutableLiveData<Boolean> mLoading;
    private MutableLiveData<Boolean> mFailure;

    public void auctionBet(String realEstateId, String userId,
                           String amount, String locale){
        AuctionBidRepository repositoryAuctionBid = AuctionBidRepository.getInstance();
        mResult = repositoryAuctionBid.auctionBet(realEstateId, userId, amount, locale);
        mLoading = repositoryAuctionBid.loading();
        mFailure = repositoryAuctionBid.failure();
    }

    public LiveData<AuctionBidModelResponse> getResult(){
        return mResult;
    }

    public LiveData<Boolean> isLoading(){
        return mLoading;
    }

    public LiveData<Boolean> failure(){return mFailure;}
}
