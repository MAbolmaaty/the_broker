package com.emupapps.the_broker.ui;

import static com.emupapps.the_broker.utils.Constants.EMAIL_ADDRESS;
import static com.emupapps.the_broker.utils.Constants.JWT;
import static com.emupapps.the_broker.utils.Constants.LOCALE;
import static com.emupapps.the_broker.utils.Constants.PHONE_NUMBER;
import static com.emupapps.the_broker.utils.Constants.Profile_Picture;
import static com.emupapps.the_broker.utils.Constants.USERNAME;
import static com.emupapps.the_broker.utils.Constants.USER_ID;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.emupapps.the_broker.R;
import com.emupapps.the_broker.databinding.ActivityMainBinding;
import com.emupapps.the_broker.models.ProfileModelResponse;
import com.emupapps.the_broker.models.register.AuthenticationModelResponse;
import com.emupapps.the_broker.models.search.request.SearchModelRequest;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.emupapps.the_broker.viewmodels.AuthenticationViewModel;
import com.emupapps.the_broker.viewmodels.DistrictsViewModel;
import com.emupapps.the_broker.viewmodels.ProfileViewModel;
import com.emupapps.the_broker.viewmodels.RealEstateCategoriesViewModel;
import com.emupapps.the_broker.viewmodels.RealEstateStatusesViewModel;
import com.emupapps.the_broker.viewmodels.RealEstatesViewModel;
import com.emupapps.the_broker.viewmodels.RegionsViewModel;
import com.emupapps.the_broker.viewmodels.SearchViewModel;
import com.emupapps.the_broker.viewmodels.SlidesViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.navigation.NavigationView;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mBinding;
    private AuthenticationViewModel mAuthenticationViewModel;

    NavigationView mNavView;

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int ERROR_DIALOG_REQUEST = 9001;

    public static DrawerLayout sDrawerLayout;

    private SearchViewModel mViewModelSearch;

    @Override
    protected void attachBaseContext(Context newBase) {
        String language = SharedPrefUtil.getInstance(newBase).read(LOCALE, Locale.getDefault().getLanguage());
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = newBase.getResources().getConfiguration();
        config.setLocale(locale);
        config.setLayoutDirection(locale);
        newBase = newBase.createConfigurationContext(config);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set App Language
        String language = SharedPrefUtil.getInstance(this).read(LOCALE, Locale.getDefault().getLanguage());
        Locale locale = new Locale(language);
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        createConfigurationContext(configuration);

        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);
        mNavView = mBinding.navView;
        getSupportActionBar().hide();

