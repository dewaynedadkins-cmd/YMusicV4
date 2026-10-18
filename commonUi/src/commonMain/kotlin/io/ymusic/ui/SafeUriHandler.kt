package io.ymusic.ui

import androidx.compose.ui.platform.UriHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/** A [UriHandler] that reports a link nothing on the device can open (no browser installed) on
 *  [failures] — the app shell shows it — instead of throwing out of a click handler. */
internal class SafeUriHandler(private val platform: UriHandler) : UriHandler {
    private val failed = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val failures: SharedFlow<String> = failed

    override fun openUri(uri: String) {
        try {
            platform.openUri(uri)
        } catch (error: IllegalArgumentException) {
            failed.tryEmit(uri)
        }
    }
}
