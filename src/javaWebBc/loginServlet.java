package javaWebBc;

import java.io.IOException;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sun.misc.BASE64Encoder;


/**
 * Servlet implementation class loginServlet
 */
@WebServlet("/loginServlet")
public class loginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Connection conn;  
    private ServletContext application;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public loginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    
    @Override
    public void init() throws ServletException {
    	
    	
    	// TODO Auto-generated method stub
    	try {
    		application=this.getServletContext();
			Class.forName(application.getInitParameter("driver-name"));
			conn=DriverManager.getConnection(application.getInitParameter("databases-url"),
											 application.getInitParameter("username"),
											 application.getInitParameter("password"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("init方法");
    	super.init();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		System.out.println("doPost");
	   
		  @SuppressWarnings("unchecked")
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
		
		
		
		
 		String cardId=request.getParameter("cardId");
 	 	String password=request.getParameter("password");
 	 	String code =request.getParameter("code");
 	 	String randStr=(String)request.getSession().getAttribute("randstr");
 	 	String remember = request.getParameter("remember");
 	 	
      
 	 	if(cardId!=null&&password!=null)
 	 	{
 	 		if(!code.equals(randStr))
 	 		{
 	 			
 	 			//request.setAttribute("errorcode", "1");
 	 			//request.getRequestDispatcher("t8/login.jsp?nocache="+new Date().getTime()).forward(request, response);
 	 			response.sendRedirect("/S2014150057/t8/login.jsp?errorcode=1");
 	 			System.out.println("验证码错误");
 	 		}
 	 
 	 		else
 	 		{
 	 			
 	 			 try{
 	 			    //数据库连接
 	 				if(j>0&&name.equals(cardId)&&pwd.equals(password))
 			 	      hasCookie=true;
 	 			    Statement stmt=conn.createStatement();
 	 			    
 	 			    //密码加密
 	 				MessageDigest md5=MessageDigest.getInstance("MD5");
 	 	 	        BASE64Encoder base64en = new BASE64Encoder();
 	 	 	       	String safePasssword=base64en.encode(md5.digest(password.getBytes("utf-8")));
 	 	 	       	String pwds="";
 	 	 	   
 	 	 	       	if(hasCookie)
 	 	 	       		pwds=password;
 	 	 	       	else
 	 	 	       		pwds=safePasssword;
 	 	 	       	//检查用户名密码
 	 			   	ResultSet rs=stmt.executeQuery("select * from t7user where username='"+cardId+"' and password ='"+pwds+"'");
 	 			 	rs.last();//移到最后一行
 	 				int count = rs.getRow();
 	 				rs.beforeFirst();//移到初始位置
 	 			    if(count>0)
 	 				{
 	 					rs.next();
 	 					Date currentTime = new Date();
 	 		 		  	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 	 		 		  	String time = formatter.format(currentTime);
 	 		 		 	String loginIp=request.getRemoteAddr();
 	 					String sql="update t7user set logintimes='"+time+"',loginip='"+loginIp+"' where username='"+cardId+"'";
 	 					Statement stmt2=conn.createStatement();
 	 		 			int i=stmt2.executeUpdate(sql);
 	 		 			if(i>0)
 	 		 			{
 	 		 				request.getSession().setAttribute("name", cardId);
 	 		 				request.getSession().setAttribute("flag", "true");
 	 		 				request.getSession().setAttribute("loginip", loginIp);
 	 		 				request.getSession().setAttribute("logintimes", time);
	 		 			    map.put(cardId, cardId);
	 		 				application.setAttribute("people", map);
	 		 				System.out.print("登陆成功"+map.size()+":"+map.get(cardId));
 	 		 				if("yes".equals(remember))
 	 		 				{
 	 		 					if(!hasCookie)
 	 		 					{
 	 		 					//创建两个Cookie对象
 	  	 		 				  Cookie nameCookie = new Cookie("username", cardId);
 	  	 		 				  //设置Cookie的有效期为60秒
 	  	 		 				  nameCookie.setMaxAge(60);
 	  	 		 				  nameCookie.setPath("/S2014150057");
 	  	 		 				  Cookie pwdCookie = new Cookie("password", safePasssword);
 	  	 		 				  pwdCookie.setMaxAge(60);
 	  	 		 				  pwdCookie.setPath("/S2014150057");
 	  	 		 				  response.addCookie(nameCookie);
 	  	 		 				  response.addCookie(pwdCookie);
 	 		 					}
 	 		 				 
 	 		 				}
 	 		 				stmt2.close();
 	 		 				System.out.println("用户名密码正确");
 	 	 					response.sendRedirect("/S2014150057/t8/hello.jsp");
 	 	 					
 	 		 			}
 	 		 			else
 	 		 			{
 
 						//request.setAttribute("errorcode", "2");
 						//request.getRequestDispatcher("t8/login.jsp?nocache="+new Date().getTime()).forward(request, response);
 	 		 			response.sendRedirect("/S2014150057/t8/login.jsp?errorcode=2");
 						System.out.println("未知错误");
 						
 	 		 			}
 	 					
 	 				
 	 					
 	 				}
 	 				else
 	 				{
 	 					
 	 					//request.setAttribute("errorcode", "3");
 	 					//request.getRequestDispatcher("t8/login.jsp?nocache="+new Date().getTime()).forward(request, response);
 	 					response.sendRedirect("/S2014150057/t8/login.jsp?errorcode=3");
 		 	 			System.out.println("用户名密码错误");
 	 					
 	 				}

 	 			   rs.close();
 	 			   stmt.close();
 	 	    	}catch(Exception e){
	 	 			e.printStackTrace();
	 	 	    	}
 	 		}
	 	 
 		}
	
 	 	
	}

}
