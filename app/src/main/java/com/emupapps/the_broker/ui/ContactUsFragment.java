package com.emupapps.the_broker.ui;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.databinding.FragmentContactUsBinding;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.emupapps.the_broker.utils.SoftKeyboard;
import com.emupapps.the_broker.viewmodels.ContactUsViewModel;
import com.emupapps.the_broker.viewmodels.ProfileViewModel;
import com.hbb20.CountryCodePicker;

import java.util.Locale;

import static com.emupapps.the_broker.ui.MainActivity.sDrawerLayout;
import static com.emupapps.the_broker.utils.Constants.LOCALE;
import static com.emupapps.the_broker.utils.Constants.SUCCESS;
import static com.emupapps.the_broker.utils.Constants.USER_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends Fragment {

    private static final String TAG = ContactUsFragment.class.getSimpleName();
    private FragmentContactUsBinding mBinding;

    private ContactUsViewModel mViewModelContactUs;
    private Toast mToast;
    private String mLocale;
    private ProfileViewModel mViewModelInfoUser;

    public ContactUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentContactUsBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        mViewModelContactUs = new ViewModelProvider(this).get(ContactUsViewModel.class);
        mViewModelInfoUser = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);
        String userId = SharedPrefUtil.getInstance(getContext()).read(USER_ID, null);

        mLocale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE, Locale.getDefault().
                getLanguage());

        mBinding.countryCodePicker.setCountryForPhoneCode(+966);
        mBinding.contactUsOpenMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenu();
            }
        });
        return view;
    }

    private void openMenu(){
        sDrawerLayout.openDrawer(GravityCompat.START);
        SoftKeyboard.dismissKeyboardInActivity(getActivity());

    }

//    public void contact() {
//        String username = mUsername.getText().toString();
//        String phoneNumber = mPhoneNumber.getText().toString();
//        String email = mEmail.getText().toString();
//        String messageTitle = mMessageTitle.getText().toString();
//        String messageText = mMessage.getText().toString();
//
//        if (mToast != null)
//            mToast.cancel();
//
//        if (TextUtils.isEmpty(username)) {
//            mToast = Toast.makeText(getContext(), getString(R.string.enter_username), Toast.LENGTH_SHORT);
//            mToast.show();
//            return;
//        }
//
//        if (TextUtils.isEmpty(phoneNumber)) {
//            mToast = Toast.makeText(getContext(), getString(R.string.enter_phone_number), Toast.LENGTH_SHORT);
//            mToast.show();
//            return;
//        }
//
//        if (TextUtils.isEmpty(email)) {
//            mToast = Toast.makeText(getContext(), getString(R.string.enter_email), Toast.LENGTH_SHORT);
//            mToast.show();
//            return;
//        }
//
//        if (TextUtils.isEmpty(messageTitle)) {
//            mToast = Toast.makeText(getContext(), getString(R.string.enter_title), Toast.LENGTH_SHORT);
//            mToast.show();
//            return;
//        }
//
//        if (TextUtils.isEmpty(messageText)) {
//            mToast = Toast.makeText(getContext(), getString(R.string.enter_message), Toast.LENGTH_SHORT);
//            mToast.show();
//            return;
//        }
//
//        String countryCode = "+" + mCountryCodePicker.getSelectedCountryCode();
//
//        SoftKeyboard.dismissKeyboardInActivity(getContext());
//
//        mViewModelContactUs.contactUs(username, email, countryCode, phoneNumber, messageText, messageTitle, mLocale);
//        mViewModelContactUs.getResult().observe(this, contactUsModelResponse -> {
//            if (contactUsModelResponse.getKey().equals(SUCCESS)) {
//                if (mToast != null)
//                    mToast.cancel();
//                mToast = Toast.makeText(getContext(), contactUsModelResponse.getResult(), Toast.LENGTH_SHORT);
//                mToast.show();
//                mUsername.setText("");
//                mPhoneNumber.setText("");
//                mEmail.setText("");
//                mMessageTitle.setText("");
//                mMessage.setText("");
//            } else {
//                mToast = Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT);
//                mToast.show();
//
//            }
//        });
//        mViewModelContactUs.isLoading().observe(this, loading -> progress(loading));
//        mViewModelContactUs.failure().observe(this, failure -> {
//            if (failure) {
//                if (mToast != null)
//                    mToast.cancel();
//                mToast = Toast.makeText(getActivity(), R.string.connection_to_server_lost, Toast.LENGTH_SHORT);
//                mToast.show();
//            }
//        });
//    }

    private void loadUserInfo(String userId) {
//        mViewModelInfoUser.userInfo(userId);
//        mViewModelInfoUser.getUserInfo().observe(ContactUsFragment.this, userInfoModelResponse -> {
//            if (userInfoModelResponse.getKey().equals(SUCCESS)) {
//                mUsername.setText(userInfoModelResponse.getUser().getName());
//                if (userInfoModelResponse.getUser().getCode() != null)
//                    mCountryCodePicker.setCountryForPhoneCode(Integer.parseInt(userInfoModelResponse.getUser().getCode()));
//                mPhoneNumber.setText(userInfoModelResponse.getUser().getPhone());
//                mEmail.setText(userInfoModelResponse.getUser().getEmail());
//            }
//        });
//        mViewModelInfoUser.isLoading().observe(ContactUsFragment.this, loading -> {
//            if (loading) {
//                mProgressBar.setVisibility(View.VISIBLE);
//            } else {
//                mProgressBar.setVisibility(View.INVISIBLE);
//            }
//        });
    }
}
