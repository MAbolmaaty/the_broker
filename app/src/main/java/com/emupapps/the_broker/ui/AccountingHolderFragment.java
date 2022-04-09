package com.emupapps.the_broker.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.models.real_estate.RealEstateModelResponse;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.emupapps.the_broker.viewmodels.RealEstateViewModel;

import java.util.Locale;

import static com.emupapps.the_broker.utils.Constants.LOCALE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountingHolderFragment extends Fragment {

    ImageView mBack;
    TextView mTitle;
    TextView mIncome;
    TextView mExpenses;
    TextView mCommission;
    TextView mBalance;

    public AccountingHolderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_accounting_holder, container, false);
        String locale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE,
                Locale.getDefault().getLanguage());
        if (locale.equals("ar")) {
            mBack.setImageResource(R.drawable.ic_arrow_ar);
        } else {
            mBack.setImageResource(R.drawable.ic_arrow);
        }
        mTitle.setText(R.string.accounting);
        RealEstateViewModel viewModelRealEstate = ViewModelProviders.of(getActivity()).get(RealEstateViewModel.class);
        viewModelRealEstate.getRealEstate().observe(this, new Observer<RealEstateModelResponse>() {
            @Override
            public void onChanged(RealEstateModelResponse realEstateModelResponse) {
                mCommission.setText(realEstateModelResponse.getRealEstate().getCommission());
                mIncome.setText(realEstateModelResponse.getRealEstate().getEarnings());
                mExpenses.setText(realEstateModelResponse.getRealEstate().getExpenses());
                mBalance.setText(realEstateModelResponse.getRealEstate().getRevenues());
            }
        });
        return view;
    }

    public void back(){
        getActivity().onBackPressed();
    }
}
