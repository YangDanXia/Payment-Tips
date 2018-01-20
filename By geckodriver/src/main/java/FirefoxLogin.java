
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;


public class FirefoxLogin {
    public static void main(String[] args){
//    使用firefox
        FirefoxBinary firefoxBinary = new FirefoxBinary();
//        firefoxBinary.addCommandLineOptions("--headless");
        System.setProperty("webdriver.gecko.driver", "F:\\Git\\Payment-Tips\\By geckodriver\\lib\\geckodriver.exe");
//        System.setProperty("webdriver.gecko.driver", "geckodriver");


        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("permissions.default.image", 2);
        profile.setPreference("accept_untrusted_certs", "true");
        profile.setPreference("extensions.firebug.allPagesActivation","off");


        BrowserMobProxy proxy = new BrowserMobProxyServer();
        proxy.start(8181);
        // get the Selenium proxy object
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBinary(firefoxBinary);
        firefoxOptions.setProfile(profile);
        firefoxOptions.setCapability(CapabilityType.PROXY,seleniumProxy);

        FirefoxDriver driver = new FirefoxDriver(firefoxOptions);
        Login log = new Login(driver,proxy);
        log.urlLogin();
    }


}
