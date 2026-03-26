package com.meetlive.app.activity;

import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.meetlive.app.R;
import com.meetlive.app.dialog.MyProgressDialog;

public class PrivacyPolicy extends AppCompatActivity {

    MyProgressDialog progressDialog;
    private WebView webview;
    String url = "https://sites.google.com/view/meetlive/home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_privacy_policy);

        init();

    }

    void init() {
        progressDialog = new MyProgressDialog(this);

        webview = findViewById(R.id.webview);
       /* webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);*/
        startWebView(url);
    }


    private void startWebView(String url) {
        progressDialog.show();

        //Create new webview Client to show progress dialog
        //When opening a url or click on link
        webview.setWebViewClient(new WebViewClient() {

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);

                progressDialog.dismiss();
            }
        });

        //Load url in webview
        webview.loadUrl(url);
    }
}