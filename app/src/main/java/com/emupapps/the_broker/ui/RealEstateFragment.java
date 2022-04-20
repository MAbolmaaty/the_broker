package com.emupapps.the_broker.ui;


import static com.emupapps.the_broker.ui.MainActivity.loadFragment;
import static com.emupapps.the_broker.ui.MainActivity.progress;
import static com.emupapps.the_broker.ui.MainActivity.sDrawerLayout;
import static com.emupapps.the_broker.utils.Constants.AGENT;
import static com.emupapps.the_broker.utils.Constants.AUCTION;
import static com.emupapps.the_broker.utils.Constants.CONTACT_OWNER;
import static com.emupapps.the_broker.utils.Constants.HOLDER;
import static com.emupapps.the_broker.utils.Constants.LOCALE;
import static com.emupapps.the_broker.utils.Constants.RENT;
import static com.emupapps.the_broker.utils.Constants.REQUEST_OWNERSHIP;
import static com.emupapps.the_broker.utils.Constants.REQUEST_RENT;
import static com.emupapps.the_broker.utils.Constants.SALE;
import static com.emupapps.the_broker.utils.Constants.TENANT;
import static com.emupapps.the_broker.utils.Constants.USER_ID;
import static com.emupapps.the_broker.utils.Constants.USER_TYPE;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.adapters.RealEstateTabsAdapter;
import com.emupapps.the_broker.adapters.SlidesAdapter;
import com.emupapps.the_broker.databinding.FragmentRealEstateBinding;
import com.emupapps.the_broker.models.RealEstateImage;
import com.emupapps.the_broker.models.requests_user.Request;
import com.emupapps.the_broker.models.slides.Slide;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.emupapps.the_broker.viewmodels.FavoriteViewModel;
import com.emupapps.the_broker.viewmodels.FavoritesViewModel;
import com.emupapps.the_broker.viewmodels.OwnerContactViewModel;
import com.emupapps.the_broker.viewmodels.ProfileViewModel;
import com.emupapps.the_broker.viewmodels.RealEstateViewModel;
import com.emupapps.the_broker.viewmodels.ReportViewModel;
import com.emupapps.the_broker.viewmodels.RequestSubmittedViewModel;
import com.emupapps.the_broker.viewmodels.RequestsUserViewModel;
import com.emupapps.the_broker.viewmodels.UnFavoriteViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.smarteist.autoimageslider.IndicatorAnimations;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class RealEstateFragment extends Fragment {
    private FragmentRealEstateBinding mBinding;

    private static final String TAG = RealEstateFragment.class.getSimpleName();

    private RealEstateViewModel mViewModelRealEstate;
    private final List<Slide> mListImages = new ArrayList<>();

    private String mLocale;


    public RealEstateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentRealEstateBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();

        mLocale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE, Locale.getDefault().getLanguage());
        if (mLocale.equals("ar")) {
            mBinding.viewPager.setRotation(180);
            mBinding.back.setImageResource(R.drawable.ic_arrow_ar);
        }

        //Initialize ViewModels
        mViewModelRealEstate =
                new ViewModelProvider(requireActivity()).get(RealEstateViewModel.class);

        mBinding.sliderViewRealEstate.setIndicatorAnimation(IndicatorAnimations.FILL);

        RealEstateTabsAdapter realEstateTabsAdapter =
                new RealEstateTabsAdapter(getContext(), getChildFragmentManager(), 0);
        mBinding.viewPager.setAdapter(realEstateTabsAdapter);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);

        mBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        getRealEstateDetails();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        sDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.START);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        sDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED, GravityCompat.START);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    private void getRealEstateDetails() {
        mViewModelRealEstate.getRealEstateDetails().observe(this, realEstate -> {
            if (realEstate != null) {
                mBinding.shimmer1.setVisibility(View.GONE);
                mBinding.shimmer2.setVisibility(View.GONE);
                mBinding.shimmer3.setVisibility(View.GONE);

                mBinding.title.setText(realEstate.getTitle());
                mBinding.price.setText(String.format("%s $", realEstate.getPrice()));
                mBinding.addressDetails.setText(realEstate.getAddress());

                switch (realEstate.getStatus()){
                    case SALE:
                        mBinding.status.setText(getString(R.string.sale));
                        break;
                    case RENT:
                        mBinding.status.setText(getString(R.string.rent));
                        break;
                    case AUCTION:
                        mBinding.status.setText(getString(R.string.auctions));
                        break;
                }

                mListImages.clear();
                for(RealEstateImage image : realEstate.getImages()){
                    mListImages.add(new Slide(image.getUrl()));
                }

                SlidesAdapter slidesShowAdapter = new SlidesAdapter(getContext(), mListImages);
                mBinding.sliderViewRealEstate.setSliderAdapter(slidesShowAdapter);
            }
        });
    }

    public void back() {
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }
}
