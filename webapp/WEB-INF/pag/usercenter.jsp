<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>

<head>
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<title>ATM系统--用户中心</title>

</head>

<body>
<h1>个人中心页面</h1>
<img src="/showPicture.do?action=showPicture" width="66px" height="66px">
<form method="POST" enctype="multipart/form-data" action="/load.do?action=load">
<input type="file" name="upfile"><br/>
 <input type="submit" value="上传">
 </form>
<h1>用户名：${user.getUsername()}</h1>
<a href="/toOpenAccount.do?action=toOpenAccount" target="_blank">去开户</a><br>

</body>

<table>
	<tr>
		<td>卡号</td>
		<td>金额</td>
		<td>时间</td>
		<td>手续</td>
	</tr>
	
	<c:forEach items="${fenye.getObject()}" var="list">
		<tr>
			<td>${list.getNumber()}</td>
			<td>${list.getMoney()}</td>
			<td><fmt:formatDate value="${list.getCreatetime()}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			<td>
			<a href="/toSave.do?action=toSave" target="_blank">存钱</a>
			<a href="/toCheck.do?action=toCheck" target="_blank">取钱</a>
			<a href="/toTransfer.do?action=toTransfer" target="_blank">汇款</a>
			<a href="/toList.do?action=toList" target="_blank">交易</a>
			<a href="/toChangePassword.do?action=toChangePassword" target="_blank">修改密码</a>
			<a href="/toDelete.do?action=toDelete" target="_blank">销户</a>
			</td>
		</tr>
	</c:forEach>
	
	<c:if test="${not empty fenye }">
		<button onclick="first()">首页</button>
		<button onclick="pre()">上一页</button>
		<button onclick="next()">下一页</button>
		<button onclick="last()">尾页</button>
		${fenye.getCurrentPage()}/${fenye.getTotalPage()}
	</c:if>
	
</table>

</body>

<script type="text/javascript">

	var currentPage = "${fenye.getCurrentPage()}";
	
	var totalPageNum = "${fenye.getTotalPage()}";
	
	function next() {
		if (currentPage == totalPageNum) {
			return false;
		}
		
		currentPage = parseInt(currentPage) + 1;
		window.location.href='/toUsercenter.do?action=toUsercenter&currentPage=' + currentPage;
	}

	function pre() {
		
		if (currentPage == 1) {
			return false;
		}
		
		currentPage = parseInt(currentPage) - 1;
		window.location.href='/toUsercenter.do?action=toUsercenter&currentPage=' + currentPage;
	}

	function first() {
		currentPage = 1;
		window.location.href='/toUsercenter.do?action=toUsercenter&currentPage=' + currentPage;
	}

	function last() {
		currentPage = totalPageNum;
		window.location.href='/toUsercenter.do?action=toUsercenter&currentPage=' + currentPage;
	}
		
	
</script>


</html>