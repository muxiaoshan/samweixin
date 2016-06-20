<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>微信关键词管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/weixin/keyword/userKeywords/">微信关键词列表</a></li>
		<shiro:hasPermission name="weixin:keyword:userKeywords:edit"><li><a href="${ctx}/weixin/keyword/userKeywords/form">微信关键词添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="userKeywords" action="${ctx}/weixin/keyword/userKeywords/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>用户id：</label>
				<form:input path="username" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>关键词：</label>
				<form:input path="keyword" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>addtime：</label>
				<input name="beginAddtime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${userKeywords.beginAddtime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endAddtime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${userKeywords.endAddtime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>用户id</th>
				<th>关键词</th>
				<th>addtime</th>
				<shiro:hasPermission name="weixin:keyword:userKeywords:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="userKeywords">
			<tr>
				<td><a href="${ctx}/weixin/keyword/userKeywords/form?id=${userKeywords.id}">
					${userKeywords.username}
				</a></td>
				<td>
					${userKeywords.keyword}
				</td>
				<td>
					<fmt:formatDate value="${userKeywords.addtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="weixin:keyword:userKeywords:edit"><td>
    				<a href="${ctx}/weixin/keyword/userKeywords/form?id=${userKeywords.id}">修改</a>
					<a href="${ctx}/weixin/keyword/userKeywords/delete?id=${userKeywords.id}" onclick="return confirmx('确认要删除该微信关键词吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>