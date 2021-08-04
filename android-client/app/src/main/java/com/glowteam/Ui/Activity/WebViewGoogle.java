package com.glowteam.Ui.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.glowteam.R;


public class WebViewGoogle extends BaseActivity {
    private WebView mywebView;
    private AlertDialog builder;

    private WebView mWebviewPop;
    private Context mContext;


    public String USER_AGENT_FAKE = "Mozilla/5.0 (Linux; Android 4.1.1; Galaxy Nexus Build/JRO03C) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_weview_google);
        mywebView = findViewById(R.id.webview);
        WebSettings webSettings = mywebView.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mywebView.clearHistory();
        mywebView.clearFormData();
        mywebView.clearCache(true);


        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);


// USER_AGENT_FAKE = mywebView.getSettings().getUserAgentString() ;

        mywebView.setWebViewClient(new MyCustomWebViewClient());
        mywebView.setWebChromeClient(new UriWebChromeClient());
        mywebView.loadUrl("https://accounts.google.com/signin/v2");
        mContext = this.getApplicationContext();

//        mywebView.setWebViewClient(new WebViewClient() {
//                                       @Override
//                                       public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                                           super.onPageStarted(view, url, favicon);
//                                           showProgressDialog();
//                                       }
//
//                                       @Override
//                                       public void onPageFinished(WebView view, String url) {
//                                           super.onPageFinished(view, url);
//                                           dismissProgressDialog();
//                                       }
//                                   }
//        );
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (mywebView.isFocused() && mywebView.canGoBack()) {
            mywebView.goBack();
        } else {
            super.onBackPressed();
        }
    }


    private class MyCustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            Log.d("onReceivedSslError", "onReceivedSslError");
            super.onReceivedSslError(view, handler, error);
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            super.onPageFinished(view, url);
        }
    }

    private class UriWebChromeClient extends WebChromeClient {

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            mWebviewPop = new WebView(mContext);
            mWebviewPop.setVerticalScrollBarEnabled(false);
            mWebviewPop.setHorizontalScrollBarEnabled(false);
            mWebviewPop.setWebViewClient(new MyCustomWebViewClient());
            mWebviewPop.setWebChromeClient(new UriWebChromeClient());
            mWebviewPop.getSettings().setJavaScriptEnabled(true);
            mWebviewPop.clearHistory();
            mWebviewPop.clearFormData();
            mWebviewPop.clearCache(true);
// mWebviewPop.getSettings().setSavePassword(true);
// mWebviewPop.getSettings().setSaveFormData(true);
            mWebviewPop.getSettings().setUserAgentString(USER_AGENT_FAKE);

            builder = new AlertDialog.Builder(WebViewGoogle.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).create();

            builder.setTitle("");
            builder.setView(mWebviewPop);

            builder.setButton("close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    mWebviewPop.destroy();
                    dialog.dismiss();

                }
            });

            builder.show();
            builder.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cookieManager.setAcceptThirdPartyCookies(mWebviewPop, true);
            }


            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(mWebviewPop);
            resultMsg.sendToTarget();

            return true;
        }


        @Override
        public void onCloseWindow(WebView window) {

            try {
                mWebviewPop.destroy();
            } catch (Exception e) {
            }

            try {
                builder.dismiss();

            } catch (Exception e) {
            }

        }

    }


}
