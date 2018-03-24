# Payment-Tips
这是一个自动登陆某网站的程序，主要使用了自动化测试工具Selenium和谷歌浏览器Chrome、Chromium

## 第一个版本的说明
[V1.0.0](https://github.com/YangDanXia/Payment-Tips/blob/master/V1.0.0.md)
在第一次尝试实现功能的时候，我使用的是Selenium+BrowserMob-Proxy的方法。当时使用这个方法，是因为在Selenium里没有发现能使用cookie的函数，于是通过代理实现向Header
添加cookie，但是这样的实现方法却被网站拦截了（可能安全防范意识较低的网站不会拦截），所以后来我尝试了别的方法，就是为了解决cookie的问题。
