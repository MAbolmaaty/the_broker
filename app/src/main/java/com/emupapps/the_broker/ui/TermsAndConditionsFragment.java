package com.emupapps.the_broker.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.adapters.PrivacyPolicyTabsAdapter;
import com.emupapps.the_broker.databinding.FragmentTermsAndConditionsBinding;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.emupapps.the_broker.utils.SoftKeyboard;

import java.util.Locale;
import static com.emupapps.the_broker.ui.MainActivity.sDrawerLayout;
import static com.emupapps.the_broker.utils.Constants.LOCALE;

/**
 * A simple {@link Fragment} subclass.
 */
public class TermsAndConditionsFragment extends Fragment {
    private FragmentTermsAndConditionsBinding mBinding;
    public TermsAndConditionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentTermsAndConditionsBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        String locale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE,
                Locale.getDefault().getLanguage());
//        mMenu.setImageResource(R.drawable.ic_menu);
//        mTitle.setText(R.string.terms_and_conditions);

//        PrivacyPolicyTabsAdapter tabsAdapter =
//                new PrivacyPolicyTabsAdapter(getContext(), getActivity().getSupportFragmentManager(), 0);
        //mBinding.viewPager.setAdapter(tabsAdapter);
        if (locale.equals("ar"))
            mBinding.viewPager.setRotation(180);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);

        return view;
    }

    public void menu() {
        sDrawerLayout.openDrawer(GravityCompat.START);
        SoftKeyboard.dismissKeyboardInActivity(getActivity());
    }
}
