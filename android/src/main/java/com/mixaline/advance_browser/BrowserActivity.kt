package com.mixaline.advance_browser

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.*
import android.view.KeyEvent
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class BrowserActivity : AppCompatActivity() {

  private lateinit var webview: WebView
  private var cookies: String? = null

  companion object {
    const val ON_PAGE_STARTED = 2982
    const val ON_PAGE_FINISHED = 2983
    const val ON_LOAD_RESOURCE = 9434
    const val CLOSE_BROWSER = 2309
    const val LOAD_URL = 9337

    var mHandler: Handler? = null
    var mPluginHandler: Handler? = null

    fun registerPluginHandler(handler: Handler) {
      mPluginHandler = handler
    }
  }

  @SuppressLint("SetJavaScriptEnabled", "ObsoleteSdkInt")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_browser)

    mHandler = object : Handler(Looper.getMainLooper()) {
      override fun handleMessage(msg: Message) {
        when(msg.arg1) {
          CLOSE_BROWSER -> {
            finish()
          }
          LOAD_URL -> {
            val data = msg.data
            webview.loadUrl(data.getString("url")!!)
          }
        }
      }
    }

    AdvanceBrowserPlugin.registerBrowserHandler(mHandler!!)

    val toolbar = findViewById<Toolbar>(R.id.toolbar)

    setSupportActionBar(toolbar)
    supportActionBar?.setLogo(android.R.drawable.ic_lock_lock)
    supportActionBar?.setDisplayUseLogoEnabled(true)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setHomeButtonEnabled(true)
    toolbar.setNavigationIcon(android.R.drawable.ic_menu_close_clear_cancel)
    toolbar.setNavigationOnClickListener {
      finish()
    }

    webview = findViewById(R.id.webview)
    val settings = webview.settings
    settings.javaScriptEnabled = true
    settings.databaseEnabled = true
    settings.domStorageEnabled = true
    settings.javaScriptCanOpenWindowsAutomatically = true
    settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      CookieManager.getInstance().setAcceptThirdPartyCookies(
        webview,
        true
      )
    }
    webview.webViewClient = BrowserClient()
    val url: Uri = intent.getParcelableExtra("url")!!
    val extras = intent.extras
    if (extras != null && extras.keySet().size > 0) {
      for (key in extras.keySet()) {
        if (key == "url") continue
      }
    }
    supportActionBar?.title = url.host
    webview.loadUrl(url.toString())
  }

  inner class BrowserClient : WebViewClient() {
    override fun onPageFinished(view: WebView?, url: String?) {
      super.onPageFinished(view, url)
      val cookies = CookieManager.getInstance().getCookie(url)
      val uri = Uri.parse(url)
      supportActionBar?.title = uri.host

      val msg = Message()
      val bundle = Bundle()
      bundle.putString("url", url)
      msg.arg1 = ON_PAGE_FINISHED
      msg.data = bundle
      mPluginHandler?.dispatchMessage(msg)
    }

    override fun onLoadResource(view: WebView?, url: String?) {
      super.onLoadResource(view, url)
      val currentCookies = CookieManager.getInstance().getCookie(url)

      if (cookies != currentCookies) {
        cookies = currentCookies
      }

      val msg = Message()
      val bundle = Bundle()
      bundle.putString("url", url)
      msg.arg1 = ON_LOAD_RESOURCE
      msg.data = bundle
      mPluginHandler?.dispatchMessage(msg)
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
      super.onPageStarted(view, url, favicon)
      val uri = Uri.parse(url)
      supportActionBar?.title = uri.host

      val msg = Message()
      val bundle = Bundle()
      bundle.putString("url", url)
      msg.arg1 = ON_PAGE_STARTED
      msg.data = bundle
      mPluginHandler?.dispatchMessage(msg)
    }

    override fun onReceivedHttpError(
      view: WebView?,
      request: WebResourceRequest?,
      errorResponse: WebResourceResponse?
    ) {
      super.onReceivedHttpError(view, request, errorResponse)
      // TODO: add implementation
    }

    override fun onReceivedError(
      view: WebView?,
      request: WebResourceRequest?,
      error: WebResourceError?
    ) {
      super.onReceivedError(view, request, error)
      // TODO: add implementation
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
      super.onReceivedSslError(view, handler, error)
      // TODO: add implementation
    }

    override fun shouldOverrideKeyEvent(view: WebView?, event: KeyEvent?): Boolean {
      return false
    }
  }
}