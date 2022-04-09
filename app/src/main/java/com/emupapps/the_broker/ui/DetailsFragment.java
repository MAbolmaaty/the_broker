package com.emupapps.the_broker.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.emupapps.the_broker.viewmodels.RealEstateViewModel;

import java.util.Locale;

import static com.emupapps.the_broker.utils.Constants.AUCTIONABLE;
import static com.emupapps.the_broker.utils.Constants.LOCALE;
import static com.emupapps.the_broker.utils.Constants.PRIVATE;
import static com.emupapps.the_broker.utils.Constants.REAL_ESTATE_SALE;
import static com.emupapps.the_broker.utils.Constants.SHARED;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    private static final String TAG = DetailsFragment.class.getSimpleName();

    //Ad Number
    View mViewAdNumber;
    ImageView mImageViewAdNumber;
    TextView mTextViewAdNumber;
    TextView mAdNumber;
    //Age
    View mViewAge;
    ImageView mImageViewAge;
    TextView mTextViewAge;
    TextView mAge;
    //Area
    View mViewArea;
    ImageView mImageViewArea;
    TextView mTextViewArea;
    TextView mTextViewAreaInMeter;
    TextView mArea;
    //Rooms
    View mViewRooms;
    ImageView mImageViewRooms;
    TextView mTextViewRooms;
    TextView mRooms;
    //Insurance
    View mViewInsurance;
    ImageView mImageViewInsurance;
    TextView mTextViewInsurance;
    TextView mInsuranceCurrency;
    TextView mInsurance;
    //Apartments
    View mViewApartments;
    ImageView mImageViewApartments;
    TextView mTextViewApartments;
    TextView mApartments;
    //Electricity
    View mViewElectricity;
    ImageView mImageViewElectricity;
    TextView mTextViewElectricity;
    TextView mElectricityStatus;
    TextView mElectricity;
    //Water
    View mViewWater;
    ImageView mImageViewWater;
    TextView mTextViewWater;
    TextView mWaterStatus;
    TextView mWater;
    //Bathrooms
    View mViewBathrooms;
    ImageView mImageViewBathrooms;
    TextView mTextViewBathrooms;
    TextView mBathrooms;
    //Floors
    View mViewFloors;
    ImageView mImageViewFloors;
    TextView mTextViewFloors;
    TextView mFloors;
    //Offices
    View mViewOffices;
    ImageView mImageViewOffices;
    TextView mTextViewOffices;
    TextView mOffices;
    //Shops
    View mViewShops;
    ImageView mImageViewShops;
    TextView mTextViewShops;
    TextView mShops;
    //Halls
    View mViewHalls;
    ImageView mImageViewHalls;
    TextView mTextViewHalls;
    TextView mHalls;
    //Street Width
    View mViewStreetWidth;
    ImageView mImageViewStreetWidth;
    TextView mTextViewStreetWidth;
    TextView mTextViewStreetWidthInMeter;
    TextView mStreetWidth;

    private RealEstateViewModel mViewModelRealEstate;
    private String mLocale;

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        mLocale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE, Locale.getDefault().getLanguage());
        if (mLocale.equals("ar"))
            view.setRotation(-180);

        mViewModelRealEstate = ViewModelProviders.of(getActivity()).get(RealEstateViewModel.class);
        mViewModelRealEstate.getRealEstate().observe(this, realEstateModelResponse -> {
            if (realEstateModelResponse.getRealEstate() != null){
                if (realEstateModelResponse.getRealEstate().getId() != null){
                    showAdNumber();
                    mAdNumber.setText(realEstateModelResponse.getRealEstate().getId());
                }
                if (realEstateModelResponse.getRealEstate().getYear_of_building() != null){
                    showAge();
                    mAge.setText(realEstateModelResponse.getRealEstate().getYear_of_building());
                }
                if (realEstateModelResponse.getRealEstate().getApartment_space() != null){
                    showArea();
                    mArea.setText(realEstateModelResponse.getRealEstate().getApartment_space());
                }
                if (realEstateModelResponse.getRealEstate().getRooms_num() != null){
                    showRooms();
                    mRooms.setText(realEstateModelResponse.getRealEstate().getRooms_num());
                }
                if (realEstateModelResponse.getRealEstate().getAmount_insurance() != null){
                    if (realEstateModelResponse.getRealEstate().getService().equals(REAL_ESTATE_SALE)
                    && !realEstateModelResponse.getRealEstate().getAuction().equals(AUCTIONABLE)){
                    } else {
                        showInsurance();
                        mInsurance.setText(realEstateModelResponse.getRealEstate().getAmount_insurance());
                    }
                }
                if (realEstateModelResponse.getRealEstate().getApartments_num() != null){
                    showApartments();
                    mApartments.setText(realEstateModelResponse.getRealEstate().getApartments_num());
                }
                if (realEstateModelResponse.getRealEstate().getElectrical_meter_num() != null){
                    showElectricity();
                    mElectricity.setText(realEstateModelResponse.getRealEstate().getElectrical_meter_num());
                    if (realEstateModelResponse.getRealEstate().getElectricityStatus().equals(SHARED)){
                        mElectricityStatus.setVisibility(View.VISIBLE);
                        mElectricityStatus.setText(R.string.shared_acct);
                    } else if (realEstateModelResponse.getRealEstate().getElectricityStatus().equals(PRIVATE)){
                        mElectricityStatus.setVisibility(View.VISIBLE);
                        mElectricityStatus.setText(R.string.private_acct);
                    }
                }
                if (realEstateModelResponse.getRealEstate().getWater_meter_num() != null){
                    showWater();
                    mWater.setText(realEstateModelResponse.getRealEstate().getWater_meter_num());
                    if (realEstateModelResponse.getRealEstate().getWaterStatus().equals(SHARED)){
                        mWaterStatus.setVisibility(View.VISIBLE);
                        mWaterStatus.setText(R.string.shared_acct);
                    } else if (realEstateModelResponse.getRealEstate().getWaterStatus().equals(PRIVATE)){
                        mWaterStatus.setVisibility(View.VISIBLE);
                        mWaterStatus.setText(R.string.private_acct);
                    }
                }
                if (realEstateModelResponse.getRealEstate().getBath_num() != null){
                    showBathrooms();
                    mBathrooms.setText(realEstateModelResponse.getRealEstate().getBath_num());
                }
                if (realEstateModelResponse.getRealEstate().getFloor_num() != null){
                    showFloors();
                    mFloors.setText(realEstateModelResponse.getRealEstate().getFloor_num());
                }
                if (realEstateModelResponse.getRealEstate().getOffices() != null){
                    showOffices();
                    mOffices.setText(realEstateModelResponse.getRealEstate().getOffices());
                }
                if (realEstateModelResponse.getRealEstate().getShops() != null){
                    showShops();
                    mShops.setText(realEstateModelResponse.getRealEstate().getShops());
                }
                if (realEstateModelResponse.getRealEstate().getHalls() != null){
                    showHalls();
                    mHalls.setText(realEstateModelResponse.getRealEstate().getHalls());
                }
                if (realEstateModelResponse.getRealEstate().getStreetWidth() != null){
                    showStreetWidth();
                    mStreetWidth.setText(realEstateModelResponse.getRealEstate().getStreetWidth());
                }
            }
        });

        return view;
    }

    private void showAdNumber(){
        mViewAdNumber.setVisibility(View.VISIBLE);
        mImageViewAdNumber.setVisibility(View.VISIBLE);
        mTextViewAdNumber.setVisibility(View.VISIBLE);
        mAdNumber.setVisibility(View.VISIBLE);
    }

    private void showAge(){
        mViewAge.setVisibility(View.VISIBLE);
        mImageViewAge.setVisibility(View.VISIBLE);
        mTextViewAge.setVisibility(View.VISIBLE);
        mAge.setVisibility(View.VISIBLE);
    }

    private void showArea(){
        mViewArea.setVisibility(View.VISIBLE);
        mImageViewArea.setVisibility(View.VISIBLE);
        mTextViewArea.setVisibility(View.VISIBLE);
        mTextViewAreaInMeter.setVisibility(View.VISIBLE);
        mArea.setVisibility(View.VISIBLE);
    }

    private void showRooms(){
        mViewRooms.setVisibility(View.VISIBLE);
        mImageViewRooms.setVisibility(View.VISIBLE);
        mTextViewRooms.setVisibility(View.VISIBLE);
        mRooms.setVisibility(View.VISIBLE);
    }

    private void showInsurance(){
        mViewInsurance.setVisibility(View.VISIBLE);
        mImageViewInsurance.setVisibility(View.VISIBLE);
        mTextViewInsurance.setVisibility(View.VISIBLE);
        mInsuranceCurrency.setVisibility(View.VISIBLE);
        mInsurance.setVisibility(View.VISIBLE);
    }

    private void showApartments(){
        mViewApartments.setVisibility(View.VISIBLE);
        mImageViewApartments.setVisibility(View.VISIBLE);
        mTextViewApartments.setVisibility(View.VISIBLE);
        mApartments.setVisibility(View.VISIBLE);
    }

    private void showElectricity(){
        mViewElectricity.setVisibility(View.VISIBLE);
        mImageViewElectricity.setVisibility(View.VISIBLE);
        mTextViewElectricity.setVisibility(View.VISIBLE);
        mElectricity.setVisibility(View.VISIBLE);
    }

    private void showWater(){
        mViewWater.setVisibility(View.VISIBLE);
        mImageViewWater.setVisibility(View.VISIBLE);
        mTextViewWater.setVisibility(View.VISIBLE);
        mWater.setVisibility(View.VISIBLE);
    }

    private void showBathrooms(){
        mViewBathrooms.setVisibility(View.VISIBLE);
        mImageViewBathrooms.setVisibility(View.VISIBLE);
        mTextViewBathrooms.setVisibility(View.VISIBLE);
        mBathrooms.setVisibility(View.VISIBLE);
    }

    private void showFloors(){
        mViewFloors.setVisibility(View.VISIBLE);
        mImageViewFloors.setVisibility(View.VISIBLE);
        mTextViewFloors.setVisibility(View.VISIBLE);
        mFloors.setVisibility(View.VISIBLE);
    }

    private void showOffices(){
        mViewOffices.setVisibility(View.VISIBLE);
        mImageViewOffices.setVisibility(View.VISIBLE);
        mTextViewOffices.setVisibility(View.VISIBLE);
        mOffices.setVisibility(View.VISIBLE);
    }

    private void showShops(){
        mViewShops.setVisibility(View.VISIBLE);
        mImageViewShops.setVisibility(View.VISIBLE);
        mTextViewShops.setVisibility(View.VISIBLE);
        mShops.setVisibility(View.VISIBLE);
    }

    private void showHalls(){
        mViewHalls.setVisibility(View.VISIBLE);
        mImageViewHalls.setVisibility(View.VISIBLE);
        mTextViewHalls.setVisibility(View.VISIBLE);
        mHalls.setVisibility(View.VISIBLE);
    }

    private void showStreetWidth(){
        mViewStreetWidth.setVisibility(View.VISIBLE);
        mImageViewStreetWidth.setVisibility(View.VISIBLE);
        mTextViewStreetWidth.setVisibility(View.VISIBLE);
        mTextViewStreetWidthInMeter.setVisibility(View.VISIBLE);
        mStreetWidth.setVisibility(View.VISIBLE);
    }

    public static DetailsFragment newInstance(){
        return new DetailsFragment();
    }
}
