package br.com.nextfrotas360.nextfuel360

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat


class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Garante que o conteúdo respeite a status bar
        WindowCompat.setDecorFitsSystemWindows(window, true)

        // Garante que a status bar esteja visível
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
        }

        WindowInsetsControllerCompat(window, window.decorView)
            .show(android.view.WindowInsets.Type.statusBars())

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        setContent {
            Home("https://combustivel-dev.nextfrotas360.com.br")
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun Home(url: String) {

    var webView: WebView? by remember { mutableStateOf(null) }
    var backButton by remember { mutableStateOf(false) }

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                layoutParams = WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT
                )

                isFocusable = true
                isFocusableInTouchMode = true
                descendantFocusability = WebView.FOCUS_AFTER_DESCENDANTS

                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.useWideViewPort = true
                settings.loadWithOverviewMode = true

                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(
                        view: WebView?,
                        url: String?,
                        favicon: Bitmap?
                    ) {
                        backButton = view?.canGoBack() ?: false
                    }
                }

                loadUrl(url)
            }
        },
        modifier = Modifier.fillMaxSize().background(Color.White).windowInsetsPadding(WindowInsets.statusBars),
        update = {
            webView = it
        }
    )

    BackHandler(enabled = backButton) {
        webView?.goBack()
    }
}