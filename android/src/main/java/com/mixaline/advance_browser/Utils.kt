package com.mixaline.advance_browser

import android.os.Bundle
import com.getcapacitor.JSObject

fun Bundle.toJSObject(): JSObject {
  val json = JSObject()

  for (key in keySet()) {
    json.put(key, get(key))
  }
  return json
}