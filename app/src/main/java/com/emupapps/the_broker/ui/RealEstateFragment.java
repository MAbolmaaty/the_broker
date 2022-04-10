package com.emupapps.the_broker.ui;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.adapters.RealEstateTabsAdapter;
import com.emupapps.the_broker.adapters.SlidesAdapter;
import com.emupapps.the_broker.databinding.FragmentRealEstateBinding;
import com.emupapps.the_broker.models.real_estate.Bid;
import com.emupapps.the_broker.models.real_estate.RealEstate;
import com.emupapps.the_broker.models.real_estate_statuses.Status;
import com.emupapps.the_broker.models.requests_user.Request;
import com.emupapps.the_broker.models.slides.Slide;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.emupapps.the_broker.viewmodels.FavoriteViewModel;
import com.emupapps.the_broker.viewmodels.FavoritesViewModel;
import com.emupapps.the_broker.viewmodels.InfoUserViewModel;
import com.emupapps.the_broker.viewmodels.LoginViewModel;
import com.emupapps.the_broker.viewmodels.OwnerContactViewModel;
import com.emupapps.the_broker.viewmodels.RealEstateCategoriesViewModel;
import com.emupapps.the_broker.viewmodels.RealEstateStatusesViewModel;
import com.emupapps.the_broker.viewmodels.RealEstateViewModel;
import com.emupapps.the_broker.viewmodels.ReportViewModel;
import com.emupapps.the_broker.viewmodels.RequestSubmittedViewModel;
import com.emupapps.the_broker.viewmodels.RequestsUserViewModel;
import com.emupapps.the_broker.viewmodels.UnFavoriteViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.emupapps.the_broker.ui.MainActivity.loadFragment;
import static com.emupapps.the_broker.ui.MainActivity.progress;
import static com.emupapps.the_broker.ui.MainActivity.sDrawerLayout;
import static com.emupapps.the_broker.utils.Constants.AGENT;
import static com.emupapps.the_broker.utils.Constants.APPROVED;
import static com.emupapps.the_broker.utils.Constants.AUCTIONABLE;
import static com.emupapps.the_broker.utils.Constants.CONTACT_OWNER;
import static com.emupapps.the_broker.utils.Constants.HOLDER;
import static com.emupapps.the_broker.utils.Constants.INDIVIDUAL_OWNER;
import static com.emupapps.the_broker.utils.Constants.LOCALE;
import static com.emupapps.the_broker.utils.Constants.REQUEST_OWNERSHIP;
import static com.emupapps.the_broker.utils.Constants.REAL_ESTATE_RENT;
import static com.emupapps.the_broker.utils.Constants.REAL_ESTATE_SALE;
import static com.emupapps.the_broker.utils.Constants.REQUEST_RENT;
import static com.emupapps.the_broker.utils.Constants.SUCCESS;
import static com.emupapps.the_broker.utils.Constants.TENANT;
import static com.emupapps.the_broker.utils.Constants.USER_ID;
import static com.emupapps.the_broker.utils.Constants.USER_TYPE;

/**
 * A simple {@link Fragment} subclass.
 */
public class RealEstateFragment extends Fragment {
    private FragmentRealEstateBinding binding;

    private static final String TAG = RealEstateFragment.class.getSimpleName();

    private Toast mToast;

    //ViewModels
    private ReportViewModel mViewModelReport;
    private FavoriteViewModel mViewModelFavorite;
    private FavoritesViewModel mViewModelFavorites;
    private UnFavoriteViewModel mViewModelUnFavorite;
    private RealEstateStatusesViewModel mViewModelRealEstateStatuses;
    private RealEstateViewModel mViewModelRealEstate;
    private RequestSubmittedViewModel mViewModelSubmittedRequest;
    private OwnerContactViewModel mViewModelContactOwner;
    private RequestsUserViewModel mViewModelUserRequests;
    private RealEstateCategoriesViewModel mViewModelRealEstateCategories;
    private InfoUserViewModel mViewModelUserInfo;

    //RealEstate Info
    private String mOwnerId;
    private String mRealEstateId;
    private String mReportStatus;
    private boolean mAuctionable;
    private String mReportMessage;
    private String mRealEstateLink;
    private String mOwnerPhoneNumber;
    private List<Slide> mListImages = new ArrayList<>();
    private List<Request> mListUserRequests = new ArrayList<>();

    //User
    private String mUserId;
    private String mUserType;
    private String mPrice;
    private String mPriceForMonth;
    private String mPriceFor3Months;
    private String mPriceFor6Months;
    private String mPriceFor12Months;
    private String mLocale;
    private boolean inMyOrders;

