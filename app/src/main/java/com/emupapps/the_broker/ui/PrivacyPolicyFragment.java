package com.emupapps.the_broker.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.adapters.PrivacyPolicyTabsAdapter;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.emupapps.the_broker.utils.SoftKeyboard;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;
import static com.emupapps.the_broker.ui.MainActivity.sDrawerLayout;
import static com.emupapps.the_broker.utils.Constants.LOCALE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrivacyPolicyFragment extends Fragment {

    TabLayout mTabLayout;
    ViewPager mViewPager;
    ImageView mMenu;
    TextView mTitle;

    public PrivacyPolicyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_privacy_policy, container, false);
        String locale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE,
                Locale.getDefault().getLanguage());
        mMenu.setImageResource(R.drawable.ic_menu);
        mTitle.setText(R.string.terms_and_conditions);

        PrivacyPolicyTabsAdapter tabsAdapter = new PrivacyPolicyTabsAdapter(getContext(), getActivity().getSupportFragmentManager(), 0);
        mViewPager.setAdapter(tabsAdapter);
        if (locale.equals("ar"))
            mViewPager.setRotation(180);
        mTabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    public void menu() {
        sDrawerLayout.openDrawer(GravityCompat.START);
        SoftKeyboard.dismissKeyboardInActivity(getActivity());
    }
}
