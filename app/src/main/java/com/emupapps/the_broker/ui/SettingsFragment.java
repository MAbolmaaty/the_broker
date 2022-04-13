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
import com.emupapps.the_broker.adapters.SettingsTabsAdapter;
import com.emupapps.the_broker.databinding.FragmentSettingsBinding;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.emupapps.the_broker.utils.SoftKeyboard;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

import static com.emupapps.the_broker.ui.MainActivity.sDrawerLayout;
import static com.emupapps.the_broker.utils.Constants.LOCALE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding mBinding;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSettingsBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        String locale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE,
                Locale.getDefault().getLanguage());
//        mMenu.setImageResource(R.drawable.ic_menu);
//        mTitle.setText(R.string.settings);
        SettingsTabsAdapter adapter = new SettingsTabsAdapter(getContext(), getChildFragmentManager(),
                0);
        mBinding.viewPager.setAdapter(adapter);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
//        if (locale.equals("ar"))
//            mViewPager.setRotation(180);

        return view;
    }

    public void menu(){
        sDrawerLayout.openDrawer(GravityCompat.START);
        SoftKeyboard.dismissKeyboardInActivity(getActivity());
    }
}
