<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="java.sql.Connection"
	import="java.sql.DriverManager"
	import="java.sql.Statement"
	import="java.sql.ResultSet"
	
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户登录界面</title>
</head>
<body bgcolor="#4886E6"> 

 <%
 	
 		String cardId=request.getParameter("cardId");
 	 	String password=request.getParameter("password");
 	 	String code =request.getParameter("code");
 	 	String randStr=(String)session.getAttribute("randstr");
 	 	if(cardId!=null&&password!=null)
 	 	{
 	 		if(!code.equals(randStr))
 	 		{
 	 			out.print("<div align='center'><font color=\"#FF0000\" size=\"+2\">验证码错误！</font></div>");
 	 		}
 	 		else
 	 		{
 	 			Class.forName("com.mysql.jdbc.Driver");

 	 			Connection conn=DriverManager.getConnection("jdbc:mysql://172.31.75.246:3306/S2014150057","S2014150057","791089");

 	 			   Statement stmt=conn.createStatement();

 	 			   ResultSet rs=stmt.executeQuery("select * from user where account='"+cardId+"' and password ='"+password+"'");
 	 			 	rs.last();//移到最后一行
 	 				int count = rs.getRow();
 	 				rs.beforeFirst();//移到初始位置
 	 				
 	 			   if(count>0)
 	 				{
 	 					rs.next();
 	 					session.setAttribute("name", rs.getString("name"));
 	 				
 	 					response.sendRedirect("reply.jsp");
 	 				
 	 					
 	 				}
 	 				else
 	 				{
 	 					out.print("<div align='center'><font color=\"#FF0000\" size=\"+2\">用户名和密码错误！</font></div>");
 	 				}

 	 			   rs.close();

 	 			   stmt.close();

 	 			conn.close();
 	 		}
 	  
 		}
	
%>
<script type="text/javascript">
	function validate(){
		cardId=document.loginform.cardId.value;
		password=document.loginform.password.value;
		code=document.loginform.code.value;

		if(cardId == "")
		{
			alert("请输入您的校园卡号")
			document.loginform.cardId.focus();
			return;
		}
		else if(password == "")
		{
			alert("请输入校园卡查询密码")
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
       		<form align="center" name="loginform" action="login.jsp" method="post">
            <table align="left" width="400">
             <tr align="right"><td colspan="2" ><h1>留言板身份认证</h1></td></tr>
             <tr><td align="right">账号：</td><td><input name="cardId" type="text"></td></tr>
             <tr><td align="right"> 密码：</td><td><input name="password" type="password"></td></tr>
              <tr><td align="right"> 验证码：</td><td><input name="code" type="text"></td><td align="left"><img id="yzm" border="0" src="yzm.jsp" onclick="refresh()"></td></tr>
             <tr>
             	<td></td>
             	<td align="center" colspan="2">(点击图片刷新验证码)</td>
             </tr>
             <p>
             <tr>
              <td width="50%" align="right"><input type="button" onclick="validate()" value="登录" ></td>
              <td width="50%" align="center"> <input type="reset" value="重置" align="center"></td>
             </tr>
             </p>
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