package com.monamedia.vmt.controller.order;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.monamedia.vmt.R;
import com.monamedia.vmt.databinding.OrderDialogFragmentBinding;

/**
 * Created by Android on 4/11/2018.
 */

public class OrderDialogFragment extends DialogFragment implements View.OnClickListener{
    private OrderDialogFragmentBinding binding;

    public OrderDialogFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.order_dialog_fragment, container, false);
        View view = binding.getRoot();
        bind();
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v==binding.btncart){}
        else if(v==binding.btnContinueBuy){}

    }
    @Override
    public void onResume() {
        super.onResume();
        setLayoutParamsForDialog();
    }

    private void setLayoutParamsForDialog() {
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes(params);
    }
    public void bind() {
        binding.btncart.setOnClickListener(this);
        binding.btnContinueBuy.setOnClickListener(this);
    }
}

