package com.jbase.modules.weixin.entity.keyword;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jbase.common.persistence.DataEntity;

/**
 * 关键词处理Entity
 * @author sam
 * @version 2016-06-20
 */
public class UserKeywords extends DataEntity<UserKeywords> {
	
	private static final long serialVersionUID = 1L;
	private String username;		// 用户id
	private String keyword;		// 关键词
	private Date addtime;		// addtime
	private Date beginAddtime;		// 开始 addtime
	private Date endAddtime;		// 结束 addtime
	
	public UserKeywords() {
		super();
	}

	public UserKeywords(String id){
		super(id);
	}

	@Length(min=1, max=64, message="用户id长度必须介于 1 和 64 之间")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@Length(min=1, max=100, message="关键词长度必须介于 1 和 100 之间")
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	
	public Date getBeginAddtime() {
		return beginAddtime;
	}

	public void setBeginAddtime(Date beginAddtime) {
		this.beginAddtime = beginAddtime;
	}
	
	public Date getEndAddtime() {
		return endAddtime;
	}

	public void setEndAddtime(Date endAddtime) {
		this.endAddtime = endAddtime;
	}
		
}