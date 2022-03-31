# advance-browser

An advance browser for ionic

## Install

```bash
npm install advance-browser
npx cap sync
```

## API

<docgen-index>

* [`open(...)`](#open)
* [`close(...)`](#close)
* [`getCookie(...)`](#getcookie)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### open(...)

```typescript
open(options: OpenOptions) => Promise<void>
```

| Param         | Type                                                |
| ------------- | --------------------------------------------------- |
| **`options`** | <code><a href="#openoptions">OpenOptions</a></code> |

--------------------


### close(...)

```typescript
close(options: OpenOptions) => Promise<void>
```

| Param         | Type                                                |
| ------------- | --------------------------------------------------- |
| **`options`** | <code><a href="#openoptions">OpenOptions</a></code> |

--------------------


### getCookie(...)

```typescript
getCookie(options: CookieOptions) => Promise<Cookie>
```

| Param         | Type                                                    |
| ------------- | ------------------------------------------------------- |
| **`options`** | <code><a href="#cookieoptions">CookieOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#cookie">Cookie</a>&gt;</code>

--------------------


### Interfaces


#### OpenOptions

| Prop      | Type                |
| --------- | ------------------- |
| **`url`** | <code>string</code> |


#### Cookie


#### CookieOptions

| Prop      | Type                |
| --------- | ------------------- |
| **`url`** | <code>string</code> |

</docgen-api>
