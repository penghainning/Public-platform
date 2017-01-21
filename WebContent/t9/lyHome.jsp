<%@page import="beans.Message"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.sql.*"
    import="java.util.Date"
    import="java.util.ArrayList"
    import="dao.MessageDao"
    import="beans.Message"
    import="beans.Reply"
    import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Welcome!</title>
<head>
<meta charset="utf-8">
<title>homePage</title>
<style>

a:link {
 font-size: 24px;
 color: #ffffff;
 text-decoration: none;
}
a:visited {
 font-size: 24px;
 color: #ffffff;
 text-decoration: none;
}
a:hover {
 font-size: 24px;
 color: #ffffff;
 text-decoration: underline;
}

.Mytextarea{
height:200px;
width:800px;
padding-top: 10px;
}

.Mydiv{
background-color: #346784;
margin-top: 30px;
}

.Mybutton {
background-color: #9DC45F;
border-radius: 5px;
border: none;
padding: 10px 25px 10px 25px;
color: #FFF;
text-shadow: 1px 1px 1px #949494;
}


.Mybutton:hover {
    background: green;
}


</style>
</head>

 <%
 	  
 		String name=request.getParameter("name");
 	 	String email=request.getParameter("email");
 	 	String message =request.getParameter("message");
 	 	if(name!=null&&email!=null&&message!=null)
 	 	{

 	 			Class.forName("com.mysql.jdbc.Driver");
 	 			Connection conn=DriverManager.getConnection("jdbc:mysql://172.31.75.246:3306/S2014150057","S2014150057","791089");
 	 			Date currentTime = new Date();
 	 		  	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 	 		  	String time = formatter.format(currentTime);
 	 		  	String sql="insert into message(name,mail,content,createtime) values(?,?,?,?)";
 	 			PreparedStatement ps=conn.prepareStatement(sql);
 	 			ps.setString(1, new String(name.getBytes("ISO-8859-1"), "utf8"));
 	 			ps.setString(2, email);
 	 			ps.setString(3, new String(message.getBytes("ISO-8859-1"), "utf8"));
 	 			ps.setString(4, time);
 	 			int i=ps.executeUpdate();
 	 			if(i>0)
 	 			
 %>
 
 		<script type="text/javascript">
 		alert("提交成功");
		</script>
<% 
				ps.close();
				conn.close();
 	 		
 	  
 		}
	
%>

<script type="text/javascript">
	function validate(){
		name=document.homeform.name.value;
		email=document.homeform.email.value;
		message=document.homeform.message.value;

		if(name == "")
		{
			alert("请输入您的名字")
			document.homeform.name.focus();
			return;
		}
		else if(email == "")
		{
			alert("请输入邮箱")
			document.homeform.email.focus();
			return;
		}
		else if(email != "")
		{
			 var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
			 isok= reg.test(email );
			 if (!isok) {
			 	 alert("邮箱格式不正确，请重新输入！");
			 	document.homeform.email.focus();
			 		return false;
			    }		
		}
		else if(message == "")
		{
			alert("请输入留言内容")
			document.homeform.message.focus();
			return;
		}
		else if(message.length>200)
		{
			alert("留言内容超过200字，请酌情删减")
			document.homeform.message.focus();
			return;
		}
		
		homeform.submit();
		
	}
