package com.emupapps.the_broker.ui;


import static com.emupapps.the_broker.ui.MainActivity.sDrawerLayout;
import static com.emupapps.the_broker.utils.Constants.AUCTION;
import static com.emupapps.the_broker.utils.Constants.RENT;
import static com.emupapps.the_broker.utils.Constants.SALE;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.databinding.FragmentHomeBinding;
import com.emupapps.the_broker.utils.SoftKeyboard;
import com.emupapps.the_broker.viewmodels.AuthenticationViewModel;
import com.emupapps.the_broker.viewmodels.RealEstateViewModel;
import com.emupapps.the_broker.viewmodels.RealEstatesViewModel;
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
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements OnMapReadyCallback {
    private FragmentHomeBinding mBinding;

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
    private AuthenticationViewModel mAuthenticationViewModel;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();

//        mUserId = SharedPrefUtil.getInstance(getContext()).read(USER_ID, null);
//        if (mUserId != null) {
//            RequestsUserViewModel viewModelUserRequests =
//                    new ViewModelProvider(getActivity()).get(RequestsUserViewModel.class);
//            viewModelUserRequests.userRequests(mUserId);
//        }
//        mLocale = SharedPrefUtil.getInstance(getActivity()).
//                read(LOCALE, Locale.getDefault().getLanguage());

        mViewModelRealEstates =
                new ViewModelProvider(getActivity()).get(RealEstatesViewModel.class);
//        mViewModelSearch =
//                new ViewModelProvider(getActivity()).get(SearchViewModel.class);

        mViewModelRealEstates.realEstates();
        mViewModelRealEstates.startMapFragment(true);

        //Real Estate Details
        mViewModelRealEstate =
                new ViewModelProvider(requireActivity()).get(RealEstateViewModel.class);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().
                findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        saleRealEstatesVisibility(true);
        rentRealEstatesVisibility(true);
        auctionRealEstatesVisibility(false);

        mBinding.imageViewSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.imageViewSale.getTag().equals(1)){
                    saleRealEstatesVisibility(false);
                } else {
                    saleRealEstatesVisibility(true);
                    auctionRealEstatesVisibility(false);
                }
            }
        });

        mBinding.imageViewRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.imageViewRent.getTag().equals(1)){
                    rentRealEstatesVisibility(false);
                } else {
                    rentRealEstatesVisibility(true);
                    auctionRealEstatesVisibility(false);
                }
            }
        });

        mBinding.imageViewAuctions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.imageViewAuctions.getTag().equals(1)){
                    auctionRealEstatesVisibility(false);
                } else {
                    auctionRealEstatesVisibility(true);
                    saleRealEstatesVisibility(false);
                    rentRealEstatesVisibility(false);
                }
            }
        });

        mDialogFilter = new BottomSheetDialog(getContext());
        mDialogFilter.setContentView(R.layout.dialog_filter);

        //SearchView
        SearchManager searchManager = (SearchManager) getActivity().
                getSystemService(Context.SEARCH_SERVICE);
        mBinding.searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().
                getComponentName()));
        mBinding.searchView.setIconifiedByDefault(false);
        mBinding.searchView.setSubmitButtonEnabled(true);
        mBinding.searchView.setFocusable(false);
        mBinding.searchView.findViewById(androidx.appcompat.R.id.search_plate).
                setBackgroundColor(Color.TRANSPARENT);
        ((LinearLayout) mBinding.searchView.findViewById(R.id.search_voice_btn).getParent()).
                setBackgroundColor(Color.TRANSPARENT);

