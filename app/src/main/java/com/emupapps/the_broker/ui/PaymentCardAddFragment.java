package com.emupapps.the_broker.ui;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.emupapps.the_broker.utils.SoftKeyboard;
import com.emupapps.the_broker.viewmodels.LoginViewModel;
import com.emupapps.the_broker.viewmodels.PaymentCardAddViewModel;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.Locale;

import static com.emupapps.the_broker.utils.Constants.DEFAULT;
import static com.emupapps.the_broker.utils.Constants.LOCALE;
import static com.emupapps.the_broker.utils.Constants.NOT_DEFAULT;
import static com.emupapps.the_broker.utils.Constants.SUCCESS;
import static com.emupapps.the_broker.utils.Constants.USER_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentCardAddFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    ImageView mBack;
    TextView mTitle;
    EditText mCardNumber;
    TextView mExpirationDate;
    EditText mCVV;
    ProgressBar mProgress;
    CheckBox mDefaultCard;

    private Toast mToast;
    private PaymentCardAddViewModel mViewModelAddCard;
    private String mUserId;
    private String mLocale;
    private String mIsDefault;

    public PaymentCardAddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_card_add, container,
                false);
        mLocale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE,
                Locale.getDefault().getLanguage());
        if (mLocale.equals("ar")) {
            mBack.setImageResource(R.drawable.ic_arrow_ar);
        } else {
            mBack.setImageResource(R.drawable.ic_arrow);
        }
        mTitle.setText(R.string.add_new_card);
        mViewModelAddCard = ViewModelProviders.of(this).get(PaymentCardAddViewModel.class);
        mUserId = SharedPrefUtil.getInstance(getActivity()).read(USER_ID, null);
        if (mUserId == null) {
            LoginViewModel viewModelLogin = ViewModelProviders.of(getActivity()).get(LoginViewModel.class);
            viewModelLogin.getUser().observe(this, loginModelResponse ->
                    mUserId = loginModelResponse.getUser().getId());
        }
        mIsDefault = NOT_DEFAULT;
        return view;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
        mExpirationDate.setText(date);

    }

    public void back() {
        getActivity().onBackPressed();
    }

    public void pickDate() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setLocale(Locale.ENGLISH);
        datePickerDialog.setMinDate(calendar);
        datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
    }

    public void addCard() {

        if (mToast != null){
            mToast.cancel();
        }

        String cardNumber = mCardNumber.getText().toString();
        //String cardType = mCardType.getText().toString();
        String expirationDate = mExpirationDate.getText().toString();
        String cvv = mCVV.getText().toString();

        if (TextUtils.isEmpty(cardNumber)){
            mToast = Toast.makeText(getContext(), R.string.enter_card_number, Toast.LENGTH_SHORT);
            mToast.show();
            return;
        }

        /*if (TextUtils.isEmpty(cardType)){
            mToast = Toast.makeText(getContext(), R.string.enter_card_type, Toast.LENGTH_SHORT);
            mToast.show();
            return;
        }*/

        if (TextUtils.isEmpty(expirationDate)){
            mToast = Toast.makeText(getContext(), R.string.enter_expiration_date, Toast.LENGTH_SHORT);
            mToast.show();
            return;
        }

        if (TextUtils.isEmpty(cvv)){
            mToast = Toast.makeText(getContext(), R.string.enter_cvv, Toast.LENGTH_SHORT);
            mToast.show();
            return;
        }

        if (mDefaultCard.isChecked()){
            mIsDefault = DEFAULT;
        }

        SoftKeyboard.dismissKeyboardInActivity(getActivity());
        mViewModelAddCard.paymentCard(cardNumber, expirationDate, mUserId, getString(R.string.visa),
                mLocale, cvv, mIsDefault);
        mViewModelAddCard.getPaymentCard().observe(this, paymentCardAddModelResponse -> {
            if (paymentCardAddModelResponse.getKey().equals(SUCCESS)){
                mToast = Toast.makeText(getContext(), paymentCardAddModelResponse.getMessage(),
                        Toast.LENGTH_SHORT);
                mToast.show();
                getActivity().onBackPressed();
            } else {
                mToast = Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT);
                mToast.show();

            }
        });
        mViewModelAddCard.isLoading().observe(this, loading -> {
            if (loading){
                mProgress.setVisibility(View.VISIBLE);
            } else {
                mProgress.setVisibility(View.INVISIBLE);
            }
        });
        mViewModelAddCard.failure().observe(this, failure -> {
            if (failure){
                if (mToast != null)
                    mToast.cancel();
                mToast = Toast.makeText(getActivity(), R.string.connection_to_server_lost, Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
    }
}
