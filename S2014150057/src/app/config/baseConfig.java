package app.config;

import com.jfinal.config.*;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.render.ViewType;

import app.controller.ArticalController;
import app.controller.IndexController;
import app.controller.OrdinaryController;
import app.controller.PublicController;
import app.controller.UserController;
import app.handler.urlHandler;
import app.model.Jfinaluser;
import app.common.CommonName;

public class baseConfig extends JFinalConfig {

	@Override
	public void configConstant(Constants me) {
		// TODO Auto-generated method stub
		loadPropertyFile("WEB-INF/config/dbsource_config.txt");
		//System.out.println(PathKit.getWebRootPath() +"\\config\\dbsource_config.txt");
		me.setDevMode(true);

		me.setEncoding("utf-8");

		me.setViewType(ViewType.JSP); 
		
	}

	@Override
	public void configRoute(Routes me) {
		// TODO Auto-generated method stub
		me.add("/", IndexController.class); 
		me.add("/user", UserController.class); 
		me.add("/artical", ArticalController.class); 
		me.add("/pub", PublicController.class);
		me.add("/ordinary", OrdinaryController.class);
	
	}

	@Override
	public void configPlugin(Plugins me) {
		// TODO Auto-generated method stub
		C3p0Plugin c3p0Plugin = new C3p0Plugin(getProperty("datasource-url"),
				getProperty("datasource-user"), getProperty(
						"datasource-password").trim());
		c3p0Plugin.setDriverClass(getProperty("datasource-driver"));
		c3p0Plugin.setMaxPoolSize(1000);
		me.add(c3p0Plugin);
		ActiveRecordPlugin arp = new ActiveRecordPlugin(CommonName.TABLE_DATABASE,c3p0Plugin);
		//arp.addMapping("jfinaluser", "uid",Jfinaluser.class);
		me.add(arp);
		// 配置属性名(字段名)大小写不敏感容器工厂
		arp.setContainerFactory(new CaseInsensitiveContainerFactory(true));
		

	}

	@Override
	public void configInterceptor(Interceptors me) {
		// TODO Auto-generated method stub

	}

	@Override
	public void configHandler(Handlers me) {
		// TODO Auto-generated method stub
		me.add(new ContextPathHandler("basePath")); 
		me.add(new urlHandler());

	}

}
