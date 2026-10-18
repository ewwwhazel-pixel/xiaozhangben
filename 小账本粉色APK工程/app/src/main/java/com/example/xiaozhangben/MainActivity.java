package com.example.xiaozhangben;

import android.app.Activity;
import android.graphics.Insets;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowInsets;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.view.View;
import android.graphics.Color;

public class MainActivity extends Activity {
    private WebView webView;
    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
    private int systemBarHeight(String name, int fallbackDp) {
        int resourceId = getResources().getIdentifier(name, "dimen", "android");
        return resourceId > 0 ? getResources().getDimensionPixelSize(resourceId) : dp(fallbackDp);
    }
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int pageBackground = Color.rgb(255, 248, 245);
        getWindow().getDecorView().setBackgroundColor(pageBackground);
        getWindow().setStatusBarColor(Color.rgb(109, 92, 231));
        getWindow().setNavigationBarColor(pageBackground);
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        webView = new WebView(this);
        webView.setBackgroundColor(pageBackground);
        if (Build.VERSION.SDK_INT >= 35) {
            getWindow().setDecorFitsSystemWindows(false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            final int statusBar = systemBarHeight("status_bar_height", 24);
            final int navigationBar = systemBarHeight("navigation_bar_height", 24);
            final int extraTop = dp(12);
            webView.setPadding(0, statusBar + extraTop, 0, navigationBar);
            webView.setOnApplyWindowInsetsListener((view, insets) -> {
                Insets bars = insets.getInsets(
                    WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout());
                view.setPadding(
                    bars.left,
                    Math.max(bars.top, statusBar) + extraTop,
                    bars.right,
                    Math.max(bars.bottom, navigationBar));
                return insets;
            });
        }
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        setContentView(webView);
        if (Build.VERSION.SDK_INT >= 35) {
            webView.post(webView::requestApplyInsets);
        }
        webView.loadUrl("file:///android_asset/index.html");
    }
    @Override public void onBackPressed() {
        if (webView != null && webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }
}
