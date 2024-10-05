package com.vitus.intelkisanmadad;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;


public class SmartIrrigationActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_irrigation);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        }

        webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient()); // Ensure links open within the WebView
        webView.getSettings().setJavaScriptEnabled(true); // Enable JavaScript
        webView.loadUrl("https://huggingface.co/spaces/Aakarsh007/SmartIrrigation"); // Load the desired URL
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack(); // Go back in the WebView history
        } else {
            super.onBackPressed(); // Default back behavior
        }
    }
}
