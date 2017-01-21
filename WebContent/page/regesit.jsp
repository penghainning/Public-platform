<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-control" content="no-cache">
<meta http-equiv="Cache" content="no-cache">
<title>新用户注册</title>
<link rel="stylesheet" type="text/css" href="css/regist.css">
<script src="scrip/jquery.js"></script>
<script type="text/javascript" src="scrip/regist.js" ></script>
</head>
<body style="background:url(image/bg.jpg);">
<div class="regist">
<form align="center" id="registform" method="post">
 <div class="username" >帐号：</div>
<div class="regist_username"><input id="account" type="text"></div>
 <div class="phone" >手机号：</div>
<div class="regist_phone"><input id="phone" type="text"></div>
<div class="pwd" >密码：</div>
<div class="regist_pwd"><input id="password" type="password"></div>
<div class="pwd2" >确认密码：</div>
<div class="regist_pwd2"><input id="password2" type="password"></div>
<div class="nickname" >昵称：</div>
<div class="regist_nickname"><input id="nickname" type="text"></div>

<div class="sex" >性别：</div>
<div class="regist_sex"><select id="sex">  
  <option value ="1">男</option>  
  <option value ="0">女</option>  
</select> 
</div>
<div class="type" >账号类型：</div>
<div class="regist_type"><select id="type">  
  <option value ="1">普通用户</option>  
  <option value ="2">公众号用户</option>  
</select> 
</div>
<div class="back"><input type="button" value="返回" onclick="javascript:history.back(-1)"></div>
<div class="sure"><input type="button" value="确定" id="regist"></div>
</form>
</div>
 </body>
</html>