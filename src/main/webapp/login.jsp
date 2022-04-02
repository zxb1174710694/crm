<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
	String basePath = request.getScheme() + "://" +request.getServerName() + ":"+
			          request.getServerPort()+request.getContextPath()+"/";
%>

<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
</head>

<script>
	$(function () {
		//当用户在服务器关闭之后刷新功能的话，登录页面就会出现在小窗口，所以要将小窗口升级为顶级窗口
		if(window.top != window.self)
		{
			window.top.Location = window.self.Location;
		}

		//页面默认用户名框聚焦
		$("#loginAct").focus();

		//用户点击登录进行验证
		$("#loginSubmit").click(function () {
			login();
		})
		//用户敲回车验证
		$(window).keydown(function (event) {
			//13是回车
			if (event.keyCode ==13)
			login();
		})


	})
	//创建用户登录方法
	function login() {
		//获取用户名和密码(要去空格)
		var loginAct = $.trim($("#loginAct").val());
		var loginPwd = $.trim($("#loginPwd").val());

		if (loginAct == "" || loginPwd == ""){
			$("#msg").html("<span style='color: red'>用户名密码不能为空</span>");
			return false;	//终止程序
		}
		//到这里说明账户密码不是空的，所以提交数据库（用ajax请求）
		$.ajax({
			url : "setting/user/login.do",
			data : {
				//向后台传递账号密码
				"loginAct" : loginAct,
				"loginPwd" : loginPwd
			},
			type : "post",
			dataType : "json",
			success :function (date) {
					//后台需要向前端传验证成功或者失败了的boolean，并且告诉错误信息
				if (date.success){
					window.location.href = "workbench/index.jsp";
				}else {
					$("#msg").html(date.msg).css("color","red");
				}
			}
		})
	}
</script>

<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2017&nbsp;动力节点</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.jsp" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" type="text" placeholder="用户名" id="loginAct">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" type="password" placeholder="密码" id="loginPwd">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						
							<span id="msg"></span>
						
					</div>
					<button type="button" id="loginSubmit" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>

		</div>
	</div>
</body>
</html>