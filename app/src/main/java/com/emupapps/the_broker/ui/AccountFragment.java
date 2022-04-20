package com.emupapps.the_broker.ui;

import static com.emupapps.the_broker.ui.MainActivity.sDrawerLayout;
import static com.emupapps.the_broker.utils.Constants.EMAIL_ADDRESS;
import static com.emupapps.the_broker.utils.Constants.JWT;
import static com.emupapps.the_broker.utils.Constants.PHONE_NUMBER;
import static com.emupapps.the_broker.utils.Constants.Profile_Picture;
import static com.emupapps.the_broker.utils.Constants.USERNAME;
import static com.emupapps.the_broker.utils.Constants.USER_ID;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.databinding.FragmentAccountBinding;
import com.emupapps.the_broker.models.ProfilePicture;
import com.emupapps.the_broker.models.login.response.User;
import com.emupapps.the_broker.models.register.AuthenticationModelResponse;
import com.emupapps.the_broker.viewmodels.AuthenticationViewModel;
import com.emupapps.the_broker.viewmodels.ProfileViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    private FragmentAccountBinding mBinding;
    private ProfileViewModel mProfileViewModel;
    private AuthenticationViewModel mAuthenticationViewModel;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAccountBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        mProfileViewModel =
                new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        mAuthenticationViewModel =
                new ViewModelProvider(requireActivity()).get(AuthenticationViewModel.class);

        mBinding.accountUsername.setText(mProfileViewModel.getResult().getValue().getUsername());
        mBinding.accountEmail.setText(mProfileViewModel.getResult().getValue().getEmail());
        mBinding.accountBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        mBinding.accountLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
                mAuthenticationViewModel.logout();
                getActivity().onBackPressed();
            }
        });
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        sDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    private void deleteUser() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                getActivity().getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(JWT, null);
        editor.putString(USER_ID, null);
        editor.putString(USERNAME, null);
        editor.putString(EMAIL_ADDRESS, null);
        editor.putString(PHONE_NUMBER, null);
        editor.putString(Profile_Picture, null);

        editor.apply();
    }
}