package com.jbase.modules.weixin.service.keyword;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jbase.common.persistence.Page;
import com.jbase.common.service.CrudService;
import com.jbase.modules.weixin.entity.keyword.UserKeywords;
import com.jbase.modules.weixin.dao.keyword.UserKeywordsDao;

/**
 * 关键词处理Service
 * @author sam
 * @version 2016-06-20
 */
@Service
@Transactional(readOnly = true)
public class UserKeywordsService extends CrudService<UserKeywordsDao, UserKeywords> {

	public UserKeywords get(String id) {
		return super.get(id);
	}
	
	public List<UserKeywords> findList(UserKeywords userKeywords) {
		return super.findList(userKeywords);
	}
	
	public Page<UserKeywords> findPage(Page<UserKeywords> page, UserKeywords userKeywords) {
		return super.findPage(page, userKeywords);
	}
	
	@Transactional(readOnly = false)
	public void save(UserKeywords userKeywords) {
		super.save(userKeywords);
	}
	
	@Transactional(readOnly = false)
	public void delete(UserKeywords userKeywords) {
		super.delete(userKeywords);
	}
	
}