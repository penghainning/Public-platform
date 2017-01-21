<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
     import="java.util.Date"
    import="java.text.SimpleDateFormat"
    import="java.sql.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Welcome!</title>
<head>
<meta charset="utf-8">
<title>homePage</title>
<style>

<style>

a:link {
 font-size: 24px;
 color: #000000;
 text-decoration: none;
}
a:visited {
 font-size: 24px;
 color: #000000;
 text-decoration: none;
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
 	
 		if(session.getAttribute("name")==null)
 			response.sendRedirect("login.jsp");
			
 		String replyid=request.getParameter("replyid");
		String did=request.getParameter("did");

 	 	String replymessage =request.getParameter("replymessage");
 	 	String message =request.getParameter("message");
 	 	if(replyid!=null&&replymessage!=null)
 	 	{

 	 			Class.forName("com.mysql.jdbc.Driver");
	 			Connection conn=DriverManager.getConnection("jdbc:mysql://172.31.75.246:3306/S2014150057","S2014150057","791089");
	 			Date currentTime = new Date();
	 		  	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 		  	String time = formatter.format(currentTime);
	 		  	String sql="insert into reply(message_id,replycontent,replytime) values(?,?,?)";
	 			PreparedStatement ps=conn.prepareStatement(sql);
	 			ps.setString(1, replyid);
	 			ps.setString(2, new String(replymessage.getBytes("ISO-8859-1"), "utf8"));
	 			ps.setString(3, time);
	 			Statement stat=conn.createStatement();
	 			String sql2="update message set state = 1 where id ='"+replyid+"'";
	 			int j=stat.executeUpdate(sql2);
	 			int i=ps.executeUpdate();
	 			if(i>0&&j>0)
	 				response.sendRedirect("reply.jsp?page="+session.getAttribute("page"));
	 			else
	 				%>
	 				<scrip>
	 					alter("操作失败，该留言不存在！")
	 				</scrip>
	 				<% 
				stat.close();
				ps.close();
				conn.close();
 	 		
 	  
 		}
 	 	
 	 	if(did!=null)
 	 	{
 	 		Class.forName("com.mysql.jdbc.Driver");
	 		Connection conn=DriverManager.getConnection("jdbc:mysql://172.31.75.246:3306/S2014150057","S2014150057","791089");
	 		String sql="delete from message where id='"+did+"'";
			Statement state=conn.createStatement();
			int i=state.executeUpdate(sql);
			if(i>0)

				if(i>0)
	 				response.sendRedirect("reply.jsp?page="+session.getAttribute("page"));
			state.close();
			conn.close();
 	 	}
 	 	
 	 	
 	 	if(message!=null)
 	 	{

 	 			Class.forName("com.mysql.jdbc.Driver");
 	 			Connection conn=DriverManager.getConnection("jdbc:mysql://172.31.75.246:3306/S2014150057","S2014150057","791089");
 	 			Date currentTime = new Date();
 	 		  	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 	 		  	String time = formatter.format(currentTime);
 	 		  	String sql="insert into message(name,mail,content,createtime) values(?,?,?,?)";
 	 			PreparedStatement ps=conn.prepareStatement(sql);
 	 			ps.setString(1, "管理员");
 	 			ps.setString(2, "");
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
		message=document.replyform.message.value;

	
		if(message == "")
		{
			alert("请输入发布内容")
			document.replyform.message.focus();
			return;
		}
		else if(message.length>200)
		{
			alert("留言内容超过200字，请酌情删减")
			document.replyform.message.focus();
			return;
		}
		
		replyform.submit();
		
	}
	




function judgeDelete(id)
   {
     if(confirm("确定要删除吗？"))
     {
      window.location.href="reply.jsp?did="+id;
     }
   }
   
   
function showreply(id)
{
	var a=document.getElementById("msg"+id).style.display;
	if(a=="none")
  	document.getElementById("msg"+id).style.display="block";
	else
		document.getElementById("msg"+id).style.display="none";
}


function sendMsg(id)
{	
	var f=document.getElementById("form"+id);
	replymessage=f.replymessage.value;
	replyid=f.replyid.value;
	if(replymessage == "")
	{
		alert("请输入回复内容")
		document.replyform.replymessage.focus();
		return;
	}
	else if(replymessage.length>200)
	{
		alert("留言内容超过200字，请酌情删减")
		document.replyform.replymessage.focus();
		return;
	}
	
	f.submit();
	
}

</script>
<body background="1.jpg">
<div align="right"><a href="lyHome.jsp"><font color="red" size='+1'>返回首页</font></a></div>
<font color="#F9F7F7"><h1 align="center">欢迎您！尊敬的管理员<%=session.getAttribute("name")%>
    <br>
    <span >请在下面填写发布内容</span>
    </h1></font>

    <form action="reply.jsp" method="post" align="center" name="replyform">
    <table bgcolor="#336685" align="center" width="900">
    	
        
       
      
        <tr>
        <td> <font color="ffffff">内 容 :</font></td>
        <td>
        	<textarea  name="message" class="Mytextarea" 
            placeholder="请输入内容，长度不超过200字"></textarea>
        </td>
        </tr>
        <tr>
    		<td colspan="2" align="center"> <input type="button" class="Mybutton" value="提交" onclick="validate()"/></td>
        </tr>
    </table>

    </form>
    <div  class="Mydiv"><font color="#FFFFFF" size="+1"> 所有留言列表:</font></div>
    <ul align="left" style="list-style-type:none">
      	<% 
    	
	      	String mypage=request.getParameter("page");
			if(mypage==null)
				mypage="1";
		     int pg=Integer.valueOf(mypage);
		     session.setAttribute("page", pg);
		     double count=0;
    	     Class.forName("com.mysql.jdbc.Driver");
			 Connection conn=DriverManager.getConnection("jdbc:mysql://172.31.75.246:3306/S2014150057","S2014150057","791089");
			 Statement stmt=conn.createStatement();
			 ResultSet rs=stmt.executeQuery("select * from message order by createtime desc limit "+(pg-1)*5+",5");
    	while(rs.next())
    	{
    		 if(rs.getInt("state")==0)
    		 {
    			 
    			 out.print("<li/><table  align='center' bgcolor='#FFFFFF' width='1000'>" 
      			       + "<tr><td width='100'><img src='buddy_1_mb5ucom.png' width='50' height='50' alt='头像'></td>"
      			       +     "<td align='left'><font color='blue'>ID:"+rs.getInt("id")+"</font>&nbsp&nbsp</td>"
      			       +       "<td align='right'>"+rs.getString("createtime")+"</td>"   
      			       +"</tr><tr><td>&nbsp;&nbsp;</td><td>用户姓名："+rs.getString("name")+"</td><td width='200' align='center'><a id='"+rs.getInt("id")+"' href=javascript: onclick='judgeDelete("+rs.getInt("id")+")'>删除</a></td></tr>"
      	     		   +"</tr><tr><td>&nbsp;&nbsp;</td><td>留言内容："+rs.getString("content")+" </td><td width='200' align='center'><a id='"+rs.getInt("id")+"' href=javascript: onclick='showreply("+rs.getInt("id")+")'>回复</a></td></tr>"
      			       +"<tr><td></td><td width='500' style=\"display:none\"  id='msg"+rs.getInt("id")+"'>"
		    		   +"<form  id='form"+rs.getInt("id")+"'><textarea name=\"replymessage\" style=\"width:100%;height:100px;resize:none \"></textarea>"
		    		   +"<div align='right'>"
		    		   +"<label><font size='-1'>最多可输入200个字</font></label>"
		    		   +"</div> <div  align=\"left\">"
		    		   +"<input name='replyid' value='"+rs.getInt("id")+"' type='hidden'>"
		    	       +"<input value='发送' onclick='sendMsg("+rs.getInt("id")+")' type='button'>"
		       		   +"</div></form></td></tr></table></li><br><li>");
      
      	 
    		 }
    		 else
    		 {
    			String s2="";
    			String s1="<li/><table  align='center' bgcolor='#FFFFFF' width='1000'>" 
     			       + "<tr><td width='100'><img src='buddy_1_mb5ucom.png' width='50' height='50' alt='头像'></td>"
     			       +     "<td align='left'><font color='blue'>ID:"+rs.getInt("id")+"</font>:&nbsp&nbsp</td>"
     			       +       "<td align='right'>"+rs.getString("createtime")+"</td>"
     			       +"</tr><tr><td>&nbsp;&nbsp;</td><td>用户姓名："+rs.getString("name")+"</td><td width='200' align='center'><a id='"+rs.getInt("id")+"' href=javascript: onclick='judgeDelete("+rs.getInt("id")+")'>删除</a></td></tr>"
     			       +"</tr><tr><td>&nbsp;&nbsp;</td><td>留言内容："+rs.getString("content")+" </td><td width='200' align='center'><a id='"+rs.getInt("id")+"' href=javascript: onclick='showreply("+rs.getInt("id")+")'>回复</a></td></tr>";
    			 		
     			      Statement stmt2=conn.createStatement(); 
     			      ResultSet rs2=stmt2.executeQuery("select * from reply where message_id='"+rs.getInt("id")+"'");
     			       while(rs2.next())
     			       {
     			    	  String s3="<tr><td>&nbsp;&nbsp;</td></tr>"
     	        		           +"<tr><td></td><td><font color='red'>管理员回复 "+rs2.getString("replytime")+"</font></td></tr>"
     	        		           +"<tr><td></td><td> "+rs2.getString("replycontent")+"</td></tr>";
     	        		   s2+=s3;    
     			       }
     			       String s4="<tr><td></td><td  id='msg"+rs.getInt("id")+"'width='none' style=\"display:none\">"
     			    		    +"<form id='form"+rs.getInt("id")+"'><textarea name=\"replymessage\" style=\"width:100%;height:100px;resize:none \"></textarea>"
     			    		    +"<div align='right'>"
     			    		    +"<label><font size='-1'>最多可输入200个字</font></label>"
     			    		    +"</div> <div align=\"left\">"
     			    		    +"<input name='replyid' value='"+rs.getInt("id")+"' type='hidden'>"
     			    		    +"<input value='发送' onclick='sendMsg("+rs.getInt("id")+")' type='button'>"
     			        		+"</div></form></td></tr></table></li><br><li>";    			      			   
     			      	 s2+=s4;
     			      
     			       out.print(s1+s2);
     			        rs2.close();
     	    		 	stmt2.close();
    		 }
    		 
    		 
    	 }
    		
    	
    	 rs = stmt.executeQuery("select count(*) count from message");
     	
     	if(rs.next()) {
     	  count=rs .getInt("count");
     	}
     	int p=(int)Math.ceil(count/5);
     	if(pg==p)
     	 out.print("<div align='center'>"
 				   +"<a href='reply.jsp?page=1'>首页</a>&nbsp&nbsp&nbsp&nbsp"
 					
 					+"<a href='reply.jsp?page="+(pg-1)+"'>上一页</a>&nbsp&nbsp&nbsp&nbsp"
 					
 					+"<a href='reply.jsp?page="+pg+"'>尾页</a>"
 				   +"</div>");
     	else if(pg==1)
     		 out.print("<div align='center'>"
   				   +"<a href='reply.jsp?page=1'>首页</a>&nbsp&nbsp&nbsp&nbsp"
   					
   					+"<a href='reply.jsp?page="+(pg+1)+"'>下一页</a>&nbsp&nbsp&nbsp&nbsp"
   					
   					+"<a href='reply.jsp?page="+p+"'>尾页</a>"
   				   +"</div>");
     	else
     		 out.print("<div align='center'>"
     				   +"<a href='reply.jsp?page=1'>首页</a>&nbsp&nbsp&nbsp&nbsp"
     				    +"<a href='reply.jsp?page="+(pg-1)+"'>上一页</a>&nbsp&nbsp&nbsp&nbsp"
     					+"<a href='reply.jsp?page="+(pg+1)+"'>下一页</a>&nbsp&nbsp&nbsp&nbsp"
     					+"<a href='reply.jsp?page="+p+"'>尾页</a>"
     				   +"</div>");
     		
     	%>
   
    </ul>

</body>
</html>