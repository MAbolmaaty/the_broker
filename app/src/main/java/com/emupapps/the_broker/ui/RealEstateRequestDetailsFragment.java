package com.emupapps.the_broker.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.utils.SharedPrefUtil;

import java.util.Locale;

import static com.emupapps.the_broker.utils.Constants.LOCALE;

/**
 * A simple {@link Fragment} subclass.
 */
public class RealEstateRequestDetailsFragment extends Fragment {

    ImageView mBack;
    TextView mTitle;
    TextView mStatus;

    public RealEstateRequestDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_real_estate_request_details,
                container, false);
        String locale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE, Locale.getDefault().getLanguage());
        if (locale.equals("ar")) {
            mBack.setImageResource(R.drawable.ic_arrow_ar);
        } else {
            mBack.setImageResource(R.drawable.ic_arrow);
        }
        mTitle.setText(R.string.request_maintenance);
        //---------------------------------------------------------------------Delete next Line
        mStatus.setVisibility(View.VISIBLE);
        mStatus.setText(R.string.pending);
        return view;
    }
}
