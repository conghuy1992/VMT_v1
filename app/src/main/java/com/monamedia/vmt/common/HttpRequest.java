package com.monamedia.vmt.common;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.monamedia.vmt.R;
import com.monamedia.vmt.common.interfaces.AccountCallBack;
import com.monamedia.vmt.common.interfaces.CommonModelCallBack;
import com.monamedia.vmt.common.interfaces.GetResponseCallBack;
import com.monamedia.vmt.common.interfaces.HttpRequestCallBack;
import com.monamedia.vmt.common.interfaces.MenuCallBack;
import com.monamedia.vmt.common.interfaces.NotificationCallBack;
import com.monamedia.vmt.common.interfaces.Statics;
import com.monamedia.vmt.model.AccountDto;
import com.monamedia.vmt.model.CommonModel;
import com.monamedia.vmt.model.HttpRequestDto;
import com.monamedia.vmt.model.MenuDto;
import com.monamedia.vmt.model.NotificationDto;
import com.monamedia.vmt.model.ProductModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class HttpRequest {
    static String TAG = "HttpRequest";

    public void getResponse(final Context context,
                            final int method,
                            final Map<String, String> params,
                            final String url,
                            final GetResponseCallBack callBack) {
        StringRequest stringRequest = new StringRequest(method, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) response = "";
                        if (callBack != null) callBack.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callBack.onVolleyError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
//                params.put("datework", datework);
//                params.put("StoreID", ""+new PrefManager(context).getStoreId());

                //returning parameter
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Statics.TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public static void tmall(final String html) {
        new AsyncTask<String, String, ProductModel>() {

            @Override
            protected ProductModel doInBackground(String... strings) {
                final ProductModel productModel = new ProductModel();
                Document document = Jsoup.parseBodyFragment(html);
                // name
                Elements mainCell = document.getElementsByClass("main cell");
                if (mainCell != null && mainCell.size() > 0) {
                    productModel.name = mainCell.get(0).text();
                }
                // qtyBuy
                Elements input = document.getElementsByTag("input");
                if (input != null && input.size() > 0) {
                    for (Element i : input) {
                        if (i.attr("id").equals("number")) {
                            if (Utils.isNumber(i.val())) {
//                                Log.d(TAG, "id:" + i.val());
                                productModel.qtyBuy = Integer.parseInt(i.val());
                            }
                            break;
                        }
                    }
                }
                //properties
                Elements skuListWrap = document.getElementsByClass("sku-list-wrap");
                if (skuListWrap != null && skuListWrap.size() > 0) {
                    for (Element sku : skuListWrap) {
                        Elements items = sku.getElementsByClass("items");
                        if (items != null && items.size() > 0) {
                            for (Element i : items) {
                                Elements checked = i.getElementsByClass("checked");
                                if (checked != null && checked.size() > 0) {
                                    for (Element c : checked) {
                                        if (c.text() != null && c.text().length() > 0)
                                            productModel.properties += c.text() + ", ";
                                    }
                                } else {
                                    // no select full properties
                                    productModel.isFullProperties = false;
                                    break;
                                }

                            }

                        }
                    }
                }

                // skuWrap
                Elements skuWrap = document.getElementsByClass("sku-wrap");
                if (skuWrap != null && skuWrap.size() > 0) {
                    // image
                    Elements imgWrap = skuWrap.get(0).getElementsByClass("img-wrap");
                    if (imgWrap != null && imgWrap.size() > 0) {
                        Elements img = imgWrap.get(0).getElementsByTag("img");
                        if (img != null && img.size() > 0) {
                            productModel.image = img.get(0).attr("src");
                        }
                    }
                    // price
                    Elements price = skuWrap.get(0).getElementsByClass("price");
                    if (price != null && price.size() > 0) {
                        if (price.text().trim().length() > 0) {
                            String p = price.text().trim().substring(1, price.text().trim().length());
                            if (Utils.isNumber(p)) {
                                productModel.price = p;
                            }
                        }
                    }
                }
                return productModel;
            }

            @Override
            protected void onPostExecute(ProductModel productModel) {
                super.onPostExecute(productModel);
                if (productModel != null
                        && productModel.properties != null
                        && productModel.properties.length() > 0)
                    productModel.properties = productModel.properties.substring(
                            0, productModel.properties.length() - 2);
                Log.d(TAG, "onPostExecute:" + new Gson().toJson(productModel));
            }
        }.execute();
    }

    public static void taobao(final String html) {
//        Utils.d(TAG, html);

        new AsyncTask<String, String, ProductModel>() {

            @Override
            protected ProductModel doInBackground(String... strings) {
                final ProductModel productModel = new ProductModel();
                Document document = Jsoup.parseBodyFragment(html);
                Elements skuNumberEdit = document
                        .select("input[class=sku-number-edit]");
                if (skuNumberEdit != null && skuNumberEdit.size() > 0) {
//                    Log.d(TAG, "skuNumberEdit:" + skuNumberEdit.get(0).val());
                    if (Utils.isNumber(skuNumberEdit.get(0).val())) {
                        productModel.qtyBuy = Integer.parseInt(skuNumberEdit.get(0).val());
                    }
                }
                Elements muiLazySlickImage = document.getElementsByClass("mui-lazy slick-image");
                if (muiLazySlickImage != null && muiLazySlickImage.size() > 0) {
                    productModel.image = muiLazySlickImage.get(0).attr("src");
//                    Log.d(TAG, "modal-sku-image:" +muiLazySlickImage.get(0).attr("src"));
                }

                Elements split = document.getElementsByClass("split");
                if (split != null && split.size() > 0) {
//                    Log.d(TAG, "split size:" + split.size());
                    for (int i = 0; i < split.size(); i++) {
//                        Log.d(TAG, "index:" + i);
                        Elements skuCard = split.get(i).getElementsByClass("sku card");
                        if (skuCard != null && skuCard.size() > 0) {
                            for (Element modalItem : skuCard) {
                                Log.d(TAG, "modal-sku-title-price:" + modalItem.getElementsByClass("modal-sku-title-price").text());
                                Log.d(TAG, "modal-sku-title-quantity:" + modalItem.getElementsByClass("modal-sku-title-quantity").text());
                                Log.d(TAG, "modal-sku-title-selected-text:" + modalItem.getElementsByClass("modal-sku-title-selected-text").text());

                                Elements modalSkuContent = modalItem.getElementsByClass("modal-sku-content");
                                if (modalSkuContent != null && modalSkuContent.size() > 0) {
//                                    Log.d(TAG, "modalItem size: " + modalSkuContent.size());
//                                    Log.d(TAG, "modalItem text: " + modalSkuContent.text());
                                    for (Element skuContent : modalSkuContent) {
//                                        Log.d(TAG, "skuContent:" + skuContent.text());
                                        Elements event = skuContent.getElementsByClass("modal-sku-content-item modal-sku-content-item-active");
//                                        Log.d(TAG,"event size: "+event.size());
                                        if (event == null || event.size() == 0) {
                                            // no select full properties
                                            productModel.isFullProperties = false;
                                            Log.d(TAG, "no data");
                                            break;
                                        } else {
                                            Log.d(TAG, "modal-sku-content-title:" + skuContent.getElementsByClass("modal-sku-content-title").text());
                                            Log.d(TAG, "event text: " + event.text());
                                            String datavId = event.attr("data-vid");
                                            Log.d(TAG, "datavId:" + datavId);
                                        }
                                    }
                                    break;
                                }
                            }
                            break;
                        }
//                        Log.d(TAG, "----------");
                    }
                }
                return productModel;
            }

            @Override
            protected void onPostExecute(ProductModel productModel) {
                super.onPostExecute(productModel);
                Log.d(TAG, "onPostExecute:" + new Gson().toJson(productModel));
            }
        }.execute();
    }

    public static void m1688(final String html) {
//        Utils.d(TAG, html);

        new AsyncTask<String, String, ProductModel>() {

            @Override
            protected ProductModel doInBackground(String... strings) {
                final ProductModel productModel = new ProductModel();
                Document document = Jsoup.parseBodyFragment(html);
                Element pageContent = document.getElementById("wing-page-content");
                if (pageContent != null) {
                    Elements detailPurchasingContainer = pageContent.getElementsByClass("m-detail-purchasing-container");
                    if (detailPurchasingContainer != null && detailPurchasingContainer.size() > 0) {
                        // qtyBuy
                        Elements amountControl = detailPurchasingContainer.get(0).getElementsByClass("unit-d-amount-control");
                        if (amountControl != null && amountControl.size() > 0) {
                            String amountNumShow = amountControl.get(0).attr("data-amount-num-show");
                            Log.d(TAG, "amountNumShow:" + amountNumShow);
                            if (Utils.isNumber(amountNumShow)) {
                                productModel.qtyBuy = Integer.parseInt(amountNumShow);
                            } else {
                                // other case
                                Elements detailSku = pageContent.getElementsByClass("m-detail-sku");
                                if (detailSku != null && detailSku.size() > 0) {
                                    Elements dContent = detailSku.get(0).getElementsByClass("d-content");
                                    if (dContent != null && dContent.size() > 0) {
                                        Elements qty = dContent.get(0).getElementsByClass("text");
                                        if (qty != null && qty.size() > 0) {
                                            String text = qty.text();
                                            if (text != null && text.length() > 0) {
                                                String s[] = text.split(" ");
                                                for (int i = 0; i < s.length; i++) {
                                                    if (Utils.isNumber(s[i])) {
                                                        Log.d(TAG, "amountNumShow:" + s[i]);
                                                        productModel.qtyBuy = Integer.parseInt(s[i]);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    Log.d(TAG, "m-detail-sku null");
                                    // other case
                                }
                            }
                        }
                        // header
                        Elements header = detailPurchasingContainer.get(0).getElementsByClass("d-header");
                        if (header != null && header.size() > 0) {
                            // product name
                            Elements title = detailPurchasingContainer.get(0).getElementsByClass("title");
                            if (title != null && title.size() > 0) {
                                String productName = title.text();
                                Log.d(TAG, "productName:" + productName);
                                productModel.name = productName;
                            }
                        }
                        // image
                        Elements imgPriview = header.get(0).getElementsByClass("img-priview");
                        if (imgPriview != null && imgPriview.size() > 0) {
                            String img = imgPriview.get(0).attr("src");
                            Log.d(TAG, "img: " + img);
                            productModel.image = img;
                        }
                        // price
                        Elements dPnum = header.get(0).getElementsByClass("d-pnum");
                        if (dPnum != null && dPnum.size() > 0) {
                            String price = dPnum.text();
                            Log.d(TAG, "price:" + price);
                            productModel.price = price;
                        }
                        // currency
                        Elements fdCny = header.get(0).getElementsByClass("fd-cny");
                        if (fdCny != null && fdCny.size() > 0) {
                            String currency = fdCny.text();
                            Log.d(TAG, "currency:" + currency);
                            productModel.currency = currency;
                        }
                        // properties
                        Elements itemSelectorContainer = detailPurchasingContainer.get(0).getElementsByClass("sku-item item-selector item-selector-container");
                        if (itemSelectorContainer != null && itemSelectorContainer.size() > 0) {
//                            Log.d(TAG, "itemSelectorContainer:" + itemSelectorContainer.size());
                            Elements btnActive = itemSelectorContainer.get(0).getElementsByClass("operator-btn fui-btn operator-btn-active");
                            if (btnActive != null && btnActive.size() > 0) {
                                Log.d(TAG, "btnActive:" + btnActive.text());
                            } else {
                                Log.d(TAG, "not select anything");
                            }
                        }
                    }
                }
                return productModel;
            }

            @Override
            protected void onPostExecute(ProductModel productModel) {
                super.onPostExecute(productModel);
                Log.d(TAG, "onPostExecute:" + new Gson().toJson(productModel));
            }
        }.execute();
    }

    public void Register(final Context context,
                         final int method,
                         final Map<String, String> params,
                         final String url,
                         final AccountCallBack callBack) {
        getResponse(context, method, params, url, new GetResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register:" + response);
                AccountDto obj = null;
                if (Utils.isResponseObject(response)) {
                    try {
                        obj = new Gson().fromJson(response, AccountDto.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (obj == null) {
                    obj = new AccountDto();
                    obj.Code = Statics.HTTP_FAIL;
                    obj.Message = Utils.getMsg(context, R.string.can_not_parse_response);
                }
                if (obj.Code == Statics.HTTP_SUCCESS)
                    callBack.onSuccess(obj);
                else callBack.onFail(obj);
            }

            @Override
            public void onVolleyError(VolleyError error) {
                if (callBack != null) callBack.onVolleyError(error);
                Log.d(TAG, "onErrorResponse:" + error.toString());
            }
        });
    }

    public void Login(final Context context,
                      final int method,
                      final Map<String, String> params,
                      final String url,
                      final AccountCallBack callBack) {
        getResponse(context, method, params, url, new GetResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login:" + response);
                AccountDto obj = null;
                if (Utils.isResponseObject(response)) {
                    try {
                        obj = new Gson().fromJson(response, AccountDto.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (obj == null) {
                    obj = new AccountDto();
                    obj.Code = Statics.HTTP_FAIL;
                    obj.Message = Utils.getMsg(context, R.string.can_not_parse_response);
                }
                if (obj.Code == Statics.HTTP_SUCCESS
                        && obj.Account != null)
                    callBack.onSuccess(obj.Account);
                else callBack.onFail(obj);
            }

            @Override
            public void onVolleyError(VolleyError error) {
                if (callBack != null) callBack.onVolleyError(error);
                Log.d(TAG, "onErrorResponse:" + error.toString());
            }
        });
    }

    public void ForgotPassword(final Context context,
                               final int method,
                               final Map<String, String> params,
                               final String url,
                               final AccountCallBack callBack) {
        getResponse(context, method, params, url, new GetResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "ForgotPassword:" + response);
                AccountDto obj = null;
                if (Utils.isResponseObject(response)) {
                    try {
                        obj = new Gson().fromJson(response, AccountDto.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (obj == null) {
                    obj = new AccountDto();
                    obj.Code = Statics.HTTP_FAIL;
                    obj.Message = Utils.getMsg(context, R.string.can_not_parse_response);
                }
                if (obj.Code == Statics.HTTP_SUCCESS)
                    callBack.onSuccess(obj);
                else callBack.onFail(obj);
            }

            @Override
            public void onVolleyError(VolleyError error) {
                if (callBack != null) callBack.onVolleyError(error);
                Log.d(TAG, "onErrorResponse:" + error.toString());
            }
        });
    }

    public void Get_Info(final Context context,
                         final int method,
                         final Map<String, String> params,
                         final String url,
                         final AccountCallBack callBack) {
        getResponse(context, method, params, url, new GetResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Get_Info:" + response);
                AccountDto obj = null;
                if (Utils.isResponseObject(response)) {
                    try {
                        obj = new Gson().fromJson(response, AccountDto.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (obj == null) {
                    obj = new AccountDto();
                    obj.Code = Statics.HTTP_FAIL;
                    obj.Message = Utils.getMsg(context, R.string.can_not_parse_response);
                }
                if (obj.Code == Statics.HTTP_SUCCESS
                        && obj.Info != null)
                    callBack.onSuccess(obj.Info);
                else callBack.onFail(obj);
            }

            @Override
            public void onVolleyError(VolleyError error) {
                if (callBack != null) callBack.onVolleyError(error);
                Log.d(TAG, "onErrorResponse:" + error.toString());
            }
        });
    }

    public void GetAllMenu(final Context context,
                           final int method,
                           final Map<String, String> params,
                           final String url,
                           final MenuCallBack callBack) {
        getResponse(context, method, params, url, new GetResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "GetAllMenu:" + response);
                MenuDto obj = null;
                if (Utils.isResponseObject(response)) {
                    try {
                        obj = new Gson().fromJson(response, MenuDto.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (obj == null) {
                    obj = new MenuDto();
                    obj.Code = Statics.HTTP_FAIL;
                    obj.Message = Utils.getMsg(context, R.string.can_not_parse_response);
                }
                if (obj.Code == Statics.HTTP_SUCCESS
                        && obj.ListMenu != null
                        && obj.ListMenu.size() > 0)
                    callBack.onSuccess(obj);
                else callBack.onFail(obj);
            }

            @Override
            public void onVolleyError(VolleyError error) {
                if (callBack != null) callBack.onVolleyError(error);
                Log.d(TAG, "onErrorResponse:" + error.toString());
            }
        });
    }

    public void UpdateProfile(final Context context,
                              final int method,
                              final Map<String, String> params,
                              final String url,
                              final AccountCallBack callBack) {
        getResponse(context, method, params, url, new GetResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "UpdateProfile:" + response);
                AccountDto obj = null;
                if (Utils.isResponseObject(response)) {
                    try {
                        obj = new Gson().fromJson(response, AccountDto.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (obj == null) {
                    obj = new AccountDto();
                    obj.Code = Statics.HTTP_FAIL;
                    obj.Message = Utils.getMsg(context, R.string.can_not_parse_response);
                }
                if (obj.Code == Statics.HTTP_SUCCESS)
                    callBack.onSuccess(obj);
                else callBack.onFail(obj);
            }

            @Override
            public void onVolleyError(VolleyError error) {
                if (callBack != null) callBack.onVolleyError(error);
                Log.d(TAG, "onErrorResponse:" + error.toString());
            }
        });
    }

    public void upload(Context context, List<String> listPath, HttpRequestCallBack callBack, String url) {
        new uploadFile(context, listPath, callBack, url).execute();
    }

    public class uploadFile extends AsyncTask<String, String, String> {
        private Context context;
        private List<String> listPath;
        private HttpRequestCallBack callBack;
        private String url;

        public uploadFile(Context context, List<String> listPath, HttpRequestCallBack callBack, String url) {
            this.context = context;
            this.listPath = listPath;
            this.callBack = callBack;
            this.url = url;
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            MultipartUtility multipart = null;
            try {
//                param(user_id : INT, location_id : INT ,title : TEXT,reason : LONGTEXT,description : LONGTEXT, images : ARRAY, report_status : BOOLEAN [1 : true,  0 : false])
                multipart = new MultipartUtility(url);
//                multipart.addFormField("query", "location_reports");
//                multipart.addFormField("user_id", user_id);
//                multipart.addFormField("location_id", location_id);
//                multipart.addFormField("description", description);
//                multipart.addFormField("report_status", report_status);

                for (String path : listPath) {
                    Log.d(TAG, "path:" + path);
                    multipart.addFilePart("images[]", new File(path));
                }

                response = multipart.finish(); // response from server.
//                Log.d(TAG, "response:" + response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response == null) response = "";
            Log.d(TAG, "response:" + response);

            if (callBack == null) return;

            HttpRequestDto obj = null;
            if (Utils.isResponseObject(response)) {
                try {
                    obj = new Gson().fromJson(response, HttpRequestDto.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (obj == null) {
                obj = new HttpRequestDto();
                obj.Code = Statics.HTTP_FAIL;
                obj.Message = Utils.getMsg(context, R.string.can_not_parse_response);
            }
            if (obj.Code == Statics.HTTP_SUCCESS
                    && obj.Message != null
                    && obj.Message.trim().length() > 0)
                callBack.onSuccess(obj);
            else callBack.onFail(obj);
        }
    }

    public void GetMenuMain(final Context context,
                            final int method,
                            final Map<String, String> params,
                            final String url,
                            final MenuCallBack callBack) {
        getResponse(context, method, params, url, new GetResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "GetMenuMain:" + response);
                MenuDto obj = null;
                if (Utils.isResponseObject(response)) {
                    try {
                        obj = new Gson().fromJson(response, MenuDto.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (obj == null) {
                    obj = new MenuDto();
                    obj.Code = Statics.HTTP_FAIL;
                    obj.Message = Utils.getMsg(context, R.string.can_not_parse_response);
                }
                if (obj.Code == Statics.HTTP_SUCCESS
                        && obj.ListMenu != null
                        && obj.ListMenu.size() > 0)
                    callBack.onSuccess(obj);
                else callBack.onFail(obj);
            }

            @Override
            public void onVolleyError(VolleyError error) {
                if (callBack != null) callBack.onVolleyError(error);
                Log.d(TAG, "onErrorResponse:" + error.toString());
            }
        });
    }

    public void ChangePassword(final Context context,
                               final int method,
                               final Map<String, String> params,
                               final String url,
                               final AccountCallBack callBack) {
        getResponse(context, method, params, url, new GetResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "ChangePassword:" + response);
                AccountDto obj = null;
                if (Utils.isResponseObject(response)) {
                    try {
                        obj = new Gson().fromJson(response, AccountDto.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (obj == null) {
                    obj = new AccountDto();
                    obj.Code = Statics.HTTP_FAIL;
                    obj.Message = Utils.getMsg(context, R.string.can_not_parse_response);
                }
                if (obj.Code == Statics.HTTP_SUCCESS)
                    callBack.onSuccess(obj);
                else callBack.onFail(obj);
            }

            @Override
            public void onVolleyError(VolleyError error) {
                if (callBack != null) callBack.onVolleyError(error);
                Log.d(TAG, "onErrorResponse:" + error.toString());
            }
        });
    }

    public void LogOut(final Context context,
                       final int method,
                       final Map<String, String> params,
                       final String url,
                       final AccountCallBack callBack) {
        getResponse(context, method, params, url, new GetResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "LogOut:" + response);
                AccountDto obj = null;
                if (Utils.isResponseObject(response)) {
                    try {
                        obj = new Gson().fromJson(response, AccountDto.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (obj == null) {
                    obj = new AccountDto();
                    obj.Code = Statics.HTTP_FAIL;
                    obj.Message = Utils.getMsg(context, R.string.can_not_parse_response);
                }
                if (obj.Code == Statics.HTTP_SUCCESS)
                    callBack.onSuccess(obj);
                else callBack.onFail(obj);
            }

            @Override
            public void onVolleyError(VolleyError error) {
                if (callBack != null) callBack.onVolleyError(error);
                Log.d(TAG, "onErrorResponse:" + error.toString());
            }
        });
    }

    public void GetAllNoti(final Context context,
                           final int method,
                           final Map<String, String> params,
                           final String url,
                           final NotificationCallBack callBack) {
        getResponse(context, method, params, url, new GetResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "GetAllNoti:" + response);
                NotificationDto obj = null;
                if (Utils.isResponseObject(response)) {
                    try {
                        obj = new Gson().fromJson(response, NotificationDto.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (obj == null) {
                    obj = new NotificationDto();
                    obj.Code = Statics.HTTP_FAIL;
                    obj.Message = Utils.getMsg(context, R.string.can_not_parse_response);
                }
                if (obj.Code == Statics.HTTP_SUCCESS
                        && obj.ListNoti != null
                        && obj.ListNoti.size() > 0)
                    callBack.onSuccess(obj);
                else callBack.onFail(obj);
            }

            @Override
            public void onVolleyError(VolleyError error) {
                if (callBack != null) callBack.onVolleyError(error);
                Log.d(TAG, "onErrorResponse:" + error.toString());
            }
        });
    }
    public void UpdateNoti(final Context context,
                           final int method,
                           final Map<String, String> params,
                           final String url,
                           final NotificationCallBack callBack) {
        getResponse(context, method, params, url, new GetResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "UpdateNoti:" + response);
                NotificationDto obj = null;
                if (Utils.isResponseObject(response)) {
                    try {
                        obj = new Gson().fromJson(response, NotificationDto.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (obj == null) {
                    obj = new NotificationDto();
                    obj.Code = Statics.HTTP_FAIL;
                    obj.Message = Utils.getMsg(context, R.string.can_not_parse_response);
                }
                if (obj.Code == Statics.HTTP_SUCCESS)
                    callBack.onSuccess(obj);
                else callBack.onFail(obj);
            }

            @Override
            public void onVolleyError(VolleyError error) {
                if (callBack != null) callBack.onVolleyError(error);
                Log.d(TAG, "onErrorResponse:" + error.toString());
            }
        });
    }

}
