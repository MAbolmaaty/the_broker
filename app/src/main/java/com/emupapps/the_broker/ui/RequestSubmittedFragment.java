package com.emupapps.the_broker.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.models.request_submitted.RequestSubmittedModelResponse;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.emupapps.the_broker.viewmodels.RealEstateViewModel;
import com.emupapps.the_broker.viewmodels.RequestSubmittedViewModel;

import java.util.Locale;

import static com.emupapps.the_broker.ui.MainActivity.loadFragment;
import static com.emupapps.the_broker.utils.Constants.LOCALE;
import static com.emupapps.the_broker.utils.Constants.REQUEST_MAINTENANCE;
import static com.emupapps.the_broker.utils.Constants.REQUEST_OWNERSHIP;
import static com.emupapps.the_broker.utils.Constants.REQUEST_RENT;
import static com.emupapps.the_broker.utils.Constants.SUCCESS;
import static com.emupapps.the_broker.utils.Constants.REQUEST_TERMINATION;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestSubmittedFragment extends Fragment {

    ProgressBar mProgressBar;
    ImageView mBack;
    TextView mTitle;
    TextView mStatus;
    ImageView mDebenture;
    TextView mTitle1;
    TextView mTitle2;
    TextView mTitle3;
    TextView mTitle4;
    TextView mTitle5;
    TextView mTitle6;
    TextView mTitle7;
    TextView mValue1;
    TextView mValue2;
    TextView mValue3;
    TextView mValue4;
    TextView mValue5;
    TextView mValue6;
    TextView mValue7;
    TextView mDocuments;
    View mDocument1;
    ImageView mDownloadDoc1;
    TextView mTitleDoc1;
    View mDocument2;
    ImageView mDownloadDoc2;
    TextView mTitleDoc2;
    Button mModifyRequest;

    private RequestSubmittedViewModel mViewModelSubmittedRequest;
    private RealEstateViewModel mViewModelRealEstate;
    private Toast mToast;

    public RequestSubmittedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request_submitted, container,
                false);
        String locale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE, Locale.getDefault().getLanguage());
        if (locale.equals("ar")) {
            mBack.setImageResource(R.drawable.ic_arrow_ar);
        } else {
            mBack.setImageResource(R.drawable.ic_arrow);
        }
        mDebenture.setVisibility(View.VISIBLE);
        mViewModelSubmittedRequest =
                new ViewModelProvider(getActivity()).get(RequestSubmittedViewModel.class);
        mViewModelRealEstate =
                new ViewModelProvider(getActivity()).get(RealEstateViewModel.class);
