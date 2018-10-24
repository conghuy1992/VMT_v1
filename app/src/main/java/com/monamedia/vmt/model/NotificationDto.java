package com.monamedia.vmt.model;

import android.graphics.Bitmap;

import java.util.List;

public class NotificationDto extends CommonModel {
    public int NotificationID;
    public String Link;
    public int Type;
    public int type;
    public String link;
    public String image;
    public String title;
    public String message;
    public Bitmap bitmap;
    public List<NotificationDto> ListNoti;
}
