package com;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChromeLogin {

    static Logger LOGGER = LoggerFactory.getLogger(ChromeLogin.class);
    private static String url = "";

    public static void main(String[] args){
//        配置浏览器
        Configure con = new ConfigureChrome();
        con.initProperty("webdriver.chrome.driver","chromedriver.exe");
        ChromeOptions options = con.ChromeOptions();

//        启动浏览器
        Operation oper = new DetailOperation();
        WebDriver driver = new ChromeDriver(options);
        oper.setDriver(driver);
        oper.setUrl(url);
        oper.signIn();
    }
}
