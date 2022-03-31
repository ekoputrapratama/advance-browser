import { WebPlugin } from '@capacitor/core';

import type { Cookie, CookieOptions, OpenOptions } from './definitions';
import { AdvanceBrowserPlugin } from './definitions';

export class AdvanceBrowserWeb
  extends WebPlugin
  implements AdvanceBrowserPlugin {
  _lastWindow: Window | null;

  constructor() {
    super();
    this._lastWindow = null;
  }

  async getCookie(_: CookieOptions): Promise<Cookie> {
    throw new Error('Method not implemented.');
  }

  async open(options: OpenOptions): Promise<void> {
    this._lastWindow = window.open(options.url);
  }

  async close(): Promise<void> {
    this._lastWindow?.close();
  }
}

const AdvanceBrowserPlugin = new AdvanceBrowserWeb();

export { AdvanceBrowserPlugin };
