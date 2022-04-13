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
public class PropertyRightsFragment extends Fragment {

    TextView mPropertyRights;
    ProgressBar mProgressBar;

    private Toast mToast;

    public PropertyRightsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_property_rights, container, false);
        String locale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE, Locale.getDefault().getLanguage());
        if (locale.equals("ar"))
            view.setRotation(-180);
        PrivacyPolicyViewModel viewModelPrivacyPolicy =
                new ViewModelProvider(this).get(PrivacyPolicyViewModel.class);
        viewModelPrivacyPolicy.privacyPolicy();
        viewModelPrivacyPolicy.getPrivacyPolicy().observe(this, privacyPolicyModelResponse -> {
            if (privacyPolicyModelResponse.getKey().equals(SUCCESS)) {
                mPropertyRights
                        .setText(Html.fromHtml(privacyPolicyModelResponse.
                                getPrivacyPolicies().getPropertyRights()[0].getDescribtion()));
            }else {
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
        viewModelPrivacyPolicy.failure().observe(this, failure -> {
            if (failure){
                if (mToast != null)
                    mToast.cancel();
                mToast = Toast.makeText(getActivity(), R.string.connection_to_server_lost, Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
        return view;
    }

    public static PropertyRightsFragment newInstance() {
        return new PropertyRightsFragment();
    }
}
