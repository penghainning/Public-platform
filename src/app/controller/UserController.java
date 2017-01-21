package app.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import app.common.CommonName;
import app.interceptor.AuthInterceptor;
import app.interceptor.HomeInterceptor;
import app.service.UserService;
import app.util.CheckFormat;
import app.util.MD5;


public class UserController extends Controller {

	private UserService userService = new UserService();
	
	
	//登陆
	public void login(){
		try{
			String account = getPara("username");
			String password = getPara("password");
			String remember = getPara("remember");
			String type = getPara("type");
			String code = getPara("code");
			HashMap<String,String> parm=new HashMap<>();
			parm.put("account", account);
			parm.put("password", password);
			parm.put("remember", remember);
			parm.put("type", type);
			parm.put("code", code);
			parm.put("randstr", getSession().getAttribute("randstr").toString());
			
			if (getCookieObject("account") == null
			      || getCookieObject("password") == null)
				{// 之前没设置过	
			      parm.put("hasCookie", "false");   
				}
			else
				{// 非第一次放置cookie
			         // System.out.println(getCookieObject("account").getValue()+"#"+getCookieObject("password").getValue());
			        if (!account.equals(getCookieObject("account").getValue())
			               && !password.equals(getCookieObject("password").getValue()))
			         {// 如果帐号有变化，则重新放置cookie
					       parm.put("hasCookie", "false");
					            // System.out.println("不是第一次勾选记住我");
			         }// 否则，不进行操作
			         else 
			         {
			                 parm.put("hasCookie", "true");
			                	// System.out.println("有cookie");
			         }
				}
		       
			Map<String, Object>mp=userService.login(parm);
			//登陆成功设置cookie
			if((boolean)mp.get("result")){
				
				if("true".equals(remember)){
				 setCookie("account", account, 8640);//设置cookie为1天
	             setCookie("password", MD5.MD5Encode(password), 8640);
				 } else {// 没有选择保存账号密码
			            setCookie("account", "", 0);
			            setCookie("password", "", 0);
			            //System.out.println("没有选择记住我");
			        }
			
	             setSessionAttr("flag", true);
	             setSessionAttr("userid", mp.get("userid"));
	             setSessionAttr("type", type);
	            
	             
			}
			renderJson(mp);
			
		}catch(Exception e){
			e.printStackTrace();
			renderJson(CheckFormat.errorMap("系统异常"));
		}
	}
	
	//注册
	public void regist(){
		try{
			
			//获取前端传来的参数并且进行转义
			String account = getPara("account");//账号
			String password = getPara("password");//密码
			String phone = getPara("phone");//手机
			String nickname = getPara("nickname");//昵称
			String sex = getPara("sex");//性别
			String type = getPara("type");//注册账号类型，1为普通用户，2为公众号用户
			HashMap<String,String> parm=new HashMap<>();
			parm.put("account", account);
			parm.put("password", password);
			parm.put("phone", phone);
			parm.put("nickname", CheckFormat.replace(nickname));
			parm.put("sex", sex);
			parm.put("type", type);
			renderJson(userService.regist(parm));
		}catch(Exception e){
			e.printStackTrace();
			renderJson(CheckFormat.errorMap("系统异常"));
		}
	
		
	}
	
	
	//首页
	@Before(HomeInterceptor.class)
	public void home(){
		Boolean loginUser = getSessionAttr("flag");
		String type = getSessionAttr("type");
		if (loginUser!=null&&loginUser==true )
		{
			if(type.equals("1"))
				render("/page/pt_index.html");
			else
				render("/page/index.html");
		}
			
		else
			render("/login");
	}
	
	
	
	@Before(AuthInterceptor.class)
	public void getNickname(){
		HashMap<String,Object> map=new HashMap<>();
		try{
			String nickname = getSessionAttr("nickname");
			String photo = getSessionAttr("photo");
			map.put("photo",photo);
			map.put("result",true);
			map.put("nickname",nickname);
		}catch(Exception e){
			map.put("result",false);
			map.put("message","系统异常，请重新登陆");
			e.printStackTrace();
		}
		
		renderJson(map);
	}
	
