
package id.ac.untirta.siakangspada

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast

class MainActivity : Activity() {

    private lateinit var webView: WebView
    private lateinit var progress: ProgressBar

    private val spadaUrl =
        "https://spada.untirta.ac.id/?redirect=0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            val layout = FrameLayout(this)

            webView = WebView(this)
            progress = ProgressBar(
                this,
                null,
                android.R.attr.progressBarStyleHorizontal
            )

            progress.max = 100

            layout.addView(
                webView,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            )

            layout.addView(
                progress,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    8
                )
            )

            setContentView(layout)

            CookieManager.getInstance().setAcceptCookie(true)
            CookieManager.getInstance()
                .setAcceptThirdPartyCookies(webView, true)

            webView.settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                databaseEnabled = true
                loadsImagesAutomatically = true
                javaScriptCanOpenWindowsAutomatically = true
                setSupportMultipleWindows(false)
                useWideViewPort = true
                loadWithOverviewMode = true
                mixedContentMode =
                    WebSettings.MIXED_CONTENT_NEVER_ALLOW
            }

            webView.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(
                    view: WebView?,
                    newProgress: Int
                ) {
                    progress.progress = newProgress
                    progress.visibility =
                        if (newProgress >= 100) View.GONE
                        else View.VISIBLE
                }
            }

            webView.webViewClient = object : WebViewClient() {

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    val host = request?.url?.host ?: return false

                    return !(host == "untirta.ac.id" ||
                        host.endsWith(".untirta.ac.id"))
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(view, request, error)

                    if (request?.isForMainFrame == true) {
                        Toast.makeText(
                            this@MainActivity,
                            "Halaman gagal dimuat. Periksa internet atau coba lagi.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            if (savedInstanceState == null ||
                webView.restoreState(savedInstanceState) == null
            ) {
                webView.loadUrl(spadaUrl)
            }

        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Aplikasi gagal dibuka: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
            finish()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (::webView.isInitialized) {
            webView.saveState(outState)
        }
        super.onSaveInstanceState(outState)
    }

    @Deprecated("Deprecated in Android")
    override fun onBackPressed() {
        if (::webView.isInitialized && webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        if (::webView.isInitialized) {
            webView.stopLoading()
            webView.destroy()
        }
        super.onDestroy()
    }
}
