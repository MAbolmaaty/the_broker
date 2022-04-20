package com.emupapps.the_broker.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.databinding.FragmentDescriptionBinding;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.emupapps.the_broker.viewmodels.RealEstateViewModel;

import java.util.Locale;

import static com.emupapps.the_broker.utils.Constants.LOCALE;

/**
 * A simple {@link Fragment} subclass.
 */
public class DescriptionFragment extends Fragment {

    private FragmentDescriptionBinding mBinding;

    private RealEstateViewModel mViewModelRealEstate;
    private String mLocale;

    public DescriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentDescriptionBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        mLocale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE, Locale.getDefault().getLanguage());
        if (mLocale.equals("ar"))
            view.setRotation(-180);
        mViewModelRealEstate = new ViewModelProvider(requireActivity())
                .get(RealEstateViewModel.class);
        mViewModelRealEstate.getRealEstateDetails().observe(this, realEstate -> {
            if (realEstate != null){
                mBinding.description.setText(realEstate.getDescription());
            }
        });

        return view;
    }

    public static DescriptionFragment newInstance(){
        return new DescriptionFragment();
    }
}