//        if (mLocale.equals("ar")) {
//            binding.searchView.findViewById(androidx.appcompat.R.id.search_plate).
//                    setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
//            binding.searchView.findViewById(androidx.appcompat.R.id.search_close_btn).
//                    setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
//            binding.searchView.findViewById(androidx.appcompat.R.id.search_go_btn).
//                    setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
//            binding.searchView.findViewById(androidx.appcompat.R.id.search_go_btn).
//                    setRotation(180);
//            binding.searchView.findViewById(androidx.appcompat.R.id.search_voice_btn).
//                    setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
//            binding.searchView.findViewById(androidx.appcompat.R.id.search_mag_icon).
//                    setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
//        }

        mBinding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenu();
            }
        });

        mAuthenticationViewModel = new ViewModelProvider(requireActivity()).
                get(AuthenticationViewModel.class);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
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
        mMap.setPadding(0, (mBinding.menu.getLayoutParams().height * 2), 0, 0);
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.maps_style_json));
        mMap.setOnMarkerClickListener(marker -> {
            mViewModelRealEstate.realEstate(marker.getTag().toString());
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
        getRealEstates();
    }

    private void openMenu(){
        sDrawerLayout.openDrawer(GravityCompat.START);
        SoftKeyboard.dismissKeyboardInActivity(getActivity());
    }

    public void openFilter() {
        mDialogFilter.show();
    }

    private void saleRealEstatesVisibility(boolean value){
        if (value){
            mBinding.imageViewSale.setAlpha(1.0f);
            mBinding.markerSale.setAlpha(1.0f);
            mBinding.sale.setAlpha(1.0f);
            for (int i = 0; i < mListSaleMarkers.size(); i++) {
                mListSaleMarkers.get(i).setVisible(true);
            }
            mBinding.imageViewSale.setTag(1);
        } else {
            mBinding.imageViewSale.setAlpha(0.5f);
            mBinding.markerSale.setAlpha(0.5f);
            mBinding.sale.setAlpha(0.5f);
            for (int i = 0; i < mListSaleMarkers.size(); i++) {
                mListSaleMarkers.get(i).setVisible(false);
            }
            mBinding.imageViewSale.setTag(0);
        }
    }

    private void rentRealEstatesVisibility(boolean value){
        if (value){
            mBinding.imageViewRent.setAlpha(1.0f);
            mBinding.markerRent.setAlpha(1.0f);
            mBinding.rent.setAlpha(1.0f);
            for (int i = 0; i < mListRentMarkers.size(); i++) {
                mListRentMarkers.get(i).setVisible(true);
            }
            mBinding.imageViewRent.setTag(1);
        } else {
            mBinding.imageViewRent.setAlpha(0.5f);
            mBinding.markerRent.setAlpha(0.5f);
            mBinding.rent.setAlpha(0.5f);
            for (int i = 0; i < mListRentMarkers.size(); i++) {
                mListRentMarkers.get(i).setVisible(false);
            }
            mBinding.imageViewRent.setTag(0);
        }
    }

    private void auctionRealEstatesVisibility(boolean value) {
        if (value){
            mBinding.imageViewAuctions.setAlpha(1.0f);
            mBinding.markerAuctions.setAlpha(1.0f);
            mBinding.auctions.setAlpha(1.0f);
            for (int i = 0; i < mListAuctionMarkers.size(); i++) {
                mListAuctionMarkers.get(i).setVisible(true);
            }
            mBinding.imageViewAuctions.setTag(1);
        } else {
            mBinding.imageViewAuctions.setAlpha(0.5f);
            mBinding.markerAuctions.setAlpha(0.5f);
            mBinding.auctions.setAlpha(0.5f);
            for (int i = 0; i < mListAuctionMarkers.size(); i++) {
                mListAuctionMarkers.get(i).setVisible(false);
            }
            mBinding.imageViewAuctions.setTag(0);
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
        BitmapDrawable drawableAuction = (BitmapDrawable) getResources().
                getDrawable(R.drawable.ic_marker_auction);
        Bitmap bitmapAuction = drawableAuction.getBitmap();
        Bitmap iconAuction = Bitmap.createScaledBitmap(bitmapAuction,
                (int) (drawableAuction.getIntrinsicWidth() / 1.2),
                (int) (drawableAuction.getIntrinsicHeight() / 1.2), false);
        //Customized Sale Marker
        BitmapDrawable drawableSale = (BitmapDrawable) getResources().
                getDrawable(R.drawable.ic_marker_sale);
        Bitmap bitmapSale = drawableSale.getBitmap();
        Bitmap iconSale = Bitmap.createScaledBitmap(bitmapSale,
                (int) (drawableSale.getIntrinsicWidth() / 1.2),
                (int) (drawableSale.getIntrinsicHeight() / 1.2), false);
        //Customized Rent Marker
        BitmapDrawable drawableRent = (BitmapDrawable) getResources().
                getDrawable(R.drawable.ic_marker_rent);
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
                                LatLng latLng = new LatLng(Double.parseDouble(realEstates.get(i).
                                        getLatitude()),
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
                            Toast toast = Toast.makeText(HomeFragment.this.getActivity(),
                                    R.string.something_went_wrong, Toast.LENGTH_SHORT);
                            toast.show();

                        }
                    }
                });
        mViewModelRealEstates.failure().observe(this, failure -> {
            if (failure) {
                Toast toast = Toast.makeText(getActivity(),
                        R.string.connection_to_server_lost, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
