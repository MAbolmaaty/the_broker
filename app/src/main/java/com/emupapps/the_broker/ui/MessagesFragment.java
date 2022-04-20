package com.emupapps.the_broker.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.databinding.FragmentMessagesBinding;
import com.emupapps.the_broker.utils.SoftKeyboard;

import static com.emupapps.the_broker.ui.MainActivity.sDrawerLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesFragment extends Fragment {

    private FragmentMessagesBinding mBinding;

    public MessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentMessagesBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
//        mMenu.setImageResource(R.drawable.ic_menu);
//        mTitle.setText(R.string.messages);

        mBinding.messagesOpenMenu.setOnClickListener(new View.OnClickListener() {
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
}
