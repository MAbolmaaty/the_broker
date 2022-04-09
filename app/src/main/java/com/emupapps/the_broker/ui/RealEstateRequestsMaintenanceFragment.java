package com.emupapps.the_broker.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.adapters.RealEstateRequestsAdapter;
import com.emupapps.the_broker.utils.SharedPrefUtil;

import java.util.Locale;

import static com.emupapps.the_broker.utils.Constants.LOCALE;

/**
 * A simple {@link Fragment} subclass.
 */
public class RealEstateRequestsMaintenanceFragment extends Fragment {

    RecyclerView mRecyclerView;
    ProgressBar mProgressBar;
    TextView mNoRequests;

    private RealEstateRequestsAdapter mAdapterRealEstateRequests;

    public RealEstateRequestsMaintenanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_real_estate_requests_maintenance,
                container, false);
        String locale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE, Locale.getDefault().getLanguage());
        if (locale.equals("ar"))
            view.setRotation(-180);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,
                false);
        mRecyclerView.setLayoutManager(layoutManager);
        loadRequests();
        return view;
    }

    public static RealEstateRequestsMaintenanceFragment newInstance(){return new RealEstateRequestsMaintenanceFragment();}

    private void loadRequests(){
        mNoRequests.setVisibility(View.VISIBLE);
    }
}
