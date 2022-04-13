package com.emupapps.the_broker.ui;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.adapters.SlidesAdapter;
import com.emupapps.the_broker.models.slides.Slide;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.emupapps.the_broker.viewmodels.LoginViewModel;
import com.emupapps.the_broker.viewmodels.SlidesViewModel;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import static com.emupapps.the_broker.ui.MainActivity.loadFragment;
import static com.emupapps.the_broker.ui.MainActivity.sDrawerLayout;
import static com.emupapps.the_broker.utils.Constants.FIRST_INSTALL;


/**
 * A simple {@link Fragment} subclass.
 */
public class SlidesShowFragment extends Fragment {

    SliderView mSliderView;
    TextView mTextViewSkip;
    ProgressBar mProgressBar;

    private static final String TAG = SlidesShowFragment.class.getSimpleName();

    private List<Slide> mListSlides = new ArrayList<>();
    private SlidesViewModel mViewModelSlides;

    public SlidesShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slides_show, container, false);

        mSliderView.setIndicatorAnimation(IndicatorAnimations.FILL);
        mViewModelSlides =
                new ViewModelProvider(getActivity()).get(SlidesViewModel.class);
        SharedPrefUtil.getInstance(getContext()).write(FIRST_INSTALL, false);
        mSliderView.setCurrentPageListener(currentPosition -> {
            if (currentPosition == 2) {
                mSliderView.setIndicatorSelectedColor(getResources().getColor(R.color.darkGrey));
                mSliderView.setIndicatorUnselectedColor(getResources().getColor(R.color.darkGrey));
                mTextViewSkip.setTextColor(getResources().getColor(R.color.darkGrey));
            } else {
                mSliderView.setIndicatorSelectedColor(Color.WHITE);
                mSliderView.setIndicatorUnselectedColor(Color.WHITE);
                mTextViewSkip.setTextColor(Color.WHITE);
            }
        });

        showSlides();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        sDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.START);
    }

    public void skip(){
        mProgressBar.setVisibility(View.VISIBLE);
        mTextViewSkip.setTextColor(getResources().getColor(R.color.grey));
        home();
    }

    private void showSlides() {
        mViewModelSlides.getSlides().observe(this, slidesModelResponse -> {
            mListSlides.add(new Slide(slidesModelResponse.getSlide()[0].getTitle1(),
                    R.drawable.slider_1, slidesModelResponse.getSlide()[0].getLink1()));
            mListSlides.add(new Slide(slidesModelResponse.getSlide()[0].getTitle2(),
                    R.drawable.slider_2, slidesModelResponse.getSlide()[0].getLink2()));
            mListSlides.add(new Slide(slidesModelResponse.getSlide()[0].getTitle3(),
                    R.drawable.slider_3, slidesModelResponse.getSlide()[0].getLink3()));

            SlidesAdapter adapter = new SlidesAdapter(getContext(), mListSlides);
            mSliderView.setSliderAdapter(adapter);
        });
    }

    private void home(){
        loadFragment(SlidesShowFragment.this.getActivity().getSupportFragmentManager(),
                new HomeFragment(), false);
//        viewModelLogin.loggedIn(false);
    }
}
