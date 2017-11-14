package com;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class zhiFuBaoSpider {

    public static void main(String[] args) throws InterruptedException {
        String url = "https://authem14.alipay.com/login/index.htm";
        System.setProperty("webdriver.chrome.driver","F:\\Git\\zhiFuBaoWebSpider\\lib\\chromedriver_win32\\chromedriver.exe");

//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("User-Agent': ' Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
//        WebDriver driver = new ChromeDriver(options);
        WebDriver driver = new ChromeDriver();


        // 加载url
        driver.get(url);
        // 等待加载完成
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        //在界面找到用户名输入栏
        WebElement elemUsername = driver.findElement(By.name("logonId"));
        System.out.println("正在输入用户名");
        zhiFuBaoSpider.wait_input(elemUsername,"...");
        Thread.sleep(1);

        //找到密码输入栏
        WebElement elemPassword = driver.findElement(By.name("password_rsainput"));
        System.out.println("正在输入密码");
        zhiFuBaoSpider.wait_input(elemPassword,"...");
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
