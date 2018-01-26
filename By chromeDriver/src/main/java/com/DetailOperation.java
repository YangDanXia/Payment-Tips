package com;

import org.apache.http.client.utils.DateUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;

import java.io.*;
import java.util.Date;
import java.util.Scanner;
import java.util.Set;

public class DetailOperation extends Operation{

    @Override
    public void setUrl(String url) {
        driver = getDriver();
        driver.get(url);
    }

    @Override
    public void signIn() {

//        选择账密登录的方法
        WebElement elemLoginMethod = driver.findElement(By.id("J-loginMethod-tabs")).findElements(By.tagName("li")).get(1);

        try{
            Thread.sleep(2);
            elemLoginMethod.click();
//            在界面找到用户名/密码输入栏
//            findElemToInput(driver,"logonId","");
            findElemToInput(driver,"password_rsainput","");
//            获取页面元素:点击确认按钮
            WebElement elemSubmit = driver.findElement(By.id("J-login-btn"));
//            loadCookies();
//            Thread.sleep(100);
//            elemSubmit.click();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    @Override
    public void checkSecurity() {
        System.out.println("Input Your Riskcode:");
        Scanner scan = new Scanner(System.in);
        if(scan.hasNext()) {
            try {
                String code = scan.next();
//              找到验证码输入栏
                WebElement elemInput = driver.findElement(By.name("riskackcode"));
                Thread.sleep(2);
                elemInput.click();
                findElemToInput(driver, "riskackcode", code);
                scan.close();

//              获取页面元素:点击确认按钮
                WebElement elemSubmit = driver.findElement(By.className("ui-button"));
                Thread.sleep(2);
                elemSubmit.click();

//              当当前网站还处于loginResultDispatch.htm或者 checkSecurity.htm 则表示还未跳转成功
                do{
                    System.out.println("Jumping...");
                }while(driver.getCurrentUrl().indexOf("loginResultDispatch")>0);

                System.out.println("Login Successfully !");
            } catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void loadCookies() {
        driver.manage().deleteAllCookies();
        File file = new File("F:\\Git\\Payment-Tips\\By chromeDriver\\lib\\cookies.txt");
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line=br.readLine())!=null) {
                String[] cookieFilds = line.split(";");
                String name = cookieFilds[0].trim();
                String value = cookieFilds[1].trim();
                String domain = cookieFilds[2].trim();
                String path = cookieFilds[3].trim();
                Date expire =  DateUtils.parseDate(cookieFilds[4]);
                Cookie cookie = new Cookie(name, value,domain,path,expire);
                System.out.println(cookie);
                driver.manage().addCookie(cookie);
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("cookies文件不存在，调用saveCookie_mjd方法重新保存cookies");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void saveCookies() {
        //      获取header中cookie的值
        Set<Cookie> cookies = driver.manage().getCookies();
        File cookieFile = new File("F:\\Git\\Payment-Tips\\By chromeDriver\\lib\\cookies.txt");
        try{
            if(cookieFile.exists()){
                cookieFile.delete();
                cookieFile.createNewFile();
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(cookieFile));
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                String value = cookie.getValue();
                String domain = cookie.getDomain();
                String path = cookie.getPath();
                Date expiry = cookie.getExpiry();
                bw.write(name+";"+value+";"+domain+";"+path+";"+expiry+";");
                bw.newLine();
            }
            bw.flush();
            bw.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

