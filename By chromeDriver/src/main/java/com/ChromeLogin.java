package com;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import sun.rmi.runtime.Log;

import java.io.IOException;

public class ChromeLogin {
    public static void main(String[] args) throws InterruptedException, IOException {
        System.setProperty("webdriver.chrome.driver","F:\\Git\\Payment-Tips\\By chromeDriver\\lib\\chromedriver.exe");
//        System.setProperty("webdriver.chrome.driver","chromedriver");

        BrowserMobProxy proxy = new BrowserMobProxyServer();
        proxy.start(8181);
//        // 设置header中cookie的值
//        proxy.addRequestFilter(new RequestFilter() {
//            @Override
//            public HttpResponse filterRequest(HttpRequest httpRequest, HttpMessageContents httpMessageContents, HttpMessageInfo httpMessageInfo) {
//                //  httpRequest.headers().add("Cookie",cookieValue);
//                String cookieValue= httpRequest.headers().get("Cookie");
//                System.out.println("COOKIE:"+cookieValue);
//                return null;
//            }
//        });
        // get the Selenium proxy object
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.addArguments("--headless");
//        chromeOptions.setCapability(CapabilityType.PROXY,seleniumProxy);


        WebDriver driver = new ChromeDriver(chromeOptions);
        Login log = new Login(driver,proxy);
        log.urlLogin();
    }

}
