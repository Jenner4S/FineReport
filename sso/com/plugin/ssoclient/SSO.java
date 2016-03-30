package com.plugin.ssoclient;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;


public class SSO {
	
//	 EnvListPane();
	
	private static CloseableHttpClient createHttpClient() throws NoSuchAlgorithmException,
	KeyManagementException {
		// 采用绕过验证的方式处理https请求
		SSLContext sslcontext = createIgnoreVerifySSL();

		// 设置协议http和https对应的处理socket链接工厂的对象
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
		.<ConnectionSocketFactory> create()
		.register("http", PlainConnectionSocketFactory.INSTANCE)
		.register("https", new SSLConnectionSocketFactory(sslcontext))
		.build();
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
		socketFactoryRegistry);
		HttpClients.custom().setConnectionManager(connManager);

// 创建自定义的httpclient对象
		CloseableHttpClient client = HttpClients.custom()
		.setConnectionManager(connManager)
		//禁用自动重定向
//		.disableRedirectHandling()
//		.setDefaultCookieStore(cookieStore)
				.build();
		return client;
	}

	public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
		SSLContext sc = SSLContext.getInstance("SSLv3");

		// 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
		X509TrustManager trustManager = new X509TrustManager() {

//			@Override
			public void checkClientTrusted(X509Certificate[] arg0, String arg1)
					throws java.security.cert.CertificateException {
				// TODO Auto-generated method stub
				
			}

//			@Override
			public void checkServerTrusted(X509Certificate[] arg0, String arg1)
					throws java.security.cert.CertificateException {
				// TODO Auto-generated method stub
				
			}

//			@Override
			public X509Certificate[] getAcceptedIssuers() {
				// TODO Auto-generated method stub
				return null;
			}
			
		};

		sc.init(null, new TrustManager[] { trustManager }, null);
		return sc;
	}

	
	
	public static String getUrl(String url,String username,String password) throws Exception{
		
		HttpClient client = SSO.createHttpClient();
		
		
		
		
		
//		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet httpGet=new HttpGet(url);
		HttpClientContext context = new HttpClientContext();
		HttpResponse httpResponse=null;
		try {
			httpResponse = client.execute(httpGet,context);
		} catch (IOException e) {
			throw new Exception("https Certificate error!");
			// TODO Auto-generated catch block
		}
		//第一次访问  重定向到URL
		String url_prefix = context.getTargetHost().toURI();
     	String uri = context.getRequest().getRequestLine().getUri();
     	String tour = url_prefix+uri;
		System.out.println(tour);
		//判断是否为重定向
		if(!tour.startsWith(url)){
			//第一次访问 得到页面信息
			HttpEntity entity = httpResponse.getEntity();
			String result = EntityUtils.toString(entity);
			CasLoginPage loginPage = new CasLoginPage(result);
			url_prefix+=loginPage.getFormUrl();
			//第二次访问 填完用户名密码提交
			HttpPost httpPost = new HttpPost(url_prefix);
			httpPost.setEntity(loginPage.fillForm(username, password));
			client.execute(httpPost, context);
			//第三次访问 提交ticket 报表服务器验证
			httpResponse =client.execute(httpGet,context);
			//从返回信息中获取cookie 从而获取会话id
			String sessionid = getSessionid(context,uri);
			return url+";jsessionid="+sessionid;
		}else{
			return url;
		}
	}
	private static String getSessionid(HttpClientContext context2,String uri) throws Exception {
		// TODO Auto-generated method stub
		
		List<Cookie> cookies=context2.getCookieStore().getCookies();
		if (!cookies.isEmpty()){
			for (int i = 0; i < cookies.size(); i++) {
				System.out.println(cookies.get(i));
				Cookie cookie = cookies.get(i);
//				System.out.println("cookkkkkkkkk"+cookie);
				System.out.println(uri+"   "+cookie.getPath());
				if (cookie.getName().equalsIgnoreCase("JSESSIONID")) {
					if(!uri.startsWith(cookie.getPath()))
						return cookie.getValue();
				}
			}
		}
		throw new Exception("Cookie is not exist");
	}

	
	public static void main (String[] st){
		try {
			String url = getUrl("http://localhost/FineR/", "s", "s");
			System.out.println("gggg"+url);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}
	
}
