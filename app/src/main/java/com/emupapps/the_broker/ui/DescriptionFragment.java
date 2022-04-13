package com.emupapps.the_broker.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.emupapps.the_broker.viewmodels.RealEstateViewModel;

import java.util.Locale;

import static com.emupapps.the_broker.utils.Constants.LOCALE;

/**
 * A simple {@link Fragment} subclass.
 */
public class DescriptionFragment extends Fragment {

    TextView mTextViewDescription;
    View mShimmer1;
    View mShimmer2;
    View mShimmer3;

    private RealEstateViewModel mViewModelRealEstate;
    private String mLocale;

    public DescriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_description, container, false);
        mLocale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE, Locale.getDefault().getLanguage());
        if (mLocale.equals("ar"))
            view.setRotation(-180);
        mViewModelRealEstate = new ViewModelProvider(getActivity()).get(RealEstateViewModel.class);
        mViewModelRealEstate.getRealEstate().observe(this, realEstateModelResponse -> {
            if (realEstateModelResponse.getRealEstate() != null){
                mShimmer1.setVisibility(View.GONE);
                mShimmer2.setVisibility(View.GONE);
                mShimmer3.setVisibility(View.GONE);
                mTextViewDescription.setText(realEstateModelResponse.getRealEstate().getDescribtion());
            }
        });

        return view;
    }

    public static DescriptionFragment newInstance(){
        return new DescriptionFragment();
    }
}
