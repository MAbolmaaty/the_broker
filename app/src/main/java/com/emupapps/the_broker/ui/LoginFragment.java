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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.databinding.FragmentLoginBinding;
import com.emupapps.the_broker.models.login.response.User;
import com.emupapps.the_broker.models.register.AuthenticationModelResponse;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.emupapps.the_broker.utils.SoftKeyboard;
import com.emupapps.the_broker.viewmodels.AuthenticationViewModel;
import com.emupapps.the_broker.viewmodels.LoginViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Locale;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private static final String TAG = LoginFragment.class.getSimpleName();
    private FragmentLoginBinding mBinding;
    private AuthenticationViewModel mAuthenticationViewModel;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentLoginBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        mAuthenticationViewModel = new ViewModelProvider(requireActivity()).
                get(AuthenticationViewModel.class);
        mBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        mBinding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        sDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.START);
    }

    public void login() {
        String identifier = mBinding.identifier.getText().toString();
        String password = mBinding.password.getText().toString();

        if (hasEmptyFields(identifier, password)) {
            return;
        }

        mBinding.progress.setVisibility(View.VISIBLE);

        mAuthenticationViewModel.login(identifier, password);
        mAuthenticationViewModel.getResult().observe(this, new Observer<AuthenticationModelResponse>() {
            @Override
            public void onChanged(AuthenticationModelResponse authenticationModelResponse) {
                mBinding.progress.setVisibility(View.INVISIBLE);
                if (authenticationModelResponse != null) {
                    getActivity().onBackPressed();
                } else {
                    Toast toast = Toast.makeText(getContext(),
                            getString(R.string.something_went_wrong),
                            Toast.LENGTH_SHORT
                    );
                    toast.show();
                }
            }
        });
        SoftKeyboard.dismissKeyboardInActivity(getContext());

    }

    private boolean hasEmptyFields(String identifier, String password) {
        Toast toast = null;

        if (TextUtils.isEmpty(identifier)) {
            toast = Toast.makeText(getContext(), getString(R.string.enter_email_or_username),
                    Toast.LENGTH_SHORT);
        } else if (TextUtils.isEmpty(password)) {
            toast = Toast.makeText(getContext(), getString(R.string.enter_password),
                    Toast.LENGTH_SHORT);
        }

        if (toast != null) {
            toast.show();
            return true;
        }

        return false;
    }
}
