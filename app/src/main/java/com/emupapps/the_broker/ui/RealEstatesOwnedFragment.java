package com.emupapps.the_broker.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.adapters.RealEstatesOwnedAdapter;
import com.emupapps.the_broker.models.real_estates_owned.RealEstatesOwned;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.emupapps.the_broker.utils.interfaces.RealEstateClickHandler;
import com.emupapps.the_broker.viewmodels.LoginViewModel;
import com.emupapps.the_broker.viewmodels.RealEstatesOwnedViewModel;
import com.emupapps.the_broker.viewmodels.RealEstateViewModel;

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
public class RealEstatesOwnedFragment extends Fragment {

    RecyclerView mRecyclerView;
    ProgressBar mProgressBar;
    TextView mNoRealEstates;

    private RealEstatesOwnedViewModel mViewModelMyRealEstates;
    private RealEstatesOwnedAdapter mAdapter;
    private RealEstateViewModel mViewModelRealEstate;
    private String mUserId;
    private List<RealEstatesOwned> mListMyRealEstates = new ArrayList<>();
    private Toast mToast;

    public RealEstatesOwnedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_real_estates_owned, container,
                false);
        String locale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE, Locale.getDefault().getLanguage());
        if (locale.equals("ar"))
            view.setRotation(-180);

        mViewModelMyRealEstates = ViewModelProviders.of(this).get(RealEstatesOwnedViewModel.class);
        mViewModelRealEstate = ViewModelProviders.of(getActivity()).get(RealEstateViewModel.class);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,
                false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

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

    private void loadRealEstates(String userId){
        mViewModelMyRealEstates.myRealEstates(userId);
        mViewModelMyRealEstates.getMyRealEstates().observe(this, myRealEstatesModelResponse -> {
            if (myRealEstatesModelResponse.getStatus().equals(SUCCESS)) {
                if (myRealEstatesModelResponse.getRealEstatesOwned().length < 1){
                    mNoRealEstates.setVisibility(View.VISIBLE);
                    return;
                }
                mListMyRealEstates.clear();
                mListMyRealEstates.addAll(Arrays.asList(myRealEstatesModelResponse.getRealEstatesOwned()));
                mAdapter = new RealEstatesOwnedAdapter(getContext(), mListMyRealEstates, new RealEstateClickHandler() {
                    @Override
                    public void onClick(int position) {
                        mViewModelRealEstate.setRealEstateId(mListMyRealEstates.get(position).getRealEstateId());
                        loadFragment(RealEstatesOwnedFragment.this.getActivity().getSupportFragmentManager(),
                                new RealEstateFragment(), true);
                    }
                });
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mToast = Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT);
                mToast.show();

            }
        });
        mViewModelMyRealEstates.isLoading().observe(this, loading -> {
            if (loading) {
                mProgressBar.setVisibility(View.VISIBLE);
            } else {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
        mViewModelMyRealEstates.failure().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean failure) {
                if (failure){
                    if (mToast != null)
                        mToast.cancel();
                    mToast = Toast.makeText(getActivity(), R.string.connection_to_server_lost, Toast.LENGTH_SHORT);
                    mToast.show();
                }
            }
        });
    }

    public static RealEstatesOwnedFragment newInstance(){
        return new RealEstatesOwnedFragment();
    }
}
