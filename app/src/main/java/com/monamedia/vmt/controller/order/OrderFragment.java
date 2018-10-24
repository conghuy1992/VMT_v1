package com.monamedia.vmt.controller.order;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.monamedia.vmt.R;
import com.monamedia.vmt.common.Utils;
import com.monamedia.vmt.databinding.OrderFragmentBinding;

/**
 * Created by Android on 4/11/2018.
 */

public class OrderFragment extends Fragment implements View.OnClickListener {
    private OrderFragmentBinding binding;

    public OrderFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.order_fragment, container, false);
        View view = binding.getRoot();
        bind();
        return view;
    }

    @Override
    public void onClick(View v) {

    }

    public void bind() {
        addFragment("", new SiteOrderFragment());
    }

    public void addFragment(String msg, Fragment fragment) {
        Utils.addFragment(getChildFragmentManager(),
                msg,
                R.id.fr,
                fragment);
    }

    private Fragment getCurrentFragment() {
        return getChildFragmentManager().findFragmentById(R.id.fr);
    }

    public void goBack() {
        if (canGoBack()) {
            Fragment fm = getCurrentFragment();
            if (fm != null && fm instanceof OrderDetailsFragment)
                ((OrderDetailsFragment) fm).goBack();
        }
    }

    public boolean canGoBack() {
        Fragment fm = getCurrentFragment();
        if (fm != null && fm instanceof OrderDetailsFragment)
            if (((OrderDetailsFragment) fm).canGoBack()) return true;
        return false;
    }
}

