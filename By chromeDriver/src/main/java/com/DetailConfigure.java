package com;

import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.chrome.ChromeOptions; 
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.util.HashMap;
import java.util.Map;

class ConfigureChrome extends Configure {

    public ChromeOptions ChromeOptions() {

        ChromeOptions chromeOptions = new ChromeOptions();
//        禁用扩展
        chromeOptions.addArguments("--disable-extensions");
//        使用无头模式运行
        chromeOptions.addArguments("--headless");
//        禁用GPU
        chromeOptions.addArguments("--display-gpu");
//        启动无沙盒模式运行
        chromeOptions.addArguments("--no-sandbox");
//        使用本地用户数据
        chromeOptions.addArguments("--profile-directory=Default");
//        chromeOptions.addArguments("user-data-dir=C:/Users/Administrator/AppData/Local/Google/Chrome/User Data");
//        chromeOptions.addArguments("user-data-dir=/home/ydx/.config/google-chrome"); // 腾讯云服务器上Debian操作系统
        chromeOptions.addArguments("user-data-dir=/home/godog/.config/chromium");
//         禁止加载图片
        Map<String,Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.managed_default_content_settings.images", 2);
        chromeOptions.setExperimentalOption("prefs",prefs);

        return chromeOptions;
    }


    public void initProperty(String driverType, String driverPath) {
        PropertyConfigurator.configure("Log4j.properties");
        System.setProperty(driverType,driverPath);
    }

}

class ConfigureFirefox extends Configure{

    public FirefoxOptions FirefoxOptions() {
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("permissions.default.image", 2);
        profile.setPreference("accept_untrusted_certs", "true");
        profile.setPreference("extensions.firebug.allPagesActivation","off");

        FirefoxBinary firefoxBinary = new FirefoxBinary();
//        firefoxBinary.addCommandLineOptions("--headless");

        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBinary(firefoxBinary);
        firefoxOptions.setProfile(profile);

        return firefoxOptions;
    }


    public void initProperty(String driverType, String driverPath) {
        PropertyConfigurator.configure("Log4j.properties");
        System.setProperty(driverType,driverPath);
    }
}
