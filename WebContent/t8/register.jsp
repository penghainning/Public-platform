<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
     import="java.util.Date"
     import="java.security.MessageDigest"
     import="sun.misc.BASE64Encoder"
    import="java.text.SimpleDateFormat"
    import="java.sql.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户注册界面</title>
</head>
<body bgcolor="#4886E6"> 

 <%
 	
 String errorcode=request.getParameter("errorcode");
 if("1".equals(errorcode))
	{
		out.println("<script>alert(\"用户名已被占用！请重新输入！\")</script>");
	}
	else if("2".equals(errorcode))
	{
		out.println("<script>alter(\"注册失败！未知错误\")</script>");
	}
	else if("3".equals(errorcode))
	{
		out.println("<script>alert(\"系统异常，稍后重试\")</script>");
	}
	
 
	
%>
<script type="text/javascript">
	function validate(){
		account=document.registform.account.value;
		password=document.registform.password.value;
		password2=document.registform.password2.value;
		var reg = /^(?![^a-zA-Z]+$)(?!\D+$).{7,20}/;

		if(account == "")
		{
			alert("请输入账号！")
			document.registform.account.focus();
			return;
		}
		else if(account.length<6 || account.length>20)
		{
			alert("账号长度必须大于6位且小于20位！")
			document.registform.account.focus();
			return;
		}
		else if(password == "")
		{
			alert("请输入密码")
			document.registform.password.focus();
			return;
		}
	
		else if(password2 == "")
		{
			alert("请确认密码！")
			document.registform.password2.focus();
			return;
		}
		else if(password2!=password)
		{
			alert("两次密码不相同，请重新输入！")
			document.registform.password2.focus();
			return;
		}
		
		else if(!password.match(reg))
		{
			alert("密码必须包含数字和字母，且长度为7-20")
			document.registform.password.focus();
			return;
		}
	
		
		registform.submit();
		
	}
</script>	
<script type="text/javascript">
	function refresh()
	{
		 document.getElementById("yzm").src=document.getElementById("yzm").src+"?nocache="+new Date().getTime();    
		 //location.reload()
	}
</script>

 <table cellpadding="0" cellspacing="0" >
  <tr><td colspan="2" bgcolor="#3464AE" width="100000"><img src="logo.png"></td></tr>
  <tr bgcolor="#4886E6"> <td width="50%" align="center"><img src="bacground.png"></td>
       <td>
       		<form align="center" name="registform" action="/S2014150057/javaWebBc/registServlet" method="post">
            <table align="left" width="400">
             <tr align="center"><td colspan="2" ><h1>用户账号注册</h1></td></tr>
             <tr><td align="right">账号：</td><td><input name="account" type="text"></td></tr>
             <tr><td align="right"> 密码：</td><td><input name="password" type="password"></td></tr>
              <tr><td align="right"> 确认密码：</td><td><input name="password2" type="password"></td></tr>
           
             <tr><td></td></tr>
             <tr><td></td></tr>
             <tr>
              <td width="50%" align="right"><input type="button" onclick="validate()" value="提交" ></td>
              <td width="50%" align="center"> <input type="reset" value="重置" align="center"></td>
             </tr>
            
              </table>
		</form>
         </td>
         </tr>
<tr>
<br>
<td align="center" valign="top" colspan="2" >
<br>
<br>
<br>
<br>
<br>
<br>
<br>　<font size="+2">深圳市南山区南海大道3688号 邮编:518060 E-mail:webmaster@szu.edu.cn</font>　
<br>总机:2653-6114 教工区网络:2653-7109 校内报警:2653-7119 心理指导:2695-8879
<br>学生区网络：电信86310108 移动18476328678(短号678678)
<br><a href="http://www.miibeian.gov.cn" >粤ICP备11018045号-7</a> 版权所有&copy;信息中心
</td>
</tr>
 </table>
 

</body>
</html>