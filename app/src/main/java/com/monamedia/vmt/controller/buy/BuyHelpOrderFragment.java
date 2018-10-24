package com.monamedia.vmt.controller.buy;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.monamedia.vmt.R;
import com.monamedia.vmt.common.CommonWebViewFragment;
import com.monamedia.vmt.common.PrefManager;
import com.monamedia.vmt.common.Utils;
import com.monamedia.vmt.databinding.BuyHelpOrderFragmentBinding;
import com.monamedia.vmt.model.MenuDto;

/**
 * Created by Android on 4/11/2018.
 */

public class BuyHelpOrderFragment extends Fragment implements View.OnClickListener {
    private BuyHelpOrderFragmentBinding binding;
    private MenuDto menuDto;

    public BuyHelpOrderFragment() {

    }

    @SuppressLint("ValidFragment")
    public BuyHelpOrderFragment(MenuDto menuDto) {
        this.menuDto = menuDto;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.buy_help_order_fragment, container, false);
        View view = binding.getRoot();
        bind();
        return view;
    }

    @Override
    public void onClick(View v) {

    }

    private CommonWebViewFragment commonWebViewFragment;

    public void bind() {
        commonWebViewFragment = new CommonWebViewFragment(menuDto.Link + new PrefManager(getActivity()).userId());
        Utils.addFragment(getChildFragmentManager(), "",
                R.id.root, commonWebViewFragment);
    }

    public boolean canGoBack() {
        return commonWebViewFragment.canGoBack();
    }
    public void goBack(){
        commonWebViewFragment.goBack();
    }
}

