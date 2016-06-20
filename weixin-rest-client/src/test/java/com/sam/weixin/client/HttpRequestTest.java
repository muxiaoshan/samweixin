package com.sam.weixin.client;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.jbase.modules.weixin.client.ResponseHandle;
import com.jbase.modules.weixin.client.WeixinHttpClient;

public class HttpRequestTest {

	@Test
	public void testGetRequest() {
		try {
			WeixinHttpClient.doGet("http://www.baidu.com", null, new ResponseHandle() {
				
				@Override
				protected void handle(CloseableHttpResponse httpResponse) {
					handleResponse(httpResponse);
				}
			});
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testGetRequestPhpServer() {
		try {
			WeixinHttpClient.doGet("http://localhost/samweixinweb/keywordsHandleServer.php/list/", null, new ResponseHandle() {
				
				@Override
				protected void handle(CloseableHttpResponse httpResponse) {
					handleResponse(httpResponse);
				}
			});
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void handleResponse(CloseableHttpResponse httpResponse) {
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				try {
					String responseStr = EntityUtils.toString(httpEntity);
					System.out.println("responseStr:" + responseStr);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
