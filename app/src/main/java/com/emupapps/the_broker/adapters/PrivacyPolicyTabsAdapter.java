package com.emupapps.the_broker.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.ui.PropertyRightsFragment;
import com.emupapps.the_broker.ui.TermsOfUseFragment;

public class PrivacyPolicyTabsAdapter extends FragmentStatePagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES =
            new int[] {R.string.terms_of_use, R.string.property_rights};

    private final Context mContext;

    public PrivacyPolicyTabsAdapter(Context context, @NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return TermsOfUseFragment.newInstance();
            case 1:
                return PropertyRightsFragment.newInstance();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}
