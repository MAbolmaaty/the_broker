package com.emupapps.the_broker.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.adapters.RealEstateRequestsUserAdapter;
import com.emupapps.the_broker.models.real_estate_requests_user.Request;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.emupapps.the_broker.viewmodels.LoginViewModel;
import com.emupapps.the_broker.viewmodels.RealEstateRequestsUserViewModel;
import com.emupapps.the_broker.viewmodels.RealEstateViewModel;
import com.emupapps.the_broker.viewmodels.RequestSubmittedViewModel;

import java.util.ArrayList;
import java.util.Locale;

import static com.emupapps.the_broker.ui.MainActivity.loadFragment;
import static com.emupapps.the_broker.utils.Constants.LOCALE;
import static com.emupapps.the_broker.utils.Constants.REQUEST_MAINTENANCE;
import static com.emupapps.the_broker.utils.Constants.SUCCESS;
import static com.emupapps.the_broker.utils.Constants.REQUEST_TERMINATION;
import static com.emupapps.the_broker.utils.Constants.USER_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class RealEstateRequestsUserHistoryFragment extends Fragment {

    ImageView mBack;
    TextView mTitle;
    TextView mNoRequests;
    RecyclerView mRecyclerView;
    ProgressBar mProgress;

    private RealEstateViewModel mViewModelRealEstate;

    private String mUserId;
    private Toast mToast;

    public RealEstateRequestsUserHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_real_estate_requests_user_history_fragment,
                container, false);
        String locale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE, Locale.getDefault().getLanguage());
        if (locale.equals("ar")) {
            mBack.setImageResource(R.drawable.ic_arrow_ar);
        } else {
            mBack.setImageResource(R.drawable.ic_arrow);
        }
        mTitle.setText(R.string.requests_history);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,
                false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mUserId = SharedPrefUtil.getInstance(getContext()).read(USER_ID, null);;
        if (mUserId == null){
//            viewModelLogin.getUser().observe(this, loginModelResponse -> {
//                mUserId = loginModelResponse.getUser().getId();
//                RealEstateViewModel viewModelRealEstate = ViewModelProviders.of(getActivity()).get(RealEstateViewModel.class);
//                viewModelRealEstate.getRealEstate().observe(this, realEstateModelResponse ->
//                        loadRequests(realEstateModelResponse.getRealEstate().getId()));
//            });
        } else {
            mViewModelRealEstate =
                    new ViewModelProvider(getActivity()).get(RealEstateViewModel.class);
            mViewModelRealEstate.getRealEstate().observe(this, realEstateModelResponse ->
                    loadRequests(realEstateModelResponse.getRealEstate().getId()));
        }
        return view;
    }

    public void back() {
        getActivity().onBackPressed();
    }

    private void loadRequests(String realEstateId) {
        if (mToast != null)
            mToast.cancel();
        RealEstateRequestsUserViewModel viewModelRealEstateRequests =
                new ViewModelProvider(this).get(RealEstateRequestsUserViewModel.class);
        viewModelRealEstateRequests.realEstateRequests(realEstateId);
        ArrayList<Request> listRequests = new ArrayList<>();
        viewModelRealEstateRequests.getRealEstateRequests().observe(this, realEstateRequestsUserModelResponse -> {
            if (realEstateRequestsUserModelResponse.getKey().equals(SUCCESS)) {
                listRequests.clear();
                for(Request request : realEstateRequestsUserModelResponse.getRequests()){
                    if (request.getUser().getId().equals(mUserId)){
                        listRequests.add(request);
                    }
                }
                if (listRequests.size() < 1) {
                    mNoRequests.setVisibility(View.VISIBLE);
                } else {
                    mNoRequests.setVisibility(View.GONE);
                }
                RealEstateRequestsUserAdapter adapter =
                        new RealEstateRequestsUserAdapter(getActivity(), listRequests,
                                position -> {
                                    RequestSubmittedViewModel viewModelRequestSubmitted =
                                            new ViewModelProvider(getActivity())
                                                    .get(RequestSubmittedViewModel.class);
                                    viewModelRequestSubmitted.setRequestId(listRequests.get(position).getId());
                                    if (listRequests.get(position).getDateExit() != null && !TextUtils.isEmpty(listRequests.get(position).getDateExit())){
                                        mViewModelRealEstate.setTypeRequest(REQUEST_TERMINATION);
                                        loadFragment(RealEstateRequestsUserHistoryFragment.this.getActivity().getSupportFragmentManager(),
                                                new RequestSubmittedFragment(), true);
                                    } else if(listRequests.get(position).getMaintenance_type() != null
                                            && !TextUtils.isEmpty(listRequests.get(position).getMaintenance_type())){
                                        mViewModelRealEstate.setTypeRequest(REQUEST_MAINTENANCE);
                                        loadFragment(RealEstateRequestsUserHistoryFragment.this.getActivity().getSupportFragmentManager(),
                                                new RequestSubmittedFragment(), true);
                                    }
                                });
                mRecyclerView.setAdapter(adapter);
            } else {
                listRequests.clear();
                mNoRequests.setVisibility(View.VISIBLE);
            }
        });
        viewModelRealEstateRequests.isLoading().observe(this, loading -> {
            if (loading) {
                mProgress.setVisibility(View.VISIBLE);
            } else {
                mProgress.setVisibility(View.GONE);
            }
        });
        viewModelRealEstateRequests.failure().observe(this, failed -> {
            if (failed) {
                mToast = Toast.makeText(RealEstateRequestsUserHistoryFragment.this.getActivity(),
                        R.string.connection_to_server_lost, Toast.LENGTH_SHORT);
                mToast.show();
                listRequests.clear();
                mNoRequests.setVisibility(View.VISIBLE);
            }
        });
    }
}