</script>
<body background="1.jpg">
<div align="right"><a href="login.jsp"><font color="red" size='+1'>管理员登录</font></a></div>
<font color="#F9F7F7"><h1 align="center">欢迎使用留言板
    <br>
    <span >请在下面填写相关内容</span>
    </h1></font>
     <div align='center'><font color='red' size=+1>(内容发布之后不可删除，若要删除请联系管理员)</font></div>

    <form action="lyHome.jsp" method="post" align="center" name='homeform'>
    <table bgcolor="#336685" align="center" width="900">
    	<tr>
        <td ><font color="ffffff">姓 名 :</font></td>
        <td >
        	 <input id="name" type="text" name="name" placeholder="请输入你的名字" 
              style="width:800px; height:20px;"  />
        </td>
        </tr>
        
        <tr>
        <td><font color="ffffff">邮 箱 :</font></td>
        <td>
        	 <input id="email" type="email" name="email" placeholder="请输入你的邮箱" 
              style="width:800px; height:20px;"/>
        </td>
        </tr>
        
        <tr>
        <td> <font color="ffffff">内 容 :</font></td>
        <td>
        	<textarea id="message" name="message" class="Mytextarea" 
            placeholder="请输入内容，长度不超过200字"></textarea>
        </td>
        </tr>
        <tr>
    		<td colspan="2" align="center"> <input type="button" class="Mybutton"  onclick="validate()" value="提交" /></td>
        </tr>
    </table>

    </form>
    <div  class="Mydiv"><font color="#FFFFFF" size="+1"> 留言列表:</font></div>
    <ul align="left" style="list-style-type:none">
    	<% 
    		String mypage=request.getParameter("page");
    		if(mypage==null)
    			mypage="1";
    	     int pg=Integer.valueOf(mypage);
    	     
    	     //获取留言列表
    	     MessageDao messageDao=new MessageDao();
    	     ArrayList<Message> messages=messageDao.findAllMessage(pg);
    	     double count;
    	     
    	     for(int i=0;i<messages.size();i++){
    	    	 Message m=messages.get(i);
    	    	 ArrayList<Reply> replys=m.getReply();
    	    	 if(m.getState()==0){
    	    		 out.print("<li/><table  align='center' bgcolor='#FFFFFF' width='1000'>" 
    	      			       + "<tr><td width='100'><img src='buddy_1_mb5ucom.png' width='50' height='50' alt='头像'></td>"
    	      			       +     "<td align='left'>用户姓名："+m.getName()+"</td>"
    	      			       +       "<td align='right'>"+m.getCreatrtime()+"</td>"
    	      			       +"<td width='100'></td>"
    	      			       +"</tr><tr><td>&nbsp;&nbsp;</td><td>留言内容："+m.getContent()+"</td></tr>"
    	      			       +" </table></li><br>");
    	    	 }
    	    	 else{
    	    		    String s2="";
    	     			String s1="<li/><table  align='center' bgcolor='#FFFFFF' width='1000'>" 
    	       			       + "<tr><td width='100'><img src='buddy_1_mb5ucom.png' width='50' height='50' alt='头像'></td>"
    	       			       +     "<td align='left'>用户姓名："+m.getName()+"</td>"
    	       			       +       "<td align='right'>"+m.getCreatrtime()+"</td>"
    	      			       +"<td width='100'></td>"
    	      			       +"</tr><tr><td>&nbsp;&nbsp;</td><td>留言内容："+m.getContent()+" </td></tr>";
    	     			 		
    	      			     
    	      			       for(int j=0;j<replys.size();j++){
    	      			    	   Reply r=replys.get(j);
    	      			    	  String s3="<tr><td>&nbsp;&nbsp;</td></tr>"
    	      	        		           +"<tr><td></td><td><font color='red'>管理员回复 "+r.getReplytime()+"</font></td></tr>"
    	      	        		           +"<tr><td></td><td> "+r.getReplycontent()+"</td></tr>";
    	      	        		   s2+=s3;    
    	      			       }
    	      			       s2+=" </table></li><br><li>";
    	      			      
    	      			       out.print(s1+s2);
    	    	 }
    	     }
      
    	count=messageDao.getMessageCount();
    	count=Math.ceil(count/5);
    	if(pg==count)
    	 out.print("<div align='center'>"
				   +"<a href='lyHome.jsp?page=1'>首页</a>&nbsp&nbsp&nbsp&nbsp"
					
					+"<a href='lyHome.jsp?page="+(pg-1)+"'>上一页</a>&nbsp&nbsp&nbsp&nbsp"
					
					+"<a href='lyHome.jsp?page="+pg+"'>尾页</a>"
				   +"</div>");
    	else if(pg==1)
    		 out.print("<div align='center'>"
  				   +"<a href='lyHome.jsp?page=1'>首页</a>&nbsp&nbsp&nbsp&nbsp"
  					
  					+"<a href='lyHome.jsp?page="+(pg+1)+"'>下一页</a>&nbsp&nbsp&nbsp&nbsp"
  					
  					+"<a href='lyHome.jsp?page="+(int)count+"'>尾页</a>"
  				   +"</div>");
    	else
    		 out.print("<div align='center'>"
    				   +"<a href='lyHome.jsp?page=1'>首页</a>&nbsp&nbsp&nbsp&nbsp"
    				   +"<a href='lyHome.jsp?page="+(pg-1)+"'>上一页</a>&nbsp&nbsp&nbsp&nbsp"
    					+"<a href='lyHome.jsp?page="+(pg+1)+"'>下一页</a>&nbsp&nbsp&nbsp&nbsp"					
    					+"<a href='lyHome.jsp?page="+(int)count+"'>尾页</a>"
    				   +"</div>");
    		
    	%>
   
    </ul>
</body>
</html>