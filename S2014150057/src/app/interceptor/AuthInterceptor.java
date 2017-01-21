package app.interceptor;

import java.util.HashMap;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

import app.util.CheckFormat;

public class AuthInterceptor implements Interceptor {
	@Override
	public void intercept(Invocation invocation) {
		try{
			Controller controller = invocation.getController();
			Boolean loginUser = controller.getSessionAttr("flag");
			if (loginUser!=null&&loginUser==true&&controller.getSessionAttr("userid")!=null)
				invocation.invoke();
			else{
				System.out.println("intercept:登陆失效，请重新登陆");
				HashMap<String, Object>mp=new HashMap<>();
				mp.put("result", false);
				mp.put("message", "登陆失效，请重新登陆！");
				mp.put("code",-1);
				controller.renderJson(mp);
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	
	}
	
	
}