//        mViewModelRealEstate.getTypeRequest().observe(this, new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer type) {
//                mViewModelSubmittedRequest.getRequestId().observe(RequestSubmittedFragment.this, id -> {
//                    switch (type){
//                        case REQUEST_OWNERSHIP:
//                            loadRequestOwnership(id);
//                            break;
//                        case REQUEST_RENT:
//                            loadRequestRent(id);
//                            break;
//                        case REQUEST_MAINTENANCE:
//                            loadRequestMaintenance(id);
//                            break;
//                        case REQUEST_TERMINATION:
//                            loadRequestTermination(id);
//                            break;
//                        default:
//                            getActivity().onBackPressed();
//                    }
//                });
//            }
//        });

        return view;
    }

    public void back() {
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }

    public void modifyRequest() {
        loadFragment(RequestSubmittedFragment.this.getActivity().getSupportFragmentManager(),
                new RequestModifyFragment(), true);
    }

    public void debenture(){
//        loadFragment(RequestSubmittedFragment.this.getActivity().getSupportFragmentManager(),
//                new DebenturesRentFragment(), true);
    }

    private void loadRequestOwnership(String id){
        if (mToast != null)
            mToast.cancel();
        viewRequestOwnership();
        mViewModelSubmittedRequest.submittedRequest(id);
        mViewModelSubmittedRequest.getResult().observe(this, new Observer<RequestSubmittedModelResponse>() {
            @Override
            public void onChanged(RequestSubmittedModelResponse requestSubmittedModelResponse) {
                if (requestSubmittedModelResponse.getKey().equals(SUCCESS)) {
                    mValue1.setText(requestSubmittedModelResponse.getResult().getUpdated_at().split(" ")[0]);
                    String startDate = requestSubmittedModelResponse.getResult().getAccess_date();
                    if (startDate == null) {
                        startDate = requestSubmittedModelResponse.getResult().getAttendees_date();
                    }
                    mValue2.setText(startDate);
                    mViewModelRealEstate.getRealEstateDetails().observe(RequestSubmittedFragment.this,
                            realEstateModelResponse -> {
                                //mValue3.setText(getString(R.string.price_amount,
                                        //realEstateModelResponse.getRealEstate().getTotal_amount()));
                            });
                    mValue4.setText(setPaymentMethod(requestSubmittedModelResponse.getResult().getPay_way()));
                    mStatus.setText(setRequestStatus(requestSubmittedModelResponse.getResult().getStatus()));
                }else {
                    mToast = Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT);
                    mToast.show();
                    getActivity().onBackPressed();
                }
            }
        });
        loading();
    }

    private void viewRequestOwnership(){
        mTitle.setText(R.string.request_ownership);
        mTitle1.setVisibility(View.VISIBLE);
        mTitle1.setText(R.string.request_date);
        mValue1.setVisibility(View.VISIBLE);
        mValue1.setText(R.string.empty);

        mTitle2.setVisibility(View.VISIBLE);
        mTitle2.setText(R.string.date_start);
        mValue2.setVisibility(View.VISIBLE);
        mValue2.setText(R.string.empty);

        mTitle3.setVisibility(View.VISIBLE);
        mTitle3.setText(R.string.amount);
        mValue3.setVisibility(View.VISIBLE);
        mValue3.setText(R.string.empty);

        mTitle4.setVisibility(View.VISIBLE);
        mTitle4.setText(R.string.method_payment);
        mValue4.setVisibility(View.VISIBLE);
        mValue4.setText(R.string.empty);
    }

    private void loadRequestRent(String id){
        if (mToast != null)
            mToast.cancel();
        viewRequestRent();
        mViewModelSubmittedRequest.submittedRequest(id);
        mViewModelSubmittedRequest.getResult().observe(this, new Observer<RequestSubmittedModelResponse>() {
            @Override
            public void onChanged(RequestSubmittedModelResponse requestSubmittedModelResponse) {
                if (requestSubmittedModelResponse.getKey().equals(SUCCESS)) {
                    mValue1.setText(requestSubmittedModelResponse.getResult().getUpdated_at().split(" ")[0]);
                    String startDate = requestSubmittedModelResponse.getResult().getAccess_date();
                    if (startDate == null) {
                        startDate = requestSubmittedModelResponse.getResult().getAttendees_date();
                    }
                    mValue2.setText(startDate);
                    if (requestSubmittedModelResponse.getResult().getDuration().equals("1")) {
                        mValue3.setText(getString(R.string.one_month, requestSubmittedModelResponse.getResult().getDuration()));
                    } else {
                        mValue3.setText(getString(R.string.months, requestSubmittedModelResponse.getResult().getDuration()));
                    }
                    mViewModelRealEstate.getRealEstateDetails().observe(RequestSubmittedFragment.this,
                            realEstateModelResponse -> {
//                        if (requestSubmittedModelResponse.getResult().getDuration().equals("1")) {
//                            mValue4.setText(getString(R.string.price_amount, realEstateModelResponse.getRealEstate().getPrice_for_month()));
//                        } else if (requestSubmittedModelResponse.getResult().getDuration().equals("3")) {
//                            mValue4.setText(getString(R.string.price_amount, realEstateModelResponse.getRealEstate().getPrice_for_3month()));
//                        } else if (requestSubmittedModelResponse.getResult().getDuration().equals("6")) {
//                            mValue4.setText(getString(R.string.price_amount, realEstateModelResponse.getRealEstate().getPrice_for_6month()));
//                        } else if (requestSubmittedModelResponse.getResult().getDuration().equals("12")) {
//                            mValue4.setText(getString(R.string.price_amount, realEstateModelResponse.getRealEstate().getPrice_for_12month()));
//                        }
//                        if (realEstateModelResponse.getRealEstate().getAmount_insurance() != null)
//                            mValue5.setText(getString(R.string.price_amount, realEstateModelResponse.getRealEstate().getAmount_insurance()));
                    });
                    mValue6.setText(setPaymentMethod(requestSubmittedModelResponse.getResult().getPay_way()));
                    mStatus.setText(setRequestStatus(requestSubmittedModelResponse.getResult().getStatus()));
                }else {
                    mToast = Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT);
                    mToast.show();
                    getActivity().onBackPressed();
                }
            }
        });
        loading();
    }

    private void viewRequestRent(){
        mTitle.setText(R.string.request_rent);
        mTitle1.setVisibility(View.VISIBLE);
        mTitle1.setText(R.string.request_date);
        mValue1.setVisibility(View.VISIBLE);
        mValue1.setText(R.string.empty);

        mTitle2.setVisibility(View.VISIBLE);
        mTitle2.setText(R.string.date_start);
        mValue2.setVisibility(View.VISIBLE);
        mValue2.setText(R.string.empty);

        mTitle3.setVisibility(View.VISIBLE);
        mTitle3.setText(R.string.duration);
        mValue3.setVisibility(View.VISIBLE);
        mValue3.setText(R.string.empty);

        mTitle4.setVisibility(View.VISIBLE);
        mTitle4.setText(R.string.amount);
        mValue4.setVisibility(View.VISIBLE);
        mValue4.setText(R.string.empty);

        mTitle5.setVisibility(View.VISIBLE);
        mTitle5.setText(R.string.amount_insurance);
        mValue5.setVisibility(View.VISIBLE);
        mValue5.setText(R.string.empty);

        mTitle6.setVisibility(View.VISIBLE);
        mTitle6.setText(R.string.method_payment);
        mValue6.setVisibility(View.VISIBLE);
        mValue6.setText(R.string.empty);
    }

    private void loadRequestTermination(String id){
        if (mToast != null)
            mToast.cancel();
        viewRequestTermination();
        mViewModelSubmittedRequest.submittedRequest(id);
        mViewModelSubmittedRequest.getResult().observe(this, requestSubmittedModelResponse -> {
            if (requestSubmittedModelResponse.getKey().equals(SUCCESS)) {
                mValue1.setText(requestSubmittedModelResponse.getResult().getUpdated_at().split(" ")[0]);
                String startDate = requestSubmittedModelResponse.getResult().getAccess_date();
                if (startDate == null) {
                    startDate = requestSubmittedModelResponse.getResult().getAttendees_date();
                }
                mValue2.setText(startDate);
                mValue3.setText(requestSubmittedModelResponse.getResult().getDateExit());
                mValue4.setText(requestSubmittedModelResponse.getResult().getAmount_insurance());
                mValue5.setText(R.string.cash);
                mValue7.setText(requestSubmittedModelResponse.getResult().getDescription());
            }else {
                mToast = Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT);
                mToast.show();
                getActivity().onBackPressed();
            }
        });
        loading();
    }

    private void viewRequestTermination(){
        mTitle.setText(R.string.request_termination);
        mTitle1.setVisibility(View.VISIBLE);
        mTitle1.setText(R.string.request_date);
        mValue1.setVisibility(View.VISIBLE);
        mValue1.setText(R.string.empty);

        mTitle2.setVisibility(View.VISIBLE);
        mTitle2.setText(R.string.date_start);
        mValue2.setVisibility(View.VISIBLE);
        mValue2.setText(R.string.empty);

        mTitle3.setVisibility(View.VISIBLE);
        mTitle3.setText(R.string.date_exit);
        mValue3.setVisibility(View.VISIBLE);
        mValue3.setText(R.string.empty);

        mTitle4.setVisibility(View.VISIBLE);
        mTitle4.setText(R.string.amount_insurance);
        mValue4.setVisibility(View.VISIBLE);
        mValue4.setText(R.string.empty);

        mTitle5.setVisibility(View.VISIBLE);
        mTitle5.setText(R.string.method_refund);
        mValue5.setVisibility(View.VISIBLE);
        mValue5.setText(R.string.empty);

        mTitle7.setVisibility(View.VISIBLE);
        mTitle7.setText(R.string.other_info);
        mValue7.setVisibility(View.VISIBLE);
        mValue7.setText(R.string.empty);
    }

    private void loadRequestMaintenance(String id){
        if (mToast != null)
            mToast.cancel();
        viewRequestMaintenance();
        mViewModelSubmittedRequest.submittedRequest(id);
        mViewModelSubmittedRequest.getResult().observe(this, requestSubmittedModelResponse -> {
            if (requestSubmittedModelResponse.getKey().equals(SUCCESS)) {
                mValue1.setText(requestSubmittedModelResponse.getResult().getUpdated_at().split(" ")[0]);
                mValue7.setText(requestSubmittedModelResponse.getResult().getDescription());
            }else {
                mToast = Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT);
                mToast.show();
                getActivity().onBackPressed();
            }
        });
        loading();
    }

    private void viewRequestMaintenance(){
        mTitle.setText(R.string.request_maintenance);
        mTitle1.setVisibility(View.VISIBLE);
        mTitle1.setText(R.string.request_date);
        mValue1.setVisibility(View.VISIBLE);
        mValue1.setText(R.string.empty);

        mTitle2.setVisibility(View.VISIBLE);
        mTitle2.setText(R.string.type_maintenance);
        mValue2.setVisibility(View.VISIBLE);
        mValue2.setText(R.string.empty);

        mTitle7.setVisibility(View.VISIBLE);
        mTitle7.setText(R.string.other_info);
        mValue7.setVisibility(View.VISIBLE);
        mValue7.setText(R.string.empty);
    }

    private void loading() {
        mViewModelSubmittedRequest.isLoading().observe(RequestSubmittedFragment.this, loading -> {
            if (loading) {
                mProgressBar.setVisibility(View.VISIBLE);
            } else {
                mProgressBar.setVisibility(View.GONE);
            }
        });
        mViewModelSubmittedRequest.failure().observe(this, failure -> {
            if (failure){
                mToast = Toast.makeText(getActivity(), R.string.connection_to_server_lost, Toast.LENGTH_SHORT);
                mToast.show();
                getActivity().onBackPressed();
            }
        });
    }

    private String setPaymentMethod(String payment) {
        if (payment.equals("1")) {
            return getString(R.string.cash);
        } else if (payment.equals("2")) {
            return getString(R.string.visa);
        } else if (payment.equals("3")) {
            return getString(R.string.bank_account);
        }
        return getString(R.string.empty);
    }

    private String setRequestStatus(String status) {
        mStatus.setVisibility(View.VISIBLE);
        if (status.equals("1")) {
            mModifyRequest.setVisibility(View.VISIBLE);
            return getString(R.string.pending);
        } else if (status.equals("2")) {
            mModifyRequest.setVisibility(View.INVISIBLE);
            return getString(R.string.negotiation);
        } else if (status.equals("3")) {
            mModifyRequest.setVisibility(View.INVISIBLE);
            return getString(R.string.negotiated);
        }
        return getString(R.string.empty);
    }
}