	@Before(AuthInterceptor.class)
	public void getAccount(){
		HashMap<String,Object> map=new HashMap<>();
		try{
			String userid=getSession().getAttribute("userid").toString();//登录人id
			String type = getSessionAttr("type");
			String tablename="";
			if(type.equals("1"))//普通用户
				tablename  = CommonName.TABLE_ORDINARYUSER;
			else//公众号用户
				tablename= CommonName.TABLE_PUBLICUSER;
			Record r=Db.findFirst("select * from "+tablename +" where id ='"+userid+"' and state = 1 ");
			if(r==null){
				map.put("result",false);
				map.put("message","无该用户信息");
			}else{
				map.put("account",r.get("account"));//账号
				map.put("nickname",r.get("nickname"));//昵称
				map.put("sex",r.getInt("sex")==1?"男":"女");//性别
				map.put("identity",r.get("identity"));//身份证号
				map.put("phone",r.get("phone"));//手机号
				map.put("photo",r.get("photo"));//头像
				map.put("photourl","/S2014150057/image/userhead/"+r.get("photo"));//头像
				map.put("city",r.get("city"));//地区
				map.put("createtime",r.get("createtime"));//创建时间
				map.put("mail",r.get("mail"));//邮箱
				map.put("flag", r.get("flag"));//用户完整性判断
				map.put("result",true);//
							
			}
			
		}catch(Exception e){
			map.put("result",false);
			map.put("message","系统异常，请重新登陆");
			e.printStackTrace();
		}
		
		renderJson(map);
	}
	
	
	public void logout(){
		HashMap<String,Object> map=new HashMap<>();
		try{
			getSession().removeAttribute("flag");
			getSession().removeAttribute("type");
			getSession().removeAttribute("nickname");
			getSession().removeAttribute("userid");
			getSession().removeAttribute("photo");
			if(getSession().getAttribute("head")!=null)//session中存在头像信息说明之前传过接着用户又选择其他图片
			    	CheckFormat.deleteFile(getSession().getAttribute("head").toString());//删除图片
			map.put("result",true);
		}catch(Exception e){
			map.put("result",false);
			map.put("message", "系统异常，稍后重试");
			e.printStackTrace();
		}
		
		renderJson(map);
	}
	
	
	//上传图片
	public void setHead(){
		
		HashMap<String,Object> map=new HashMap<>();
		try{
			
			//获取传过来的文件名、文件对象
			UploadFile upload = getFile("file");
			String filename=upload.getFileName();
		    File file=upload.getFile();
		    if(getSession().getAttribute("head")!=null)//session中存在头像信息说明之前传过接着用户又选择其他图片
		    	CheckFormat.deleteFile(getSession().getAttribute("head").toString());//删除图片
		    
		    
			if(!CheckFormat.checkPicName(filename))//检查是否是图片格式
			{
				map.put("result",false);
				map.put("message", "上传格式错误！请重新选择");
				upload.getFile().delete();
				renderJson(map);
				return;
			}
			FileInputStream inputStream=new FileInputStream(file);//获取文件流判断文件大小
			if(inputStream.available()> 1024*1024*10){
				map.put("result",false);
				map.put("message", "上传文件大小不能超过10M");
				upload.getFile().delete();
				renderJson(map);
			}
			String path=PathKit.getWebRootPath()+"/image/userhead";//设置头像上传路径
			//System.out.println(path);
			String type=filename.substring(filename.lastIndexOf("."));//获取头像后缀名
			SimpleDateFormat formatter=new SimpleDateFormat("yyyyMMddhhmmss");
			Date currentTime =new Date();
			String dataString= formatter.format(currentTime);//以当前时间作为文件名
			FileOutputStream outputStream=new FileOutputStream(path+"/"+dataString+type);
			byte[] buf=new byte[1024];
			int length=0;
			while((length=inputStream.read(buf))!=-1){
				outputStream.write(buf,0,length);
			}//将文件流写到新的以当前时间命名的文件中
			inputStream.close();
			outputStream.flush();
			outputStream.close();
			getSession().setAttribute("head", dataString+type);//在session中设置头像信息
			map.put("result",true);
			map.put("message", "上传成功");
			map.put("filename",  dataString+type);
			map.put("photourl","/S2014150057/image/userhead/"+dataString+type);//返回成功信息及文件名给客户端
			upload.getFile().delete();//删除临时文件
		}catch(Exception e){
			map.put("result",false);
			map.put("message",e.getMessage());
			e.printStackTrace();
		}
		
		renderJson(map);
	}


}
