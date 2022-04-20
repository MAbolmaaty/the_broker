package com.emupapps.the_broker.ui;


import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.adapters.RealEstatesTabsAdapter;
import com.emupapps.the_broker.databinding.FragmentRealEstatesBinding;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.emupapps.the_broker.utils.SoftKeyboard;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

import static com.emupapps.the_broker.ui.MainActivity.sDrawerLayout;
import static com.emupapps.the_broker.utils.Constants.LOCALE;

/**
 * A simple {@link Fragment} subclass.
 */
public class RealEstatesFragment extends Fragment {

    private FragmentRealEstatesBinding mBinding;

    public RealEstatesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentRealEstatesBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();

//        String locale = SharedPrefUtil.getInstance(getActivity()).
//                read(LOCALE, Locale.getDefault().getLanguage());
//        if (locale.equals("ar"))
//            mViewPager.setRotation(180);

//        mMenu.setImageResource(R.drawable.ic_menu);
//        mTitle.setText(R.string.real_estates);
        RealEstatesTabsAdapter tabsAdapter = new RealEstatesTabsAdapter(getContext(), getChildFragmentManager(), 0);
        mBinding.viewPager.setAdapter(tabsAdapter);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);

        mBinding.realEstatesOpenMenu.setOnClickListener(new View.OnClickListener() {
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
}
