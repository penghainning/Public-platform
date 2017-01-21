package app.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

import app.interceptor.AuthInterceptor;
import app.service.ArticalService;
import app.service.PublicService;
import app.service.UserService;
import app.util.CheckFormat;
import app.util.MD5;

@Before(AuthInterceptor.class)
public class PublicController extends Controller {

	private PublicService publicService=new PublicService();
	

	//公众号用户获取已订阅自己的用户（根据相关条件搜索）
	public void findUserList() throws UnsupportedEncodingException{
		HashMap<String, String>mp=new HashMap<>();
		try{
			String userid="";
			userid=getSession().getAttribute("userid").toString();//登录人id
			String nickname = getPara("nickname");//昵称
			String sex = getPara("sex");//性别
			String city = getPara("city");//地区
			String curPage = getPara("curpage");
			String pageSize = getPara("pagesize");
			mp.put("userid", userid);
			mp.put("sex", CheckFormat.replace(sex));
			mp.put("city", CheckFormat.replace(city));
			mp.put("nickname", CheckFormat.replace(nickname));
			mp.put("curPage", curPage);
			mp.put("pageSize", pageSize);
			renderJson(publicService.findUserList(mp));
		}catch(Exception e){
			e.printStackTrace();
			renderJson(mp);
		}
		
	}
	
	//查看订阅用户详情
		public void getUserDetail(){
			try{
				String id = getPara("id");
				renderJson(publicService.getUserDetail(id));
			}catch(Exception e){
				e.printStackTrace();
				renderJson(CheckFormat.errorMap("系统异常"));
			}
		
			
		}
	

		//公众号用户修改密码
		public void changePassword(){
			try{
				String userid=getSession().getAttribute("userid").toString();//登录人id
				String newPassword = getPara("newpassword");//新密码
				String oldPassword = getPara("oldpassword");//旧密码
				renderJson(publicService.changePassword(userid, newPassword, oldPassword));
			}catch(Exception e){
				renderError(0);
				e.printStackTrace();

			}	
		}
		
		
		
		//修改公众号信息
		public void editPublic(){
			try{
				String userid=getSession().getAttribute("userid").toString();//登录人id
				String nickname = getPara("nickname");
				String city = getPara("city");
				String identity = getPara("identity");
				String mail = getPara("mail");
				String phone = getPara("phone");
				String sex = getPara("sex");
				String oldhead = getPara("oldhead");//旧头像
				String newhead = getPara("newhead");//新头像
				HashMap<String, Object>mp=new HashMap<>();
				mp.put("userid", userid);
				mp.put("nickname", CheckFormat.replace(nickname));
				mp.put("city", CheckFormat.replace(city));
				mp.put("mail", CheckFormat.replace(mail));
				mp.put("identity", identity);
				mp.put("phone", phone);
				mp.put("sex", sex);
				mp.put("oldhead", oldhead);
				mp.put("newhead", newhead);
				Map<String,Object> result=publicService.editPublic(mp);
				if(Boolean.getBoolean(result.get("result").toString())){
					getSession().removeAttribute("head");//清除session中保存的头像的值
				}
				  
				renderJson(result);
			}catch(Exception e){
				renderError(0);
				e.printStackTrace();
			}
			
		}
		
		
		
	

}
