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
		// ����ģ�����������������ָ����ͬ���͵������
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		//JS��CSS��Խ�����Ӱ��
		webClient.getOptions().setUseInsecureSSL(true);//�����κ��������� �����Ƿ�����Ч֤��  
		webClient.getOptions().setCssEnabled(false);//�ر�css
		webClient.getOptions().setJavaScriptEnabled(true);//�ر�js
		webClient.getOptions().setThrowExceptionOnScriptError(false);//js���д���ʱ���׳��쳣  
		webClient.getOptions().setTimeout(5000);//�������ӳ�ʱʱ��  
		webClient.getOptions().setDoNotTrackEnabled(false); 
		// ģ�����������url������ȡurl�ڵ�html���ݸ�ֵ��page�����Խ�һ���Ľ�������
		HtmlPage page = webClient.getPage("https://www.alipay.com/");
		// ���ó�ʱ
		webClient.setJavaScriptTimeout(5000);
		//����쳣����
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",    "org.apache.commons.logging.impl.NoOpLog");
		 java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit")  
         .setLevel(Level.OFF);  
		 java.util.logging.Logger.getLogger("org.apache.commons.httpclient")  
         .setLevel(Level.OFF);  

		//��������¼����
		page = page.getElementsByTagName("a").get(0).click();

	    //��ȡ�˻���λ��
		HtmlElement userName = page.getElementByName("logonId");
		//�������Ƕ�̬���ɵģ���Ҫ��ִ��JS��Ż������Դ����
		HtmlElement password = page.getElementByName("password_rsainput");//��̬����
  	   //�����˻���Ϣ
		userName.focus();
		userName.type("******");
		
		System.out.println(userName.asXml());
		
        password.focus();
		password.type("******");
		System.out.println(password.asXml());
		
		//�ҵ���¼��ť
		HtmlElement submit = (HtmlElement) page.getElementById("J-login-btn");
		//�����¼
		page = submit.click();
        String error =  page.getElementById("J-errorBox").asXml();
        System.out.println(error);
		
	}
}
