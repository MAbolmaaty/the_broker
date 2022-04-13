package com.emupapps.the_broker.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.adapters.ProfileTabsAdapter;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.emupapps.the_broker.viewmodels.ProfileViewModel;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.emupapps.the_broker.ui.MainActivity.loadFragment;
import static com.emupapps.the_broker.ui.MainActivity.sDrawerLayout;
import static com.emupapps.the_broker.utils.Constants.BASE_URL;
import static com.emupapps.the_broker.utils.Constants.LOCALE;
import static com.emupapps.the_broker.utils.Constants.SUCCESS;
import static com.emupapps.the_broker.utils.Constants.USER_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = ProfileFragment.class.getSimpleName();

    TabLayout mTabLayout;
    ViewPager mViewPager;
    CircleImageView mUserImage;
    TextView mUsername;
    TextView mUserType;
    AppCompatRatingBar mRatingBar;
    ImageView mBack;
    TextView mTitle;

    private ProfileViewModel mViewModelUserInfo;
    private String mUserId;
    private Toast mToast;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mTitle.setText(R.string.profile);
        String locale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE, Locale.getDefault().getLanguage());
        if (locale.equals("ar")) {
            mViewPager.setRotation(180);
            mBack.setImageResource(R.drawable.ic_arrow_ar);
        } else {
            mBack.setImageResource(R.drawable.ic_arrow);
        }

        mViewModelUserInfo =
                new ViewModelProvider(getActivity()).get(ProfileViewModel.class);

        ProfileTabsAdapter profileTabsAdapter = new ProfileTabsAdapter(getContext(),
                getChildFragmentManager(),
                0);
        mViewPager.setAdapter(profileTabsAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mUserId = SharedPrefUtil.getInstance(getContext()).read(USER_ID, null);
        if (mUserId == null) {
//            viewModelLogin.getUser().observe(this, loginModelResponse -> {
//                mUserId = loginModelResponse.getUser().getId();
//                loadUser(mUserId);
//            });
        } else {
            loadUser(mUserId);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Navigation View not allowed
        sDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.START);
    }

    @Override
    public void onStop() {
        super.onStop();
        sDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED, GravityCompat.START);
    }

    public void back() {
        getActivity().onBackPressed();
    }

    public void accounting() {
        loadFragment(ProfileFragment.this.getActivity().getSupportFragmentManager(),
                new AccountingHolderFragment(), true);
    }

    private void setUserType(int type, TextView textView) {
        switch (type) {
            case 1:
                textView.setText(R.string.admin);
                break;
            case 2:
                textView.setText(R.string.supervisor);
                break;
            case 3:
                textView.setText(R.string.agent);
                break;
            case 4:
                textView.setText(R.string.real_estate_holder);
                break;
            case 5:
                textView.setText(R.string.individual_owner);
                break;
            case 6:
                textView.setText(R.string.user);
                break;
            default:
                textView.setText("");

        }
    }

    private void loadUser(String userId){
        mViewModelUserInfo.userInfo(userId);
//        mViewModelUserInfo.getUserInfo().observe(ProfileFragment.this, userInfoModelResponse -> {
//            if (userInfoModelResponse.getKey().equals(SUCCESS)) {
//                if (userInfoModelResponse.getUser().getStar() != null)
//                    mRatingBar.setRating(Float.parseFloat(userInfoModelResponse.getUser().getStar()));
//                Glide.with(getActivity()).load(BASE_URL + userInfoModelResponse.getUser().getPhoto()).into(mUserImage);
//                mUsername.setText(userInfoModelResponse.getUser().getName());
//            } else {
//                mToast = Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT);
//                mToast.show();
//            }
//        });
    }
}
