package com.emupapps.the_broker.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.adapters.RealEstatesRentedAdapter;
import com.emupapps.the_broker.models.real_estates_rented.RentedRealEstates;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.emupapps.the_broker.utils.interfaces.RealEstateClickHandler;
import com.emupapps.the_broker.viewmodels.LoginViewModel;
import com.emupapps.the_broker.viewmodels.RealEstateViewModel;
import com.emupapps.the_broker.viewmodels.RealEstatesRentedViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.emupapps.the_broker.ui.MainActivity.loadFragment;
import static com.emupapps.the_broker.utils.Constants.LOCALE;
import static com.emupapps.the_broker.utils.Constants.SUCCESS;
import static com.emupapps.the_broker.utils.Constants.USER_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class RealEstatesRentedFragment extends Fragment {

    RecyclerView mRecyclerView;
    ProgressBar mProgressBar;
    TextView mNoRealEstates;

    private RealEstatesRentedViewModel mViewModelRentedRealEstates;
    private RealEstateViewModel mViewModelRealEstate;
    private String mUserId;
    private RealEstatesRentedAdapter mAdapter;
    private List<RentedRealEstates> mRentedRealEstates = new ArrayList<>();
    private Toast mToast;

    public RealEstatesRentedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_real_estates_rented, container,
                false);
        String locale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE, Locale.getDefault().getLanguage());
        if (locale.equals("ar"))
            view.setRotation(-180);

        mViewModelRentedRealEstates = ViewModelProviders.of(this).get(RealEstatesRentedViewModel.class);
        mViewModelRealEstate = ViewModelProviders.of(getActivity()).get(RealEstateViewModel.class);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mUserId = SharedPrefUtil.getInstance(getContext()).read(USER_ID, null);
        if (mUserId == null) {
            LoginViewModel viewModelLogin = ViewModelProviders.of(getActivity()).get(LoginViewModel.class);
            viewModelLogin.getUser().observe(this, loginModelResponse -> {
                mUserId = loginModelResponse.getUser().getId();
                loadRealEstates(mUserId);
            });
        } else {
            loadRealEstates(mUserId);
        }

        return view;
    }

    public static RealEstatesRentedFragment newInstance() {
        return new RealEstatesRentedFragment();
    }

    private void loadRealEstates(String userId){
        mViewModelRentedRealEstates.rentedRealEstates(userId);
        mViewModelRentedRealEstates.getRentedRealEstates().observe(this, rentedRealEstatesModelResponse -> {
            if (rentedRealEstatesModelResponse.getKey().equals(SUCCESS)){
                mRentedRealEstates.clear();
                mRentedRealEstates.addAll(Arrays.asList(rentedRealEstatesModelResponse.getRentedRealEstates()));
                if (mRentedRealEstates.size() < 1){
                    mNoRealEstates.setVisibility(View.VISIBLE);
                    return;
                }
                mAdapter = new RealEstatesRentedAdapter(getContext(), mRentedRealEstates, new RealEstateClickHandler() {
                    @Override
                    public void onClick(int position) {
                        mViewModelRealEstate.setRealEstateId(mRentedRealEstates.get(position).getAkar_id());
                        loadFragment(RealEstatesRentedFragment.this.getActivity().getSupportFragmentManager(),
                                new RealEstateFragment(), true);
                    }
                });
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setHasFixedSize(true);
            } else {
                mToast = Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
        mViewModelRentedRealEstates.isLoading().observe(this, loading -> {
            if (loading){
                mProgressBar.setVisibility(View.VISIBLE);
            } else {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
        mViewModelRentedRealEstates.failure().observe(this, failure -> {
            if (failure){
                if (mToast != null)
                    mToast.cancel();
                mToast = Toast.makeText(getActivity(), R.string.connection_to_server_lost, Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
    }
}
