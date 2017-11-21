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
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;

import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.NoSuchElementException;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class SimulatedLogin {
    public static void main(String[] args) throws InterruptedException {

        String url = "https://authem14.alipay.com/login/index.htm";
        final String cookieValue = "...";


        BrowserMobProxy proxy = new BrowserMobProxyServer();
        proxy.start(8181);
        // get the Selenium proxy object
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

        // configure it as a desired capability
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);

        // 使用chrome
//        System.setProperty("webdriver.chrome.driver","F:\\Git\\Payment-Tips\\paying\\lib\\chromedriver.exe");
//        WebDriver driver = new ChromeDriver();

        // 使用FireFox
//        System.setProperty("webdriver.firefox.bin","firefox.exe");
        // 启动firefox的配置
//        FirefoxProfile fp = new FirefoxProfile();
//        WebDriver driver = new FirefoxDriver(fp);
        // 打开一个干净的firefox
//        WebDriver driver = new FirefoxDriver();

        // 使用PhantomJS
        System.setProperty("phantomjs.binary.path","");
        WebDriver driver = new PhantomJSDriver();

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
        SimulatedLogin.wait_input(elemUsername,"....");
        Thread.sleep(1);

        //找到密码输入栏
        WebElement elemPassword = driver.findElement(By.name("password_rsainput"));
        SimulatedLogin.wait_input(elemPassword,"....");
        Thread.sleep(1);

        // 获取页面元素:点击确认按钮
        WebElement elemSubmit = driver.findElement(By.id("J-login-btn"));
        Thread.sleep(2);
        elemSubmit.click();

        // 等待加载完成
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);



        if(!SimulatedLogin.isElemAppear(driver,"amount-pay")){
            System.out.println("请输入手机验证码");
            proxy.stop();
            driver.quit();
            return;
        }
        // 每秒刷新一次界面，并获取数据
        SimulatedLogin.task(driver);

    }

    /***
     *
     * "在输入用户名和密码的时候不能一次性输入，要模拟人的动作，增大每次输入的间隔时间"
     * @param elem "输入内容所在位置"
     * @param content "输入的内容"
     * @throws InterruptedException
     */
    private static void wait_input(WebElement elem,String content) throws InterruptedException {
        char[] str = content.toCharArray();
        for (int i=0;i<str.length;i++){
            String c = String.valueOf(str[i]);
            elem.sendKeys(c);
            Thread.sleep(1);
        }
    }


    /***
     *
     * 定时任务，每隔一秒刷新一次界面，获取最新的交易记录
     * @param driver "浏览器驱动"
     */
    private static void task(WebDriver driver){
        final WebDriver drivers = driver;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //找到第一条交易记录的位置
                WebElement elemAmount = drivers.findElements(By.className("amount-pay")).get(0);
                // 获取交易金额
                String amountValue = elemAmount.getText();
                System.out.println(amountValue);
                // 刷新界面
                drivers.navigate().refresh();
                // 等待加载完成
                drivers.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

            }
        };
        Timer timer = new Timer();
        long delay = 1000;
        long period = 1000;
        timer.scheduleAtFixedRate(task,delay,period);
    }

    /***
     *
     * 检测元素是否存在
     *
     * @param driver "浏览器驱动"
     * @param elem "要测试的元素"
     * @return "存在则返回yes，不存在则返回false
     */
    private static boolean isElemAppear(WebDriver driver,String elem){
        try{
            driver.findElement(By.className(elem));
            return true;
        }catch (NoSuchElementException e){
            return false;
        }
    }
}
