package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;



import beans.Message;
import beans.Reply;

public class MessageDao {

	public ArrayList<Message> findAllMessage(int pg) throws Exception{
		ArrayList<Message> messages=new ArrayList<>();
		Connection conn=null;
		try{
			 Class.forName("com.mysql.jdbc.Driver");
			 conn= DriverManager.getConnection("jdbc:mysql://172.31.75.246:3306/S2014150057","S2014150057","791089");
			 Statement stmt=conn.createStatement();
			 ResultSet rs=stmt.executeQuery("select * from message order by createtime desc limit "+(pg-1)*5+",5");
			 while(rs.next()){
				 ArrayList<Reply> replys=new ArrayList<>();
				 Message m=new Message();
				 m.setId(rs.getInt("id"));
				 m.setContent(replace(rs.getString("content")));
				 m.setCreatrtime(rs.getString("createtime"));
				 m.setName(replace(rs.getString("name")));
				 m.setMail(replace(rs.getString("mail")));
				 m.setState(rs.getInt("state"));
				 Statement stmt2=conn.createStatement(); 
 			     ResultSet rs2=stmt2.executeQuery("select * from reply where message_id='"+rs.getInt("id")+"'");
 			       while(rs2.next())
 			       {
 			    	   Reply r=new Reply();
 	        		   r.setReplytime(rs2.getString("replytime"));
 	        		   r.setReplycontent(replace(rs2.getString("replycontent")));
 	        		   replys.add(r);
 			       }
 			     m.setReply(replys);  
				 messages.add(m);
				 stmt2.close();
				 rs2.close();

			 }
			 rs.close();
			 stmt.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			try{
				if(conn!=null){
					conn.close();
					conn=null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return messages;
	}
	
	
	public int getMessageCount() throws Exception{
		int count =0;
		Connection conn=null;
		try{
			 Class.forName("com.mysql.jdbc.Driver");
			 conn= DriverManager.getConnection("jdbc:mysql://172.31.75.246:3306/S2014150057","S2014150057","791089");
			 Statement stmt=conn.createStatement();
			 ResultSet rs = stmt.executeQuery("select count(*) count from message");
		    	if(rs.next()) {
		    	  count=rs .getInt("count");
		    	}
			 rs.close();
			 stmt.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			try{
				if(conn!=null){
					conn.close();
					conn=null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return count;
	}
	
	
	//特殊字符转义
	private String replace(String a){
		 String html = a;
		     html = html.replaceAll( "&", "&amp;");
		     html = html.replace( "\"", "&quot;");  //"
		     html = html.replace( "\t", "&nbsp;&nbsp;");// 替换跳格
		     html = html.replace( " ", "&nbsp;");// 替换空格
		     html = html.replace("<", "&lt;");
		     html = html.replaceAll( ">", "&gt;");
		     return html;

	}
}
