package com.emupapps.the_broker.ui;


import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.adapters.BidsAdapter;
import com.emupapps.the_broker.models.real_estate.Bid;
import com.emupapps.the_broker.models.real_estate.RealEstate;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.emupapps.the_broker.utils.SoftKeyboard;
import com.emupapps.the_broker.viewmodels.AuctionBidViewModel;
import com.emupapps.the_broker.viewmodels.RealEstateViewModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.emupapps.the_broker.utils.Constants.APPROVED;
import static com.emupapps.the_broker.utils.Constants.LOCALE;
import static com.emupapps.the_broker.utils.Constants.SUCCESS;
import static com.emupapps.the_broker.utils.Constants.USER_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class AuctionFragment extends Fragment {

    private static final String TAG = AuctionFragment.class.getSimpleName();

    RecyclerView mBids;
    TextView mBidsCount;
    ProgressBar mProgressBar;
    TextView mNoBids;
    View mAuctionDetails;
    TextView mTextViewCurrentBid;
    View mViewCurrentBid;
    TextView mCurrentBid;
    View mViewHideCurrentBid;
    View mViewBid;
    EditText mBid;
    TextView mCurrency;
    TextView mBidAction;
    View mViewExpiration;
    TextView mTextViewExpiration;
    TextView mExpiration;
    View mViewMargin;
    ImageView mBack;
    TextView mTitle;

    private RealEstateViewModel mViewModelRealEstate;
    private AuctionBidViewModel mViewModelAuctionBid;
    private String mUserId;
    private String mRealEstateId;
    private BidsAdapter mBidsAdapter;
    private Toast mToast;
    private float mMaxBid = 0.0f;
    private String mLocale;
    private static List<Bid> mBidsList = new ArrayList<>();
    private int mAnimateBy;

    public AuctionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_auction, container, false);
        mTitle.setText(R.string.auction_details);
        mLocale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE, Locale.getDefault().getLanguage());
        if (mLocale.equals("ar")) {
            mBack.setImageResource(R.drawable.ic_arrow_ar);
        } else {
            mBack.setImageResource(R.drawable.ic_arrow);
        }

        mCurrentBid.setTextLocale(Locale.ENGLISH);
        mBid.setTextLocale(Locale.ENGLISH);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mAnimateBy = (int)(displayMetrics.widthPixels / 1.5);

        mViewModelRealEstate = new ViewModelProvider(getActivity()).get(RealEstateViewModel.class);
        mViewModelAuctionBid = new ViewModelProvider(this).get(AuctionBidViewModel.class);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mBidsAdapter = new BidsAdapter(getContext(), mBidsList);
        mBids.setLayoutManager(layoutManager);
        mBids.setAdapter(mBidsAdapter);
        mBids.setHasFixedSize(true);
        mUserId = SharedPrefUtil.getInstance(getContext()).read(USER_ID, null);
        if (mUserId == null) {

//            viewModelLogin.getUser().observe(this, loginModelResponse -> {
//                mUserId = loginModelResponse.getUser().getId();
//                bids(mUserId);
//            });
        } else {
           // bids(mUserId);
        }

        return view;
    }

    public void back() {
        getActivity().onBackPressed();
    }

    public void bidAction() {
        if (mToast != null)
            mToast.cancel();
        if (mBidAction.getText().equals(getString(R.string.join))) {
            if (newBid(mBid.getText().toString())) {
                mBidAction.setEnabled(false);
                mBidAction.animate().translationX(-mAnimateBy).setDuration(296);
                animateAuctionEdit(-mAnimateBy, 296, true);
                new Handler().postDelayed(() -> {
                    mBidAction.animate().translationX(0).setDuration(296);
                    mViewHideCurrentBid.animate().translationX(0).setDuration(296);
                }, 296);

                new Handler().postDelayed(() -> {
                    mBidAction.setText(AuctionFragment.this.getString(R.string.edit));
                    mBidAction.setEnabled(true);
                }, 496);
            } else {
                bidRejectAnimation();
            }
        } else {
            mBidAction.setEnabled(false);
            mBidAction.animate().translationX(-mAnimateBy).setDuration(296);
            mViewHideCurrentBid.animate().translationX(-mAnimateBy).setDuration(296);
            mBid.requestFocus();
            new Handler().postDelayed(() -> {
                mBidAction.animate().translationX(0).setDuration(296);
                animateAuctionEdit(0, 296, true);
            }, 296);

            new Handler().postDelayed(() -> {
                mBidAction.setText(AuctionFragment.this.getString(R.string.join));
                mBidAction.setEnabled(true);
            }, 496);
        }
    }

