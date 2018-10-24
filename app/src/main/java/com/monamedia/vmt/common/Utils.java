package com.monamedia.vmt.common;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.monamedia.vmt.R;
import com.monamedia.vmt.WebViewActivity;
import com.monamedia.vmt.common.interfaces.DateCallback;
import com.monamedia.vmt.common.interfaces.GetBitmapCallBack;
import com.monamedia.vmt.common.interfaces.Statics;
import com.monamedia.vmt.model.AccountDto;
import com.monamedia.vmt.model.CategoryModel;
import com.monamedia.vmt.model.MenuDto;
import com.monamedia.vmt.model.NotificationDto;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Utils {
    public static String TAG = "Utils";
    public static int getColor(Context context, int id) {
        return context.getResources().getColor(id);
    }

    public static String getMsg(Context context, int id) {
        if (context == null) return "";
        return context.getResources().getString(id);
    }

    public static void showMsg(Context context, String msg) {
        if (context != null) {
            try {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void showMsg(Context context, int id) {
        if (context != null) {
            try {
                Toast.makeText(context, getMsg(context, id), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static int getDimenInPx(Context context, int id) {
        return (int) context.getResources().getDimension(id);
    }

    public static void addFragment(FragmentManager fragmentManager, String addToBackStack,
                                   int resId, android.support.v4.app.Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (addToBackStack != null && addToBackStack.trim().length() > 0)
            transaction.addToBackStack(addToBackStack);
        transaction.replace(resId, fragment);
        transaction.commit();
    }

    public static void d(String TAG, String str) {
        if (str.length() > 4000) {
            Log.d(TAG, str.substring(0, 4000));
            d(TAG, str.substring(4000));
        } else
            Log.d(TAG, str);
    }

    public static boolean isNumber(String msg) {
        return msg.matches("-?\\d+(\\.\\d+)?");
    }

    public static String tmallSite = "tmall.com/item.htm?id";
    public static String tmallSiteHk = "tmall.hk/item.htm?id";
    public static String taobaoSite = "taobao.com/detail/detail.html?spm";
    public static String m1688 = "1688.com/offer/";

    public static boolean showMenu(String url) {
        if (url.contains(tmallSite)
                || url.contains(tmallSiteHk)
                || url.contains(taobaoSite)
                || url.contains(m1688))
            return true;
        return false;
    }

    public static int val_tmall = 1;
    public static int val_taobao = 2;
    public static int val_1688 = 3;

    public static int getSite(String url) {
        if (url.contains(tmallSite) || url.contains(tmallSiteHk)) return val_tmall;
        if (url.contains(taobaoSite)) return val_taobao;
        if (url.contains(m1688)) return val_1688;
        return 0;
    }

    public static List<CategoryModel> categoryModelList() {
        List<CategoryModel> list = new ArrayList<>();
        list.add(new CategoryModel(1, "Chọn danh mục"));
        list.add(new CategoryModel(2, "Áo nữ"));
        list.add(new CategoryModel(3, "Áo nam"));
        list.add(new CategoryModel(4, "Quần nữ"));
        list.add(new CategoryModel(5, "Quần nam"));
        list.add(new CategoryModel(6, "Quần áo trẻ em"));
        list.add(new CategoryModel(7, "váy"));
        list.add(new CategoryModel(8, "Giày nam"));
        list.add(new CategoryModel(9, "Giày nữ"));
        list.add(new CategoryModel(10, "Giày trẻ em"));
        list.add(new CategoryModel(11, "Phụ kiện thời trang"));
        list.add(new CategoryModel(12, "Túi xách"));
        list.add(new CategoryModel(13, "Ví"));
        list.add(new CategoryModel(14, "Mỹ phẩm"));
        list.add(new CategoryModel(15, "Vải vóc"));
        list.add(new CategoryModel(16, "Tóc giả"));
        list.add(new CategoryModel(17, "Đồ chơi"));
        list.add(new CategoryModel(18, "Trang sức"));
        list.add(new CategoryModel(19, "Phụ tùng ô tô, xe máy"));
        list.add(new CategoryModel(20, "Thiết bị điện tử"));
        list.add(new CategoryModel(21, "Linh kiện điện tử"));
        list.add(new CategoryModel(22, "Phụ kiện điện tử"));
        list.add(new CategoryModel(23, "Sách báo, tranh ảnh, đồ sưu tập"));
        list.add(new CategoryModel(24, "Quà tặng"));
        list.add(new CategoryModel(25, "Đồ gia dụng"));
        list.add(new CategoryModel(-1, "Khác"));
        return list;
    }

    public static int getElementsByClassName = 1;
    public static int getElementsByTagName = 2;

    public static String convertTextToVietnamese(int type, String label) {
        String list = "";
        if (type == getElementsByTagName) {
            list = "var priceEls = document.getElementsByTagName(\"" + label + "\");";
        } else {
            // getElementsByClassName
            list = "var priceEls = document.getElementsByClassName(\"" + label + "\");";
        }
        return list +
                "if(priceEls != null && priceEls.length > 0)" +
                "{" +
                "   for (var i = 0; i < priceEls.length; i++) " +
                "   {" +
                "       if(priceEls[i].innerText == \"" + Statics.CN_color + "\" || priceEls[i].innerText == \"" + Statics.CN_color_v2 + "\")" +
                "           priceEls[i].innerHTML = \"" + Statics.VN_color + "\";" +
                "       else if(priceEls[i].innerText == \"" + Statics.CN_number + "\")" +
                "           priceEls[i].innerHTML = \"" + Statics.VN_number + "\";" +
                "       else if(priceEls[i].innerText == \"" + Statics.CN_size + "\")" +
                "           priceEls[i].innerHTML = \"" + Statics.VN_size + "\";" +
                "   }" +
                "}";
    }

    public static void showDateDialog(final Context context, final Calendar calendar, final DateCallback callback) {
        DatePickerDialog dpd = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, monthOfYear);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        callback.onDateSetected(cal);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dpd.show();
    }

    public static void showTimeDialog(Context context, final Calendar calendar, final DateCallback callback) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        cal.set(Calendar.MINUTE, minute);
                        callback.onDateSetected(cal);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }

    public static String formatTime(long date, String defaultPattern) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(defaultPattern, Locale.getDefault());
            return simpleDateFormat.format(new Date(date));
        } catch (Exception e) {
            return "";
        }
    }

    public static void typePassword(EditText ed) {
        if (ed == null) return;
        ed.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    public static boolean isResponseObject(String response) {
        if (response.startsWith("\ufeff"))
            return true;
        if (response.startsWith("{"))
            return true;
        return false;
    }

    public static void hideKeyboard(Activity activity) {
        try {
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveInfo(Context context, AccountDto accountDto) {
        PrefManager prefManager = new PrefManager(context);
        String token = prefManager.getRegId();
        prefManager.clearData();
        prefManager.setRegId(token);
        prefManager.setUserId(accountDto.ID);
        prefManager.setUserName(accountDto.UserName == null ? "" : accountDto.UserName);
        prefManager.setEmail(accountDto.Email == null ? "" : accountDto.Email);
        prefManager.setFirstName(accountDto.FirstName == null ? "" : accountDto.FirstName);
        prefManager.setLastName(accountDto.LastName == null ? "" : accountDto.LastName);
        prefManager.setPhone(accountDto.Phone == null ? "" : accountDto.Phone);
        prefManager.setAddress(accountDto.Address == null ? "" : accountDto.Address);
        prefManager.setBirthDay(accountDto.BirthDay == null ? "" : accountDto.BirthDay);
        prefManager.setGender(accountDto.Gender);
        prefManager.setWallet(accountDto.Wallet);
        prefManager.setWalletCYN(accountDto.WalletCYN);
        prefManager.setRole(accountDto.Role);
        prefManager.setLevel(accountDto.Level == null ? "" : accountDto.Level);
        prefManager.setIMGUser(accountDto.IMGUser == null ? "" : accountDto.IMGUser);
    }

    public static View createItem(Context context, String title) {
        View v = LayoutInflater.from(context).inflate(R.layout.tab_layout_custom, null);
        TextView tvTitle = (TextView) v.findViewById(R.id.tv);
        tvTitle.setText(title);
//        ImageView imgIcon = (ImageView) v.findViewById(R.id.imgIcon);
//        imgIcon.setImageResource(icon);
//        TextView tvNotify = (TextView) v.findViewById(R.id.tvNotify);
//        tvNotify.setVisibility(View.GONE);
//        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        return v;
    }

    public static long getTimeMilisecondFromDate(String myDateString, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
            Date date = sdf.parse(myDateString);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            return calendar.getTimeInMillis();
        } catch (Exception e) {
            return 0;
        }
    }

    public static void loadImageFromURL(final Context context, String url, final ImageView iv) {
        if (context == null || iv == null) return;

        if (url != null && url.trim().length() > 0) {
            Log.d(TAG, "url:" + url);
            Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            // call callback when loading error
                            if (context != null) {
                                Log.d(TAG,"onException");
                                iv.setImageResource(R.drawable.ic_launcher_background);
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            // call callback when loading success
                            return false;
                        }
                    })
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            if (context != null) {
                                Log.d(TAG,"onResourceReady");
                                DisplayMetrics metrics = new DisplayMetrics();
                                ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
                                int width = metrics.widthPixels;
                                int w = resource.getWidth();
                                int h = resource.getHeight();
                                if (width != 0) {
                                    float percent = (width * 1f) / (w * 1f);
                                    w = width;
                                    h = (int) (h * percent);
//                                Log.d(TAG, "after resize w:" + w + " h:" + h);
                                    resource = Bitmap.createScaledBitmap(resource, w, h, false);
                                }
//                                iv.setImageResource(R.drawable.ic_calendar);
                                iv.setImageBitmap(resource);
                            }
                        }
                    });
        } else {
            Log.d(TAG,"url null");
            if (context != null) iv.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    public static MenuDto accountChild(Context context, String msg, int id, String link) {
        MenuDto mn = new MenuDto();
        mn.Parent = 1;
        mn.ItemName = msg;
        mn.GroupID = id;
        mn.Link = link;
        return mn;
    }
    public static MenuDto HOME(){
        MenuDto mn = new MenuDto();
        mn.ItemName = "Trang chủ";
        mn.type = MenuDto.MENU;
        mn.GroupID = MenuDto.HOME;
        return mn;
    }
    public static MenuDto ACCOUNT_MANAGER(Context context, AccountDto accountDto) {
        MenuDto mn = new MenuDto();
        mn.ItemName = Utils.getMsg(context, R.string.account_manager);
        mn.type = MenuDto.PARENT;
        mn.GroupID = MenuDto.ACCOUNT_MANAGER;
        mn.ListMenu = new ArrayList<>();
        mn.ListMenu.add(accountChild(context, Utils.getMsg(context, R.string.account_info), MenuDto.ACCOUNT_INFO, ""));
        if (accountDto != null && accountDto.Menu != null) {
            for (MenuDto menuDto : accountDto.Menu) {
                mn.ListMenu.add(accountChild(context, menuDto.ItemName, 0, menuDto.Link));
            }
        }

        mn.ListMenu.add(accountChild(context, Utils.getMsg(context, R.string.change_pass), MenuDto.CHANGE_PASS, ""));
        return mn;
    }

    public static void showNotification(Context context, final NotificationDto obj) {
        String chanelId = Utils.getMsg(context, R.string.app_name);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        Intent myIntent = new Intent(context, WebViewActivity.class);
        myIntent.putExtra(MenuDto.KEY, obj.link);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        final long[] vibrate = new long[]{800, 800, 0, 0, 0};
        final Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        final PendingIntent contentIntent = PendingIntent.getActivity(context, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        int smallIcon = R.drawable.ic_noti_android_6;
        boolean isN = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            isN = true;
        }

        if (isN) {
            smallIcon = R.drawable.ic_noti_android_7;
            mBuilder.setColor(context.getResources().getColor(R.color.colorPrimary));
        }

        mBuilder.setSmallIcon(smallIcon)
                .setLargeIcon(obj.bitmap)
                .setContentTitle(obj.title)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText("bigText"))
                .setContentText(obj.message)
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(true);

        // Check notification setting and config notification
//                    if (isEnableSound)
        mBuilder.setSound(soundUri);
//                    if (isEnableVibrate)
        mBuilder.setVibrate(vibrate);
        mBuilder.setContentIntent(contentIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder.setChannelId(chanelId);
            /* Create or update. */
            NotificationChannel channel = new NotificationChannel(chanelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }

        Notification notification = mBuilder.build();


        notification.number = 100;

        int idNoti = new Random().nextInt(1000);
        mNotificationManager.notify(idNoti, notification);
        mNotificationManager.notify(idNoti, mBuilder.build());
    }

    public static void getBitmap(final Context context, final NotificationDto obj) {
        new DownloadImageTask(obj.image, new GetBitmapCallBack() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                if (bitmap == null) {
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
                }
                obj.bitmap = bitmap;
                Utils.showNotification(context, obj);
            }
        }).execute();
    }

    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private String avatarUrl;
        private GetBitmapCallBack calback;

        public DownloadImageTask(String avatarUrl, GetBitmapCallBack calback) {
            this.avatarUrl = avatarUrl;
            this.calback = calback;

        }

        Bitmap bitmapOrg = null;

        protected Bitmap doInBackground(String... urls) {

            if (avatarUrl == null || avatarUrl.length() == 0) return null;
            try {
                URL url = new URL(avatarUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmapOrg = BitmapFactory.decodeStream(input);

                bitmapOrg = bitmapOrg.createScaledBitmap(bitmapOrg, 100, 100, false);
            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmapOrg;
        }

        protected void onPostExecute(final Bitmap result) {
            calback.onSuccess(result);
        }
    }
    public static void setHtmlText(TextView tv,String bodyData){
        if(bodyData==null)bodyData="";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            tv.setText(Html.fromHtml(bodyData,Html.FROM_HTML_MODE_LEGACY));
        } else {
            tv.setText(Html.fromHtml(bodyData));
        }
    }
}
