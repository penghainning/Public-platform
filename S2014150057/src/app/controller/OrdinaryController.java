package app.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

import app.interceptor.AuthInterceptor;
import app.service.ArticalService;
import app.service.OrdinaryService;
import app.service.UserService;
import app.util.CheckFormat;
import app.util.MD5;

@Before(AuthInterceptor.class)
public class OrdinaryController extends Controller {

	private OrdinaryService ordinaryService=new OrdinaryService();
	
	
	

	//普通用户获取已订阅公众号昵称
	public void findPubList() throws UnsupportedEncodingException{
		HashMap<String, Object>mp=new HashMap<>();
		try{
			String userid="";
			userid=getSession().getAttribute("userid").toString();
			renderJson(ordinaryService.findPubList(userid));
		}catch(Exception e){
			e.printStackTrace();
			mp.put("result", false);
			mp.put("message", "系统错误！");
			renderJson(mp);
		}
		
	}
	
	
	//普通用户获取已订阅的公众号用户列表（根据相关条件搜索）
		public void findCareUserList() throws UnsupportedEncodingException{
			HashMap<String, String>mp=new HashMap<>();
			try{
				String userid="";
				userid=getSession().getAttribute("userid").toString();
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
				renderJson(ordinaryService.findCareUserList(mp));
			}catch(Exception e){
				e.printStackTrace();
				renderJson(mp);
			}
			
		}
		
		
		
		//普通用户获取未订阅的公众号用户列表（根据相关条件搜索）
			public void findNotCareUserList() throws UnsupportedEncodingException{
				HashMap<String, String>mp=new HashMap<>();
				try{
					String userid="";
					userid=getSession().getAttribute("userid").toString();//登录人id
					String nickname = getPara("nickname");//昵称
					String sex = getPara("sex");//性别
					String city = getPara("city");//地区
					String curPage = getPara("curpage");//当前页
					String pageSize = getPara("pagesize");//一页几条
					
					//将数据进行转义
					mp.put("userid", userid);
					mp.put("sex", CheckFormat.replace(sex));
					mp.put("city", CheckFormat.replace(city));
					mp.put("nickname",CheckFormat.replace(nickname));
					mp.put("curPage", curPage);
					mp.put("pageSize", pageSize);
					renderJson(ordinaryService.findNotCareUserList(mp));
				}catch(Exception e){
					e.printStackTrace();
					renderJson(mp);
				}
				
			}
		
		//查看公众号用户详情
			public void getPubUserDetail(){
				try{
					
					String id = getPara("id");
					renderJson(ordinaryService.getPubUserDetail(id));
				}catch(Exception e){
					e.printStackTrace();
					renderJson(CheckFormat.errorMap("系统异常"));
				}
			
				
			}
			
			//订阅公众号公众号用户
			public void carePubUser(){
				try{
					String userid="";	
					userid=getSession().getAttribute("userid").toString();//登录人id
					String id = getPara("id");//要订阅的公众号id
					renderJson(ordinaryService.carePubUser(id,userid));
				}catch(Exception e){
					e.printStackTrace();
					renderJson(CheckFormat.errorMap("系统异常"));
				}
			
				
			}

			//取消订阅公众号公众号用户
			public void cancelCarePubUser(){
				try{
					String userid="";
					userid=getSession().getAttribute("userid").toString();//登录人id
					String id = getPara("id");//要取消订阅的公众号id
					renderJson(ordinaryService.cancelCarePubUser(id,userid));
				}catch(Exception e){
					e.printStackTrace();
					renderJson(CheckFormat.errorMap("系统异常"));
				}
			
				
			}
		
		

			//普通用户修改密码
			public void changePassword(){
				try{
					String userid=getSession().getAttribute("userid").toString();//登录人id
					String newPassword = getPara("newpassword");//新密码
					String oldPassword = getPara("oldpassword");//旧密码
					renderJson(ordinaryService.changePassword(userid, newPassword, oldPassword));
				}catch(Exception e){
					renderError(0);
					e.printStackTrace();

				}	
			}
			
			
			
			//修改普通用户信息
			public void editOrdinary(){
				try{
					String userid=getSession().getAttribute("userid").toString();//登录人id
					String nickname = getPara("nickname");
					String city = getPara("city");
					String identity = getPara("identity");
					String mail = getPara("mail");
					String phone = getPara("phone");
					String sex = getPara("sex");
					String newhead = getPara("newhead");//新头像
					String oldhead=getPara("oldhead");//旧头像
					HashMap<String, Object>mp=new HashMap<>();
					mp.put("userid", userid);
					mp.put("nickname", CheckFormat.replace(nickname));
					mp.put("city", CheckFormat.replace(city));
					mp.put("mail", CheckFormat.replace(mail));
					mp.put("identity", identity);
					mp.put("phone", phone);
					mp.put("sex", sex);
					mp.put("newhead", newhead);
					mp.put("oldhead", oldhead);
					Map<String,Object> result=ordinaryService.editOrdinary(mp);
					if((boolean)result.get("result"))
						getSession().removeAttribute("head");//清除session的值
					renderJson(result);
				}catch(Exception e){
					renderError(0);
					e.printStackTrace();
				}
				
			}
			
	


}
