package com;

import jni.SendMsg;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

public class DetailOperation extends Operation{

    private String logonId;
    private String password;

    @Override
    public void setUrl(String url) {
        driver = getDriver();
        driver.get(url);
    }

    @Override
    public void getInfo() {
        try{
            File paramFile = new File("info.properties");
            Properties props = new Properties();
            if(paramFile.exists()){
                props.load(new FileInputStream("info.properties"));
                logonId = props.getProperty("logonId");
                password = props.getProperty("password");
            }else{
                paramFile.createNewFile();
                System.out.println("Input Your ID:");
                Scanner scan = new Scanner(System.in);
                if(scan.hasNext()){
                    logonId = scan.next();
                    props.setProperty("logonId",logonId);
                }
                System.out.println("Input Your Password:");
                if(scan.hasNext()){
                    password = scan.next();
                    props.setProperty("password",password);
                }
                props.store(new FileOutputStream("info.properties"),"");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void signIn() {
        getInfo();
//        选择账密登录的方法
        WebElement elemLoginMethod = driver.findElement(By.id("J-loginMethod-tabs")).findElements(By.tagName("li")).get(1);
        try{
            Thread.sleep(2);
            elemLoginMethod.click();
//            在界面找到用户名/密码输入栏
            findElemToInput(driver,"logonId",logonId);
            findElemToInput(driver,"password_rsainput",password);
//            获取页面元素:点击确认按钮
            WebElement elemSubmit = driver.findElement(By.id("J-login-btn"));
            elemSubmit.click();

        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

//    @Override
//    public void checkSecurity() {
////        System.out.println("Input Your Riskcode:");
//        Scanner scan = new Scanner(System.in);
//        if(scan.hasNext()) {
//            try {
//                String code = scan.next();
////              找到验证码输入栏
//                WebElement elemInput = driver.findElement(By.name("riskackcode"));
//                Thread.sleep(2);
//                elemInput.click();
//                findElemToInput(driver, "riskackcode", code);
//                scan.close();
//
////              获取页面元素:点击确认按钮
//                WebElement elemSubmit = driver.findElement(By.className("ui-button"));
//                Thread.sleep(2);
//                elemSubmit.click();
//            } catch(InterruptedException e){
//                e.printStackTrace();
//            }
//        }
//    }

    @Override
    public void checkSecurity() {
        SendMsg sendmsg = new SendMsg();
        String code = sendmsg.ReceiveMsgFromC();
        if(code != null) {
            try {
                logger.info("The code:"+code);
//              找到验证码输入栏
                WebElement elemInput = driver.findElement(By.name("riskackcode"));
                Thread.sleep(2);
                elemInput.click();
                findElemToInput(driver, "riskackcode", code);

//              获取页面元素:点击确认按钮
                WebElement elemSubmit = driver.findElement(By.className("ui-button"));
                Thread.sleep(2);
                elemSubmit.click();
            } catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void task() {
        final String[] currentUrl = new String[1];
        TimerTask task = new TimerTask() {
        @Override
         public void run() {
           try{
               currentUrl[0] = driver.getCurrentUrl();
               WebDriverWait wait = new WebDriverWait(driver, 3);
//             找到第一条交易记录的位置
               WebElement elemAmount = driver.findElements(By.className("amount-pay")).get(0);
               wait.until(ExpectedConditions.attributeToBeNotEmpty(elemAmount,"class"));
//             获取交易金额
               String amountValue = elemAmount.getText();
               logger = getLogger();
               logger.info(amountValue);
//             刷新界面
               driver.navigate().refresh();
           }catch(IndexOutOfBoundsException e){
               logger.info("CurrentUrl:"+currentUrl[0]);
               logger.info("Reconnecting...");
               if(currentUrl[0].contains("auth.alipay.com")){
                   logger.info("The program was interrupted");
                   System.exit(0);
               }else if(currentUrl[0].contains("render.alipay.com")){
                   driver.get("https://my.alipay.com/portal/i.htm?referer=https%3A%2F%2Fauthem14.alipay.com%2Flogin%2Findex.htm");
               }else{
                   driver.get(currentUrl[0]);
               }
           }catch (Exception e){
               logger.error(e.toString());
               driver.quit();
               driver.close();
               System.exit(0);
           }
         }
      };
        Timer timer = new Timer();
        long delay = 1000;
        long period = 1000;
//         等待delay时间后执行任务，每隔period时间执行一次
        timer.scheduleAtFixedRate(task,delay,period);
    }

    @Override
    public void isEnter() {
        int percentage = 0;
        logger = getLogger();
        while(!driver.getCurrentUrl().contains("portal/i.htm")){
 //        当处于中转页面时
            while(driver.getCurrentUrl().indexOf("loginResultDispatch")>0){
                percentage +=10;
                logger.info("Jumping:"+percentage+"%");
            }
            while(driver.getCurrentUrl().contains("checkSecurity")&& !driver.getCurrentUrl().contains("i.htm")){
                logger.info("Located on the checkSecurity");
                checkSecurity();
            }
            while(driver.getCurrentUrl().contains("authem14.alipay.com/login/index.htm")){
                System.out.println(driver.findElement(By.className("sl-error-text")).getText());
                File paramFile = new File("info.properties");
                paramFile.delete();
                driver.findElement(By.name("logonId")).clear();
                signIn();
            }
        }
        logger.info("Login Successfully !");
    }
}

