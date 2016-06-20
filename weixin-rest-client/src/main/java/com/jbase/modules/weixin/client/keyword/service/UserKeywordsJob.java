package com.jbase.modules.weixin.client.keyword.service;

import java.io.IOException;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.jbase.modules.weixin.client.ResponseHandle;
import com.jbase.modules.weixin.client.WeixinHttpClient;
/**
 * 用户关键词处理定时任务，从服务器上读取用户关键词
 * 暂时不用
 * @see com.jbase.modules.weixin.client.keyword.web.web.UserKeywordsLocalRestServer
 * @author lenovo-pc
 *
 */
public class UserKeywordsJob extends QuartzJobBean{
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	public void query(){
		log.info(" log "+new Date());
		//查询最新用户关键词
		try {
			WeixinHttpClient.doGet("http://samweixinweb.applinzi.com/keywordsHandleServer.php/list/", null, new ResponseHandle() {
				
				@Override
				protected void handle(CloseableHttpResponse httpResponse) {
					handleResponse(httpResponse);
				}
			});
		} catch (IOException e) {
			log.error("查询用户关键词失败", e);
		}
	}
	private void handleResponse(CloseableHttpResponse httpResponse) {
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				try {
					String responseStr = EntityUtils.toString(httpEntity);
					log.info("responseStr:" + responseStr);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		query();
	}
}