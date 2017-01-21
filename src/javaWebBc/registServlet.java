package javaWebBc;

import java.io.IOException;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sun.misc.BASE64Encoder;

/**
 * Servlet implementation class registServlet
 */
@WebServlet("/registServlet")
public class registServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Connection conn;  
    private ServletContext application;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public registServlet() {
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
		String account=request.getParameter("account");
 	 	String password=request.getParameter("password");
 	 	String password2 =request.getParameter("password2");
 	 	try{
 	 	if(account!=null&&password!=null)
 	 	{
 	 		Class.forName("com.mysql.jdbc.Driver");
 			Connection conn=DriverManager.getConnection("jdbc:mysql://172.31.75.246:3306/S2014150057","S2014150057","791089");
 			
 			Statement stmt=conn.createStatement();
 			ResultSet rs=stmt.executeQuery("select * from t7user where username='"+account+"'");
 			rs.last();//移到最后一行
			int count = rs.getRow();
			rs.beforeFirst();//移到初始位置
		    if(count>0)
		    {
		    	
		    	
					response.sendRedirect("/S2014150057/t8/register.jsp?errorcode=1");
		 	 		System.out.println("用户名已被占用！请重新输入！");
		
		    }
		    else{
		 			Date currentTime = new Date();
		 		  	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 		  	String time = formatter.format(currentTime);
		 		  	MessageDigest md5=MessageDigest.getInstance("MD5");
		 	        BASE64Encoder base64en = new BASE64Encoder();
		 			//加密后的密码
		 	        String safePasssword=base64en.encode(md5.digest(password.getBytes("utf-8")));
		 			String loginIp=request.getRemoteAddr();
		 		  	String sql="insert into t7user(username,password,loginip,registtimes) values(?,?,?,?)";
		 			PreparedStatement ps=conn.prepareStatement(sql);
		 			ps.setString(1, account);
		 			ps.setString(2, safePasssword);
		 			ps.setString(3, loginIp);
		 			ps.setString(4, time);
		 			int i=ps.executeUpdate();
		 			if(i>0)
		 			{
		 				response.sendRedirect("/S2014150057/t8/login.jsp?errorcode=0");
		 			}
		 			else
		 			{
		 				response.sendRedirect("/S2014150057/t8/register.jsp?errorcode=2");
			 	 		System.out.println("注册失败！未知错误");
		 			}
		    }
 		}
 	 	}catch(Exception e){
 	 		e.printStackTrace();
 	 		response.sendRedirect("/S2014150057/t8/register.jsp?errorcode=3");
 	 		System.out.println("系统异常，稍后重试");
 	 	}
	}

}
