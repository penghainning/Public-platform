package app.interceptor;

import java.util.HashMap;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

import app.util.CheckFormat;

public class HomeInterceptor implements Interceptor {
	@Override
	public void intercept(Invocation invocation) {
		try{
			Controller controller = invocation.getController();
			Boolean loginUser = controller.getSessionAttr("flag");
			if (loginUser!=null&&loginUser==true&&controller.getSessionAttr("userid")!=null)
				invocation.invoke();
			else{
			
				controller.redirect("/login");
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	
	}
	
	
}