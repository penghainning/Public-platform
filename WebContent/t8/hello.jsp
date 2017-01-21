<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"    	
	import ="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>欢迎登陆</title>
</head>
<%
if(!"true".equals(session.getAttribute("flag")))
	response.sendRedirect("login.jsp");
Map<String,String> map=(HashMap<String,String>)application.getAttribute("people"); 


%>
<script>
function quit()
{
	
	<%/*
	session.setAttribute("flag", "false");
	application.removeAttribute("map");
	map.remove(session.getAttribute("name"));
	application.setAttribute("map", map);
	Integer c=(Integer)application.getAttribute("counts");
	c--;
	application.setAttribute("counts", c);
	*/
	%>
	window.location="login.jsp?flag=false"

}
</script>
<body>

<p>欢迎你！！！！亲爱的    <%=session.getAttribute("name")%></p>
<p>登录时间: <%=session.getAttribute("logintimes")%></p>
<p>登录IP: <%=session.getAttribute("loginip")%></p>
<p>在线人数: <%=map.size()%></p>
<p>在线用户列表：<br><%
Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
while (iterator.hasNext()) {
	Map.Entry<String, String> entry = iterator.next();
	out.println(entry.getValue()+"<br>");
}


%></p>
<p><a href=javascript: onclick='quit()'>注销</a></p>


</body>
</html>