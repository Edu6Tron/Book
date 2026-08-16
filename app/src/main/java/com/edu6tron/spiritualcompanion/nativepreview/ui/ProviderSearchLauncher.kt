package com.edu6tron.spiritualcompanion.nativepreview.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

/** Opens provider content in a browser-managed tab rather than embedding unbounded provider UI. */
fun openProviderSearch(
  context: Context,
  query: String,
  onUnavailable: () -> Unit,
) {
  val uri = Uri.parse(ProviderSearchPolicy.providerSearchUrl(query))
  val customTab = CustomTabsIntent.Builder()
    .setShowTitle(true)
    .build()

  runCatching { customTab.launchUrl(context, uri) }
    .recoverCatching {
      context.startActivity(
        Intent(Intent.ACTION_VIEW, uri).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
      )
    }
    .onFailure { onUnavailable() }
}
