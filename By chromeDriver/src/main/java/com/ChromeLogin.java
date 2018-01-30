package com;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeLogin {

    private static String url = "https://authem14.alipay.com/login/index.htm";

    public static void main(String[] args) throws InterruptedException{
//        配置浏览器
        Configure con = new ConfigureChrome();
//        con.initProperty("webdriver.chrome.driver","F:\\Git\\Payment-Tips\\By chromeDriver\\lib\\chromedriver.exe");
        con.initProperty("webdriver.chrome.driver", "chromedriver");

        ChromeOptions options = con.ChromeOptions();

//        启动浏览器
        Operation oper = new DetailOperation();
        WebDriver driver = new ChromeDriver(options);
        oper.setLogger("ChromeLogin.class");
        oper.setDriver(driver);
        oper.setUrl(url);
        oper.signIn();
        oper.isEnter();
        oper.task();
    }
}
