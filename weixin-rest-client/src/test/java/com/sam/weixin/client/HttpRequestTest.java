package com.sam.weixin.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
	@Test
	public void testSearchBaidu() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("wd", "长沙小吃");
		try {
			WeixinHttpClient.doGet("https://www.baidu.com/s", params, new ResponseHandle() {
				
				@Override
				protected void handle(CloseableHttpResponse httpResponse) {
					List<Map<String, Object>> ret = new ArrayList<Map<String,Object>>();
					handleResponseOfBaidu(httpResponse, ret);
					System.out.println("result:" + ret);
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
	private void handleResponseOfBaidu(CloseableHttpResponse httpResponse, List<Map<String, Object>> ret) {
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				try {
					String responseStr = EntityUtils.toString(httpEntity);
					Document jsoup = Jsoup.parse(responseStr);
					Elements result = jsoup.getElementsByAttributeValueContaining("class", "result c-container");
					if (result != null && result.size() > 0) {
						for (Element element : result) {
							Map<String, Object> map = new HashMap<String, Object>();
							Elements titleElements = element.getElementsByAttributeValueContaining("class", "t");
							if (titleElements != null && titleElements.size() > 0) {
								map.put("title", titleElements.get(0).text());
							}
							Elements abstractElements = element.getElementsByAttributeValueContaining("class", "c-abstract");
							if (abstractElements != null && abstractElements.size() > 0) {
								map.put("abstract", abstractElements.get(0).text());
							}
							ret.add(map);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
