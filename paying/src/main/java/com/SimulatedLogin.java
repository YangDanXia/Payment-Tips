package com;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.filters.RequestFilter;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.concurrent.TimeUnit;


public class SimulatedLogin {
    public static void main(String[] args) throws InterruptedException {

        String url = "https://authem14.alipay.com/login/index.htm";
        final String cookieValue = "****";


        BrowserMobProxy proxy = new BrowserMobProxyServer();
        proxy.start(8181);
        // get the Selenium proxy object
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

        // configure it as a desired capability
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);

        System.setProperty("webdriver.chrome.driver","F:\\Git\\Payment-Tips\\paying\\lib\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        proxy.addRequestFilter(new RequestFilter() {
            @Override
            public HttpResponse filterRequest(HttpRequest httpRequest, HttpMessageContents httpMessageContents, HttpMessageInfo httpMessageInfo) {
                httpRequest.headers().add("Cookie",cookieValue);
                return null;
            }
        });

        // 加载url
        driver.get(url);
//        // 等待加载完成
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        //在界面找到用户名输入栏
        WebElement elemUsername = driver.findElement(By.name("logonId"));
        SimulatedLogin.wait_input(elemUsername,"***");
        Thread.sleep(1);

        //找到密码输入栏
        WebElement elemPassword = driver.findElement(By.name("password_rsainput"));
        SimulatedLogin.wait_input(elemPassword,"****");
        Thread.sleep(1);

        // 获取页面元素:点击确认按钮
        WebElement elemSubmit = driver.findElement(By.id("J-login-btn"));
        Thread.sleep(2);
        elemSubmit.click();
    }

    private static void wait_input(WebElement elem,String content) throws InterruptedException {
        char[] str = content.toCharArray();
        for (int i=0;i<str.length;i++){
            String c = String.valueOf(str[i]);
            elem.sendKeys(c);
            Thread.sleep(1);
        }
    }
}
