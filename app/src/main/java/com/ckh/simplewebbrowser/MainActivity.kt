package com.ckh.simplewebbrowser

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.widget.ContentLoadingProgressBar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MainActivity : AppCompatActivity() {

    private val addressBar: EditText by lazy { findViewById(R.id.addressBar) }
    private val refreshLayout: SwipeRefreshLayout by lazy { findViewById(R.id.refreshLayout) }
    private val goHomeBtn: ImageButton by lazy { findViewById(R.id.homeBtn) }
    private val goBackBtn: ImageButton by lazy { findViewById(R.id.backBtn) }
    private val goFowardBtn: ImageButton by lazy { findViewById(R.id.forwartBtn) }
    private val webView: WebView by lazy { findViewById(R.id.webView) }
    private val progressBar: ContentLoadingProgressBar by lazy { findViewById(R.id.progressBar) }

    companion object {
        private const val DEFAULT_URL = "https://www.google.com"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        bindView()
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {
        webView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            loadUrl(DEFAULT_URL)
        }
    }

    private fun bindView() {
        addressBar.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                webView.loadUrl(v.text.toString())
                val loadingUrl = v.text.toString()
                if(URLUtil.isNetworkUrl(loadingUrl)){
                    webView.loadUrl(loadingUrl)
                }else{
                    webView.loadUrl("http://$loadingUrl")
                }
            }
            return@setOnEditorActionListener false
        }
        goBackBtn.setOnClickListener {
            webView.goBack()
        }
        goFowardBtn.setOnClickListener {
            webView.goForward()
        }
        goHomeBtn.setOnClickListener {
            webView.loadUrl(DEFAULT_URL)
        }
        refreshLayout.setOnRefreshListener {
            webView.reload()
        }
    }
    inner class WebViewClient:android.webkit.WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            refreshLayout.isRefreshing=false
            progressBar.hide()
            goBackBtn.isEnabled = webView.canGoBack()
            goFowardBtn.isEnabled = webView.canGoBack()
            addressBar.setText(url)
        }
    }
}