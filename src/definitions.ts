export interface OpenOptions {
  url: string;
}
export interface CookieOptions {
  url: string;
}
export interface Cookie {
  [name: string]: string;
}

export interface AdvanceBrowserPlugin {
  open(options: OpenOptions): Promise<void>;
  close(options: OpenOptions): Promise<void>;
  getCookie(options: CookieOptions): Promise<Cookie>;
}
