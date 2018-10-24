package com.monamedia.vmt.controller.order;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.monamedia.vmt.R;
import com.monamedia.vmt.common.interfaces.Statics;
import com.monamedia.vmt.databinding.SiteOrderFragmentBinding;

/**
 * Created by Android on 4/11/2018.
 */

public class SiteOrderFragment extends Fragment implements View.OnClickListener {
    private SiteOrderFragmentBinding binding;

    public SiteOrderFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.site_order_fragment, container, false);
        View view = binding.getRoot();
        bind();
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == binding.ivTaobao) {
            details(Statics.taobao);
        } else if (v == binding.ivTmall) {
            details(Statics.tmall);
        } else if (v == binding.iv1688) {
            details(Statics.m1688);
        }
    }

    public void bind() {
        binding.ivTaobao.setOnClickListener(this);
        binding.ivTmall.setOnClickListener(this);
        binding.iv1688.setOnClickListener(this);
    }

    private void details(String site) {
        if (getParentFragment() instanceof OrderFragment) {
            ((OrderFragment) getParentFragment()).addFragment("", new OrderDetailsFragment(site));
        }
    }
}

