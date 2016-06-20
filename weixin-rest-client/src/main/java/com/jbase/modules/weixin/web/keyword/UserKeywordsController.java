package com.jbase.modules.weixin.web.keyword;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jbase.common.config.Global;
import com.jbase.common.persistence.Page;
import com.jbase.common.web.BaseController;
import com.jbase.common.utils.StringUtils;
import com.jbase.modules.weixin.entity.keyword.UserKeywords;
import com.jbase.modules.weixin.service.keyword.UserKeywordsService;

/**
 * 关键词处理Controller
 * @author sam
 * @version 2016-06-20
 */
@Controller
@RequestMapping(value = "${adminPath}/weixin/keyword/userKeywords")
public class UserKeywordsController extends BaseController {

	@Autowired
	private UserKeywordsService userKeywordsService;
	
	@ModelAttribute
	public UserKeywords get(@RequestParam(required=false) String id) {
		UserKeywords entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = userKeywordsService.get(id);
		}
		if (entity == null){
			entity = new UserKeywords();
		}
		return entity;
	}
	
	@RequiresPermissions("weixin:keyword:userKeywords:view")
	@RequestMapping(value = {"list", ""})
	public String list(UserKeywords userKeywords, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UserKeywords> page = userKeywordsService.findPage(new Page<UserKeywords>(request, response), userKeywords); 
		model.addAttribute("page", page);
		return "modules/weixin/keyword/userKeywordsList";
	}

	@RequiresPermissions("weixin:keyword:userKeywords:view")
	@RequestMapping(value = "form")
	public String form(UserKeywords userKeywords, Model model) {
		model.addAttribute("userKeywords", userKeywords);
		return "modules/weixin/keyword/userKeywordsForm";
	}

	@RequiresPermissions("weixin:keyword:userKeywords:edit")
	@RequestMapping(value = "save")
	public String save(UserKeywords userKeywords, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, userKeywords)){
			return form(userKeywords, model);
		}
		userKeywordsService.save(userKeywords);
		addMessage(redirectAttributes, "保存微信关键词成功");
		return "redirect:"+Global.getAdminPath()+"/weixin/keyword/userKeywords/?repage";
	}
	
	@RequiresPermissions("weixin:keyword:userKeywords:edit")
	@RequestMapping(value = "delete")
	public String delete(UserKeywords userKeywords, RedirectAttributes redirectAttributes) {
		userKeywordsService.delete(userKeywords);
		addMessage(redirectAttributes, "删除微信关键词成功");
		return "redirect:"+Global.getAdminPath()+"/weixin/keyword/userKeywords/?repage";
	}

}