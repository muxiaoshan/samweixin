package com.jbase.modules.weixin.client;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;

public class WeixinHttpClient {
	private static CloseableHttpClient httpClient;
	static {
		// 配置请求的超时设置
        RequestConfig requesftConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(3000)
                .setConnectTimeout(3000)
                .setSocketTimeout(3000)
                .build();
        ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
        LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", plainsf)
                .register("https", sslsf)
                .build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        // 将最大连接数增加到200
        cm.setMaxTotal(200);
        // 将每个路由基础的连接增加到20
        cm.setDefaultMaxPerRoute(20);
        //请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception,int executionCount, HttpContext context) {
                if (executionCount >= 5) {// 如果已经重试了5次，就放弃                    
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试                    
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常                    
                    return false;
                }                
                if (exception instanceof InterruptedIOException) {// 超时                    
                    return false;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达                    
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝                    
                    return false;
                }
                if (exception instanceof SSLException) {// ssl握手异常                    
                    return false;
                }
                 
                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {                    
                    return true;
                }
                return false;
            }
        };  
        httpClient = HttpClients.custom()
        		.setDefaultRequestConfig(requesftConfig)
                .setConnectionManager(cm)
                .setRetryHandler(httpRequestRetryHandler)
                .build();
	}
	public static CloseableHttpClient getHttpClient() {
		return httpClient;
	}
	public static void doGet(String url, Map<String, Object> params, ResponseHandle responseHandle) throws ClientProtocolException, IOException {
		url += "?";
		if (params != null && params.size() > 0) {
			for (Entry<String, Object> entry : params.entrySet()) {
				url += "&" + entry.getKey() + "=" + entry.getValue();
			}
		}
		HttpGet get = new HttpGet(url);
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(get);
			responseHandle.handle(httpResponse);
		} finally {
			if (httpResponse != null) {
				try {
					httpResponse.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void doPost(String url, Map<String, Object> params, ResponseHandle responseHandle) throws ClientProtocolException, IOException {
		CloseableHttpResponse httpResponse = null;
		try {
            HttpPost post = new HttpPost(url);          //这里用上本机的某个工程做测试
            if (params != null && params.size() > 0) {
            	//创建参数列表
            	List<NameValuePair> list = new ArrayList<NameValuePair>();
            	for (Entry<String, Object> entry : params.entrySet()) {
            		list.add(new BasicNameValuePair(entry.getKey(), (String) entry.getValue()));
            	}
            	//url格式编码
            	UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(list,"UTF-8");
            	post.setEntity(uefEntity);
            }
            System.out.println("POST 请求...." + post.getURI());
            //执行请求
            httpResponse = httpClient.execute(post);
            responseHandle.handle(httpResponse);
        } 
        finally{
        	if (httpResponse != null) {
        		try{
        			httpResponse.close();               
        		} catch(IOException e){
        			e.printStackTrace();
        		}
        	}
        }
	}
}