//        viewModelLogin.isLoggedIn().observe(this, loggedIn -> {
//            mLoggedIn = loggedIn;
//            mNavView.getMenu().getItem(11).setVisible(loggedIn);
//            mNavView.getMenu().getItem(0).setChecked(true);
//            mNavView.getMenu().getItem(1).setChecked(true);
//            mNavView.getMenu().getItem(3).setVisible(true);
//            mNavView.getMenu().getItem(4).setVisible(false);
//            mNavView.getMenu().getItem(1).setVisible(false);
//            mNavView.getMenu().getItem(0).setVisible(true);
//            mNavView.getMenu().getItem(2).setVisible(true);
//            mNavView.getMenu().getItem(5).setVisible(true);
//            mNavView.getMenu().getItem(10).setVisible(true);
//            ((NavigationMenuView)mNavView.getChildAt(0)).scrollToPosition(0);
//            setNavHeader(loggedIn);
//            if (loggedIn){
//                String userId = SharedPrefUtil.getInstance(this).read(USER_ID, null);
//                String userType;
//                if (userId != null){
//                    userType = SharedPrefUtil.getInstance(this).read(USER_TYPE, "");
//                    if (userType.equals(AGENT)){
//                        mNavView.getMenu().getItem(4).setVisible(true);
//                        mNavView.getMenu().getItem(1).setVisible(true);
//                        mNavView.getMenu().getItem(2).setVisible(false);
//                        mNavView.getMenu().getItem(0).setVisible(false);
//                        mNavView.getMenu().getItem(3).setVisible(false);
//                        mNavView.getMenu().getItem(5).setVisible(false);
//                        mNavView.getMenu().getItem(10).setVisible(false);
//                        loadFragment(getSupportFragmentManager(), new RealEstatesAgentFragment(),
//                                false);
//                    } else {
//                        loadFragment(getSupportFragmentManager(),
//                                new HomeFragment(), false);
//                    }
//                } else {
//                    mViewModelLogin.getUser().observe(this, loginModelResponse -> {
//                        if (false){
//                            mNavView.getMenu().getItem(4).setVisible(true);
//                            mNavView.getMenu().getItem(3).setVisible(false);
//                            mNavView.getMenu().getItem(1).setVisible(true);
//                            mNavView.getMenu().getItem(2).setVisible(false);
//                            mNavView.getMenu().getItem(0).setVisible(false);
//                            mNavView.getMenu().getItem(5).setVisible(false);
//                            mNavView.getMenu().getItem(10).setVisible(false);
//                            loadFragment(getSupportFragmentManager(), new RealEstatesAgentFragment(),
//                                    false);
//                        }else {
//                            loadFragment(getSupportFragmentManager(),
//                                    new HomeFragment(), false);
//                        }
//                    });
//                }
//            }
//        });

        sDrawerLayout = findViewById(R.id.drawer_layout);

        mNavView.getChildAt(0).setVerticalScrollBarEnabled(false);

        mAuthenticationViewModel =
                new ViewModelProvider(MainActivity.this).get(AuthenticationViewModel.class);
        fetchUser();
        setNavHeader();
        mViewModelSearch = new ViewModelProvider(this).get(SearchViewModel.class);

        //Get RealEstates
        RealEstatesViewModel viewModelRealEstates =
                new ViewModelProvider(this).get(RealEstatesViewModel.class);
        viewModelRealEstates.realEstates("1", "1", "0");
        //Get Slides
        SlidesViewModel viewModelSlides =
                new ViewModelProvider(this).get(SlidesViewModel.class);
        viewModelSlides.slides();
        //Get Real Estate Statuses, Ex : sale, rent
        RealEstateStatusesViewModel viewModelRealEstateStatuses =
                new ViewModelProvider(this).get(RealEstateStatusesViewModel.class);
        //Get Real Estate Categories, Ex : apartment, office
        RealEstateCategoriesViewModel viewModelRealEstateCategories =
                new ViewModelProvider(this).get(RealEstateCategoriesViewModel.class);
        //Get Regions
        RegionsViewModel viewModelRegions =
                new ViewModelProvider(this).get(RegionsViewModel.class);
        //Get Districts
        DistrictsViewModel viewModelDistricts =
                new ViewModelProvider(this).get(DistrictsViewModel.class);

        home();

        setNavigationItemSelected();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mNavView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleSearchIntent(intent);
    }

    private void setNavigationItemSelected() {
        mNavView.setNavigationItemSelectedListener(menuItem -> {
            sDrawerLayout.closeDrawer(GravityCompat.START, false);
            int itemId = menuItem.getItemId();
            switch (itemId) {
                case R.id.nav_home:
                    loadFragment(getSupportFragmentManager(), new HomeFragment(), false);
                    return true;
                case R.id.nav_realEstates:
                    if (mAuthenticationViewModel.getResult().getValue() != null){
                        loadFragment(getSupportFragmentManager(), new RealEstatesFragment(),
                                false);
                    } else {
                        loadFragment(getSupportFragmentManager(), new LoginFragment(),
                                false);
                    }
                case R.id.nav_requests:
                    if (mAuthenticationViewModel.getResult().getValue() != null){
                        loadFragment(getSupportFragmentManager(), new RequestsFragment(),
                                false);
                    } else {
                        loadFragment(getSupportFragmentManager(), new LoginFragment(),
                                false);
                    }
                case R.id.nav_messages:
                    if (mAuthenticationViewModel.getResult().getValue() != null){
                        loadFragment(getSupportFragmentManager(), new MessagesFragment(),
                                false);
                    } else {
                        loadFragment(getSupportFragmentManager(), new LoginFragment(),
                                false);
                    }
                case R.id.nav_notifications:
                    if (mAuthenticationViewModel.getResult().getValue() != null){
                        loadFragment(getSupportFragmentManager(), new NotificationsFragment(),
                                false);
                    } else {
                        loadFragment(getSupportFragmentManager(), new LoginFragment(),
                                false);
                    }
                case R.id.nav_favorites:
                    if (mAuthenticationViewModel.getResult().getValue() != null){
                        loadFragment(getSupportFragmentManager(), new FavoritesFragment(),
                                false);
                    } else {
                        loadFragment(getSupportFragmentManager(), new LoginFragment(),
                                false);
                    }
                case R.id.nav_termsAndConditions:
                    loadFragment(getSupportFragmentManager(), new TermsAndConditionsFragment(),
                            false);
                    return true;
                case R.id.nav_contactUs:
                    loadFragment(getSupportFragmentManager(), new ContactUsFragment(), false);
                    return true;
                case R.id.nav_settings:
                    loadFragment(getSupportFragmentManager(), new SettingsFragment(), false);
                    return true;
            }
            return false;
        });
    }

    private void handleSearchIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String address = intent.getStringExtra(SearchManager.QUERY);
            mViewModelSearch.setParameters(new SearchModelRequest(null, null, null, null,
                    address, null, null, null, null, null,
                    0, 0));
            loadFragment(getSupportFragmentManager(), new SearchFragment(), false);
        }
    }

    //Splash
    private void viewSplash() {
        loadFragment(getSupportFragmentManager(), new SplashFragment(), false);
    }

    private void home() {
        loadFragment(getSupportFragmentManager(),
                new HomeFragment(), false);
        String userId = SharedPrefUtil.getInstance(this).read(USER_ID, null);
//        if (userId != null) {
//            viewModelLogin.loggedIn(true);
//        }else {
//            viewModelLogin.loggedIn(false);
//        }
    }

    public boolean isGooglePlayServicesAvailable() {

        int isAvailable =
                GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);

        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(isAvailable)) {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, isAvailable, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, getString(R.string.google_play_services_not_available), Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    private void logout() {
        //mViewModelLogin.loggedIn(false);
//        SharedPrefUtil.getInstance(this).write(REMEMBER_ME, false);
//        SharedPrefUtil.getInstance(this).write(USER_ID, null);
//        SharedPrefUtil.getInstance(this).write(USER_IMAGE, null);
//        SharedPrefUtil.getInstance(this).write(USERNAME, null);
//        SharedPrefUtil.getInstance(this).write(USER_TYPE, null);
//        SharedPrefUtil.getInstance(this).write(BIRTH_DATE, null);
//        SharedPrefUtil.getInstance(this).write(ADDRESS, null);
//        SharedPrefUtil.getInstance(this).write(EMAIL, null);
//        SharedPrefUtil.getInstance(this).write(PHONE_NUMBER, null);
//        SharedPrefUtil.getInstance(this).write(PHONE_CODE, null);
    }

    private void setNavHeader() {
        View header = mNavView.getHeaderView(0);
        ImageView closeMenu = header.findViewById(R.id.close);
        Button login = header.findViewById(R.id.login);
        Button register = header.findViewById(R.id.register);
        CircleImageView userImage = header.findViewById(R.id.userImage);
        TextView username = header.findViewById(R.id.userName);
        TextView userEmail = header.findViewById(R.id.userEmail);
        closeMenu.setOnClickListener(v ->
                sDrawerLayout.closeDrawer(GravityCompat.START, false));

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(MainActivity.this.getSupportFragmentManager(), new LoginFragment(),
                        true);
                sDrawerLayout.closeDrawer(GravityCompat.START, false);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(MainActivity.this.getSupportFragmentManager(),
                        new RegisterFragment(),
                        true);
                sDrawerLayout.closeDrawer(GravityCompat.START, false);
            }
        });

        mAuthenticationViewModel.getResult().observe(this,
                new Observer<AuthenticationModelResponse>() {
                    @Override
                    public void onChanged(AuthenticationModelResponse authenticationModelResponse) {
                        if (authenticationModelResponse != null) {
                            login.setVisibility(View.INVISIBLE);
                            register.setVisibility(View.INVISIBLE);
                            userImage.setVisibility(View.VISIBLE);
                            username.setVisibility(View.VISIBLE);
                            userEmail.setVisibility(View.VISIBLE);

                            ProfileViewModel profileViewModel =
                                    new ViewModelProvider(MainActivity.this).
                                            get(ProfileViewModel.class);

                            profileViewModel.userInfo(authenticationModelResponse.getJwt());
                            profileViewModel.getResult().observe(MainActivity.this,
                                    new Observer<ProfileModelResponse>() {
                                @Override
                                public void onChanged(ProfileModelResponse profileModelResponse) {
                                    username.setText(profileModelResponse.getUsername());
                                    userEmail.setText(profileModelResponse.getEmail());
                                    Glide.with(MainActivity.this)
                                    .load(profileModelResponse.getProfilePicture().getUrl())
                                    .into(userImage);
                                    saveUser(authenticationModelResponse.getJwt(),
                                            profileModelResponse.getId(),
                                            profileModelResponse.getUsername(),
                                            profileModelResponse.getEmail(),
                                            profileModelResponse.getPhoneNumber(),
                                            profileModelResponse.getProfilePicture().getUrl());
                                }
                            });

                        }
                    }
                });

            userImage.setOnClickListener(v -> {
                loadFragment(getSupportFragmentManager(), new ProfileFragment(), true);
                sDrawerLayout.closeDrawer(GravityCompat.START, false);
            });

            username.setOnClickListener(v -> {
                loadFragment(getSupportFragmentManager(), new ProfileFragment(), true);
                sDrawerLayout.closeDrawer(GravityCompat.START, false);
            });
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

    //Attaching Fragment
    public static void loadFragment(FragmentManager fragmentManager,
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

    public static void progress(boolean loading, ProgressBar progressBar, View view) {
        if (loading) {
            progressBar.setVisibility(View.VISIBLE);
            view.setEnabled(false);

        } else {
            progressBar.setVisibility(View.GONE);
            view.setEnabled(true);

        }
    }

    private void saveUser(String jwt, String id, String username, String emailAddress,
                          String phoneNumber, String profilePicture) {
        SharedPreferences sharedPreferences = getSharedPreferences(
                getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(JWT, jwt);
        editor.putString(USER_ID, id);
        editor.putString(USERNAME, username);
        editor.putString(EMAIL_ADDRESS, emailAddress);
        editor.putString(PHONE_NUMBER, phoneNumber);
        editor.putString(Profile_Picture, profilePicture);

        editor.apply();
    }

    private void fetchUser(){
        SharedPreferences sharedPreferences = getSharedPreferences(
                getPackageName(), Context.MODE_PRIVATE);
        String jwt = sharedPreferences.getString(JWT, null);
        if(jwt != null){
            mAuthenticationViewModel.setResult(jwt);
        }
    }
}
