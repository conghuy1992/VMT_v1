package com.monamedia.vmt.controller.menu;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.monamedia.vmt.MainActivity;
import com.monamedia.vmt.R;
import com.monamedia.vmt.common.HttpRequest;
import com.monamedia.vmt.common.PrefManager;
import com.monamedia.vmt.common.Utils;
import com.monamedia.vmt.common.interfaces.AccountCallBack;
import com.monamedia.vmt.common.interfaces.Statics;
import com.monamedia.vmt.databinding.MenuProfileFragmentBinding;
import com.monamedia.vmt.model.AccountDto;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Android on 4/11/2018.
 */

public class MenuProfileFragment extends Fragment implements View.OnClickListener {
    private MenuProfileFragmentBinding binding;
    private String TAG = "MenuProfileFragment";

    public MenuProfileFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.menu_profile_fragment, container, false);
        View view = binding.getRoot();
        bind();
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == binding.content.btnReload) {
            Get_Info();
        }
    }

    public void bind() {
        binding.info.setVisibility(View.GONE);
        binding.load.setVisibility(View.VISIBLE);
        refreshProfile();
        binding.content.btnReload.setOnClickListener(this);
        stopLoading("");
//        Get_Info();
    }

    public void refreshProfile() {
        PrefManager prefManager = new PrefManager(getActivity());
        String iMGUser = prefManager.iMGUser();
        if (iMGUser.trim().length() > 0) {
            Log.d(TAG,"iMGUser > 0");
            Utils.loadImageFromURL(getActivity(), Statics.ROOT_URL_FOR_IMAGE + iMGUser, binding.iv);
        } else {
            Log.d(TAG,"iMGUser == 0");
            binding.iv.setImageResource(R.drawable.ic_launcher_background);
        }

        String name = prefManager.firstName() + " ";
        name += prefManager.lastName();
        binding.name.setText(name);
        binding.amount.setText("" + prefManager.wallet());
        binding.level.setText(prefManager.level());
    }

    private void startLoading() {
        binding.content.btnReload.setEnabled(false);
        binding.content.tvNodata.setVisibility(View.GONE);
        binding.content.btnReload.setVisibility(View.GONE);
        binding.content.progressBar.setVisibility(View.VISIBLE);
    }

    private void stopLoading(String msg) {
        if (getActivity() == null) return;
        binding.content.btnReload.setEnabled(true);
        binding.content.tvNodata.setVisibility(View.GONE);
        binding.content.btnReload.setVisibility(View.GONE);
        binding.content.progressBar.setVisibility(View.GONE);
        if (msg != null && msg.length() > 0) Utils.showMsg(getActivity(), msg);
    }

    private void Get_Info() {
        startLoading();
        Map<String, String> params = new HashMap<>();
        params.put("UID", "" + new PrefManager(getActivity()).userId());
        Log.d(TAG, "Get_Info params:" + new Gson().toJson(params));
        new HttpRequest().Get_Info(getActivity(),
                Statics.POST,
                params,
                Statics.Get_Info,
                new AccountCallBack() {
                    @Override
                    public void onSuccess(AccountDto accountDto) {
                        if (getActivity() == null) return;
//                        String msg = accountDto == null
//                                || accountDto.Message == null
//                                || accountDto.Message.length() == 0 ? "Success" : accountDto.Message;
                        firstLaunch = true;
                        stopLoading("");
                        displayInfo(accountDto);
                    }

                    @Override
                    public void onFail(AccountDto accountDto) {
                        if (getActivity() == null) return;
//                        String msg = accountDto == null
//                                || accountDto.Message == null
//                                || accountDto.Message.length() == 0 ? "fail" : accountDto.Message;
                        stopLoading("");
                        if (!firstLaunch) {
                            binding.content.tvNodata.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onVolleyError(VolleyError error) {
                        if (getActivity() == null) return;
                        if (!firstLaunch) {
                            stopLoading(error.toString());
                            binding.content.btnReload.setVisibility(View.VISIBLE);
                        } else {
                            stopLoading("");
                        }
                    }
                });
    }

    private void displayInfo(AccountDto accountDto) {
        accountDto.UserName = accountDto.Username;
        binding.info.setVisibility(View.VISIBLE);
        binding.load.setVisibility(View.GONE);

//        String IMGUser = accountDto.IMGUser == null ? "" : accountDto.IMGUser;
//        new PrefManager(getActivity()).setIMGUser(IMGUser);

        // update Currency
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setTextCurrency("" + accountDto.Currency);
        }

        refreshProfile();
//        String name = accountDto.FirstName == null ? "" : accountDto.FirstName + " ";
//        name += accountDto.LastName == null ? "" : accountDto.LastName;
//        binding.name.setText(name);
//        binding.amount.setText("" + accountDto.Wallet);
//        binding.level.setText(accountDto.Level == null ? "" : accountDto.Level);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        startThread();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        stopThread();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    private Handler handler = new Handler();
    private int timeDelay = 60000;
    private boolean firstLaunch = false;

    private void stopThread() {
        handler.removeCallbacks(runnable);
    }

    private void startThread() {
        handler.post(runnable);
    }

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            Get_Info();
            handler.postDelayed(runnable, timeDelay);
        }
    };

}

