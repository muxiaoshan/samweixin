package com.jbase.modules.weixin.client.keyword.web;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jbase.common.vo.ResultVo;
import com.jbase.common.web.BaseController;
import com.jbase.modules.weixin.entity.keyword.UserKeywords;
import com.jbase.modules.weixin.service.keyword.UserKeywordsService;
/**
 * 用户关键词的本地rest服务端
 * @author lenovo-pc
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/weixin/keyword/localrest")
public class UserKeywordsLocalRestServer extends BaseController  {
	@Autowired
	private UserKeywordsService userKeywordsService;
	
	
	
	@RequestMapping(value = "add")
	public String add(UserKeywords userKeywords, HttpServletResponse response) {
		ResultVo retVo = new ResultVo();
		retVo.setRet(1);
		try {
			userKeywordsService.save(userKeywords);
			logger.info("保存微信关键词成功");
			retVo.setData(userKeywords);
			retVo.setCode("keyword_add_success");
			retVo.setMsg("success to add weixin user keyword.");
		} catch (Exception e) {
			logger.error("保存微信关键词失败", e);
			retVo.setCode("keyword_add_fail");
			retVo.setMsg("fail to add weixin user keyword.");
		}
		return renderString(response, retVo);
	}
}
