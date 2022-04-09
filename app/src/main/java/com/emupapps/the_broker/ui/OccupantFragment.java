package com.emupapps.the_broker.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.models.info_user.UserInfoModelResponse;
import com.emupapps.the_broker.models.real_estate.Owner;
import com.emupapps.the_broker.models.real_estate.RealEstateModelResponse;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.emupapps.the_broker.viewmodels.InfoUserViewModel;
import com.emupapps.the_broker.viewmodels.RealEstateViewModel;
import com.bumptech.glide.Glide;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.emupapps.the_broker.utils.Constants.BASE_URL;
import static com.emupapps.the_broker.utils.Constants.LOCALE;
import static com.emupapps.the_broker.utils.Constants.SUCCESS;

/**
 * A simple {@link Fragment} subclass.
 */
public class OccupantFragment extends Fragment {

    private static final String TAG = OccupantFragment.class.getSimpleName();

    ImageView mBack;
    TextView mTitle;
    CircleImageView mImage;
    TextView mName;
    TextView mEmail;
    TextView mPhoneNumber;
    TextView mRequestDate;
    TextView mStartDate;
    TextView mDuration;
    TextView mAmount;
    TextView mInsuranceAmount;
    TextView mPayment;
    ProgressBar mProgress;

    private Toast mToast;

    public OccupantFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_occupant, container, false);
        String locale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE, Locale.getDefault().getLanguage());
        if (locale.equals("ar")) {
            mBack.setImageResource(R.drawable.ic_arrow_ar);
        } else {
            mBack.setImageResource(R.drawable.ic_arrow);
        }

        RealEstateViewModel viewModelRealEstate =
                ViewModelProviders.of(getActivity()).get(RealEstateViewModel.class);
        viewModelRealEstate.getRealEstate().observe(this, new Observer<RealEstateModelResponse>() {
            @Override
            public void onChanged(RealEstateModelResponse realEstateModelResponse) {
                if (realEstateModelResponse.getKey().equals(SUCCESS) &&
                        realEstateModelResponse.getRealEstate().getOwnership().getTenant_id() != null &&
                        !realEstateModelResponse.getRealEstate().getOwnership().getTenant_id().equals("0")) {
                    loadTenant(realEstateModelResponse.getRealEstate().getOwnership().getTenant_id());
                } else {
                    loadOwner(realEstateModelResponse.getRealEstate().getOwnership().getOwner());
                }
            }
        });
        return view;
    }

    public void back() {
        getActivity().onBackPressed();
    }

    private void loadTenant(String id) {
        if (mToast != null) {
            mToast.cancel();
        }
        mTitle.setText(R.string.tenant);
        InfoUserViewModel viewModelInfoUser =
                ViewModelProviders.of(this).get(InfoUserViewModel.class);
        viewModelInfoUser.userInfo(id);
        viewModelInfoUser.getUserInfo().observe(this, new Observer<UserInfoModelResponse>() {
            @Override
            public void onChanged(UserInfoModelResponse userInfoModelResponse) {
                if (userInfoModelResponse.getKey().equals(SUCCESS)) {
                    Glide.with(OccupantFragment.this).load(BASE_URL +
                            userInfoModelResponse.getUser().getPhoto()).into(mImage);
                    mName.setText(userInfoModelResponse.getUser().getName());
                    mEmail.setText(userInfoModelResponse.getUser().getEmail());
                    if (userInfoModelResponse.getUser().getCode() != null)
                        mPhoneNumber.setText(getString(R.string.space, userInfoModelResponse.getUser().getCode(),
                                userInfoModelResponse.getUser().getPhone()));
                } else {
                    mToast = Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT);
                    mToast.show();
                }
            }
        });
        viewModelInfoUser.isLoading().observe(this, loading -> {
            mProgress.setVisibility(loading ? View.VISIBLE : View.GONE);
        });
        viewModelInfoUser.failure().observe(this, failed -> {
            if (failed) {
                mToast = Toast.makeText(getActivity(), R.string.connection_to_server_lost, Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
    }

    private void loadOwner(Owner owner) {
        mTitle.setText(R.string.owner);
        Glide.with(this).load(BASE_URL + owner.getPhoto()).into(mImage);
        mName.setText(owner.getName());
        mEmail.setText(owner.getEmail());
        mPhoneNumber.setText(getString(R.string.space, owner.getCode(), owner.getPhone()));

    }
}
