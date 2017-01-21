<%@ page language="java" contentType="text/html; charset=UTF-8"
		import="java.sql.*"
	import="java.security.MessageDigest"
	import="java.text.SimpleDateFormat"
	import="java.util.Date"
	import="sun.misc.BASE64Encoder"	
	import ="java.util.*"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户登录界面</title>
</head>
<body bgcolor="#4886E6"> 

  <%
 	 Map<String,String> map=(HashMap<String,String>)application.getAttribute("people");
	 if(map==null)
		 map=new HashMap<String,String>();
		    
    String name = "";
     String pwd = "";
     boolean hasCookie=false;
    //获取当前站点的所有Cookie
     Cookie[] cookies = request.getCookies();
    int j=0;
   if(cookies!=null)
    {
    	for ( j = 0; j < cookies.length; j++) {//对cookies中的数据进行遍历，找到用户名、密码的数据
            if ("username".equals(cookies[j].getName())) {
            	name = cookies[j].getValue();
             } else if ("password".equals(cookies[j].getName())) {
            	 pwd = cookies[j].getValue();
            }     
         }
         	
    }
   
   		System.out.println(j+":"+name+","+pwd);
     
	  
 	 	String flag=request.getParameter("flag");
 	 	String errorcode=request.getParameter("errorcode");
 	 	String cardId=request.getParameter("cardId");
 	 	String password=request.getParameter("password");

 	 	
 		if("false".equals(flag))
 		{
 			application.removeAttribute("map");
 			map.remove(session.getAttribute("name"));
 			application.setAttribute("map", map);
 		}
 		
 		if("1".equals(errorcode))
 		{
 			out.println("<script>alert(\"验证码错误\")</script>");
 		}
 		else if("2".equals(errorcode))
 		{
 			out.println("<script>	alert(\"登录失败！未知错误\")</script>");
 		}
 		else if("3".equals(errorcode))
 		{
 			out.println("<script>alert(\"用户名或密码错误！请重新输入\")</script>");
 		}
 		
		
		
 		
 	 	
%>
<script type="text/javascript">
	function validate(){
		cardId=document.loginform.cardId.value;
		password=document.loginform.password.value;
		code=document.loginform.code.value;

		if(cardId == "")
		{
			alert("请输入您的用户名")
			document.loginform.cardId.focus();
			return;
		}
		else if(password == "")
		{
			alert("请输入您的密码")
			document.loginform.password.focus();
			return;
		}
		else if(code == "")
		{
			alert("请输入验证码")
			document.loginform.password.focus();
			return;
		}
		loginform.submit();
		
	}
	
	
	function regist()
	{
		window.location="register.jsp" ;
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
       		<form align="center" name="loginform" action="/S2014150057/javaWebBc/loginServlet" method="post">
            <table align="left" width="400">
             <tr align="right"><td colspan="2" ><h1>深圳大学统一身份认证</h1></td></tr>
             <tr><td align="right">用户名：</td><td><input name="cardId" type="text" value=<%=name %>></td></tr>
             <tr><td align="right"> 密码：</td><td><input name="password" type="password" value=<%=pwd %>></td></tr>
              <tr><td align="right"> 验证码：</td><td><input name="code" type="text"></td><td align="left"><img id="yzm" border=0 src="yzm.jsp" onclick="refresh()"></td></tr>
             <tr><td align="center" colspan="2" ><input type="checkbox" id="remember" name="remember" value="yes"  />记住用户名</td><td align="right">(点击图片刷新验证码)</td></tr>
            
           
             <tr>
              <td width="50%" align="right"><input type="button" onclick="validate()" value="登录" ></td>
              <td width="50%" align="center"> <input type="button" value="注册"  onclick="regist()"></td>
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