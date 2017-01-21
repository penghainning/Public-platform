<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" >
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-control" content="no-cache">
<meta http-equiv="Cache" content="no-cache">
<title>公众号平台</title>
<link rel="stylesheet" type="text/css" href="css/login.css">
</head>
<body style="background:url(image/bg.jpg);">
<script src="scrip/jquery.js" type="text/javascript"></script>
<script type="text/javascript" src="scrip/login.js" ></script>
<div class="png login_logo"><img src="image/logo.jpg" alt=""></div>
<br>
<div class="login">
<form align="center" name="loginform" method="post">

 <div class="type" >帐号类型：</div>
<div class="login_type"><select id="type">  
  <option value ="1">普通用户</option>  
  <option value ="2">公众号用户</option>  
</select> 
</div>
 <div class="username" >帐号：</div>
<div class="login_1"><input id="account" type="text" value=<%=request.getAttribute("account") %>></div>
<div class="pwd" >密码：</div>
<div class="login_2"><input id="password" type="password" value=<%=request.getAttribute("password") %>></div>
<div class="yzm" >验证码：</div>
<div class="login_yzm"><input id="code" type="text"></div>
<div class="img_yzm"><img id="yzm" width="100px" height="22px" src="page/yzm.jsp" /></div>

<div class="login_3"><input id="remember" type="checkbox" value=""></div>
<div class="login_remember">记住我</div>
<div class="login_4"><input type="button" value="登录" id="login"></div>
<div class="login_5"><input type="button" value="注册" id="regist"></div>
</form>
</div>
</body>
</html>
