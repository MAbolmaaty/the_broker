package com.emupapps.the_broker.ui;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.utils.SharedPrefUtil;

import java.util.Locale;

import static com.emupapps.the_broker.utils.Constants.LOCALE;

/**
 * A simple {@link Fragment} subclass.
 */
public class RealEstateRequestModifyFragment extends Fragment {

    ImageView mBack;
    TextView mTitle;
    ImageView mDelete;
    Button mPending;
    Button mNegotiated;
    Button mNegotiate;

    public RealEstateRequestModifyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_real_estate_request_modify,
                container, false);
        String locale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE, Locale.getDefault().getLanguage());
        if (locale.equals("ar")) {
            mBack.setImageResource(R.drawable.ic_arrow_ar);
        } else {
            mBack.setImageResource(R.drawable.ic_arrow);
        }
        mTitle.setText(R.string.request_modify);
        mDelete.setVisibility(View.VISIBLE);

        return view;
    }

    public void back(){
        getActivity().onBackPressed();
    }

    public void pending(){
        mPending.setEnabled(false);
        mPending.setTextColor(Color.WHITE);
        mNegotiate.setEnabled(true);
        mNegotiate.setTextColor(getResources().getColor(R.color.darkGrey));
        mNegotiated.setEnabled(true);
        mNegotiated.setTextColor(getResources().getColor(R.color.darkGrey));
    }

    public void negotiated(){
        mPending.setEnabled(true);
        mPending.setTextColor(getResources().getColor(R.color.darkGrey));
        mNegotiate.setEnabled(true);
        mNegotiate.setTextColor(getResources().getColor(R.color.darkGrey));
        mNegotiated.setEnabled(false);
        mNegotiated.setTextColor(Color.WHITE);
    }

    public void negotiate(){
        mPending.setEnabled(true);
        mPending.setTextColor(getResources().getColor(R.color.darkGrey));
        mNegotiate.setEnabled(false);
        mNegotiate.setTextColor(Color.WHITE);
        mNegotiated.setEnabled(true);
        mNegotiated.setTextColor(getResources().getColor(R.color.darkGrey));
    }
}
