package com.emupapps.the_broker.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.emupapps.the_broker.viewmodels.AccountBankViewModel;
import com.emupapps.the_broker.viewmodels.RealEstateViewModel;

import java.util.Locale;

import static com.emupapps.the_broker.ui.MainActivity.loadFragment;
import static com.emupapps.the_broker.utils.Constants.LOCALE;
import static com.emupapps.the_broker.utils.Constants.SUCCESS;

/**
 * A simple {@link Fragment} subclass.
 */
public class AuctionJoinFragment extends Fragment {

    TextView mInsuranceAmount;
    TextView mBankName;
    TextView mAccountNumber;
    TextView mIBAN;
    ProgressBar mProgress;
    Button mConfirm;
    ImageView mBack;
    TextView mTitle;

    private AccountBankViewModel mViewModelBankAccount;

    public AuctionJoinFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_auction_join, container, false);
        String locale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE,
                Locale.getDefault().getLanguage());
        if (locale.equals("ar")) {
            mBack.setImageResource(R.drawable.ic_arrow_ar);
        } else {
            mBack.setImageResource(R.drawable.ic_arrow);
        }
        mTitle.setText(R.string.auction_join);
        mViewModelBankAccount = new ViewModelProvider(this).get(AccountBankViewModel.class);
        RealEstateViewModel viewModelRealEstate =
                new ViewModelProvider(getActivity()).get(RealEstateViewModel.class);
        viewModelRealEstate.getRealEstateId().observe(this, realEstateId -> {
            mViewModelBankAccount.bankAccount(realEstateId);
            mViewModelBankAccount.getBankAccount().observe(AuctionJoinFragment.this, bankAccountModelResponse -> {
                if (bankAccountModelResponse.getKey().equals(SUCCESS)) {
                    mConfirm.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.darkGrey));
                    mConfirm.setEnabled(true);
                    mInsuranceAmount.setText(bankAccountModelResponse.getBankAccount().getInsuranceAmount());
                    mAccountNumber.setText(bankAccountModelResponse.getBankAccount().getAccountNumber());
                    mBankName.setText(bankAccountModelResponse.getBankAccount().getBankName());
                    mIBAN.setText(bankAccountModelResponse.getBankAccount().getIBAN());
                }
            });
            mViewModelBankAccount.isLoading().observe(AuctionJoinFragment.this, loading -> {
                if (loading){
                    mProgress.setVisibility(View.VISIBLE);
                } else {
                    mProgress.setVisibility(View.INVISIBLE);
                }
            });
        });

        return view;
    }

    public void back() {
        getActivity().onBackPressed();
    }

    public void confirm() {
        loadFragment(AuctionJoinFragment.this.getActivity().getSupportFragmentManager(),
                new AuctionJoinConfirmFragment(),
                true);
    }

}
