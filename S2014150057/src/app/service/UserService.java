package app.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import app.common.CommonName;
import app.util.CheckFormat;
import app.util.MD5;

public class UserService {

	//注册接口
		public Map<String, Object> regist(HashMap<String,String> parm){
			HashMap<String,Object> map=new HashMap<>();
			try{
			  
				String account = parm.get("account");
				String password = parm.get("password");
				String phone = parm.get("phone");
				String nickname=parm.get("nickname");
				String sex = parm.get("sex");
				String type = parm.get("type");
				String tablename="";
				//1为普通用户
				if(type.equals("1")){
					tablename=CommonName.TABLE_ORDINARYUSER;
					
				}else{
					tablename=CommonName.TABLE_PUBLICUSER;
				
				}
				   //判断要注册账号在所有用户表中是存在
				   String sql="select * from "
						   +CommonName.TABLE_ORDINARYUSER
						   +" where account = '"
						   + account
						   + "'";
					Record rec=Db.findFirst(sql);
					sql="select * from "
							   +CommonName.TABLE_PUBLICUSER
							   +" where account = '"
							   + account
							   + "'";
					Record rec2=Db.findFirst(sql);
					if(rec!=null || rec2!=null){
						 map.put("message", "该账号已被占用，请重新输入");
					     map.put("result", false);
					     return map;
					}else{
						
						if(type.equals("2")){
							//公众号用户注册需要判断公众号名称是否被占用
						 	String ss="select * from "
									   +tablename
									   +" where nickname = ?";
								Record r=Db.findFirst(ss,new Object[]{nickname});
								if(r!=null){
									 map.put("message", "该昵称已被其他公众号所使用，请重新输入");
								     map.put("result", false);						   
								     return map;
								}
						}
						if(!CheckFormat.isMobileNO(phone)){
							return CheckFormat.errorMap("手机号码格式不正确");
						}else{
							//注册判断手机是否被占用
						 	String ss="select * from "
									   +tablename
									   +" where phone = ?";
								Record r=Db.findFirst(ss,new Object[]{phone});
								if(r!=null){
									 map.put("message", "该手机号码已被其他用户所注册，请重新输入");
								     map.put("result", false);						   
								     return map;
								}
							Record ordinary=new Record();
							ordinary.set("account", CheckFormat.replace(account));
							ordinary.set("phone", phone);
							ordinary.set("password", MD5.MD5Encode(password));
							ordinary.set("nickname", nickname);
							ordinary.set("photo", "default.jpg");
							ordinary.set("createtime", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
							ordinary.set("sex", sex);
							ordinary.set("state", 1);
							if(Db.save(tablename, ordinary)){
								return CheckFormat.successMap("注册成功！");
							}else{
								return CheckFormat.errorMap("注册失败，请稍后重试");
							}
						}
						
					}
			}catch(Exception e){
				e.printStackTrace();
			        map.put("message", "注册失败，系统异常");
			        map.put("result", false);
			        return map;
			}
						
		}
		
		
	//登录接口
		public Map<String,Object>  login(HashMap<String,String> parm){
			HashMap<String,Object> map=new HashMap<>();
			try{
				
				String account = parm.get("account");
				String password = parm.get("password");
				boolean hasCookie="true".equals(parm.get("hasCookie"))?true:false;
				String code=parm.get("code");
				String type = parm.get("type");
				String randstr = parm.get("randstr");
				String tablename="";
				
				if(!code.equals(randstr)){
					map.put("message", "验证码错误");
			        map.put("result", false);
				}else{
					//如果不存在cookie
					if(type.equals("1")){
						tablename=CommonName.TABLE_ORDINARYUSER;
						
					}else{
						tablename=CommonName.TABLE_PUBLICUSER;
					}
					if(!hasCookie){
						//1为普通用户
						String safePassword=MD5.MD5Encode(password);
						//String SQL = "SELECT * FROM "+tablename+" WHERE account ='"+account+"' and password='"+safePassword+"' and state = '1'";
						String SQL = "SELECT * FROM "+tablename+" WHERE account =? and password=? and state = ?";
						System.out.println(SQL);
						Record record=Db.findFirst(SQL, new Object[]{account,safePassword,1});
						if(record!=null){
							 map.put("userid",record.get("id"));
					         map.put("message", "");
					         map.put("result", true);
						}
							
						else{
					        map.put("message", "用户名或者密码错误");
					        map.put("result", false);
						}
					}else{
						String SQL = "SELECT * FROM "+tablename+" WHERE account ='"+account+"'  and  state = '1' ";
						System.out.println(SQL);
						Record record=Db.findFirst(SQL);
						  map.put("userid",record.get("id"));
						  map.put("message", "");
					      map.put("result", true);
					}
					
				}
				
			}catch(Exception e){
				e.printStackTrace();
			        map.put("message", "系统错误");
			        map.put("result", false);
			        return map;
			}
			return map;
			
		
				
		}
		
		
		
}