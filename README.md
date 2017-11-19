# Payment-Tips
这是一个自动登陆某网站的程序，主要使用了自动化测试工具Selenium和代理BrowserMob-Proxy

# 一、前期尝试
因为以前没有做过模拟登录的内容，所以一开始上手就是各种查资料，然后首先找到了一种模拟登录的方式：使用HtmlUnit，但是这个方法并没有成功，后来又找到了另一种方式：使用自动测试工具Selenium

## By HtmlUnit / [By HtmlUnit](https://github.com/YangDanXia/Payment-Tips/tree/master/By%20HtmlUnit)  
  HtmlUnit 是一款开源的java 页面分析工具，读取页面后，可以有效的使用htmlunit分析页面上的内容。项目可以模拟浏览器运行，被誉为java浏览器的开源实现。是一个没有界面的浏览器，运行速度迅速。
  由于HtmlUnit是无界面操作，所以在执行程序时并不知道程序的执行过程，在执行完程序后就提示：网络异常（理想的结果应该是提示登录成功）。
  查阅了资料，有说是因为需要登录的网站为防止机器操作，有严格的认证机制：
  - 输入账户和密码不能过快或过慢
  - 点击按钮时间不能过长或过短
  - 对headers的严格把控
  所以为了解决以上问题，我尝试找了解决方法，但并没有找到相关的使用HtmlUnit的解决方法。无意间发现了测试工具Selenium，于是决定用这个工具尝试一次。
  
## By Selenium / [By Selenium](https://github.com/YangDanXia/Payment-Tips/tree/master/By%20Selenium)  
  Selenium 是一个用于Web应用程序测试的工具。Selenium测试直接运行在浏览器中，就像真正的用户在操作一样。支持的浏览器包括IE（7, 8, 9, 10, 11），Mozilla Firefox，Safari，Google Chrome，Opera等。
  与其他测试工具相比，使用 Selenium 的最大好处是：
  - Selenium 测试直接在浏览器中运行，就像真实用户所做的一样。Selenium 测试可以在 Windows、Linux 和 Macintosh上的 Internet Explorer、Mozilla 和 Firefox 中运行。其他测试工具都不能覆盖如此多的平台。
  - 通过编写模仿用户操作的 Selenium 测试脚本，可以从终端用户的角度来测试应用程序。通过在不同浏览器中运行测试，更容易发现浏览器的不兼容性。
所以在这里我是利用了Selenium的测试功能实现了模拟登录的过程。
  在使用Selenium后，成功的进入了网站的下一个界面了，但是第一次登录强制一定要输入手机验证码，因此要完全实现登录进该网站，必须修改Headers,自行在Headers添加cookies，但是Selenium并不允许修改Headers内容，所以我找到了使用代理的方法来修改Headers。
  
  
## 二、正式开始 / [By Selenium And BrowserMob-Proxy](https://github.com/YangDanXia/Payment-Tips/tree/master/paying)    
BrowserMob-Proxy是一个不错的代理，可以直接下载JAR包后导入到项目内，也可以在Maven内添加依赖关系。  
JAR下载地址：[JAR包](http://bmp.lightbody.net/)    
GitHub项目说明：[BrowserMob-Proxy的使用说明](https://github.com/lightbody/browsermob-proxy)

添加Selenium和BrowserMob-Proxy的依赖关系：  
````
<dependency>
   <groupId>org.seleniumhq.selenium</groupId>
   <artifactId>selenium-java</artifactId>
   <version>2.16.1</version>
</dependency>
<dependency>
   <groupId>net.lightbody.bmp</groupId>
   <artifactId>browsermob-core</artifactId>
   <version>2.1.5</version>
</dependency>
 ````
在本程序里我使用的是有界面浏览器Chrome，可自行选择其他浏览器的驱动或者是无界面的PhantomJS.  
在这里为什么我要使用代理呢？是因为我需要修改header，需要在请求头里添加,而Selenium本身是不支持修改header的，所以只能借助BrowserMob-Proxy来实现。
在header添加cookies的函数用法是：
````
 BrowserMobProxy proxy = new BrowserMobProxyServer();
 proxy.start(8181); //端口号自由选择
 proxy.addRequestFilter(new RequestFilter() {
    @Override
     public HttpResponse filterRequest(HttpRequest httpRequest, HttpMessageContents httpMessageContents, HttpMessageInfo httpMessageInfo) {
         httpRequest.headers().add("Cookie",cookieValue);
         return null;
            }
 });
 ````
<hr>
由于在下载Selenium 和 BrowserMob-Proxy 的jar包时会自动下载与其有关的其他依赖包，当编译执行程序时就会有错误提示，若要解决错误，还需要添加以下两个依赖关系：
````
<dependency>
   <groupId>org.slf4j</groupId>
   <artifactId>slf4j-log4j12</artifactId>
   <version>1.7.21</version>
</dependency>
<dependency>
   <groupId>com.google.guava</groupId>
   <artifactId>guava</artifactId>
   <version>21.0</version>
</dependency>
````
详细参考：http://blog.csdn.net/qijiqiguai/article/details/73930140?locationNum=14&fps=1

## 三、导出Maven项目
在完成了项目后，我们可以将项目导出放在服务器上或者其他电脑上执行。在这里我用的是maven-assembly-plugin插件进行打包。要使用该插件，需要配置pom.xml文件：
````
 <build>
   <plugins>
     <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-assembly-plugin</artifactId>
        <version>2.5.5</version>
        <configuration>
          <archive>
            <manifest>
              <mainClass> 
                 com.SimulatedLogin // 执行的主类
              </mainClass>
            </manifest>
          </archive>
          <descriptorRefs>
             <descriptorRef>
                  jar-with-dependencies // 在版本名版本号后添加的名字
             </descriptorRef>
          </descriptorRefs>
        </configuration>
      </plugin>
   </plugins>
 </build>
````
打开电脑终端Win+R,进入到工程的根目录，输入：
`mvn package assembly:single`
打包后会在target目录下生成一个xxx-jar-with-dependencies.jar文件，这个文件不但包含了自己项目中的代码和资源，还包含了所有依赖包的内容。所以可以直接通过java -jar来运行。  
详细参考：http://blog.csdn.net/xiao__gui/article/details/47341385
