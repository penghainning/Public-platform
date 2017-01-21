package app.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.jfinal.core.Controller;

public class IndexController extends Controller {
	
	public void index(){

		this.renderJson("web大作业");

		}
	
	public void regist()
	{
		this.renderJsp("page/regesit.jsp");
	}

	public void login(){
		try{
			//this.renderJson("登录");
		     String userName = "";
		        String userPass = "";
		        if (getCookieObject("account") != null
		                && getCookieObject("password") != null) {
		            try {
		                userName = URLDecoder.decode(getCookieObject("account")
		                        .getValue(), "utf-8");
		                userPass = URLDecoder.decode(getCookieObject("password")
		                        .getValue(), "utf-8");
		            } catch (UnsupportedEncodingException e) {
		                e.printStackTrace();
		            }
		        }
		        //System.out.println(userName+"#"+userPass);
		        setAttr("account", userName);
		        setAttr("password", userPass);
		        setSessionAttr("flag", false);
		        this.renderJsp("/page/login.jsp");
		        //getResponse().sendRedirect("/page/login.jsp");
			
	

		}catch(Exception e){
			e.printStackTrace();
		}
	}
		
	

}
