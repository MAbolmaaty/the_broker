package com.emupapps.the_broker.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.databinding.FragmentAdvancedSettingsBinding;
import com.emupapps.the_broker.utils.SharedPrefUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Locale;

import static com.emupapps.the_broker.utils.Constants.LOCALE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdvancedSettingsFragment extends Fragment {

    private static final String TAG = AdvancedSettingsFragment.class.getSimpleName();

    private FragmentAdvancedSettingsBinding mBinding;

    private BottomSheetDialog mDialogLanguage;

    public AdvancedSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAdvancedSettingsBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        String locale = SharedPrefUtil.getInstance(getActivity()).read(LOCALE,
                Locale.getDefault().getLanguage());
        if (locale.equals("ar")) {
            view.setRotation(-180);
            //mSelectedLanguage.setText(R.string.arabic);
        } else {
           // mSelectedLanguage.setText(R.string.english);
        }
        mDialogLanguage = new BottomSheetDialog(getActivity());
        mDialogLanguage.setContentView(R.layout.dialog_select_language);
        setLanguageDialog();
        return view;
    }

    public void setLanguage(){
        mDialogLanguage.show();
    }

    private void setLanguageDialog(){
        Window dialog = mDialogLanguage.getWindow();
        ImageView close = dialog.findViewById(R.id.action1);
        TextView title = dialog.findViewById(R.id.title);
        TextView english = dialog.findViewById(R.id.english);
        TextView arabic = dialog.findViewById(R.id.arabic);
        close.setImageResource(R.drawable.ic_close);
        close.setOnClickListener(v -> mDialogLanguage.cancel());
        title.setText(R.string.select_language);
        english.setOnClickListener(v -> {
            mDialogLanguage.cancel();
            SharedPrefUtil.getInstance(getActivity()).write(LOCALE, "en");
            //mSelectedLanguage.setText(R.string.english);
            getActivity().recreate();
        });
        arabic.setOnClickListener(v -> {
            mDialogLanguage.cancel();
            SharedPrefUtil.getInstance(getActivity()).write(LOCALE, "ar");
            //mSelectedLanguage.setText(R.string.arabic);
            getActivity().recreate();
        });
    }

    public static AdvancedSettingsFragment newInstance(){
        return new AdvancedSettingsFragment();
    }
}
