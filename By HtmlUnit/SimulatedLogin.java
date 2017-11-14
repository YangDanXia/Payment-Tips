package zhiFuBaoSpider;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class SimulatedLogin {
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		// 创建模拟浏览器，参数可以指定不同类型的浏览器
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		//JS和CSS会对解析有影响
		webClient.getOptions().setUseInsecureSSL(true);//接受任何主机连接 无论是否有有效证书  
		webClient.getOptions().setCssEnabled(false);//关闭css
		webClient.getOptions().setJavaScriptEnabled(true);//关闭js
		webClient.getOptions().setThrowExceptionOnScriptError(false);//js运行错误时不抛出异常  
		webClient.getOptions().setTimeout(5000);//设置连接超时时间  
		webClient.getOptions().setDoNotTrackEnabled(false); 
		// 模拟浏览器进入url，并获取url内的html内容赋值给page，可以进一步的解析数据
		HtmlPage page = webClient.getPage("https://www.alipay.com/");
		// 设置超时
		webClient.setJavaScriptTimeout(5000);
		//解决异常警告
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",    "org.apache.commons.logging.impl.NoOpLog");
		 java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit")  
         .setLevel(Level.OFF);  
		 java.util.logging.Logger.getLogger("org.apache.commons.httpclient")  
         .setLevel(Level.OFF);  

		//点击进入登录界面
		page = page.getElementsByTagName("a").get(0).click();

	    //获取账户栏位置
		HtmlElement userName = page.getElementByName("logonId");
		//密码栏是动态生成的，需要先执行JS后才会出现在源码里
		HtmlElement password = page.getElementByName("password_rsainput");//动态界面
  	   //输入账户信息
		userName.focus();
		userName.type("******");
		
		System.out.println(userName.asXml());
		
        password.focus();
		password.type("******");
		System.out.println(password.asXml());
		
		//找到登录按钮
		HtmlElement submit = (HtmlElement) page.getElementById("J-login-btn");
		//点击登录
		page = submit.click();
        String error =  page.getElementById("J-errorBox").asXml();
        System.out.println(error);
		
	}
}
