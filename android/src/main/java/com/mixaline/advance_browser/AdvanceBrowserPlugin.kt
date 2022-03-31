package com.mixaline.advance_browser

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.*
import android.webkit.*
import com.getcapacitor.JSObject
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin
import com.mixaline.advance_browser.BrowserActivity.Companion.CLOSE_BROWSER
import com.mixaline.advance_browser.BrowserActivity.Companion.ON_LOAD_RESOURCE
import com.mixaline.advance_browser.BrowserActivity.Companion.ON_PAGE_FINISHED
import com.mixaline.advance_browser.BrowserActivity.Companion.ON_PAGE_STARTED


@CapacitorPlugin(name = "AdvanceBrowser")
class AdvanceBrowserPlugin : Plugin() {
  private var cookies: String? = null
  private var dialog: Dialog? = null
  private var mHandler: Handler? = null

  companion object {
    var mBrowserHandler: Handler? = null

    fun registerBrowserHandler(handler: Handler) {
      mBrowserHandler = handler
    }
  }

  init {
    mHandler = object : Handler(Looper.getMainLooper()) {
      override fun handleMessage(msg: Message) {
        when(msg.arg1) {
          ON_PAGE_FINISHED -> {
            notifyListeners("onPageFinished", msg.data.toJSObject())
          }
          ON_LOAD_RESOURCE -> {
            notifyListeners("onLoadResource", msg.data.toJSObject())
          }
          ON_PAGE_STARTED -> {
            notifyListeners("onPageStarted", msg.data.toJSObject())
          }

        }
      }
    }
    BrowserActivity.registerPluginHandler(mHandler!!)
  }

  @PluginMethod
  fun open(call: PluginCall) {
    val urlString = call.getString("url")
    if (urlString == null) {
      call.reject("Must provide a URL to open")
      return
    }
    if (urlString.isEmpty()) {
      call.reject("URL must not be empty")
      return
    }
    val url: Uri = try {
      Uri.parse(urlString)
    } catch (ex: Exception) {
      call.reject(ex.localizedMessage)
      return
    }

    val intent = Intent(activity, BrowserActivity::class.java)
    intent.putExtra("url", url)
    activity.startActivity(intent)
    call.resolve()
  }

  @PluginMethod
  fun close(call: PluginCall) {
    val msg = Message()
    msg.arg1 = CLOSE_BROWSER
    mBrowserHandler?.dispatchMessage(msg)
    call.resolve()
  }

  @PluginMethod
  fun loadUrl(call: PluginCall) {
    val url = call.getString("url")
    if (url == null) {
      call.reject("Must provide a URL to open")
      return
    }
    if (url.isEmpty()) {
      call.reject("URL must not be empty")
      return
    }

    val msg = Message()
    val bundle = Bundle()
    bundle.putString("url", url)
    msg.data = bundle
    mBrowserHandler?.dispatchMessage(msg)
  }

  @PluginMethod
  fun getCookie(call: PluginCall) {
    val url = call.getString("url")
    if (url == null) {
      call.reject("Must provide a URL to open")
      return
    }
    if (url.isEmpty()) {
      call.reject("URL must not be empty")
      return
    }

    val cookie = CookieManager.getInstance().getCookie(url)
    var result: JSObject? = null
    if(cookie != null) {
      result = JSObject()
      val temp = cookie.split(";")
      for (ar1 in temp) {
        val data = ar1.split("=").toTypedArray()
        result.put(data[0].replace(" ", ""), data[1])
      }
    }
    call.resolve(result)
  }

  @PluginMethod
  fun setCookie(call: PluginCall) {
    val url = call.getString("url")
    val cookieString = call.getString("cookie")
    CookieManager.getInstance().setCookie(url, cookieString)
    call.resolve()
  }

  @PluginMethod
  @SuppressWarnings("deprecation")
  fun clearCookies(call: PluginCall) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
      CookieManager.getInstance().removeAllCookies(null)
      CookieManager.getInstance().flush()
    } else if (context != null) {
      val cookieSyncManager = CookieSyncManager.createInstance(context)
      cookieSyncManager.startSync()
      val cookieManager: CookieManager = CookieManager.getInstance()
      cookieManager.removeAllCookie()
      cookieManager.removeSessionCookie()
      cookieSyncManager.stopSync()
      cookieSyncManager.sync()
    }
  }
}