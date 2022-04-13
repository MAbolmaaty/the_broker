package com.emupapps.the_broker.ui;


import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.emupapps.the_broker.viewmodels.PrivacyPolicyViewModel;

import java.util.Locale;

import static com.emupapps.the_broker.utils.Constants.LOCALE;
import static com.emupapps.the_broker.utils.Constants.SUCCESS;

/**
 * A simple {@link Fragment} subclass.
 */
public class TermsOfUseFragment extends Fragment {

    TextView mTermsOfUse;
    ProgressBar mProgressBar;

    private Toast mToast;

    public TermsOfUseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_terms_of_use, container, false);
        String locale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE,
                Locale.getDefault().getLanguage());
        if (locale.equals("ar"))
            view.setRotation(-180);
        PrivacyPolicyViewModel viewModelPrivacyPolicy =
                new ViewModelProvider(this).get(PrivacyPolicyViewModel.class);
        viewModelPrivacyPolicy.privacyPolicy();
        viewModelPrivacyPolicy.getPrivacyPolicy().observe(this, privacyPolicyModelResponse -> {
            if (privacyPolicyModelResponse.getKey().equals(SUCCESS)) {
                mTermsOfUse
                        .setText(Html.fromHtml(privacyPolicyModelResponse.
                                getPrivacyPolicies().getTermsOfUses()[0].getDescribtion()));
            } else {
                mToast = Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT);
                mToast.show();

            }
        });

        viewModelPrivacyPolicy.isLoading().observe(this, loading -> {
            if (loading) {
                mProgressBar.setVisibility(View.VISIBLE);
            } else {
                mProgressBar.setVisibility(View.GONE);
            }
        });
        return view;
    }

    public static TermsOfUseFragment newInstance() {
        return new TermsOfUseFragment();
    }
}
