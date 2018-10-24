package com.monamedia.vmt.model;

import java.util.List;

public class AccountDto extends CommonModel {
    public static int MALE = 1;
    public static int FEMALE = 0;
    public static String NAM = "Nam";
    public static String NU = "Nữ";
    public static String KEY = "AccountDto";
    public String FirstName;
    public String LastName;
    public String Address;
    public String Phone;
    public String Email;
    public String BirthDay;
    public int Gender;
    public String UserName;
    public String Username;
    public String Password;
    public String ConfirmPassword;
    public int ID;
    public String Wallet;
    public String WalletCYN;
    public int Role;
    public String Level;
    public String IMGUser;
    public AccountDto Account;
    public AccountDto Info;
    public float Currency;
    public List<MenuDto> Menu;

    public static String getName(int type) {
        if (type == MALE) return NAM;
        return NU;
    }

    public static int getGender(String str) {
        if (str.equals(NAM)) return MALE;
        return FEMALE;
    }
}