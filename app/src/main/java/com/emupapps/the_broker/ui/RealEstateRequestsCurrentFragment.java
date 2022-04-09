package com.emupapps.the_broker.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.adapters.RealEstateRequestsAdapter;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

import static com.emupapps.the_broker.ui.MainActivity.loadFragment;
import static com.emupapps.the_broker.utils.Constants.LOCALE;

/**
 * A simple {@link Fragment} subclass.
 */
public class RealEstateRequestsCurrentFragment extends Fragment {

    TabLayout mTabLayout;
    ViewPager mViewPager;
    ImageView mBack;
    TextView mTitle;
    ImageView mRequestsHistory;

    public RealEstateRequestsCurrentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_real_estate_requests_current, container,
                false);
        String locale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE, Locale.getDefault().getLanguage());
        if (locale.equals("ar")) {
            mViewPager.setRotation(180);
            mBack.setImageResource(R.drawable.ic_arrow_ar);
        } else {
            mBack.setImageResource(R.drawable.ic_arrow);
        }
        mTitle.setText(R.string.requests);
        mRequestsHistory.setVisibility(View.VISIBLE);

        RealEstateRequestsAdapter tabsAdapter =
                new RealEstateRequestsAdapter(getContext(), getChildFragmentManager(), 0);
        mViewPager.setAdapter(tabsAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        return view;
    }

    public void back(){
        getActivity().onBackPressed();
    }

    public void requestsHistory() {
        loadFragment(RealEstateRequestsCurrentFragment.this.getActivity().getSupportFragmentManager(),
                new RealEstateRequestsPreviousFragment(),
                true);
    }
}
