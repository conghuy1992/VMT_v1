package com.monamedia.vmt.controller.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.monamedia.vmt.LoginActivity;
import com.monamedia.vmt.R;
import com.monamedia.vmt.common.HttpRequest;
import com.monamedia.vmt.common.Utils;
import com.monamedia.vmt.common.interfaces.AccountCallBack;
import com.monamedia.vmt.common.interfaces.Statics;
import com.monamedia.vmt.databinding.ForgotFragmentBinding;
import com.monamedia.vmt.model.AccountDto;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Android on 4/11/2018.
 */

public class ForgotFragment extends Fragment implements View.OnClickListener {
    private String TAG = "ForgotFragment";
    private ForgotFragmentBinding binding;

    public ForgotFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.forgot_fragment, container, false);
        View view = binding.getRoot();
        bind();
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == binding.btnConfirm) {
            confirm();
        }
    }

    public void bind() {
        if (getActivity() != null && getActivity() instanceof LoginActivity) {
            ((LoginActivity) getActivity()).setToolbar(true, Utils.getMsg(getActivity(), R.string.forgot_pass));
        }
        binding.email.tvTitle.setText(R.string.email);
        binding.email.ed.setHint(R.string.email);

        binding.btnConfirm.setOnClickListener(this);
        stopLoading("");
    }

    private void startLoading() {
        binding.btnConfirm.setEnabled(false);
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    private void stopLoading(String msg) {
        if (getActivity() == null) return;
        binding.btnConfirm.setEnabled(true);
        binding.progressBar.setVisibility(View.GONE);
        if (msg != null && msg.length() > 0) Utils.showMsg(getActivity(), msg);
    }

    private void confirm() {
        String email = binding.email.ed.getText().toString().trim();
        if (email.length() == 0) {
            Utils.showMsg(getActivity(), Utils.getMsg(getActivity(), R.string.not_yet_input) + " " + Utils.getMsg(getActivity(), R.string.email));
            return;
        }

        startLoading();
        Map<String, String> params = new HashMap<>();
        params.put("Email", "" + email);
        Log.d(TAG, "ForgotPassword params:" + new Gson().toJson(params));
        new HttpRequest().ForgotPassword(getActivity(),
                Statics.POST,
                params,
                Statics.ForgotPassword,
                new AccountCallBack() {
                    @Override
                    public void onSuccess(AccountDto accountDto) {
                        if (getActivity() == null) return;
                        String msg = accountDto == null
                                || accountDto.Message == null
                                || accountDto.Message.length() == 0 ? "Success" : accountDto.Message;
                        stopLoading(msg);
                    }

                    @Override
                    public void onFail(AccountDto accountDto) {
                        if (getActivity() == null) return;
                        String msg = accountDto == null
                                || accountDto.Message == null
                                || accountDto.Message.length() == 0 ? "fail" : accountDto.Message;
                        stopLoading(msg);
                    }

                    @Override
                    public void onVolleyError(VolleyError error) {
                        if (getActivity() == null) return;
                        stopLoading(error.toString());
                    }
                });
    }
}

