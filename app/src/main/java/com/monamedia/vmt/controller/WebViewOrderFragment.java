package com.monamedia.vmt.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.monamedia.vmt.TestActivity;
import com.monamedia.vmt.R;
import com.monamedia.vmt.common.Utils;
import com.monamedia.vmt.common.interfaces.HtmlCallBack;
import com.monamedia.vmt.common.interfaces.Statics;
import com.monamedia.vmt.databinding.WebviewFragmentBinding;

/**
 * Created by Android on 4/11/2018.
 */

public class WebViewOrderFragment extends Fragment {
    private String TAG = "WebviewFragment";
    private WebviewFragmentBinding binding;
    private String url = Statics.m1688;
    private String urlLoading = "";

    public WebViewOrderFragment() {

    }

    @SuppressLint("ValidFragment")
    public WebViewOrderFragment(String url) {
        if (url != null) this.url = url;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.webview_fragment, container, false);
        View view = binding.getRoot();
        bind();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        binding.webView.loadUrl(Statics.ABOUT_BLANK);
    }

    private String HtmlViewer = "HtmlViewer";

    @SuppressLint("JavascriptInterface")
    public void bind() {
        Log.d(TAG, "URL:" + url);
        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        binding.webView.addJavascriptInterface(new MyJavaScriptInterface(getActivity()), HtmlViewer);
        // scale web
        binding.webView.setInitialScale(1);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);


        // enable load video
        webSettings.setDomStorageEnabled(true);
        binding.webView.setWebViewClient(new MyWebViewClient());
        binding.webView.setWebChromeClient(new MyWebChromeClient());
        binding.webView.loadUrl(url);
//        binding.webView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.d(TAG,"onTouch");
//                return false;
//            }
//        });
    }

    private HtmlCallBack htmlCallBack;

    public void setHtmlCallBack(HtmlCallBack htmlCallBack) {
        this.htmlCallBack = htmlCallBack;
    }

    class MyJavaScriptInterface {

        private Context ctx;

        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }

        @JavascriptInterface
        @SuppressWarnings("unused")
        public void showHTML(String html) {
//            Log.d(TAG, "html:" + html);
            if (htmlCallBack != null) htmlCallBack.onReceive(html, urlLoading);
        }
    }

    public void getHtml() {
//        binding.webView.loadUrl("javascript:this.document.location.href = 'source://' + encodeURI(document.documentElement.outerHTML);");
        binding.webView.loadUrl("javascript:window." + HtmlViewer + ".showHTML" +
                "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
    }

    private class MyWebViewClient extends WebViewClient {
        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            Log.d(TAG, "shouldInterceptRequest WebResourceRequest");
            return super.shouldInterceptRequest(view, request);
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            Log.d(TAG, "shouldInterceptRequest url");
            handler.obtainMessage(MSG_SEND, "").sendToTarget();
            return super.shouldInterceptRequest(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Log.d(TAG, "shouldOverrideUrlLoading WebResourceRequest");
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "shouldOverrideUrlLoading url");
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            binding.progressBar.setVisibility(View.VISIBLE);
            Log.d(TAG, "onPageStarted");
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            String ht = "javascript:window.droid.print(document.getElementsByTagName('html')[0].innerHTML);";
            urlLoading = url;
            if (getActivity() != null&&getActivity() instanceof TestActivity)
                ((TestActivity) getActivity()).showMenu(url);
            Log.d(TAG, "onPageFinished url:" + url);
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }

//        @Override
//        public boolean onConsoleMessage(ConsoleMessage cm) {
//            Log.d(TAG, cm.message() + " #" + cm.lineNumber() + " --" + cm.sourceId() );
//            return super.onConsoleMessage(cm);
//        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
//            Log.d(TAG, "newProgress:" + newProgress);
            binding.progressBar.setProgress(newProgress);
            binding.progressBar.setVisibility(newProgress == 100 ? View.GONE : View.VISIBLE);
        }
    }

    public void injectHtml() {
        String name = "Huynh Cong Huy";
        String javascript = "javascript: document.getElementsByClassName('anchor-btn actived')[0].innerHTML = '" + name + "'; huyFun();";
        binding.webView.loadUrl(javascript);
    }

    public String getUrl() {
        return binding.webView.getUrl();
    }

    private final static int MSG_SEND = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MSG_SEND:
                    addGuideVietnamese();
                    break;
            }
        }
    };


    public void addGuideVietnamese() {
        Log.d(TAG, "addGuideVietnamese");
        // tmall
        String className = "number-wrap";
        int style = Utils.getSite(urlLoading);
        if (style == Utils.val_taobao) {
            className = "huy";
        } else if (style == Utils.val_1688) {
//            className = "m-detail-purchasing-header  J_Style_PreventTouchMoveThrough";
            className = "spec-operator-item";
        }

        String javascript = "javascript:(function() { "
                // replace word to vietnamese
                // taobao
                +Utils.convertTextToVietnamese(Utils.getElementsByClassName,"modal-sku-content-title")
                +Utils.convertTextToVietnamese(Utils.getElementsByClassName,"modal-sku-content-quantity-title")
                // tmall
                +Utils.convertTextToVietnamese(Utils.getElementsByTagName,"h2")
                +Utils.convertTextToVietnamese(Utils.getElementsByTagName,"label")
                // 1688

//                + "var len = document.querySelectorAll(\"._warning-on-page\").length;"
//                + "if(len) return;"
//                + "var element = document.createElement('div');"
//                + "element.className = 'block-warning-on-page-addon _warning-on-page';"
//                + "element.textContent = 'Vui lòng chọn đầy đủ thuộc tính của sản phẩm (màu sắc, kích thước,..), tiếp đó click vào nút \"Đặt hàng\"';"
//                + "element.style.color = \"#ff0000\";"
//                + "element.style.backgroundColor = \"#bce6f7\";"
//                + "element.style.marginTop = \"10px\";"
//                + "element.style.marginBottom = \"10px\";"
//                + "if(document.getElementsByClassName('" + className + "').length > 0) document.getElementsByClassName('" + className + "')[0].insertBefore(element,null)"
                + "})()";
        if (binding.webView != null) binding.webView.loadUrl(javascript);
    }
    public void showPopup(){
        String js = "javascript:document.getElementsByClassName('m-detail-sku')[0].click();void(0);";
        binding.webView.loadUrl(js);
    }
    public boolean canGoBack() {
        return binding.webView.canGoBack();
    }

    public void goBack() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack();
        }
    }
}

