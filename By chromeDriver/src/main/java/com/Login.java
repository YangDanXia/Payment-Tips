package com;


import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.filters.RequestFilter;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.IndexOutOfBoundsException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Login{

    private  WebDriver driver;
    private BrowserMobProxy proxy;
    private String cookieValue="";


    public Login(WebDriver driver,BrowserMobProxy proxy){
        this.driver = driver;
        this.proxy = proxy;
    }

    public void urlLogin(){
        String url = "...";
        try {
            driver.get(url);
           System.out.println("Enter Successfully ! ");
//           选择账密登录的方法
           WebElement elemLoginMethod = driver.findElement(By.id("J-loginMethod-tabs")).findElements(By.tagName("li")).get(1);
           Thread.sleep(2);
           elemLoginMethod.click();

//         在界面找到用户名/密码输入栏
           Login.findElemToInput(driver,"logonId","...");
           Login.findElemToInput(driver,"password_rsainput","...");

         // 获取页面元素:点击确认按钮
           Login.findElemToClick(driver,By.id("J-login-btn"));

//           当当前网站还处于loginResultDispatch.htm或者 index.htm 则表示还未跳转成功
           do{
             System.out.println("Jumping...");
           }while(driver.getCurrentUrl().indexOf("loginResultDispatch")>0||driver.getCurrentUrl() == url);

           WebDriverWait wait = new WebDriverWait(driver, 3);
           wait.until(ExpectedConditions.urlContains("checkSecurity"));
           String secureURL = driver.getCurrentUrl();
           System.out.println(secureURL);

           System.out.println("Input Your Riskcode:");
           Scanner scan = new Scanner(System.in);
           if(scan.hasNext()) {
             try {
                 String code = scan.next();
                 driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
                 //找到验证码输入栏
                 Login.findElemToClick(driver,By.name("riskackcode"));
                 Login.findElemToInput(driver, "riskackcode", code);
                 scan.close();

                 // 获取页面元素:点击确认按钮
                 Login.findElemToClick(driver,By.className("ui-button"));
//                 当当前网站还处于loginResultDispatch.htm或者 checkSecurity.htm 则表示还未跳转成功
                 do{
                   System.out.println("Jumping...");
                   System.out.println("80:"+ driver.getCurrentUrl() ==secureURL);
                 }while(driver.getCurrentUrl().indexOf("loginResultDispatch")>0 || driver.getCurrentUrl() ==secureURL);
                 System.out.println("Login Successfully !");

                 } catch(TimeoutException e){
                     e.printStackTrace();
                     System.exit(0);
                 }
             }
            // 获取header中cookie的值
            Set<Cookie> cookies = driver.manage().getCookies();
            for (Cookie cookie : cookies) {
                cookieValue += cookie.getName() + "=" + cookie.getValue() + ";";
            }

           Login login = new Login(driver,proxy);
           login.task();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch(TimeoutException e){
            e.printStackTrace();
            System.exit(0);
       }
    }




    /***
     *
     * 定时任务，每隔一秒刷新一次界面，获取最新的交易记录
     */
    protected void task() throws InterruptedException {
        final int[] i = {0};
        TimerTask task = new TimerTask() {
          @Override
          public void run() {
            try{

               //找到第一条交易记录的位置
               WebElement elemAmount = driver.findElements(By.className("amount-pay")).get(0);
               // 获取交易金额
               String amountValue = elemAmount.getText();
               System.out.println(new SimpleDateFormat("mm:ss:SSS").format(new Date())+":");
               System.out.println(amountValue);
//              // 刷新界面
//               driver.navigate().refresh();
              // 等待加载完成
//               driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
                    String current_url = driver.getCurrentUrl();
                    driver.get(current_url);
            }catch (IndexOutOfBoundsException e){
               e.printStackTrace();
               String current_url = driver.getCurrentUrl();
               driver.get(current_url);
                ++i[0];
               if(i[0] == 2){
                   System.exit(0);
               }
            }catch (NullPointerException e){
                e.printStackTrace();
                System.exit(0);
            }
          }
        };

        Timer timer = new Timer();
        long delay = 0;
        long period = 1000;
        timer.scheduleAtFixedRate(task,delay,period);
    }



    /***
     *
     * "在输入用户名和密码的时候不能一次性输入，要模拟人的动作，增大每次输入的间隔时间"
     * @param elem "输入内容所在位置"
     * @param content "输入的内容"
     * @throws InterruptedException
     */
    private static void wait_input(WebElement elem, String content) throws InterruptedException {
        char[] str = content.toCharArray();
        for (int i=0;i<str.length;i++){
            String c = String.valueOf(str[i]);
            elem.sendKeys(c);
            Thread.sleep(100);
        }
    }

    /***
     *
     * 找到元素并输入值
     *
     * @param driver "浏览器驱动"
     * @param name "需要的元素"
     * @param value "输入的值"
     */
    private static void findElemToInput(WebDriver driver,String name,String value){
        //找到验证码输入栏
        WebElement elem= driver.findElement(By.name(name));
        try {
            Login.wait_input(elem,value);
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /***
     *
     * 找到元素并点击
     *
     * @param driver "浏览器驱动"
     * @param by "需要的元素"
     */
    private static void findElemToClick(WebDriver driver,By by){
        WebElement elem = driver.findElement(by);
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        elem.click();
    }
}
