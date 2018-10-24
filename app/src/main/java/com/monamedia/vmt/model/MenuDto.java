package com.monamedia.vmt.model;

import java.util.List;

public class MenuDto extends CommonModel {
    public static int CHILDREN = 0;
    public static int PARENT = 1;
    public static int MENU = 2;
    public static int INFO = -99;

    public static String KEY = "MenuDto";
    // menu function
    public static int ACCOUNT_MANAGER = 101;
    public static int ACCOUNT_INFO = 102;
    public static int HOME = 103;
    public static int CHANGE_PASS = 104;



    public List<MenuDto> ListMenu;
    public String ItemName;
    public int GroupID;
    public int Parent;
    public String Link;
    public int ShowType;
    public int type;
    public boolean isExistChild;

}
