package app.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;

public class urlHandler extends Handler {

	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		// TODO Auto-generated method stub
		
		System.out.println("handler:"+target);
		if("/javaWebBc/loginServlet".equals(target)||"/javaWebBc/registServlet".equals(target))
			return;
		else
		 nextHandler.handle(target, request, response, isHandled);
	}

}