    public RealEstateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRealEstateBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        mLocale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE, Locale.getDefault().getLanguage());
        if (mLocale.equals("ar")) {
            binding.viewPager.setRotation(180);
            binding.back.setImageResource(R.drawable.ic_arrow_ar);
        }

        //Initialize ViewModels
        mViewModelRealEstate = ViewModelProviders.of(getActivity()).get(RealEstateViewModel.class);
        mViewModelFavorite = ViewModelProviders.of(this).get(FavoriteViewModel.class);
        mViewModelUnFavorite = ViewModelProviders.of(this).get(UnFavoriteViewModel.class);
        mViewModelRealEstateStatuses = ViewModelProviders.of(getActivity()).get(RealEstateStatusesViewModel.class);
        mViewModelSubmittedRequest = ViewModelProviders.of(getActivity()).get(RequestSubmittedViewModel.class);
        mViewModelFavorites = ViewModelProviders.of(getActivity()).get(FavoritesViewModel.class);
        mViewModelRealEstateCategories = ViewModelProviders.of(getActivity()).get(RealEstateCategoriesViewModel.class);
        mViewModelUserInfo = ViewModelProviders.of(getActivity()).get(InfoUserViewModel.class);

        binding.favorite.setTranslationX(264);
        binding.favorite.setTag(0);
        binding.favoriteForHolder.setTag(0);

        binding.sliderViewRealEstate.setIndicatorAnimation(IndicatorAnimations.FILL);

        binding.share.setTranslationX(196);
        binding.report.setTranslationX(232);

        RealEstateTabsAdapter realEstateTabsAdapter = new RealEstateTabsAdapter(getContext(), getChildFragmentManager(), 0);
        binding.viewPager.setAdapter(realEstateTabsAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        binding.tabLayout.getTabAt(1).select();

        LoginViewModel viewModelLogin = ViewModelProviders.of(getActivity()).get(LoginViewModel.class);
        mUserId = SharedPrefUtil.getInstance(getContext()).read(USER_ID, null);
        mUserType = "-1";
        if (mUserId == null) {
            viewModelLogin.getUser().observe(this, loginModelResponse -> {
                mUserId = loginModelResponse.getUser().getId();
                //mUserType = loginModelResponse.getUser().getType();
            });
        } else {
            mUserType = SharedPrefUtil.getInstance(getContext()).read(USER_TYPE, "-1");
        }
        viewModelLogin.isLoggedIn().observe(this, loggedIn -> {
            if (!loggedIn) {
                mUserId = null;
                mUserType = "-1";
            }
        });

        mViewModelRealEstate.getRealEstateId().observe(this, RealEstateFragment.this::getRealEstate);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Navigation View not allowed
        sDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.START);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        sDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED, GravityCompat.START);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void requestControls() {
        //Calculate Screen Width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        RealEstateFragment.this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        binding.hideMaxBid.getLayoutParams().width = 2 * displayMetrics.widthPixels;
        binding.expandForRequest.setEnabled(false);
        //Close
        if (mAuctionable) {
            binding.viewBid.setVisibility(View.VISIBLE);
            binding.maxBid.setVisibility(View.VISIBLE);
            binding.hideMaxBid.setVisibility(View.VISIBLE);
            binding.hideMaxBid.animate().translationX(0).setDuration(296);
            binding.join.animate().translationX(0).setDuration(296);
            binding.viewRequest.setVisibility(View.INVISIBLE);
            binding.imageViewRequest.setVisibility(View.INVISIBLE);
        }
        if (binding.expandForRequest.getTag().equals(1)) {
            binding.share.animate().translationX(196);
            new Handler().postDelayed(() -> {
                binding.share.setVisibility(View.GONE);
                binding.requestIcon.setVisibility(View.GONE);
            }, 96);
            //
            binding.report.animate().translationX(232);
            new Handler().postDelayed(() -> {
                binding.report.setVisibility(View.GONE);
            }, 132);
            //
            binding.favorite.animate().translationX(264);
            new Handler().postDelayed(() -> {
                binding.favorite.setVisibility(View.GONE);
                binding.viewRequest.setVisibility(View.VISIBLE);
                binding.imageViewRequest.setVisibility(View.VISIBLE);
            }, 164);
            binding.expandForRequest.setTag(0);
            new Handler().postDelayed(() -> {
                binding.expandForRequest.animate().translationX(binding.expandForRequest.getWidth() / 2f);
            }, 196);

            new Handler().postDelayed(() -> {
                binding.expandForRequest.animate().translationX(0);
                binding.expandForRequest.setImageResource(R.drawable.ic_circular_dots);
                binding.expandForRequest.setEnabled(true);
            }, 408);

        } else {
            //Open
            if (mAuctionable) {
                binding.hideMaxBid.setVisibility(View.VISIBLE);
                binding.hideMaxBid.animate().translationX(-displayMetrics.widthPixels).setDuration(496);
                binding.join.animate().translationX(-displayMetrics.widthPixels).setDuration(496);
                binding.viewRequest.setVisibility(View.INVISIBLE);
                binding.imageViewRequest.setVisibility(View.INVISIBLE);
                binding.requestIcon.setImageResource(R.drawable.ic_auction);
            }
            binding.expandForRequest.setTag(1);
            binding.expandForRequest.animate().
                    translationX(-(binding.expandForRequest.getWidth() / 2f));
            new Handler().postDelayed(() -> {
                binding.expandForRequest.animate().translationX(0);
                binding.expandForRequest.setImageResource(R.drawable.ic_circular_close);
                binding.expandForRequest.setEnabled(true);
            }, 348);

            binding.share.animate().translationX(0);
            new Handler().postDelayed(() -> {
                binding.share.setVisibility(View.VISIBLE);
                binding.viewRequest.setVisibility(View.INVISIBLE);
                binding.imageViewRequest.setVisibility(View.INVISIBLE);
            }, 196);
            //
            binding.report.animate().translationX(0);
            new Handler().postDelayed(() -> {
                binding.report.setVisibility(View.VISIBLE);
            }, 232);
            //
            binding.favorite.animate().translationX(0);
            new Handler().postDelayed(() -> {
                binding.favorite.setVisibility(View.VISIBLE);
                binding.requestIcon.setVisibility(View.VISIBLE);
                binding.viewBid.setVisibility(View.INVISIBLE);
                binding.maxBid.setVisibility(View.INVISIBLE);
                binding.hideMaxBid.setVisibility(View.INVISIBLE);
            }, 264);
        }
    }

    public void holderControls() {
        binding.expandForRequest.setEnabled(false);
        if (binding.expandForHolder.getTag().equals(1)) {
            //Close
            binding.expandForHolder.animate().translationX(-binding.expandForHolder.getWidth() / 1.5f).setDuration(308);
            new Handler().postDelayed(() -> {
                binding.expandForHolder.animate().translationX(0).setDuration(308);
                binding.expandForHolder.setImageResource(R.drawable.ic_circular_dots);
                animateAccounting(0, 308);
                animateRentInfo(0, 308);
                binding.icon2.setVisibility(View.GONE);
                binding.icon1.setVisibility(View.GONE);
                binding.favoriteForHolder.setVisibility(View.GONE);
                binding.shareForHolder.setVisibility(View.GONE);
                binding.expandForRequest.setEnabled(true);
            }, 308);
            binding.expandForHolder.setTag(0);
        } else {
            //Open
            binding.expandForHolder.animate().translationX(-binding.expandForHolder.getWidth() / 1.5f).setDuration(308);
            new Handler().postDelayed(() -> {
                binding.expandForHolder.animate().translationX(0).setDuration(308);
                binding.expandForHolder.setImageResource(R.drawable.ic_circular_close);
                animateAccounting(-binding.view2.getWidth() * 3, 308);
                animateRentInfo(-binding.view2.getWidth() * 3, 308);
                binding.icon2.setVisibility(View.VISIBLE);
                binding.icon1.setVisibility(View.VISIBLE);
                binding.favoriteForHolder.setVisibility(View.VISIBLE);
                binding.shareForHolder.setVisibility(View.VISIBLE);
                binding.expandForRequest.setEnabled(true);
            }, 308);
            binding.expandForHolder.setTag(1);
        }
    }

    public void report() {
        if (mUserId == null) {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(getContext(), getString(R.string.login), Toast.LENGTH_SHORT);
            mToast.show();
            return;
        }
        BottomSheetDialog dialogReport = new BottomSheetDialog(getContext());
        dialogReport.setContentView(R.layout.dialog_report);
        dialogReport.show();
        ImageView close = dialogReport.getWindow().findViewById(R.id.action1);
        TextView title = dialogReport.getWindow().findViewById(R.id.title);
        AppCompatSpinner realEstateStatuses = dialogReport.getWindow().findViewById(R.id.realEstateStatus);
        EditText reportTitle = dialogReport.getWindow().findViewById(R.id.reportTitle);
        EditText reportMessage = dialogReport.getWindow().findViewById(R.id.reportMessage);
        Button sendReport = dialogReport.getWindow().findViewById(R.id.send);
        ProgressBar progressBar = dialogReport.getWindow().findViewById(R.id.progress);

        close.setImageResource(R.drawable.ic_close);
        close.setOnClickListener(v -> dialogReport.dismiss());
        title.setText(R.string.report);

        List<String> listStatuses = new ArrayList<>();
        mViewModelRealEstateStatuses.getStatuses().observe(this, realEstateStatusesModelResponse -> {
            if (realEstateStatusesModelResponse.getKey().equals(SUCCESS)) {
                listStatuses.clear();
                listStatuses.add(getString(R.string.choose_status));
                for (Status status : realEstateStatusesModelResponse.getStatuses()) {
                    listStatuses.add(status.getName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                        R.layout.list_item_spinner, listStatuses);
                realEstateStatuses.setAdapter(adapter);
            }
        });

        realEstateStatuses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mReportStatus = null;
                        break;
                    case 1:
                        mReportStatus = "1";
                        break;
                    case 2:
                        mReportStatus = "0";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sendReport.setOnClickListener(v -> {
            mReportMessage = reportMessage.getText().toString();
            if (TextUtils.isEmpty(mReportMessage)) {
                if (mToast != null)
                    mToast.cancel();
                mToast = Toast.makeText(getContext(), getString(R.string.enter_message), Toast.LENGTH_SHORT);
                mToast.show();
                return;
            }
            if (mReportStatus == null) {
                if (mToast != null)
                    mToast.cancel();
                mToast = Toast.makeText(getContext(), getString(R.string.choose_status_first), Toast.LENGTH_SHORT);
                mToast.show();
                return;
            }
            mViewModelReport = ViewModelProviders.of(this).get(ReportViewModel.class);
            mViewModelReport.report(mReportMessage, mUserId, mRealEstateId, mLocale, mReportStatus);
            mViewModelReport.getResult().observe(this, reportModelResponse -> {
                if (reportModelResponse.getMessage() != null) {
                    if (mToast != null)
                        mToast.cancel();
                    mToast = Toast.makeText(getContext(), reportModelResponse.getMessage(), Toast.LENGTH_SHORT);
                    mToast.show();
                    dialogReport.dismiss();
                }
            });
            mViewModelReport.isLoading().observe(this, loading -> {
                progress(loading, progressBar, sendReport);
            });
            mViewModelReport.failure().observe(this, failure -> {
                if (failure) {
                    if (mToast != null)
                        mToast.cancel();
                    mToast = Toast.makeText(getActivity(), R.string.connection_to_server_lost, Toast.LENGTH_SHORT);
                    mToast.show();
                }
            });
        });
    }

    public void request() {
        if (mUserId == null) {
            if (mToast != null)
                mToast.cancel();
            mToast = Toast.makeText(getContext(), getString(R.string.login), Toast.LENGTH_SHORT);
            mToast.show();
            return;
        }
        if (binding.viewRequest.getTag().equals(CONTACT_OWNER)) {
            contactOwner();
        } else if (binding.viewRequest.getTag().equals(REQUEST_OWNERSHIP)) {
            mViewModelRealEstate.setTypeRequest(REQUEST_OWNERSHIP);
            if (inMyOrders) {
                loadFragment(RealEstateFragment.this.getActivity().getSupportFragmentManager(),
                        new RequestSubmittedFragment(), true);
            } else {
                loadFragment(RealEstateFragment.this.getActivity().getSupportFragmentManager(),
                        new RequestFragment(), true);
            }
        } else if (binding.viewRequest.getTag().equals(REQUEST_RENT)) {
            mViewModelRealEstate.setTypeRequest(REQUEST_RENT);
            if (inMyOrders) {
                loadFragment(RealEstateFragment.this.getActivity().getSupportFragmentManager(),
                        new RequestSubmittedFragment(), true);
            } else {
                loadFragment(RealEstateFragment.this.getActivity().getSupportFragmentManager(),
                        new RequestFragment(), true);
            }
        } else if (binding.viewRequest.getTag().equals(TENANT)){
            loadFragment(RealEstateFragment.this.getActivity().getSupportFragmentManager(),
                    new RealEstateRequestsUserFragment(), true);
        }
    }

    public void share() {
        if (mRealEstateLink != null) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
            intent.putExtra(Intent.EXTRA_TEXT, mRealEstateLink);
            startActivity(Intent.createChooser(intent, "Share URL"));
        } else {
            mToast = Toast.makeText(getContext(), getString(R.string.no_sharable_link), Toast.LENGTH_SHORT);
            if (mToast != null)
                mToast.cancel();
            mToast.show();
        }
    }

    public void shareForHolder() {
        if (mRealEstateLink != null) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
            intent.putExtra(Intent.EXTRA_TEXT, mRealEstateLink);
            startActivity(Intent.createChooser(intent, "Share URL"));
        } else {
            mToast = Toast.makeText(getContext(), getString(R.string.no_sharable_link), Toast.LENGTH_SHORT);
            if (mToast != null)
                mToast.cancel();
            mToast.show();
        }
    }

    public void favorite() {
        if (mToast != null)
            mToast.cancel();

        if (mUserId == null) {
            mToast = Toast.makeText(getContext(), getString(R.string.login), Toast.LENGTH_SHORT);
            mToast.show();
            return;
        }

        if (binding.favorite.getTag().equals(0)) {
            mToast = Toast.makeText(getActivity(), R.string.added_to_favorites, Toast.LENGTH_SHORT);
            mToast.show();
            mViewModelFavorite.favorite(mRealEstateId, mUserId, mLocale);
            mViewModelFavorite.isLoading().observe(this, loading -> {
                if (loading) {
                    binding.favorite.setEnabled(false);
                } else {
                    binding.favorite.setEnabled(true);
                }
            });

            new Handler().postDelayed(() -> {
                binding.favorite.setImageResource(R.drawable.ic_liked);
            }, 148);
            binding.favorite.setTag(1);
        } else {
            mToast = Toast.makeText(getActivity(), R.string.removed_from_favorites, Toast.LENGTH_SHORT);
            mToast.show();
            mViewModelUnFavorite.unFavorite(mRealEstateId, mUserId, mLocale);
            mViewModelUnFavorite.isLoading().observe(this, loading -> {
                if (loading) {
                    binding.favorite.setEnabled(false);
                } else {
                    binding.favorite.setEnabled(true);
                }
            });

            new Handler().postDelayed(() -> {
                binding.favorite.setImageResource(R.drawable.ic_like);
            }, 148);
            binding.favorite.setTag(0);
        }
    }

    public void favoriteForHolder() {
        if (mUserId == null) {
            if (mToast != null)
                mToast.cancel();

            mToast = Toast.makeText(getContext(), getString(R.string.login), Toast.LENGTH_SHORT);
            mToast.show();
            return;
        }

        if (binding.favoriteForHolder.getTag().equals(0)) {
            mToast = Toast.makeText(getActivity(), R.string.added_to_favorites, Toast.LENGTH_SHORT);
            mToast.show();
            mViewModelFavorite.favorite(mRealEstateId, mUserId, mLocale);
            mViewModelFavorite.isLoading().observe(this, loading -> {
                if (loading) {
                    binding.favoriteForHolder.setEnabled(false);
                } else {
                    binding.favoriteForHolder.setEnabled(true);
                }
            });
            new Handler().postDelayed(() -> {
                binding.favoriteForHolder.setImageResource(R.drawable.ic_liked);
            }, 148);
            new Handler().postDelayed(() -> {
            }, 896);
            binding.favoriteForHolder.setTag(1);
        } else {
            mToast = Toast.makeText(getActivity(), R.string.removed_from_favorites, Toast.LENGTH_SHORT);
            mToast.show();
            mViewModelUnFavorite.unFavorite(mRealEstateId, mUserId, mLocale);
            mViewModelUnFavorite.isLoading().observe(this, loading -> {
                if (loading) {
                    binding.favoriteForHolder.setEnabled(false);
                } else {
                    binding.favoriteForHolder.setEnabled(true);
                }
            });
            new Handler().postDelayed(() -> {
                binding.favoriteForHolder.setImageResource(R.drawable.ic_like);
            }, 148);
            binding.favoriteForHolder.setTag(0);
        }
    }

    public void back() {
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }

    public void join() {
        if (mToast != null)
            mToast.cancel();

        if (mUserId == null) {
            mToast = Toast.makeText(getContext(), getString(R.string.login), Toast.LENGTH_SHORT);
            mToast.show();
            return;
        }

        if (mOwnerId.equals(mUserId)) {
            loadFragment(getActivity().getSupportFragmentManager(),
                    new AuctionFragment(), true);
            return;
        }

        mViewModelRealEstate.getRealEstate().observe(RealEstateFragment.this, realEstateModelResponse -> {
            for (Bid bid : realEstateModelResponse.getRealEstate().getBids()) {
                if (bid.getUser_id().equals(mUserId)) {
                    loadFragment(getActivity().getSupportFragmentManager(),
                            new AuctionFragment(), true);
                    return;
                }
            }
            loadFragment(getActivity().getSupportFragmentManager(),
                    new AuctionJoinFragment(), true);
        });
    }

    public void view1() {
        if (mToast != null)
            mToast.cancel();
        if (binding.text1.getTag().equals(AGENT)){
            loadFragment(RealEstateFragment.this.getActivity().getSupportFragmentManager(),
                    new RealEstateRequestsCurrentFragment(), true);
        } else if (binding.text1.getTag().equals(HOLDER)){
            mViewModelRealEstate.getRealEstate().observe(this, realEstateModelResponse -> {
                if (realEstateModelResponse.getRealEstate().getService().equals(REAL_ESTATE_RENT) &&
                realEstateModelResponse.getRealEstate().getOwnership().getTenant_id() .equals("0") ||
                        realEstateModelResponse.getRealEstate().getOwnership().getTenant_id() == null){
                    mToast = Toast.makeText(getActivity(), R.string.no_tenant_yet, Toast.LENGTH_SHORT);
                    mToast.show();
                    return;
                }
                loadFragment(RealEstateFragment.this.getActivity().getSupportFragmentManager(),
                        new OccupantFragment(), true);
            });

        } else {
            mToast = Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

    public void view2() {
        if (binding.text2.getTag().equals(AGENT)){
            loadFragment(RealEstateFragment.this.getActivity().getSupportFragmentManager(),
                    new AccountingAgentFragment(), true);
        } else if(binding.text2.getTag().equals(HOLDER)){
            loadFragment(RealEstateFragment.this.getActivity().getSupportFragmentManager(),
                    new AccountingHolderFragment(), true);
        }
    }

    public void viewBid(){}

    private void getRealEstate(String id) {
        mViewModelRealEstate.realEstate(id);
        mViewModelRealEstate.getRealEstate().observe(this, realEstateModelResponse -> {
            if (realEstateModelResponse.getKey().equals(SUCCESS)) {
                binding.shimmer1.setVisibility(View.GONE);
                binding.shimmer2.setVisibility(View.GONE);
                binding.shimmer3.setVisibility(View.GONE);
                RealEstate realEstate = realEstateModelResponse.getRealEstate();
                mRealEstateId = realEstate.getId();
                mOwnerId = realEstate.getOwnership().getOwner_id();
                mRealEstateLink = realEstate.getLink();
                binding.title.setText(realEstate.getTitle());
                binding.addressDetails.setText(realEstate.getFull_address());
                mPrice = realEstate.getTotal_amount();
                mPriceForMonth = realEstate.getPrice_for_month();
                mPriceFor3Months = realEstate.getPrice_for_3month();
                mPriceFor6Months = realEstate.getPrice_for_6month();
                mPriceFor12Months = realEstate.getPrice_for_12month();

                mViewModelUserRequests = ViewModelProviders.of(getActivity()).get(RequestsUserViewModel.class);
                mViewModelUserRequests.userRequests(mUserId);
                getFavorites(mUserId);
                mViewModelUserRequests.getUserRequests().observe(this, userRequestsModelResponse ->
                {
                    if (userRequestsModelResponse.getKey().equals(SUCCESS)) {
                        mListUserRequests.addAll(Arrays.asList(userRequestsModelResponse.getRequests()));
                        for (Request request : mListUserRequests) {
                            if (request.getAkar_id() != null && request.getAkar_id().equals(mRealEstateId)) {
                                inMyOrders = true;
                                mViewModelSubmittedRequest.setRequestId(request.getId());
                            }
                        }

                        mListImages.clear();
                        for (int i = 0; i < realEstate.getGallery().length; i++) {
                            mListImages.add(new Slide(realEstate.getGallery()[i].getPhoto()));
                        }
                        SlidesAdapter slidesShowAdapter = new SlidesAdapter(getContext(), mListImages);
                        binding.sliderViewRealEstate.setSliderAdapter(slidesShowAdapter);

                        mViewModelRealEstateCategories.getCategories().observe(this, realEstateCategoriesModelResponse -> {
                            if (realEstateCategoriesModelResponse.getKey().equals(SUCCESS)) {
                                binding.category.setVisibility(View.VISIBLE);
                                binding.category.setText(realEstateCategoriesModelResponse.
                                        getCategories()[Integer.parseInt(realEstateModelResponse.
                                        getRealEstate().getCate())].getName());
                            }
                        });

                        //Set Real Estate page depends on Type of the User and the Real Estate
                        if (realEstate.getService().equals(REAL_ESTATE_SALE)) {
                            binding.status.setText(getString(R.string.sale));
                        } else if (realEstate.getService().equals(REAL_ESTATE_RENT)) {
                            binding.status.setText(getString(R.string.rent));
                        }

                        if (mUserType.equals(AGENT)){
                            setRealEstateForAdministrator(realEstateModelResponse.getRealEstate().getService());
                            return;
                        }

                        if (mOwnerId.equals(mUserId) && realEstate.getAuction().equals(AUCTIONABLE)) {
                            setRealEstateForAuctionHolder();
                            if (realEstateModelResponse.getRealEstate().getBids().length > 0) {
                                ArrayList<Bid> bids = new ArrayList<>();
                                double maxBid = 0;
                                for (Bid bid : realEstateModelResponse.getRealEstate().getBids()) {
                                    if (bid.getApprove().equals(APPROVED)) {
                                        bids.add(bid);
                                        if (bid.getPrice() != null && !TextUtils.isEmpty(bid.getPrice()) && Double.parseDouble(bid.getPrice()) > maxBid) {
                                            maxBid = Double.parseDouble(bid.getPrice());
                                        }
                                    }
                                }
                                binding.bidsCount.setText(String.valueOf(bids.size()));
                                binding.maxBid.setText(getString(R.string.max_bid, String.valueOf(maxBid)));
                            }
                            return;
                        }

                        if (mOwnerId.equals(mUserId)) {
                            setRealEstateForHolder(realEstateModelResponse.getRealEstate().getService());
                            return;
                        }

                        if (realEstateModelResponse.getType().equals(INDIVIDUAL_OWNER)) {
                            setRealEstateForIndividualOwner(realEstate.getAuction());
                            return;
                        }

                        if (realEstate.getAuction().equals(AUCTIONABLE)) {
                            setRealEstateForAuction();
                            if (realEstateModelResponse.getRealEstate().getBids().length > 0) {
                                ArrayList<Bid> bids = new ArrayList<>();
                                double maxBid = 0;
                                for (Bid bid : realEstateModelResponse.getRealEstate().getBids()) {
                                    if (bid.getApprove().equals(APPROVED)) {
                                        bids.add(bid);
                                        if (bid.getPrice() != null && !TextUtils.isEmpty(bid.getPrice()) && Double.parseDouble(bid.getPrice()) > maxBid) {
                                            maxBid = Double.parseDouble(bid.getPrice());
                                        }
                                    }
                                }
                                binding.bidsCount.setText(String.valueOf(bids.size()));
                                binding.maxBid.setText(getString(R.string.max_bid, String.valueOf(maxBid)));
                            }
                            return;
                        }

                        if (realEstate.getOwnership().getTenant_id().equals(mUserId)){
                            setRealEstateForTenant();
                            return;
                        }

                        if (realEstate.getService().equals(REAL_ESTATE_SALE)) {
                            setRealEstateForSale();
                            return;
                        }

                        if (realEstate.getService().equals(REAL_ESTATE_RENT)) {
                            setRealEstateForRent();
                        }
                    }
                });
                offsetAnimation();
            } else {
                mToast = Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
        mViewModelRealEstate.failure().observe(this, failure -> {
            if (failure) {
                if (mToast != null)
                    mToast.cancel();
                mToast = Toast.makeText(getActivity(), R.string.connection_to_server_lost, Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
    }

    private void getFavorites(String userId) {
        if (userId == null) {
            return;
        }
        mViewModelFavorites.favorites(userId);
        mViewModelFavorites.getFavorites().observe(this, favoritesModelResponse -> {
            if (favoritesModelResponse.getKey().equals(SUCCESS)) {
                for (com.emupapps.the_broker.models.favorites.RealEstate favorite : favoritesModelResponse.getRealEstates()) {
                    if (favorite.getAkar_id().equals(mRealEstateId)) {
                        binding.favorite.setTag(1);
                        binding.favorite.setImageResource(R.drawable.ic_liked);
                        binding.favoriteForHolder.setTag(1);
                        binding.favoriteForHolder.setImageResource(R.drawable.ic_liked);
                    }
                }
            }
        });
    }

    private void setRealEstateForAdministrator(String status){
        if (status.equals(REAL_ESTATE_SALE)) {
            binding.amount.setText(String.format(getResources().getString(R.string.price_amount), mPrice));
        } else if (status.equals(REAL_ESTATE_RENT)) {
            setRentPrice();
        }
        expandForAgent();
    }

    private void setRealEstateForHolder(String status) {
        if (status.equals(REAL_ESTATE_SALE)) {
            binding.text1.setText(R.string.owner);
            binding.amount.setText(String.format(getResources().getString(R.string.price_amount), mPrice));
        } else if (status.equals(REAL_ESTATE_RENT)) {
            binding.text1.setText(R.string.tenant);
            setRentPrice();
        }
        expandForHolder();
    }

    private void setRealEstateForSale() {
        binding.status.setText(getString(R.string.sale));
        expandForRequest();
        binding.viewRequest.setTag(REQUEST_OWNERSHIP);
        if (inMyOrders) {
            binding.request.setText(getString(R.string.show_your_request));
        } else {
            binding.request.setText(getString(R.string.request_ownership));
        }
        binding.imageViewRequest.setImageResource(R.drawable.ic_key);
        binding.requestIcon.setImageResource(R.drawable.ic_key);
        binding.amount.setText(String.format(getResources().getString(R.string.price_amount), mPrice));
    }

    private void setRealEstateForRent() {
        binding.status.setText(getString(R.string.rent));
        setRentPrice();
        expandForRequest();
        binding.viewRequest.setTag(REQUEST_RENT);
        if (inMyOrders) {
            binding.request.setText(getString(R.string.show_your_request));
        } else {
            binding.request.setText(getString(R.string.request_rent));
        }
        binding.imageViewRequest.setImageResource(R.drawable.ic_clock);
        binding.requestIcon.setImageResource(R.drawable.ic_clock);
    }

    private void setRealEstateForAuction() {
        expandForRequest();
        showAuctionView();
    }

    private void setRealEstateForAuctionHolder() {
        expandForRequest();
        showAuctionView();
        binding.report.setEnabled(false);
        binding.join.setText(R.string.bids);
    }

    private void setRealEstateForIndividualOwner(String auction) {
        if (auction.equals(AUCTIONABLE)) {
            showAuctionView();
            expandForRequest();
        } else {
            expandForRequest();
            binding.viewRequest.setTag(CONTACT_OWNER);
            binding.request.setText(getString(R.string.owner_contact));
            binding.imageViewRequest.setImageResource(R.drawable.ic_contact);
            binding.requestIcon.setImageResource(R.drawable.ic_contact);
        }
    }

    private void setRealEstateForTenant(){
        expandForRequest();
        binding.imageViewRequest.setImageResource(R.drawable.ic_info);
        binding.requestIcon.setImageResource(R.drawable.ic_info);
        binding.request.setText(getString(R.string.requests));
        binding.viewRequest.setTag(TENANT);
    }

    private void contactOwner() {
        BottomSheetDialog dialogContactOwner = new BottomSheetDialog(getContext());
        dialogContactOwner.setContentView(R.layout.dialog_contact_owner);
        dialogContactOwner.show();
        ImageView close = dialogContactOwner.getWindow().findViewById(R.id.action1);
        TextView title = dialogContactOwner.getWindow().findViewById(R.id.title);
        TextView phoneNumber = dialogContactOwner.getWindow().findViewById(R.id.phoneNumber);
        EditText message = dialogContactOwner.getWindow().findViewById(R.id.editText_message);
        Button send = dialogContactOwner.getWindow().findViewById(R.id.send);
        ProgressBar progressBar = dialogContactOwner.getWindow().findViewById(R.id.progress);

        close.setImageResource(R.drawable.ic_close);
        close.setOnClickListener(v -> dialogContactOwner.dismiss());
        title.setText(R.string.owner_contact);

        if (mOwnerPhoneNumber != null) {
            phoneNumber.setText(mOwnerPhoneNumber);
        }

        mViewModelContactOwner = ViewModelProviders.of(this).get(OwnerContactViewModel.class);
        send.setOnClickListener(v -> {
            String text = message.getText().toString();
            if (TextUtils.isEmpty(text)) {
                if (mToast != null)
                    mToast.cancel();
                mToast = Toast.makeText(getContext(), getString(R.string.enter_message), Toast.LENGTH_SHORT);
                mToast.show();
                return;
            }
            mViewModelContactOwner.contactOwner(text, mOwnerId, mLocale);
            mViewModelContactOwner.isLoading().observe(this, loading -> progress(loading, progressBar, send));
            mViewModelContactOwner.getResult().observe(this, contactOwnerModelResponse -> {
                if (contactOwnerModelResponse.getMessage() != null) {
                    if (mToast != null)
                        mToast.cancel();
                    mToast = Toast.makeText(getContext(), contactOwnerModelResponse.getMessage(), Toast.LENGTH_SHORT);
                    mToast.show();
                    dialogContactOwner.dismiss();
                }
            });
            mViewModelContactOwner.failure().observe(this, failure -> {
                if (failure) {
                    if (mToast != null)
                        mToast.cancel();
                    mToast = Toast.makeText(getActivity(), R.string.connection_to_server_lost, Toast.LENGTH_SHORT);
                    mToast.show();
                }
            });
        });
    }

    private void expandForRequest() {
        binding.expandForRequest.setVisibility(View.VISIBLE);
        binding.viewRequest.setVisibility(View.VISIBLE);
        binding.request.setVisibility(View.VISIBLE);
        binding.imageViewRequest.setVisibility(View.VISIBLE);
        binding.expandForRequest.setTag(0);
    }

    private void expandForHolder() {
        binding.expandForHolder.setVisibility(View.VISIBLE);
        binding.view2.setVisibility(View.VISIBLE);
        binding.view1.setVisibility(View.VISIBLE);
        binding.text2.setVisibility(View.VISIBLE);
        binding.image2.setVisibility(View.VISIBLE);
        binding.text1.setVisibility(View.VISIBLE);
        binding.image1.setVisibility(View.VISIBLE);
        binding.text1.setTag(HOLDER);
        binding.text2.setTag(HOLDER);
        binding.expandForHolder.setTag(0);
    }

    private void expandForAgent(){
        binding.view2.setVisibility(View.VISIBLE);
        binding.view1.setVisibility(View.VISIBLE);
        binding.text2.setVisibility(View.VISIBLE);
        binding.image2.setVisibility(View.VISIBLE);
        binding.text1.setVisibility(View.VISIBLE);
        binding.image1.setVisibility(View.VISIBLE);
        binding.text1.setText(R.string.requests);
        binding.text1.setTag(AGENT);
        binding.text2.setTag(AGENT);
    }

    private void showAuctionView() {
        binding.maxBid.setVisibility(View.VISIBLE);
        binding.viewBid.setVisibility(View.VISIBLE);
        binding.join.setVisibility(View.VISIBLE);
        binding.textViewExpirationDate.setVisibility(View.VISIBLE);
        binding.viewExpirationDate.setVisibility(View.VISIBLE);
        binding.expirationDate.setVisibility(View.VISIBLE);
        binding.bidsCount.setVisibility(View.VISIBLE);
        binding.bids.setVisibility(View.VISIBLE);
        mAuctionable = true;
    }

    private void animateAccounting(float translation, long duration) {
        binding.view2.animate().translationX(translation).setDuration(duration);
        binding.text2.animate().translationX(translation).setDuration(duration);
        binding.image2.animate().translationX(translation).setDuration(duration);
    }

    private void animateRentInfo(float translation, long duration) {
        binding.view1.animate().translationX(translation).setDuration(duration);
        binding.text1.animate().translationX(translation).setDuration(duration);
        binding.image1.animate().translationX(translation).setDuration(duration);
    }

    private void setRentPrice() {
        if (mPriceForMonth != null) {
            binding.amount.setText(String.format(getResources().getString(R.string.price_amount), mPriceForMonth));
        } else if (mPriceFor3Months != null) {
            binding.amount.setText(String.format(getResources().getString(R.string.price_amount), mPriceFor3Months));
        } else if (mPriceFor6Months != null) {
            binding.amount.setText(String.format(getResources().getString(R.string.price_amount), mPriceFor6Months));
        } else if (mPriceFor12Months != null) {
            binding.amount.setText(String.format(getResources().getString(R.string.price_amount), mPriceFor12Months));
        }
    }

    private void offsetAnimation(){
        if (binding.title.getLineCount() + binding.addressDetails.getLineCount() > 6) {
            int originalRange = binding.appBarLayout.getTotalScrollRange();
            int infoHeight = ((binding.title.getLineCount() * binding.title.getLineHeight()) +
                    (binding.addressDetails.getLineCount() * binding.addressDetails.getLineHeight())) -
                    ((int)(binding.view2.getLayoutParams().height * 1.5f));

            binding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                    int offset = (originalRange + i) / 100;
                    if (-i > 100) {
                        binding.title.animate().alpha(offset / 10f);
                        binding.addressDetails.animate().alpha(offset / 10f);
                    } else {
                        binding.title.animate().alpha(1);
                        binding.addressDetails.animate().alpha(1);
                    }

                    if (originalRange + i < 100) {
                        translateViewAccounting(-infoHeight, true);
                        translateViewClientInfo(-infoHeight, true);
                        translateExpandForHolder(-infoHeight, true);
                        translateViewsAuction(-infoHeight, true);
                        translateExpandForRequest(-infoHeight, true);
                    } else {
                        translateViewAccounting(0, true);
                        translateViewClientInfo(0, true);
                        translateExpandForHolder(0, true);
                        translateViewsAuction(0, true);
                        translateExpandForRequest(0, true);
                    }

                    if (originalRange == -i){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                binding.constraintLayout.setVisibility(View.GONE);
                            }
                        }, 296);
                    }

                    if (i == 0){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                binding.constraintLayout.setVisibility(View.VISIBLE);
                            }
                        }, 296);
                    }
                }
            });
        }
    }

    private void translateViewAccounting(int value, boolean animation){
        if (animation){
            binding.view2.animate().translationY(value);
            binding.text2.animate().translationY(value);
            binding.image2.animate().translationY(value);

        } else {
            binding.view2.setTranslationY(value);
            binding.text2.setTranslationY(value);
            binding.image2.setTranslationY(value);
        }
    }

    private void translateViewClientInfo(int value, boolean animation){
        if (animation){
            binding.view1.animate().translationY(value);
            binding.text1.animate().translationY(value);
            binding.image1.animate().translationY(value);
        } else {
            binding.view1.setTranslationY(value);
            binding.text1.setTranslationY(value);
            binding.image1.setTranslationY(value);
        }
    }

    private void translateExpandForRequest(int value, boolean animation){
        if (animation){
            binding.expandForRequest.animate().translationY(value);
            binding.viewRequest.animate().translationY(value);
            binding.request.animate().translationY(value);
            binding.imageViewRequest.animate().translationY(value);
            binding.favorite.animate().translationY(value);
            binding.report.animate().translationY(value);
            binding.share.animate().translationY(value);
            binding.viewRequest.animate().translationY(value);
            binding.requestIcon.animate().translationY(value);
        } else {
            binding.expandForRequest.setTranslationY(value);
            binding.viewRequest.setTranslationY(value);
            binding.request.setTranslationY(value);
            binding.imageViewRequest.setTranslationY(value);
            binding.favorite.setTranslationY(value);
            binding.report.setTranslationY(value);
            binding.share.setTranslationY(value);
            binding.viewRequest.setTranslationY(value);
            binding.requestIcon.setTranslationY(value);
        }
    }

    private void translateExpandForHolder(int value, boolean animation){
        if (animation){
            binding.expandForHolder.animate().translationY(value);
            binding.favoriteForHolder.animate().translationY(value);
            binding.shareForHolder.animate().translationY(value);
            binding.icon1.animate().translationY(value);
            binding.icon2.animate().translationY(value);
        } else {
            binding.expandForHolder.setTranslationY(value);
            binding.favoriteForHolder.setTranslationY(value);
            binding.shareForHolder.setTranslationY(value);
            binding.icon1.setTranslationY(value);
            binding.icon2.setTranslationY(value);
        }
    }

    private void translateViewsAuction(int value, boolean animation){
        if (animation){
            binding.hideMaxBid.animate().translationY(value);
            binding.maxBid.animate().translationY(value);
            binding.viewBid.animate().translationY(value);
            binding.join.animate().translationY(value);
            binding.bids.animate().translationY(value);
            binding.bidsCount.animate().translationY(value);
            binding.textViewExpirationDate.animate().translationY(value);
            binding.expirationDate.animate().translationY(value);
            binding.viewExpirationDate.animate().translationY(value);
        } else {
            binding.join.setTranslationY(value);
            binding.hideMaxBid.setTranslationY(value);
            binding.bids.setTranslationY(value);
            binding.bidsCount.setTranslationY(value);
            binding.textViewExpirationDate.setTranslationY(value);
            binding.expirationDate.setTranslationY(value);
            binding.viewExpirationDate.setTranslationY(value);
            binding.maxBid.setTranslationY(value);
            binding.viewBid.setTranslationY(value);
        }
    }
}
