package com.monamedia.vmt.controller.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.monamedia.vmt.LoginActivity;
import com.monamedia.vmt.MainActivity;
import com.monamedia.vmt.R;
import com.monamedia.vmt.common.HttpRequest;
import com.monamedia.vmt.common.PrefManager;
import com.monamedia.vmt.common.Utils;
import com.monamedia.vmt.common.interfaces.AccountCallBack;
import com.monamedia.vmt.common.interfaces.Statics;
import com.monamedia.vmt.controller.FCM.Config;
import com.monamedia.vmt.databinding.LoginFragmentBinding;
import com.monamedia.vmt.model.AccountDto;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Android on 4/11/2018.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {
    private String TAG = "LoginFragment";
    private LoginFragmentBinding binding;

    public LoginFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.login_fragment, container, false);
        View view = binding.getRoot();
        bind();
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == binding.btnLogin) {
            login();
            Utils.hideKeyboard(getActivity());
        } else if (v == binding.btnForgot) {
            forgot();
        } else if (v == binding.btnRegister) {
            register();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveDB();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mRegistrationBroadcastReceiver);
    }
    private void keepLogin(boolean flag)
    {
        if(flag)binding.frLoading.setVisibility(View.VISIBLE);
        else binding.frLoading.setVisibility(View.GONE);
    }
    public void bind() {
        PrefManager prefManager = new PrefManager(getActivity());
        boolean isSave = prefManager.isRemember();
        String userName = prefManager.getUserName();
        String pw = prefManager.getUserPw();
        if (isSave && userName.trim().length() > 0 && pw.length() > 0) {
            keepLogin(true);
            binding.edUsername.setText(prefManager.getUserName());
            binding.edPassword.setText(prefManager.getUserPw());
            binding.cbRemember.setChecked(isSave);
            login();
        } else {
            keepLogin(false);
            if (getActivity() != null && getActivity() instanceof LoginActivity) {
                ((LoginActivity) getActivity()).setToolbar(false, Utils.getMsg(getActivity(), R.string.login));
            }
            FCM();
            binding.btnLogin.setOnClickListener(this);
            binding.btnRegister.setOnClickListener(this);
            binding.btnForgot.setOnClickListener(this);

            Utils.typePassword(binding.edPassword);
            stopLoading("");
            loadDB();
        }
    }

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    void FCM() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "onReceive:" + intent.toString());
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    String message = intent.getStringExtra("message");
                    if (message == null) message = "";
//                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                    Log.d(TAG, "message:" + message);
                }
            }
        };

        displayFirebaseRegId();
    }

    private void displayFirebaseRegId() {
        String regId = new PrefManager(getActivity()).getRegId();
        Log.d(TAG, "Firebase reg id: " + regId);
        // update regId

        if (!TextUtils.isEmpty(regId)) {
//            txtRegId.setText("Firebase Reg Id: " + regId);
            Log.d(TAG, "Firebase Reg Id: " + regId);
        } else {
            Log.d(TAG, "Firebase Reg Id is not received yet!");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
        // clear the notification area when the app is opened
//        NotificationUtils.clearNotifications(getApplicationContext());
    }


    private void loadDB() {
        PrefManager prefManager = new PrefManager(getActivity());
        boolean isSave = prefManager.isRemember();
        if (isSave) {
            binding.edUsername.setText(prefManager.getUserName());
            binding.edPassword.setText(prefManager.getUserPw());
            binding.cbRemember.setChecked(isSave);
        }
    }

    private void saveDB() {
        if (getActivity() == null) return;
        PrefManager prefManager = new PrefManager(getActivity());
        if (binding.cbRemember.isChecked()) {
            prefManager.setRemember(true);
            prefManager.setUserName(binding.edUsername.getText().toString().trim());
            prefManager.setUserPw(binding.edPassword.getText().toString());
        } else {
            prefManager.setRemember(false);
            prefManager.setUserName("");
            prefManager.setUserPw("");
        }
    }

    private void register() {
        ((LoginActivity) getActivity()).addFragment("RegisterFragment", new RegisterFragment());
    }

    private void forgot() {
        ((LoginActivity) getActivity()).addFragment("ForgotFragment", new ForgotFragment());
    }

    private void startLoading() {
        binding.btnLogin.setEnabled(false);
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    private void stopLoading(String msg) {
        if (getActivity() == null) return;
        binding.btnLogin.setEnabled(true);
        binding.progressBar.setVisibility(View.GONE);
        if (msg != null && msg.length() > 0) Utils.showMsg(getActivity(), msg);
    }

    private void login() {
        String username = binding.edUsername.getText().toString().trim();
        String password = binding.edPassword.getText().toString().trim();
        if (username.length() == 0) {
            Utils.showMsg(getActivity(), Utils.getMsg(getActivity(), R.string.not_yet_input) + " " + Utils.getMsg(getActivity(), R.string.username));
            return;
        }
        if (password.length() == 0) {
            Utils.showMsg(getActivity(), Utils.getMsg(getActivity(), R.string.not_yet_input) + " " + Utils.getMsg(getActivity(), R.string.password));
            return;
        }
        startLoading();
        Map<String, String> params = new HashMap<>();
        params.put("Username", "" + username);
        params.put("Password", "" + password);
        params.put("Type", "1");
        params.put("DeviceToken", new PrefManager(getActivity()).getRegId());
        params.put("TypeName", "Android ");
        Log.d(TAG, "Login params:" + new Gson().toJson(params));
        new HttpRequest().Login(getActivity(),
                Statics.POST,
                params,
                Statics.Login,
                new AccountCallBack() {
                    @Override
                    public void onSuccess(AccountDto accountDto) {
                        if (getActivity() == null) return;
//                        keepLogin(false);
//                        String msg = accountDto == null
//                                || accountDto.Message == null
//                                || accountDto.Message.length() == 0 ? "Success" : accountDto.Message;
//                        stopLoading(msg);
                        Utils.saveInfo(getActivity(), accountDto);
                        launchMain(accountDto);
                    }

                    @Override
                    public void onFail(AccountDto accountDto) {
                        if (getActivity() == null) return;
                        keepLogin(false);
                        String msg = accountDto == null
                                || accountDto.Message == null
                                || accountDto.Message.length() == 0 ? "fail" : accountDto.Message;
                        stopLoading(msg);
                    }

                    @Override
                    public void onVolleyError(VolleyError error) {
                        if (getActivity() == null) return;
                        keepLogin(false);
                        stopLoading(error.toString());
                    }
                });
    }

    private void launchMain(AccountDto accountDto) {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra(accountDto.KEY, accountDto);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
        getActivity().finish();
    }
}

