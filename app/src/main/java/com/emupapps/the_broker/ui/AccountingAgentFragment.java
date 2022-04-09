package com.emupapps.the_broker.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.adapters.AccountingAgentAdapter;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.Locale;

import static com.emupapps.the_broker.utils.Constants.LOCALE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountingAgentFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    ImageView mBack;
    TextView mTitle;
    ImageView mArrow;
    RecyclerView mRecyclerView;
    TextView mFrom;
    TextView mTo;

    private String mDate;

    public AccountingAgentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_accounting_agent, container, false);
        String locale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE,
                Locale.getDefault().getLanguage());
        if (locale.equals("ar")) {
            mBack.setImageResource(R.drawable.ic_arrow_ar);
            mArrow.setImageResource(R.drawable.ic_arrow_3_ar);
        } else {
            mBack.setImageResource(R.drawable.ic_arrow);
            mArrow.setImageResource(R.drawable.ic_arrow_3);
        }
        mTitle.setText(R.string.details_accounting);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        loadAccounting();
        return view;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
        if (mDate.equals("FROM")){
            mFrom.setText(date);
        } else if (mDate.equals("TO")){
            mTo.setText(date);
        }
    }

    public void back() {
        getActivity().onBackPressed();
    }

    public void from(){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setLocale(Locale.ENGLISH);
        datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
        mDate = "FROM";
    }

    public void to(){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setLocale(Locale.ENGLISH);
        datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
        mDate = "TO";
    }

    private void loadAccounting() {
        AccountingAgentAdapter adapter;
        adapter = new AccountingAgentAdapter(getActivity(), position -> {
        }, position -> {
        });
        mRecyclerView.setAdapter(adapter);
    }
}
