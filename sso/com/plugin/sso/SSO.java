package com.plugin.sso;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;


public class SSO {
	public static String getUrl(String url,String username,String password) throws Exception{
		
		
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet httpGet=new HttpGet(url);
		HttpClientContext context = new HttpClientContext();
		HttpResponse httpResponse=null;
		try {
			httpResponse = client.execute(httpGet,context);
		} catch (IOException e) {
			throw new Exception("https Certificate error!");
			// TODO Auto-generated catch block
		}
		//��һ�η���  �ض���URL
		String url_prefix = context.getTargetHost().toURI();
     	String uri = context.getRequest().getRequestLine().getUri();
     	String tour = url_prefix+uri;
		System.out.println(tour);
		//�ж��Ƿ�Ϊ�ض���
		if(!tour.startsWith(url)){
			//��һ�η��� �õ�ҳ����Ϣ
			HttpEntity entity = httpResponse.getEntity();
			String result = EntityUtils.toString(entity);
			CasLoginPage loginPage = new CasLoginPage(result);
			url_prefix+=loginPage.getFormUrl();
			//�ڶ��η��� �����û��������ύ
			HttpPost httpPost = new HttpPost(url_prefix);
			httpPost.setEntity(loginPage.fillForm(username, password));
			client.execute(httpPost, context);
			//����η��� �ύticket �����������֤
			httpResponse =client.execute(httpGet,context);
			//�ӷ�����Ϣ�л�ȡcookie �Ӷ��ȡ�Ựid
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
		
		String path ="http://localhost:80/FineR/ReportServer;jsessionid=718D4E0DEC9AF7B01198BC5602F2ADC3";
		String[] s = path.split(";");
		System.out.println(s[0]);
		try {
			String url = getUrl("http://localhost/ServletDemo", "s", "s");
			System.out.println("gggg"+url);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}
	
}
