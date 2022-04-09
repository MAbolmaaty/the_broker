package com.emupapps.the_broker.ui;


import static com.emupapps.the_broker.ui.MainActivity.loadFragment;
import static com.emupapps.the_broker.ui.MainActivity.sDrawerLayout;
import static com.emupapps.the_broker.utils.Constants.FAILED;
import static com.emupapps.the_broker.utils.Constants.LOCALE;
import static com.emupapps.the_broker.utils.Constants.SUCCESS;
import static com.emupapps.the_broker.utils.Constants.USER_ID;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.emupapps.the_broker.utils.SoftKeyboard;
import com.emupapps.the_broker.viewmodels.LoginViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private static final String TAG = LoginFragment.class.getSimpleName();

    private LoginViewModel mViewModelLogin;

    Button mButtonLogin;
    EditText mEditTextEmail;
    TextInputEditText mEditTextPassword;
    TextView mSkip;
    CheckBox mRemember;
    ProgressBar mProgress;
    ProgressBar mProgressSkip;

    private String mFcmToken;
    private String mDeviceOS;
    private Toast mToast;
    private String mLocale;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mLocale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE, Locale.getDefault().getLanguage());
        retrieveToken();
        mViewModelLogin = ViewModelProviders.of(getActivity()).get(LoginViewModel.class);
        String userId = SharedPrefUtil.getInstance(getActivity()).read(USER_ID, null);
        if (userId != null) {
            mViewModelLogin.loggedIn(true);
        } else {
            mViewModelLogin.loggedIn(false);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        sDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.START);
    }

    public void login() {
        if (mToast != null)
            mToast.cancel();
        String email = mEditTextEmail.getText().toString();
        String password = mEditTextPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            mToast = Toast.makeText(getActivity(), R.string.enter_email, Toast.LENGTH_SHORT);
            mToast.show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            mToast = Toast.makeText(getActivity(), R.string.enter_password, Toast.LENGTH_SHORT);
            mToast.show();
            return;
        }

        SoftKeyboard.dismissKeyboardInActivity(getContext());

        mViewModelLogin.login(email, password, mLocale, mFcmToken, mDeviceOS);
        mViewModelLogin.getUser().observe(this, loginModelResponse -> {
            if (loginModelResponse.getKey().equals(SUCCESS)) {
                if (mRemember.isChecked()) {
                    //saveUser(loginModelResponse.getUser());
                }
                mViewModelLogin.loggedIn(true);
                mToast = Toast.makeText(getActivity(), loginModelResponse.getMessage(), Toast.LENGTH_SHORT);
                mToast.show();

            } else if (loginModelResponse.getKey().equals(FAILED)) {
                mToast = Toast.makeText(getActivity(), loginModelResponse.getMessage(), Toast.LENGTH_SHORT);
                mToast.show();
                mProgress.setVisibility(View.INVISIBLE);
            }
        });
        mViewModelLogin.isLoading().observe(this, loading -> {
            mProgress.setVisibility(loading ? View.VISIBLE : View.GONE);
        });
        mViewModelLogin.failure().observe(this, failure -> {
            if (failure) {
                if (mToast != null)
                    mToast.cancel();
                mToast = Toast.makeText(getActivity(), R.string.connection_to_server_lost, Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
    }

    public void skip() {
        mProgressSkip.setVisibility(View.VISIBLE);
        mSkip.setTextColor(getResources().getColor(R.color.grey));
        loadFragment(getActivity().getSupportFragmentManager(), new HomeFragment(), false);
    }

    private void retrieveToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        return;
                    }
                    mFcmToken = task.getResult().getToken();
                    Log.d(TAG, mFcmToken);
                    mDeviceOS = "android";
                });
    }
}
