package com.jbase.modules.weixin.dao.keyword;

import com.jbase.common.persistence.CrudDao;
import com.jbase.common.persistence.annotation.MyBatisDao;
import com.jbase.modules.weixin.entity.keyword.UserKeywords;

/**
 * 关键词处理DAO接口
 * @author sam
 * @version 2016-06-20
 */
@MyBatisDao
public interface UserKeywordsDao extends CrudDao<UserKeywords> {
	
}