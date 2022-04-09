package com.emupapps.the_broker.ui;


import static com.emupapps.the_broker.ui.MainActivity.sDrawerLayout;
import static com.emupapps.the_broker.utils.Constants.AUCTION;
import static com.emupapps.the_broker.utils.Constants.LOCALE;
import static com.emupapps.the_broker.utils.Constants.RENT;
import static com.emupapps.the_broker.utils.Constants.SALE;
import static com.emupapps.the_broker.utils.Constants.SUCCESS;
import static com.emupapps.the_broker.utils.Constants.USER_ID;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.databinding.FragmentHomeBinding;
import com.emupapps.the_broker.models.real_estate_categories.Category;
import com.emupapps.the_broker.models.real_estate_statuses.Status;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.emupapps.the_broker.utils.SoftKeyboard;
import com.emupapps.the_broker.viewmodels.DistrictsViewModel;
import com.emupapps.the_broker.viewmodels.RealEstateCategoriesViewModel;
import com.emupapps.the_broker.viewmodels.RealEstateStatusesViewModel;
import com.emupapps.the_broker.viewmodels.RealEstateViewModel;
import com.emupapps.the_broker.viewmodels.RealEstatesViewModel;
import com.emupapps.the_broker.viewmodels.RegionsViewModel;
import com.emupapps.the_broker.viewmodels.RequestsUserViewModel;
import com.emupapps.the_broker.viewmodels.SearchViewModel;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements OnMapReadyCallback {
    private FragmentHomeBinding binding;

    ImageView mMenu;

    private static final String TAG = HomeFragment.class.getSimpleName();

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;
    private RealEstatesViewModel mViewModelRealEstates;
    private RealEstateViewModel mViewModelRealEstate;
    private SearchViewModel mViewModelSearch;
    private final static int REQUEST_CHECk_SETTINGS = 7007;
    private final static int ACCESS_LOCATION_REQUEST_PERMISSION = 7017;
    private static List<Marker> mListSaleMarkers = new ArrayList<>();
    private static List<Marker> mListRentMarkers = new ArrayList<>();
    private static List<Marker> mListAuctionMarkers = new ArrayList<>();
    private BottomSheetDialog mDialogFilter;
    private Toast mToast;
    private String mLocale;
    private String mUserId;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        mUserId = SharedPrefUtil.getInstance(getContext()).read(USER_ID, null);
        if (mUserId != null) {
            RequestsUserViewModel viewModelUserRequests = ViewModelProviders.of(getActivity()).get(RequestsUserViewModel.class);
            viewModelUserRequests.userRequests(mUserId);
        }
        mLocale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE, Locale.getDefault().getLanguage());
        mViewModelRealEstates = ViewModelProviders.of(getActivity()).get(RealEstatesViewModel.class);
        mViewModelSearch = ViewModelProviders.of(getActivity()).get(SearchViewModel.class);
        mViewModelRealEstates.realEstates("1", "1", "0");
        mViewModelRealEstates.startMapFragment(true);

        //Real Estate Details
        mViewModelRealEstate = ViewModelProviders.of(getActivity()).get(RealEstateViewModel.class);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        getRealEstates();

        binding.imageViewSale.setTag(1);
        binding.imageViewRent.setTag(1);
        binding.imageViewAuctions.setTag(0);

        mDialogFilter = new BottomSheetDialog(getContext());
        mDialogFilter.setContentView(R.layout.dialog_filter);

        //SearchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        binding.searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        binding.searchView.setIconifiedByDefault(false);
        binding.searchView.setSubmitButtonEnabled(true);
        binding.searchView.setFocusable(false);
        binding.searchView.findViewById(androidx.appcompat.R.id.search_plate).setBackgroundColor(Color.TRANSPARENT);
        ((LinearLayout) binding.searchView.findViewById(R.id.search_voice_btn).getParent()).setBackgroundColor(Color.TRANSPARENT);

        if (mLocale.equals("ar")) {
            binding.searchView.findViewById(androidx.appcompat.R.id.search_plate).setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            binding.searchView.findViewById(androidx.appcompat.R.id.search_close_btn).setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            binding.searchView.findViewById(androidx.appcompat.R.id.search_go_btn).setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            binding.searchView.findViewById(androidx.appcompat.R.id.search_go_btn).setRotation(180);
            binding.searchView.findViewById(androidx.appcompat.R.id.search_voice_btn).setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            binding.searchView.findViewById(androidx.appcompat.R.id.search_mag_icon).setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        sDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED, GravityCompat.START);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ACCESS_LOCATION_REQUEST_PERMISSION && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED) {
            createLocationRequest();
        } else {
            Toast.makeText(getContext(), getString(R.string.location_access_denied), Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setPadding(0, (binding.menu.getLayoutParams().height * 2), 0, 0);
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.maps_style_json));
        mMap.setOnMarkerClickListener(marker -> {
            mViewModelRealEstate.setRealEstateId(marker.getTag().toString());
            HomeFragment.this.getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new RealEstateFragment())
                    .addToBackStack(null)
                    .commit();
            return true;
        });
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            createLocationRequest();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_PERMISSION);
        }
    }

    public void menu() {
        sDrawerLayout.openDrawer(GravityCompat.START);
        SoftKeyboard.dismissKeyboardInActivity(getActivity());
    }

    public void openFilter() {
        mDialogFilter.show();
    }

    public void controlSaleRealEstates() {
        if (binding.imageViewSale.getTag().equals(1)) {
            binding.imageViewSale.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.white));
            binding.sale.setTextColor(getResources().getColor(R.color.darkGrey));
            saleMarkersVisibility(false);
            binding.imageViewSale.setTag(0);
        } else {
            binding.imageViewSale.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.darkGrey));
            binding.sale.setTextColor(getResources().getColor(R.color.white));
            binding.imageViewAuctions.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.white));
            binding.auctions.setTextColor(getResources().getColor(R.color.darkGrey));
            saleMarkersVisibility(true);
            binding.imageViewSale.setTag(1);
            binding.imageViewAuctions.setTag(0);
            auctionMarkersVisibility(false);
        }
    }

    public void controlRentRealEstates() {
        if (binding.imageViewRent.getTag().equals(1)) {
            binding.imageViewRent.setBackgroundTintList(ContextCompat.getColorStateList(getContext(),
                    R.color.white));
            binding.rent.setTextColor(getResources().getColor(R.color.darkGrey));
            rentMarkersVisibility(false);
            binding.imageViewRent.setTag(0);
        } else {
            binding.imageViewRent.setBackgroundTintList(ContextCompat.getColorStateList(getContext(),
                    R.color.darkGrey));
            binding.rent.setTextColor(getResources().getColor(R.color.white));
            rentMarkersVisibility(true);
            binding.imageViewAuctions.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.white));
            binding.auctions.setTextColor(getResources().getColor(R.color.darkGrey));
            binding.imageViewRent.setTag(1);
            binding.imageViewAuctions.setTag(0);
            auctionMarkersVisibility(false);
        }
    }

    public void controlAuctionRealEstates() {
        if (binding.imageViewAuctions.getTag().equals(1)) {
            binding.imageViewAuctions.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.white));
            binding.auctions.setTextColor(getResources().getColor(R.color.darkGrey));
            binding.imageViewAuctions.setTag(0);
            auctionMarkersVisibility(false);
        } else {
            binding.imageViewAuctions.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.darkGrey));
            binding.auctions.setTextColor(getResources().getColor(R.color.white));
            binding.imageViewAuctions.setTag(1);
            auctionMarkersVisibility(true);
            binding.imageViewRent.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.white));
            binding.rent.setTextColor(getResources().getColor(R.color.darkGrey));
            rentMarkersVisibility(false);
            binding.imageViewRent.setTag(0);
            binding.imageViewSale.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.white));
            binding.sale.setTextColor(getResources().getColor(R.color.darkGrey));
            saleMarkersVisibility(false);
            binding.imageViewSale.setTag(0);
        }
    }

    private void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(getContext());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(getActivity(), locationSettingsResponse -> {
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 4));
                        mMap.getUiSettings().setMyLocationButtonEnabled(true);
                        mMap.getUiSettings().setCompassEnabled(true);
                        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
                    }

                }

            };

            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.getMainLooper());
        });

        task.addOnFailureListener(getActivity(), e -> {
            if (e instanceof ResolvableApiException) {
                try {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(getActivity(), REQUEST_CHECk_SETTINGS);
                } catch (IntentSender.SendIntentException sendEx) {

                }
            }
        });
    }

    private void getRealEstates() {
        //Customized Auction Marker
        BitmapDrawable drawableAuction = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_marker_auction);
        Bitmap bitmapAuction = drawableAuction.getBitmap();
        Bitmap iconAuction = Bitmap.createScaledBitmap(bitmapAuction,
                (int) (drawableAuction.getIntrinsicWidth() / 1.2),
                (int) (drawableAuction.getIntrinsicHeight() / 1.2), false);
        //Customized Sale Marker
        BitmapDrawable drawableSale = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_marker_sale);
        Bitmap bitmapSale = drawableSale.getBitmap();
        Bitmap iconSale = Bitmap.createScaledBitmap(bitmapSale,
                (int) (drawableSale.getIntrinsicWidth() / 1.2),
                (int) (drawableSale.getIntrinsicHeight() / 1.2), false);
        //Customized Rent Marker
        BitmapDrawable drawableRent = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_marker_rent);
        Bitmap bitmapRent = drawableRent.getBitmap();
        Bitmap iconRent = Bitmap.createScaledBitmap(bitmapRent,
                (int) (drawableRent.getIntrinsicWidth() / 1.2),
                (int) (drawableRent.getIntrinsicHeight() / 1.2), false);

        mViewModelRealEstates.getRealEstates().observe(this,
                new Observer<List<com.emupapps.the_broker.models.RealEstate>>() {
                    @Override
                    public void onChanged(List<com.emupapps.the_broker.models.RealEstate> realEstates) {
                        if (realEstates.size() > 0) {
                            Marker marker;
                            for (int i = 0; i < realEstates.size(); i++) {
                                LatLng latLng = new LatLng(Double.parseDouble(realEstates.get(i).getLatitude()),
                                        Double.parseDouble(realEstates.get(i).getLongitude()));
                                if (realEstates.get(i).getStatus() == AUCTION) {
                                    marker = mMap.addMarker(new MarkerOptions().position(latLng)
                                            .icon(BitmapDescriptorFactory.fromBitmap(iconAuction)));
                                    marker.setTag(realEstates.get(i).getId());
                                    marker.setVisible(false);
                                    mListAuctionMarkers.add(marker);
                                } else if (realEstates.get(i).getStatus() == SALE) {
                                    marker = mMap.addMarker(new MarkerOptions().position(latLng)
                                            .icon(BitmapDescriptorFactory.fromBitmap(iconSale)));
                                    marker.setTag(realEstates.get(i).getId());
                                    mListSaleMarkers.add(marker);
                                } else if (realEstates.get(i).getStatus() == RENT) {
                                    marker = mMap.addMarker(new MarkerOptions().position(latLng)
                                            .icon(BitmapDescriptorFactory.fromBitmap(iconRent)));
                                    marker.setTag(realEstates.get(i).getId());
                                    mListRentMarkers.add(marker);
                                }
                            }
                        } else {
                            mToast = Toast.makeText(HomeFragment.this.getActivity(),
                                    R.string.something_went_wrong, Toast.LENGTH_SHORT);
                            mToast.show();

                        }
                    }
                });
        mViewModelRealEstates.failure().observe(this, failure -> {
            if (failure) {
                if (mToast != null)
                    mToast.cancel();
                mToast = Toast.makeText(getActivity(), R.string.connection_to_server_lost, Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
    }

    private void saleMarkersVisibility(boolean visibility) {
        if (visibility && mListSaleMarkers.size() > 0) {
            for (int i = 0; i < mListSaleMarkers.size(); i++) {
                mListSaleMarkers.get(i).setVisible(true);
            }
        } else if (!visibility && mListSaleMarkers.size() > 0) {
            for (int i = 0; i < mListSaleMarkers.size(); i++) {
                mListSaleMarkers.get(i).setVisible(false);
            }
        }
    }

    private void rentMarkersVisibility(boolean visibility) {
        if (visibility && mListRentMarkers.size() > 0) {
            for (int i = 0; i < mListRentMarkers.size(); i++) {
                mListRentMarkers.get(i).setVisible(true);
            }
        } else if (!visibility && mListRentMarkers.size() > 0) {
            for (int i = 0; i < mListRentMarkers.size(); i++) {
                mListRentMarkers.get(i).setVisible(false);
            }
        }
    }

    private void auctionMarkersVisibility(boolean visibility) {
        if (visibility && mListAuctionMarkers.size() > 0) {
            for (int i = 0; i < mListAuctionMarkers.size(); i++) {
                mListAuctionMarkers.get(i).setVisible(true);
            }
        } else if (!visibility && mListAuctionMarkers.size() > 0) {
            for (int i = 0; i < mListAuctionMarkers.size(); i++) {
                mListAuctionMarkers.get(i).setVisible(false);
            }
        }
    }

    private void setRealEstateStatuses(AppCompatSpinner spinner) {
        List<String> listStatuses = new ArrayList<>();
        RealEstateStatusesViewModel viewModelStatuses = ViewModelProviders.of(getActivity()).get(RealEstateStatusesViewModel.class);
        viewModelStatuses.getStatuses().observe(this, realEstateStatusesModelResponse -> {
            if (realEstateStatusesModelResponse.getKey().equals(SUCCESS)) {
                listStatuses.clear();
                listStatuses.add(getString(R.string.choose_status));
                for (Status status : realEstateStatusesModelResponse.getStatuses()) {
                    listStatuses.add(status.getName());
                }
                ArrayAdapter<String> adapter =
                        new ArrayAdapter<>(getActivity(), R.layout.list_item_spinner, listStatuses);
                spinner.setAdapter(adapter);
            }
        });
    }

    private void setRealEstateCategories(AppCompatSpinner spinner) {
        List<String> listCategories = new ArrayList<>();
        RealEstateCategoriesViewModel viewModelCategories = ViewModelProviders.of(getActivity()).get(RealEstateCategoriesViewModel.class);
        viewModelCategories.getCategories().observe(this, realEstateCategoriesModelResponse -> {
            if (realEstateCategoriesModelResponse.getKey().equals(SUCCESS)) {
                listCategories.clear();
                listCategories.add(getString(R.string.choose_category));
                for (Category category : realEstateCategoriesModelResponse.getCategories()) {
                    listCategories.add(category.getName());
                }
                ArrayAdapter<String> adapter =
                        new ArrayAdapter<>(getActivity(), R.layout.list_item_spinner, listCategories);
                spinner.setAdapter(adapter);
            }
        });
    }

    private void setRegions(AppCompatSpinner spinner) {
        List<String> listRegions = new ArrayList<>();
        RegionsViewModel viewModelRegions = ViewModelProviders.of(getActivity()).get(RegionsViewModel.class);
        viewModelRegions.getRegions().observe(this, regionsModelResponse -> {
            if (regionsModelResponse.getKey().equals(SUCCESS)) {
                listRegions.clear();
                listRegions.add(getString(R.string.choose_region));
                listRegions.addAll(Arrays.asList(regionsModelResponse.getRegions()));
                ArrayAdapter<String> adapter =
                        new ArrayAdapter<>(getActivity(), R.layout.list_item_spinner, listRegions);
                spinner.setAdapter(adapter);
            }
        });
    }

    private void setDistricts(AppCompatSpinner spinner) {
        List<String> listDistricts = new ArrayList<>();
        DistrictsViewModel viewModelDistricts = ViewModelProviders.of(getActivity()).get(DistrictsViewModel.class);
        viewModelDistricts.getDistricts().observe(this, districtsModelResponse -> {
            if (districtsModelResponse.getKey().equals(SUCCESS)) {
                listDistricts.clear();
                listDistricts.add(getString(R.string.choose_district));
                listDistricts.addAll(Arrays.asList(districtsModelResponse.getDistricts()));
                ArrayAdapter<String> adapter =
                        new ArrayAdapter<>(getActivity(), R.layout.list_item_spinner, listDistricts);
                spinner.setAdapter(adapter);
            }
        });
    }
}
