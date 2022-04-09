package com.emupapps.the_broker.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.adapters.DebenturesRentAdapter;
import com.emupapps.the_broker.utils.SharedPrefUtil;

import java.util.Locale;

import static com.emupapps.the_broker.ui.MainActivity.loadFragment;
import static com.emupapps.the_broker.utils.Constants.LOCALE;

/**
 * A simple {@link Fragment} subclass.
 */
public class DebenturesRentFragment extends Fragment {

    ImageView mBack;
    TextView mTitle;
    RecyclerView mRecyclerView;

    public DebenturesRentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_debentures_rent, container, false);
        String locale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE,
                Locale.getDefault().getLanguage());
        if (locale.equals("ar")) {
            mBack.setImageResource(R.drawable.ic_arrow_ar);
        } else {
            mBack.setImageResource(R.drawable.ic_arrow);
        }
        mTitle.setText(R.string.debentures_rent);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        loadDebentures();
        return view;
    }

    private void loadDebentures(){
        DebenturesRentAdapter adapter = new DebenturesRentAdapter(getActivity(),
                position -> {

                }, position -> loadFragment(DebenturesRentFragment.this.getActivity().getSupportFragmentManager(),
                        new DebentureFragment(), true));
        mRecyclerView.setAdapter(adapter);
    }

    public void back(){
        getActivity().onBackPressed();
    }
}
