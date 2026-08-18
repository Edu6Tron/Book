# WebView Viewport Rendering Notes

## Purpose

This note records the Android platform guidance consulted while correcting the embedded official YouTube page’s broken horizontal rendering on portrait Android devices. It applies only to the provider WebView and does not alter provider page content, controls, advertising, or media delivery.

## Platform finding

Android documents that the viewport is the horizontal CSS-pixel area exposed to a page, which can differ from the physical screen width. Android browsers commonly use a wide viewport of about 980 CSS pixels, while a `WebView` does not enable wide viewport mode by default. Android further recommends a page viewport width of `device-width` for a mobile layout that fits the device screen.

For the embedded official provider page, the app retains the default non-wide viewport behavior, explicitly disables overview scaling, preserves a normal 100% text scale, and removes only the `wv`/`Version/4.0` markers from the standard Android WebView user agent so YouTube supplies its ordinary mobile-web page. The app does not inject, restyle, or otherwise modify provider content. These settings must be applied before the initial provider URL load.

## Source

1. Android Developers, [Support different screens in web apps](https://developer.android.com/develop/ui/views/layout/webapps/targeting), accessed 18 August 2026.
