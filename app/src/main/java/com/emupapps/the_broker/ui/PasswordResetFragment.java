package com.emupapps.the_broker.ui;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.emupapps.the_broker.utils.SoftKeyboard;
import com.emupapps.the_broker.viewmodels.PasswordForgetViewModel;
import com.emupapps.the_broker.viewmodels.PasswordResetViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

import static com.emupapps.the_broker.ui.MainActivity.loadFragment;
import static com.emupapps.the_broker.utils.Constants.LOCALE;
import static com.emupapps.the_broker.utils.Constants.SUCCESS;

/**
 * A simple {@link Fragment} subclass.
 */
public class PasswordResetFragment extends Fragment {

    TextInputEditText mNewPassword;
    TextInputEditText mConfirmPassword;
    Button mConfirm;
    ProgressBar mProgress;

    private PasswordForgetViewModel mViewModelForgetPassword;
    private PasswordResetViewModel mViewModelResetPassword;
    private Toast mToast;
    private String mLocale;

    public PasswordResetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        mLocale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE, Locale.getDefault().getLanguage());

        mViewModelResetPassword = ViewModelProviders.of(this).get(PasswordResetViewModel.class);
        mViewModelForgetPassword = ViewModelProviders.of(getActivity()).get(PasswordForgetViewModel.class);

        return view;
    }

    public void resetPassword() {

        String newPassword = mNewPassword.getText().toString();
        String confirmPassword = mConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(newPassword)) {
            if (mToast != null)
                mToast.cancel();
            mToast = Toast.makeText(getContext(), getString(R.string.enter_new_password), Toast.LENGTH_SHORT);
            mToast.show();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            if (mToast != null)
                mToast.cancel();
            mToast = Toast.makeText(getContext(), getString(R.string.enter_confirm_password), Toast.LENGTH_SHORT);
            mToast.show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            if (mToast != null)
                mToast.cancel();
            mToast = Toast.makeText(getContext(), getString(R.string.password_not_match), Toast.LENGTH_SHORT);
            mToast.show();
            return;
        }

        SoftKeyboard.dismissKeyboardInActivity(getContext());
        mViewModelForgetPassword.getResult().observe(this, forgetPasswordModelResponse -> {
            mViewModelResetPassword.resetPassword(forgetPasswordModelResponse.getEmail(), newPassword, confirmPassword, mLocale);
            mViewModelResetPassword.getResult().observe(PasswordResetFragment.this, resetPasswordModelResponse -> {
                if (resetPasswordModelResponse.getKey().equals(SUCCESS)) {
                    if (mToast != null)
                        mToast.cancel();
                    mToast = Toast.makeText(getContext(), resetPasswordModelResponse.getResult(), Toast.LENGTH_SHORT);
                    mToast.show();
                    loadFragment(getActivity().getSupportFragmentManager(), new LoginFragment(), false);
                } else {
                    mToast = Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT);
                    mToast.show();

                }
            });

            mViewModelResetPassword.isLoading().observe(PasswordResetFragment.this, loading -> {
                if (loading){
                    mProgress.setVisibility(View.VISIBLE);
                } else {
                    mProgress.setVisibility(View.INVISIBLE);
                }
            });
            mViewModelResetPassword.failure().observe(this, failure -> {
                if (failure){
                    if (mToast != null)
                        mToast.cancel();
                    mToast = Toast.makeText(getActivity(), R.string.connection_to_server_lost, Toast.LENGTH_SHORT);
                    mToast.show();
                }
            });
        });
    }

    public void close(){
        getActivity().onBackPressed();
    }
}
