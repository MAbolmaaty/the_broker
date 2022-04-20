package com.emupapps.the_broker.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emupapps.the_broker.adapters.RequestsUserAdapter;
import com.emupapps.the_broker.databinding.FragmentRequestsBinding;
import com.emupapps.the_broker.models.requests_user.Request;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.emupapps.the_broker.utils.SoftKeyboard;
import com.emupapps.the_broker.viewmodels.RealEstateViewModel;
import com.emupapps.the_broker.viewmodels.RequestsUserViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.emupapps.the_broker.ui.MainActivity.loadFragment;
import static com.emupapps.the_broker.ui.MainActivity.sDrawerLayout;
import static com.emupapps.the_broker.utils.Constants.SUCCESS;
import static com.emupapps.the_broker.utils.Constants.USER_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestsFragment extends Fragment {

    private static final String TAG = RequestsFragment.class.getSimpleName();
    private FragmentRequestsBinding mBinding;

    private String mUserId;
    private RequestsUserAdapter mAdapterUserRequests;
    private RealEstateViewModel mViewModelRealEstate;
    private List<Request> mListRequests = new ArrayList<>();

    public RequestsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentRequestsBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
//        mMenu.setImageResource(R.drawable.ic_menu);
//        mTitle.setText(R.string.requests);

        mViewModelRealEstate =
                new ViewModelProvider(getActivity()).get(RealEstateViewModel.class);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mBinding.recyclerView.setLayoutManager(layoutManager);
        mBinding.recyclerView.setHasFixedSize(true);

        mUserId = SharedPrefUtil.getInstance(getContext()).read(USER_ID, null);
        if (mUserId == null) {
//            viewModelLogin.getUser().observe(this, loginModelResponse -> {
//                mUserId = loginModelResponse.getUser().getId();
//                loadRealEstates(mUserId);
//            });
        } else {
            loadRealEstates(mUserId);
        }

        mBinding.requestsOpenMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenu();
            }
        });

        return view;
    }

    private void openMenu(){
        sDrawerLayout.openDrawer(GravityCompat.START);
        SoftKeyboard.dismissKeyboardInActivity(getActivity());
    }

    private void loadRealEstates(String userId){
        RequestsUserViewModel viewModelUserRequests =
                new ViewModelProvider(getActivity()).get(RequestsUserViewModel.class);
        viewModelUserRequests.userRequests(userId);
        viewModelUserRequests.getUserRequests().observe(this, userRequestsModelResponse -> {
            if (userRequestsModelResponse.getKey().equals(SUCCESS)) {
                mListRequests.clear();
                mListRequests.addAll(Arrays.asList(userRequestsModelResponse.getRequests()));
                if (mListRequests.size() < 1){
                    //mNoRequests.setVisibility(View.VISIBLE);
                } else {
                    //mNoRequests.setVisibility(View.GONE);
                }
                mAdapterUserRequests = new RequestsUserAdapter(getContext(), mListRequests, position -> {
                    //mViewModelRealEstate.setRealEstateId(mListRequests.get(position).getAkar_id());
                    loadFragment( RequestsFragment.this.getActivity().getSupportFragmentManager(),
                            new RealEstateFragment(), true);
                });
                //mRecyclerView.setAdapter(mAdapterUserRequests);
            }else {
//                mToast = Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT);
//                mToast.show();
            }
        });
        viewModelUserRequests.isLoading().observe(this, loading -> {
            if (loading){
                //mProgressBar.setVisibility(View.VISIBLE);
            } else {
                //mProgressBar.setVisibility(View.GONE);
            }
        });
        viewModelUserRequests.failure().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean failed) {
                if (failed){
//                    mToast = Toast.makeText(getActivity(), R.string.connection_to_server_lost, Toast.LENGTH_SHORT);
//                    mToast.show();
                }
            }
        });
    }
}
