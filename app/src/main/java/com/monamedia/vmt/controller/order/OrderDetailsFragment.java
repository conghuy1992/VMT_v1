package com.monamedia.vmt.controller.order;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.monamedia.vmt.R;
import com.monamedia.vmt.common.Utils;
import com.monamedia.vmt.controller.WebViewOrderFragment;
import com.monamedia.vmt.databinding.OrderDetailsFragmentBinding;

/**
 * Created by Android on 4/11/2018.
 */

public class OrderDetailsFragment extends Fragment implements View.OnClickListener {
    private OrderDetailsFragmentBinding binding;
    private String url = "";
    private WebViewOrderFragment webviewFragment;

    public OrderDetailsFragment() {

    }

    @SuppressLint("ValidFragment")
    public OrderDetailsFragment(String url) {
        this.url = url;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.order_details_fragment, container, false);
        View view = binding.getRoot();
        bind();
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == binding.btnBack) {
        } else if (v == binding.btnHeart) {
        } else if (v == binding.btnHome) {
        } else if (v == binding.btnCart) {
        } else if (v == binding.btnCategory) {
        } else if (v == binding.btnNotes) {
        } else if (v == binding.btnOrder) {
        }
    }

    public void bind() {
        webviewFragment = new WebViewOrderFragment(url);
        Utils.addFragment(getChildFragmentManager(),
                "",
                R.id.webView,
                webviewFragment);
        binding.btnBack.setOnClickListener(this);
        binding.btnHeart.setOnClickListener(this);
        binding.btnHome.setOnClickListener(this);
        binding.btnCart.setOnClickListener(this);
        binding.btnCategory.setOnClickListener(this);
        binding.btnNotes.setOnClickListener(this);
        binding.btnOrder.setOnClickListener(this);

    }
    private void orderSuccessDialog(){
        FragmentManager fm =getChildFragmentManager();
        OrderDialogFragment editNameDialogFragment = new OrderDialogFragment();
        editNameDialogFragment.show(fm, "OrderDialogFragment");
    }
    public boolean canGoBack() {
        return webviewFragment.canGoBack();
    }

    public void goBack() {
        if (webviewFragment.canGoBack()) {
            webviewFragment.goBack();
        }
    }
}

