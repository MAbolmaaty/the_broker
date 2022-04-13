package com.emupapps.the_broker.ui;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.adapters.FavoritesAdapter;
import com.emupapps.the_broker.databinding.FragmentFavoritesBinding;
import com.emupapps.the_broker.models.favorites.RealEstate;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.emupapps.the_broker.utils.SoftKeyboard;
import com.emupapps.the_broker.viewmodels.FavoritesViewModel;
import com.emupapps.the_broker.viewmodels.LoginViewModel;
import com.emupapps.the_broker.viewmodels.RealEstateViewModel;
import com.emupapps.the_broker.viewmodels.UnFavoriteViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.emupapps.the_broker.ui.MainActivity.loadFragment;
import static com.emupapps.the_broker.ui.MainActivity.sDrawerLayout;
import static com.emupapps.the_broker.utils.Constants.LOCALE;
import static com.emupapps.the_broker.utils.Constants.SUCCESS;
import static com.emupapps.the_broker.utils.Constants.USER_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends Fragment {

    private static final String TAG = FavoritesFragment.class.getSimpleName();
    private FragmentFavoritesBinding mBinding;

    private FavoritesViewModel mViewModelFavorites;
    private RealEstateViewModel mViewModelRealEstate;
    private String mUserId;
    private FavoritesAdapter mAdapter;
    private List<RealEstate> mListFavorites;
    private UnFavoriteViewModel mViewModelUnFavorite;
    private String mLocale;
    private Toast mToast;

    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentFavoritesBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();

        mLocale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE, Locale.getDefault().getLanguage());
//        mMenu.setImageResource(R.drawable.ic_menu);
//        mTitle.setText(R.string.favorites);

        mViewModelFavorites =
                new ViewModelProvider(this).get(FavoritesViewModel.class);
        mViewModelRealEstate =
                new ViewModelProvider(getActivity()).get(RealEstateViewModel.class);
        mViewModelUnFavorite =
                new ViewModelProvider(this).get(UnFavoriteViewModel.class);

        mListFavorites = new ArrayList<>();
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mBinding.recyclerView.setLayoutManager(layoutManager);
        mBinding.recyclerView.setHasFixedSize(true);
        mAdapter = new FavoritesAdapter(getContext(), mListFavorites, position -> {
            mViewModelRealEstate.setRealEstateId(mListFavorites.get(position).getAkar_id());
            loadFragment(FavoritesFragment.this.getActivity().getSupportFragmentManager(),
                    new RealEstateFragment(), true);
        }, position -> {
            mViewModelUnFavorite.unFavorite(mListFavorites.get(position).getAkar_id(), mUserId, mLocale);
            mViewModelUnFavorite.getResult().observe(FavoritesFragment.this, unFavoriteModelResponse -> {
                if (unFavoriteModelResponse.getKey().equals(SUCCESS)) {
                    mListFavorites.remove(position);
                    if (mListFavorites.size() < 1) {
                        //mNoRealEstates.setVisibility(View.VISIBLE);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            });
            mViewModelUnFavorite.isLoading().observe(FavoritesFragment.this, loading -> {
                if (loading) {
                    //mProgressBar.setVisibility(View.VISIBLE);
                } else {
                   // mProgressBar.setVisibility(View.GONE);
                }
            });
        });
        mBinding.recyclerView.setAdapter(mAdapter);

        mUserId = SharedPrefUtil.getInstance(getContext()).read(USER_ID, null);
        if (mUserId == null){

        } else {
            getFavorites();
        }

        return view;
    }

    public void menu(){
        sDrawerLayout.openDrawer(GravityCompat.START);
        SoftKeyboard.dismissKeyboardInActivity(getActivity());
    }

    private void getFavorites(){
        Log.i(TAG, "get favorites");
        mViewModelFavorites.favorites(mUserId);
        mViewModelFavorites.getFavorites().observe(this, favoritesModelResponse -> {
            if (favoritesModelResponse.getKey().equals(SUCCESS)){
                mListFavorites.clear();
                mListFavorites.addAll(Arrays.asList(favoritesModelResponse.getRealEstates()));
                if (mListFavorites.size() < 1){
                    //mNoRealEstates.setVisibility(View.VISIBLE);
                } else {
                    //mNoRealEstates.setVisibility(View.GONE);
                    mAdapter.notifyDataSetChanged();
                }

            } else {
                mToast = Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT);
                mToast.show();

            }
        });
        mViewModelFavorites.isLoading().observe(this, loading -> {
            if (loading){
                //mProgressBar.setVisibility(View.VISIBLE);
            }else {
                //mProgressBar.setVisibility(View.GONE);
            }
        });
        mViewModelFavorites.failure().observe(this, failure -> {
            if (failure){
                if (mToast != null)
                    mToast.cancel();
                mToast = Toast.makeText(getActivity(), R.string.connection_to_server_lost, Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
    }
}
