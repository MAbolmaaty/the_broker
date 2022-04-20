package com.emupapps.the_broker.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.adapters.SettingItemsAdapter;
import com.emupapps.the_broker.databinding.FragmentSettingsBinding;
import com.emupapps.the_broker.models.SettingItem;
import com.emupapps.the_broker.models.register.AuthenticationModelResponse;
import com.emupapps.the_broker.utils.SoftKeyboard;
import com.emupapps.the_broker.viewmodels.AuthenticationViewModel;

import static com.emupapps.the_broker.ui.MainActivity.sDrawerLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding mBinding;
    private static List<SettingItem> mSettingsList = new ArrayList<>();
    private AuthenticationViewModel mAuthenticationViewModel;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSettingsBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        getSettings(false);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        SettingItemsAdapter adapter = new SettingItemsAdapter(mSettingsList,
                new SettingItemsAdapter.SettingItemClickHandler() {
            @Override
            public void onClick(int position) {
                switch (position){
                    case 0 :
                        loadFragment(getActivity().getSupportFragmentManager(),
                                new AccountFragment(), true);
                        break;
                }
            }
        });

        mBinding.settingsList.setLayoutManager(layoutManager);
        mBinding.settingsList.setAdapter(adapter);
        mBinding.settingsList.setHasFixedSize(true);

        mAuthenticationViewModel =
                new ViewModelProvider(requireActivity()).get(AuthenticationViewModel.class);
        mAuthenticationViewModel.getResult().observe(this,
                new Observer<AuthenticationModelResponse>() {
            @Override
            public void onChanged(AuthenticationModelResponse authenticationModelResponse) {
                getSettings(authenticationModelResponse != null);
            }
        });

        mBinding.settingsOpenMenu.setOnClickListener(new View.OnClickListener() {
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

    private void getSettings(boolean authenticated){
        mSettingsList.clear();
        if (authenticated){
            mSettingsList.add(new SettingItem(getContext().getDrawable(R.drawable.ic_profile_2),
                    "Account", "Manage your account."));
        }
        mSettingsList.add(new SettingItem(getContext().getDrawable(R.drawable.ic_phone_2),
                "Display", "Manage app theme."));
        mSettingsList.add(new SettingItem(getContext().getDrawable(R.drawable.ic_earth),
                "Language", "Manage app language."));
    }

    private void loadFragment(FragmentManager fragmentManager,
                              Fragment fragment,
                              boolean addToBackStack) {
        if (addToBackStack) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
    }
}
