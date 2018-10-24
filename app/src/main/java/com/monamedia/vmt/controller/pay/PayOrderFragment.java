package com.monamedia.vmt.controller.pay;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.monamedia.vmt.R;
import com.monamedia.vmt.databinding.PayOrderFragmentBinding;

/**
 * Created by Android on 4/11/2018.
 */

public class PayOrderFragment extends Fragment implements View.OnClickListener{
    private PayOrderFragmentBinding binding;

    public PayOrderFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.pay_order_fragment, container, false);
        View view = binding.getRoot();
        bind();
        return view;
    }

    @Override
    public void onClick(View v) {

    }

    public void bind() {
//        ((GoOutArticleFragment) getParentFragment()).setCommonInfo(Utils.getMsg(getActivity(), R.string.go_out_article), false);
//        ((GoOutArticleFragment) getParentFragment()).setCommonInfo(Utils.getMsg(getActivity(), R.string.go_out_article), false,
//                true, Utils.getMsg(getActivity(), R.string.Filter));
//        ((GoOutArticleFragment) getParentFragment()).setMenuListenerCallBack(this);


        // add fragment
//        ((GoOutArticleFragment) getParentFragment()).addChild("RegisterFavoriteFragment", new RegisterFavoriteFragment());
    }
}

