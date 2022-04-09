package com.emupapps.the_broker.ui;

import android.os.Bundle;

import androidx.core.view.GravityCompat;
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
import com.emupapps.the_broker.utils.SoftKeyboard;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

import static com.emupapps.the_broker.ui.MainActivity.loadFragment;
import static com.emupapps.the_broker.ui.MainActivity.sDrawerLayout;
import static com.emupapps.the_broker.utils.Constants.LOCALE;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestsAgentFragment extends Fragment {

    TabLayout mTabLayout;
    ViewPager mViewPager;
    ImageView mMenu;
    TextView mTitle;
    ImageView mRequestsHistory;

    public RequestsAgentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_requests_admin, container, false);
        String locale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE, Locale.getDefault().getLanguage());
        if (locale.equals("ar")) {
            mViewPager.setRotation(180);
        }
        mMenu.setImageResource(R.drawable.ic_menu);
        mTitle.setText(R.string.requests_current);
        mRequestsHistory.setVisibility(View.VISIBLE);
        RealEstateRequestsAdapter tabsAdapter =
                new RealEstateRequestsAdapter(getContext(), getChildFragmentManager(), 0);
        mViewPager.setAdapter(tabsAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        return view;
    }

    public void menu() {
        sDrawerLayout.openDrawer(GravityCompat.START);
        SoftKeyboard.dismissKeyboardInActivity(getActivity());
    }

    public void requestsHistory() {
        loadFragment(RequestsAgentFragment.this.getActivity().getSupportFragmentManager(),
                new RealEstateRequestsPreviousFragment(),
                true);
    }
}
