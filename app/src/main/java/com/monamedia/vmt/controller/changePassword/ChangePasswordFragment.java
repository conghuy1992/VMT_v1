package com.monamedia.vmt.controller.changePassword;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.monamedia.vmt.R;
import com.monamedia.vmt.common.HttpRequest;
import com.monamedia.vmt.common.PrefManager;
import com.monamedia.vmt.common.Utils;
import com.monamedia.vmt.common.interfaces.AccountCallBack;
import com.monamedia.vmt.common.interfaces.Statics;
import com.monamedia.vmt.databinding.ChangePasswordFragmentBinding;
import com.monamedia.vmt.model.AccountDto;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Android on 4/11/2018.
 */

public class ChangePasswordFragment extends Fragment implements View.OnClickListener {
    private ChangePasswordFragmentBinding binding;
    private String TAG = "ChangePasswordFragment";
    public ChangePasswordFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.change_password_fragment, container, false);
        View view = binding.getRoot();
        bind();
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == binding.btnSave) {
            save();
        }
    }

    private void startLoading() {
        if (getActivity() == null) return;
        binding.btnSave.setEnabled(false);
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    private void stopLoading(String msg) {
        if (getActivity() == null) return;
        binding.btnSave.setEnabled(true);
        binding.progressBar.setVisibility(View.GONE);
        if (msg != null && msg.length() > 0) Utils.showMsg(getActivity(), msg);
    }

    public void bind() {
        stopLoading("");
        binding.btnSave.setOnClickListener(this);
    }

    private void save() {
        String pw = binding.edPassword.getText().toString();
        final String pwNew = binding.edPasswordNew.getText().toString();
        String pwConfirm = binding.edPasswordConfirm.getText().toString();
        if (pw.length() == 0) {
            Utils.showMsg(getActivity(), Utils.getMsg(getActivity(), R.string.not_yet_input) + " " + Utils.getMsg(getActivity(), R.string.password));
            return;
        }
        if (pwNew.length() == 0) {
            Utils.showMsg(getActivity(), Utils.getMsg(getActivity(), R.string.not_yet_input) + " " + Utils.getMsg(getActivity(), R.string.password_new));
            return;
        }
        if (pwConfirm.length() == 0) {
            Utils.showMsg(getActivity(), Utils.getMsg(getActivity(), R.string.not_yet_input) + " " + Utils.getMsg(getActivity(), R.string.password_confirm));
            return;
        }
        if (!pwNew.equals(pwConfirm)) {
            Utils.showMsg(getActivity(), Utils.getMsg(getActivity(), R.string.password_confirm) + " không đúng");
            return;
        }
        startLoading();
        Map<String, String> params = new HashMap<>();
        params.put("UID", "" + new PrefManager(getActivity()).userId());
        params.put("Password", "" + pw);
        params.put("NewPassword", "" + pwNew);
        params.put("ConfirmNewPassword", "" + pwConfirm);
        Log.d(TAG, "ChangePassword params:" + new Gson().toJson(params));
        new HttpRequest().ChangePassword(getActivity(),
                Statics.POST,
                params,
                Statics.ChangePassword,
                new AccountCallBack() {
                    @Override
                    public void onSuccess(AccountDto accountDto) {
                        if (getActivity() == null) return;
                        String msg = accountDto == null
                                || accountDto.Message == null
                                || accountDto.Message.length() == 0 ? "Success" : accountDto.Message;
                        stopLoading(msg);
                        // save pw new
                        new PrefManager(getActivity()).setUserPw(pwNew);
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

