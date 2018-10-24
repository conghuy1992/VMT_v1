package com.monamedia.vmt.controller.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.monamedia.vmt.LoginActivity;
import com.monamedia.vmt.R;
import com.monamedia.vmt.common.HttpRequest;
import com.monamedia.vmt.common.PrefManager;
import com.monamedia.vmt.common.Utils;
import com.monamedia.vmt.common.interfaces.AccountCallBack;
import com.monamedia.vmt.common.interfaces.DateCallback;
import com.monamedia.vmt.common.interfaces.Statics;
import com.monamedia.vmt.databinding.RegisterFragmentBinding;
import com.monamedia.vmt.model.AccountDto;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Android on 4/11/2018.
 */

public class RegisterFragment extends Fragment implements View.OnClickListener {
    private String TAG = "RegisterFragment";
    private RegisterFragmentBinding binding;

    public RegisterFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.register_fragment, container, false);
        View view = binding.getRoot();
        bind();
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == binding.birthday.btnCalendar) {
            birthdaySelect();
        } else if (v == binding.btnLogin) {
            ((LoginActivity) getActivity()).onBackPressed();
        } else if (v == binding.btnRegister) {
            register();
            Utils.hideKeyboard(getActivity());
        }
    }

    public void bind() {
        if (getActivity() != null && getActivity() instanceof LoginActivity) {
            ((LoginActivity) getActivity()).setToolbar(true, Utils.getMsg(getActivity(), R.string.register_account));
        }
        binding.name.tvTitle.setText(R.string.name);
        binding.phone.tvTitle.setText(R.string.phone);
        binding.email.tvTitle.setText(R.string.email);
        binding.pw.tvTitle.setText(R.string.password);
        binding.confirmPw.tvTitle.setText(R.string.confirm_password);
        binding.birthday.tvTitle.setText(R.string.birthday);
        binding.gender.tvTitle.setText(R.string.gender);

        binding.name.ed.setHint(R.string.username);
        binding.phone.ed.setHint(R.string.phone);
        binding.email.ed.setHint(R.string.email);
        binding.pw.ed.setHint(R.string.password);
        binding.confirmPw.ed.setHint(R.string.confirm_password);
//        android:inputType="textPassword"
//        binding.pw.ed.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//        binding.confirmPw.ed.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        Utils.typePassword(binding.pw.ed);
        Utils.typePassword(binding.confirmPw.ed);

        binding.birthday.tvBirthday.setText(Utils.formatTime(Calendar.getInstance().getTimeInMillis(), Statics.DATE_FORMAT_DD_MM_YYYY));

        binding.birthday.btnCalendar.setOnClickListener(this);
        binding.btnRegister.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);
        stopLoading("");
    }

    private void birthdaySelect() {
        Utils.showDateDialog(getActivity(), Calendar.getInstance(), new DateCallback() {
            @Override
            public void onDateSetected(Calendar calendar) {
                binding.birthday.tvBirthday.setText(Utils.formatTime(calendar.getTimeInMillis(), Statics.DATE_FORMAT_DD_MM_YYYY));
            }
        });
    }

    private void startLoading() {
        binding.btnRegister.setEnabled(false);
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    private void stopLoading(String msg) {
        if (getActivity() == null) return;
        binding.btnRegister.setEnabled(true);
        binding.progressBar.setVisibility(View.GONE);
        if (msg != null && msg.length() > 0) Utils.showMsg(getActivity(), msg);
    }

    private void register() {
        String username = binding.name.ed.getText().toString().trim();
        String phone = binding.phone.ed.getText().toString().trim();
        String email = binding.email.ed.getText().toString().trim();
        String password = binding.pw.ed.getText().toString().trim();
        String confirmPassword = binding.confirmPw.ed.getText().toString().trim();
        if (username.length() == 0) {
            Utils.showMsg(getActivity(), Utils.getMsg(getActivity(), R.string.not_yet_input) + " " + Utils.getMsg(getActivity(), R.string.name));
            return;
        }
        if (phone.length() == 0) {
            Utils.showMsg(getActivity(), Utils.getMsg(getActivity(), R.string.not_yet_input) + " " + Utils.getMsg(getActivity(), R.string.phone));
            return;
        }
        if (email.length() == 0) {
            Utils.showMsg(getActivity(), Utils.getMsg(getActivity(), R.string.not_yet_input) + " " + Utils.getMsg(getActivity(), R.string.email));
            return;
        }
        if (password.length() == 0) {
            Utils.showMsg(getActivity(), Utils.getMsg(getActivity(), R.string.not_yet_input) + " " + Utils.getMsg(getActivity(), R.string.password));
            return;
        }
        if (confirmPassword.length() == 0) {
            Utils.showMsg(getActivity(), Utils.getMsg(getActivity(), R.string.not_yet_input) + " " + Utils.getMsg(getActivity(), R.string.confirm_password));
            return;
        }
        if (!password.equals(confirmPassword)) {
            Utils.showMsg(getActivity(), Utils.getMsg(getActivity(), R.string.confirm_password) + " không đúng");
            return;
        }

        AccountDto accountDto = new AccountDto();
        accountDto.FirstName = "";
        accountDto.LastName = "";
        accountDto.Address = "";
        accountDto.Phone = phone;
        accountDto.Email = email;
        accountDto.BirthDay = binding.birthday.tvBirthday.getText().toString().trim();
        accountDto.Gender = binding.gender.male.isChecked() ? AccountDto.MALE : AccountDto.FEMALE;
        accountDto.UserName = username;
        accountDto.Password = password;
        accountDto.ConfirmPassword = confirmPassword;
        startLoading();
        Map<String, String> params = new HashMap<>();
        params.put("FirstName", "" + accountDto.FirstName);
        params.put("LastName", "" + accountDto.LastName);
        params.put("Address", "" + accountDto.Address);
        params.put("Phone", "" + accountDto.Phone);
        params.put("Email", "" + accountDto.Email);
        params.put("BirthDay", "" + accountDto.BirthDay);
        params.put("Gender", "" + accountDto.Gender);
        params.put("UserName", "" + accountDto.UserName);
        params.put("Password", "" + accountDto.Password);
        params.put("ConfirmPassword", "" + accountDto.ConfirmPassword);
        Log.d(TAG, "Register params:" + new Gson().toJson(params));
        new HttpRequest().Register(getActivity(),
                Statics.POST,
                params,
                Statics.Register,
                new AccountCallBack() {
                    @Override
                    public void onSuccess(AccountDto accountDto) {
                        if (getActivity() == null) return;
                        String msg = accountDto == null
                                || accountDto.Message == null
                                || accountDto.Message.length() == 0 ? "Success" : accountDto.Message;
                        stopLoading(msg);
//                        saveInfo(accountDto);
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

