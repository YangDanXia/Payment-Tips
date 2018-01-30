package com;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Operation {

    protected WebDriver driver;
    protected Logger logger;

    public void setDriver(WebDriver driver) {
        this.driver = driver;
     }

    public WebDriver getDriver() {
         return driver;
     }

    public void setLogger(String className) {
        this.logger = LoggerFactory.getLogger(className);
    }

    public Logger getLogger(){
        return logger;
    }



    /**
     *  输入手机验证码通过安全验证
     *  @param url 需要打开的页面的链接
     */
    public abstract void setUrl(String url);

    /**
     *  获取用户的账号和密码
     */
    public void getInfo(){}

    /**
     *  输入账号密码进行登录
     */
    public void signIn(){}

    /**
     *  输入手机验证码通过安全验证
     */
    public void checkSecurity(){}

    /**
     *  每隔一秒刷新一次界面并获取数据
     */
    public void task(){}


    /**
     *  是否成功进入登录中心页面
     *  ①可能进入的是中转界面
     *  ②可能进入的是安全验证中心
     *  ③可能进入的是登录中心
     */
    public void isEnter(){}


    /***
     *
     * "在输入用户名和密码的时候不能一次性输入，要模拟人的动作，增大每次输入的间隔时间"
     * @param elem "输入内容所在位置"
     * @param content "输入的内容"
     * @throws InterruptedException
     */
    protected void wait_input(WebElement elem, String content) throws InterruptedException {
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
    protected void findElemToInput(WebDriver driver, String name, String value){
        //找到验证码输入栏
        WebElement elem= driver.findElement(By.name(name));
        elem.clear();
        try {
            wait_input(elem,value);
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
