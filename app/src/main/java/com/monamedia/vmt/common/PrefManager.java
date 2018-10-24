package com.monamedia.vmt.common;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    // shared pref mode
    int PRIVATE_MODE = 0;

    private String PREF_NAME = "china_order";
    private String userId = "userId";
    private String userName = "userName";
    private String email = "email";
    private String firstName = "FirstName";
    private String lastName = "LastName";
    private String phone = "Phone";
    private String address = "Address";
    private String birthDay = "BirthDay";
    private String gender = "Gender";
    private String wallet = "Wallet";
    private String walletCYN = "WalletCYN";
    private String role = "Role";
    private String level = "Level";
    private String iMGUser = "IMGUser";
    private String regId = "regId";
    private String userPw = "userPw";
    private String Remember = "Remember";

    public PrefManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void clearData() {
        editor.clear();
        editor.commit();
    }

    public void setUserName(String s) {
        editor.putString(userName, s);
        editor.commit();
    }

    public String getUserName() {
        return pref.getString(userName, "");
    }

    public void setUserPw(String s) {
        editor.putString(userPw, s);
        editor.commit();
    }

    public String getUserPw() {
        return pref.getString(userPw, "");
    }

    public void setRemember(boolean s) {
        editor.putBoolean(Remember, s);
        editor.commit();
    }

    public boolean isRemember() {
        return pref.getBoolean(Remember, false);
    }

    // ID
    public void setUserId(int s) {
        editor.putInt(userId, s);
        editor.commit();
    }

    public int userId() {
        return pref.getInt(userId, 0);
    }

    // email
    public void setEmail(String s) {
        editor.putString(email, s);
        editor.commit();
    }

    public String email() {
        return pref.getString(email, "");
    }

    // FirstName
    public void setFirstName(String s) {
        editor.putString(firstName, s);
        editor.commit();
    }

    public String firstName() {
        return pref.getString(firstName, "");
    }

    // lastName
    public void setLastName(String s) {
        editor.putString(lastName, s);
        editor.commit();
    }

    public String lastName() {
        return pref.getString(lastName, "");
    }

    // phone
    public void setPhone(String s) {
        editor.putString(phone, s);
        editor.commit();
    }

    public String phone() {
        return pref.getString(phone, "");
    }

    // address
    public void setAddress(String s) {
        editor.putString(address, s);
        editor.commit();
    }

    public String address() {
        return pref.getString(address, "");
    }

    // birthDay
    public void setBirthDay(String s) {
        editor.putString(birthDay, s);
        editor.commit();
    }

    public String birthDay() {
        return pref.getString(birthDay, "");
    }

    // gender
    public void setGender(int s) {
        editor.putInt(gender, s);
        editor.commit();
    }

    public int gender() {
        return pref.getInt(gender, 0);
    }

    // wallet
    public void setWallet(String s) {
        editor.putString(wallet, s);
        editor.commit();
    }

    public String wallet() {
        return pref.getString(wallet, "0");
    }

    // walletCYN
    public void setWalletCYN(String s) {
        editor.putString(walletCYN, s);
        editor.commit();
    }

    public String walletCYN() {
        return pref.getString(walletCYN, "0");
    }

    // role
    public void setRole(int s) {
        editor.putInt(role, s);
        editor.commit();
    }

    public int role() {
        return pref.getInt(role, 0);
    }

    // level
    public void setLevel(String s) {
        editor.putString(level, s);
        editor.commit();
    }

    public String level() {
        return pref.getString(level, "");
    }

    // iMGUser
    public void setIMGUser(String s) {
        editor.putString(iMGUser, s);
        editor.commit();
    }

    public String iMGUser() {
        return pref.getString(iMGUser, "");
    }

    // regId
    public String getRegId() {
        return pref.getString(regId, "");
    }

    public void setRegId(String data) {
        editor.putString(regId, data);
        editor.commit();
    }
}
