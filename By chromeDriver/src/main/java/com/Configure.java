package com;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

public abstract class Configure {

    public ChromeOptions ChromeOptions(){
       return null;
    }

    public FirefoxOptions FirefoxOptions(){
        return null;
    }

    /***
     * 初始化操作；配置Log4j日志组件，并设置使用的浏览器驱动
     * @param driverType 使用的浏览器驱动类型
     * @param driverPath  驱动所在路径
     * @throws InterruptedException
     */
    public abstract void initProperty(String driverType,String driverPath);

}