//    private void bids(String userId){
//        mViewModelRealEstate.getRealEstateDetails().observe(this, realEstateModelResponse -> {
//            RealEstate realEstate = realEstateModelResponse.getRealEstate();
//            if (realEstate.getOwnership().getOwner_id().equals(mUserId)){
//                mAuctionDetails.setVisibility(View.GONE);
//                mTextViewCurrentBid.setVisibility(View.GONE);
//                mViewCurrentBid.setVisibility(View.GONE);
//                mCurrentBid.setVisibility(View.GONE);
//                mViewHideCurrentBid.setVisibility(View.GONE);
//                mViewBid.setVisibility(View.GONE);
//                mBid.setVisibility(View.GONE);
//                mCurrency.setVisibility(View.GONE);
//                mBidAction.setVisibility(View.GONE);
//                mViewExpiration.setVisibility(View.GONE);
//                mTextViewExpiration.setVisibility(View.GONE);
//                mExpiration.setVisibility(View.GONE);
//                mViewMargin.setVisibility(View.GONE);
//            }
//            mRealEstateId = realEstate.getId();
//            if (realEstate.getAuction_date() != null) {
//                mExpiration.setText(realEstate.getAuction_date());
//            }
//            mBidsList.clear();
//            mBidAction.setText(getString(R.string.join));
//            mViewHideCurrentBid.setTranslationX(-mAnimateBy);
//            animateAuctionEdit(0, 0, false);
//            mBid.requestFocus();
//            for (Bid bid : realEstate.getBids()) {
//                if (bid.getApprove().equals(APPROVED)) {
//                    mBidsList.add(bid);
//                    if (Float.parseFloat(bid.getPrice()) > mMaxBid){
//                        mMaxBid = Float.parseFloat(bid.getPrice());
//                    }
//                    if (bid.getUser_id().equals(userId)){
//                        mCurrentBid.setText(getString(R.string.price_amount, bid.getPrice()));
//                        mBid.setText(bid.getPrice());
//                        mBidAction.setText(getString(R.string.edit));
//                        mViewHideCurrentBid.setTranslationX(0);
//                        animateAuctionEdit(-mAnimateBy, 0, false);
//                        mViewCurrentBid.setVisibility(View.VISIBLE);
//                        mCurrentBid.setVisibility(View.VISIBLE);
//                    }
//                }
//            }
//
//            if (mBidsList.size() == 0) {
//                mNoBids.setVisibility(View.VISIBLE);
//            }
//
//        });
//    }

    private void animateAuctionEdit(int position, long duration, boolean animate) {
        if (animate) {
            mViewBid.animate().translationX(position).setDuration(duration);
            mBid.animate().translationX(position).setDuration(duration);
            mCurrency.animate().translationX(position).setDuration(duration);
        } else {
            mViewBid.setTranslationX(position);
            mBid.setTranslationX(position);
            mCurrency.setTranslationX(position);
        }
    }

    private boolean newBid(String amount) {
        Float amountFloat;
        try {
            amountFloat = Float.parseFloat(amount);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        if (amountFloat < mMaxBid + 100) {
            bidRejectAnimation();
            mToast = Toast.makeText(getActivity(), R.string.low_bid, Toast.LENGTH_SHORT);
            mToast.show();
            return false;
        }

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        decimalFormat.applyPattern("0.00");
        amount = decimalFormat.format(amountFloat);

        mViewModelAuctionBid.auctionBet(mRealEstateId, mUserId, amount, mLocale);
        mViewModelAuctionBid.getResult().observe(this, auctionBetModelResponse -> {
            if (auctionBetModelResponse.getKey().equals(SUCCESS)) {
                mToast = Toast.makeText(getContext(), auctionBetModelResponse.getResult(), Toast.LENGTH_SHORT);
                mToast.show();
                mCurrentBid.setText(getString(R.string.price_amount, decimalFormat.format(amountFloat)));
                mBid.setText(decimalFormat.format(amountFloat));
                SoftKeyboard.dismissKeyboardInActivity(getActivity());
                //loadBids();
            } else {
                mToast = Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT);
                mToast.show();

            }
        });
        mViewModelAuctionBid.isLoading().observe(this, loading -> {
            if (loading) {
                mBids.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
            } else {
                mBids.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
        mViewModelAuctionBid.failure().observe(this, failure -> {
            if (failure) {
                if (mToast != null)
                    mToast.cancel();
                mToast = Toast.makeText(getActivity(), R.string.connection_to_server_lost, Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
        return true;
    }

//    private void loadBids() {
//        mViewModelRealEstate.realEstate(mRealEstateId);
//        mViewModelRealEstate.getRealEstateDetails().observe(this, realEstateModelResponse -> {
//            mBidsList.clear();
//            for (Bid bid : realEstateModelResponse.getRealEstate().getBids()) {
//                if (bid.getApprove().equals(APPROVED)) {
//                    mBidsList.add(bid);
//                    if (Float.parseFloat(bid.getPrice()) > mMaxBid){
//                        mMaxBid = Float.parseFloat(bid.getPrice());
//                    }
//                }
//            }
//            if (mBidsList.size() > 0) {
//                mNoBids.setVisibility(View.INVISIBLE);
//                mBidsCount.setText(String.valueOf(mBidsList.size()));
//            }
//            mBidsAdapter.swapData(mBidsList);
//        });
//
//        mViewModelRealEstate.isLoading().observe(this, loading -> {
//            if (loading) {
//                mBids.setVisibility(View.INVISIBLE);
//                mProgressBar.setVisibility(View.VISIBLE);
//            } else {
//                mBids.setVisibility(View.VISIBLE);
//                mProgressBar.setVisibility(View.INVISIBLE);
//            }
//        });
//    }

    private void bidRejectAnimation() {
        mBidAction.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.shake50));
    }
}
