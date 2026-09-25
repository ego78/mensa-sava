package it.egolab.mensasava

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.webkit.*
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var authDialog: Dialog? = null
    private var authWebView: WebView? = null

    private fun isHttp(url: String) =
        url.startsWith("https://", true) || url.startsWith("http://", true)

    private fun openExternal(uri: Uri): Boolean {
        return try {
            startActivity(Intent(Intent.ACTION_VIEW, uri).addCategory(Intent.CATEGORY_BROWSABLE))
            true
        } catch (_: ActivityNotFoundException) {
            false
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun showWebAuth(url: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_web_auth)

        val web = dialog.findViewById<WebView>(R.id.authWebView)
        val progress = dialog.findViewById<ProgressBar>(R.id.webProgress)

        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(web, true)

        web.settings.javaScriptEnabled = true
        web.settings.domStorageEnabled = true
        web.settings.databaseEnabled = true
        web.settings.loadsImagesAutomatically = true
        web.settings.javaScriptCanOpenWindowsAutomatically = true
        web.settings.setSupportMultipleWindows(false)
        web.settings.userAgentString = WebSettings.getDefaultUserAgent(this)

        // V4.1: solo adattamento responsive del contenuto interno.
        // Popup, barra, dimensioni e flusso CIE/SPID restano invariati.
        web.settings.useWideViewPort = true
        web.settings.loadWithOverviewMode = true
        web.settings.builtInZoomControls = true
        web.settings.displayZoomControls = false
        web.settings.textZoom = 85

        web.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progress.progress = newProgress
                progress.visibility = if (newProgress >= 100) android.view.View.GONE else android.view.View.VISIBLE
            }
        }

        web.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val uri = request?.url ?: return false
                val scheme = uri.scheme?.lowercase() ?: return false

                // Normali pagine web restano nel popup.
                if (scheme == "https" || scheme == "http") return false

                // Deep-link/app CIE/SPID: Android apre l'app richiesta.
                if (scheme == "intent") {
                    return try {
                        val intent = Intent.parseUri(uri.toString(), Intent.URI_INTENT_SCHEME)
                        startActivity(intent)
                        true
                    } catch (_: Throwable) {
                        true
                    }
                }

                // cieid://, spid:// o altri schemi applicativi.
                return openExternal(uri)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                CookieManager.getInstance().flush()
            }
        }

        dialog.findViewById<Button>(R.id.closeWeb).setOnClickListener {
            web.stopLoading()
            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            authWebView?.destroy()
            authWebView = null
            authDialog = null
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setDimAmount(0.32f)
        dialog.window?.addFlags(android.view.WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        dialog.show()

        // Quasi tutto lo schermo, ma la Home resta visibile dietro al popup.
        val w = (resources.displayMetrics.widthPixels * 0.94).toInt()
        val h = (resources.displayMetrics.heightPixels * 0.88).toInt()
        dialog.window?.setLayout(w, h)

        authDialog = dialog
        authWebView = web
        web.loadUrl(url)
    }

    override fun onResume() {
        super.onResume()
        // Dopo CieID Android torna qui se il flusso lo consente.
        // La WebView e i cookie restano vivi e il portale può continuare il redirect.
        authWebView?.onResume()
    }

    override fun onPause() {
        authWebView?.onPause()
        super.onPause()
    }

    override fun onBackPressed() {
        val web = authWebView
        if (authDialog?.isShowing == true && web != null && web.canGoBack()) {
            web.goBack()
        } else if (authDialog?.isShowing == true) {
            authDialog?.dismiss()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.login).setOnClickListener {
            showWebAuth("https://www.comune.sava.ta.it/mensascolastica")
        }
        findViewById<Button>(R.id.assenze).setOnClickListener {
            showWebAuth("https://www.comune.sava.ta.it/mensascolastica_assenze")
        }
        findViewById<Button>(R.id.info).setOnClickListener {
            showWebAuth("https://www.comune.sava.ta.it/mensascolastica_info/")
        }
        findViewById<Button>(R.id.dashboard).setOnClickListener {
            showWebAuth("https://www.comune.sava.ta.it/mensascolastica")
        }
    }
}