package com.jbase.modules.weixin.client.keyword.web;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jbase.common.vo.ResultVo;
import com.jbase.common.web.BaseController;
import com.jbase.modules.weixin.client.ResponseHandle;
import com.jbase.modules.weixin.client.WeixinHttpClient;
import com.jbase.modules.weixin.entity.keyword.UserKeywords;
import com.jbase.modules.weixin.service.keyword.UserKeywordsService;
/**
 * 用户关键词的本地rest服务端
 * @author lenovo-pc
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/weixin/keyword/lanrest")
public class UserKeywordsLanRestServer extends BaseController  {
	@Autowired
	private UserKeywordsService userKeywordsService;
	
	@RequestMapping(value = "add")
	public String add(UserKeywords userKeywords, HttpServletResponse response) {
		ResultVo retVo = new ResultVo();
		retVo.setRet(1);
		try {
			userKeywords.setKeyword(URLDecoder.decode(userKeywords.getKeyword(),"utf-8"));
			userKeywords.setAddtime(new Timestamp(System.currentTimeMillis()));
			userKeywordsService.save(userKeywords);
			logger.info("保存微信关键词成功");
			//根据关键词爬取内容
			Map<String, List<Map<String, Object>>> crawlByUserKeywords = crawlByUserKeywords(userKeywords);
			retVo.setData(crawlByUserKeywords);
			retVo.setCode("keyword_add_success");
			retVo.setMsg("success to add weixin user keyword.");
		} catch (Exception e) {
			logger.error("保存微信关键词失败", e);
			retVo.setCode("keyword_add_fail");
			retVo.setMsg("fail to add weixin user keyword.");
		}
		return renderString(response, retVo);
	}
	
	/**
	 * 根据用户名查询用户关键词，并根据关键词爬取内容返回给客户端
	 * @param userKeywords
	 * @return
	 */
	private Map<String, List<Map<String, Object>>> crawlByUserKeywords(UserKeywords userKeywords) {
		List<UserKeywords> list = userKeywordsService.findList(userKeywords);
		if (list != null && list.size() > 0) {
			//关键词-爬取到的内容集合
			Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String,Object>>>();
			for (UserKeywords userKeyword : list) {
				String keyword = userKeyword.getKeyword();
				//根据关键词爬取内容
				List<Map<String, Object>> results = crawlBykeyword(keyword);
				map.put(keyword, results);
			}
			return map;
		}
		return null;
	}
	/**
	 * 根据关键词爬取内容
	 * @param keyword
	 * @return
	 */
	private List<Map<String, Object>> crawlBykeyword(String keyword) {

		final List<Map<String, Object>> ret = new ArrayList<Map<String,Object>>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("wd", keyword);
		
		try {
			WeixinHttpClient.doGet("https://www.baidu.com/s", params, new ResponseHandle() {
				@Override
				protected void handle(CloseableHttpResponse httpResponse) {
					handleResponse(httpResponse, ret);
				}
			});
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
	private void handleResponse(CloseableHttpResponse httpResponse, List<Map<String, Object>> ret) {
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
