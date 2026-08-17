package com.edu6tron.spiritualcompanion.nativepreview.ui

import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceError
import android.webkit.WebStorage
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/**
 * Displays the official YouTube web experience inside Spiritual Companion.
 *
 * This component deliberately does not alter provider controls, branding, advertising,
 * or media delivery. Each dialog owns a fresh WebView and clears its in-app web session
 * when closed, so provider searches do not become part of Spiritual Companion's history.
 */
@Composable
fun InAppYouTubePlayer(
  query: String,
  onDismiss: () -> Unit,
) {
  val initialUrl = remember(query) { ProviderSearchPolicy.providerSearchUrl(query) }
  var isLoading by remember { mutableStateOf(true) }
  var errorMessage by remember { mutableStateOf<String?>(null) }
  var webView by remember { mutableStateOf<WebView?>(null) }

  fun closeYouTube() {
    val activeWebView = webView
    webView = null
    activeWebView?.clearProviderSession()
    onDismiss()
  }

  DisposableEffect(Unit) {
    onDispose {
      webView?.clearProviderSession()
    }
  }

  BackHandler {
    val currentWebView = webView
    if (currentWebView?.canGoBack() == true) {
      currentWebView.goBack()
    } else {
      closeYouTube()
    }
  }

  Dialog(
    onDismissRequest = ::closeYouTube,
    properties = DialogProperties(usePlatformDefaultWidth = false),
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(12.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
      Column(modifier = Modifier.fillMaxSize()) {
        Row(
          modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text("YouTube", style = MaterialTheme.typography.titleLarge)
            Text(
              "Official provider experience",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
          TextButton(onClick = ::closeYouTube) { Text("Close YouTube") }
        }

        Box(modifier = Modifier.fillMaxSize()) {
          AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
              WebView(context).also { createdWebView ->
                webView = createdWebView
                createdWebView.settings.javaScriptEnabled = true
                createdWebView.settings.domStorageEnabled = true
                createdWebView.settings.mediaPlaybackRequiresUserGesture = true
                createdWebView.settings.setSupportMultipleWindows(false)
                createdWebView.webChromeClient = WebChromeClient()
                createdWebView.webViewClient = object : WebViewClient() {
                  override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    val scheme = request.url.scheme?.lowercase()
                    if (scheme == "http" || scheme == "https") return false

                    errorMessage = "This YouTube link requires an external app, so it was kept inside Spiritual Companion."
                    return true
                  }

                  override fun onPageStarted(view: WebView, url: String?, favicon: android.graphics.Bitmap?) {
                    isLoading = true
                    errorMessage = null
                  }

                  override fun onPageFinished(view: WebView, url: String?) {
                    isLoading = false
                  }

                  override fun onReceivedError(
                    view: WebView,
                    request: WebResourceRequest,
                    error: WebResourceError,
                  ) {
                    if (request.isForMainFrame) {
                      isLoading = false
                      errorMessage = "YouTube could not load. Check your connection and try again."
                    }
                  }
                }
                createdWebView.loadUrl(initialUrl)
              }
            },
          )

          if (isLoading) {
            Card(
              modifier = Modifier.align(Alignment.Center),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
            ) {
              Row(
                modifier = Modifier.padding(18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
              ) {
                CircularProgressIndicator(modifier = Modifier.padding(2.dp))
                Text("Loading official YouTube…")
              }
            }
          }

          errorMessage?.let { message ->
            Card(
              modifier = Modifier
                .align(Alignment.Center)
                .padding(20.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
            ) {
              Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
              ) {
                Text(message, color = MaterialTheme.colorScheme.onErrorContainer)
                Button(onClick = {
                  errorMessage = null
                  isLoading = true
                  webView?.loadUrl(initialUrl)
                }) { Text("Try again") }
              }
            }
          }
        }
      }
    }
  }
}

private fun WebView.clearProviderSession() {
  stopLoading()
  clearHistory()
  clearCache(true)
  CookieManager.getInstance().removeAllCookies(null)
  CookieManager.getInstance().flush()
  WebStorage.getInstance().deleteAllData()
  destroy()
}
