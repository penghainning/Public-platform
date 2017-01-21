package app.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import app.common.CommonName;
import app.util.CheckFormat;
import app.util.MD5;

public class PublicService {



	//获取已订阅自己用户列表
		public HashMap<String,Object> findUserList(HashMap<String, String> parm) {
			// TODO Auto-generated method stub
			HashMap<String,Object> mp=new HashMap<>();
			ArrayList<HashMap<String,Object>> tbody=new ArrayList<>();
			int curPage=parm.get("curPage")==null||"".equals(parm.get("curPage"))?1:Integer.valueOf(parm.get("curPage"));
			int pageSize=parm.get("pageSize")==null?5:Integer.valueOf(parm.get("pageSize"));
			String search="";
			try{
				String sex=parm.get("sex");//性别
				String city=parm.get("city");//城市
				String nickname=parm.get("nickname");//昵称
				String userid=parm.get("userid");//本人id

			  
				String SQL = "SELECT a.*,b.userid,b.caretime FROM "
						     +CommonName.TABLE_ORDINARYUSER
						     +" a,care b where b.state= '1' and a.state='1' and a.id=b.userid and b.careid=' "
						     +userid
						     +"' ";
			
						   
				if(sex!=null && !"".equals(sex)){
					if("男".equals(sex)){
						SQL+=" and a.sex = '1' ";
						search+=" and a.sex = '1' ";
					}
					else if("女".equals(sex)){
						SQL+=" and a.sex = '0' ";
						search+=" and a.sex = '0' ";
					}
					
				}
			
				if(city!=null && !"".equals(city)){
					SQL+=" and a.city like '%"+city+"%' ";
					search+=" and a.city like '%"+city+"%' ";
					
				}
				
				if(nickname!=null && !"".equals(nickname)){
					SQL+=" and a.nickname like '%"+nickname+"%' ";
					search+=" and a.nickname like '%"+nickname+"%' ";
					
				}
				
				
				SQL+=" order by b.caretime desc limit "+(curPage-1)*pageSize+" , "+pageSize;
				int i=0;
				List<Record> list=Db.find(SQL);		
					if(list.size()>0){
						for(Record rec:list){
							i++;
							HashMap<String,Object> map=new HashMap<>();
							map.put("nickname", rec.get("nickname"));//昵称
							map.put("id", rec.get("id"));//用户id
							map.put("caretime", rec.get("caretime"));//订阅时间
							map.put("num", (curPage-1)*pageSize+i);//序号
							map.put("city",rec.get("city"));//地区
							map.put("sex", rec.getInt("sex")==1?"男":"女");//性别
							tbody.add(map);		
						}
						HashMap<String,Object> pageArgument=new HashMap<>();
						
						
						
						//计算总条数，算出总页数
						String page= "SELECT count(*) amount  FROM "
							     +CommonName.TABLE_ORDINARYUSER
							     +" a,care b where b.state= '1' and a.state='1' and a.id=b.userid and b.careid=' "
							     +userid
							     +"' ";;
							     
						Record r=Db.findFirst(page+search);
						long total=r.get("amount");
						int pagecount =(int) total / pageSize;
								if (total % pageSize != 0)
								{
								pagecount ++;
								}
						mp.put("result", true);
						mp.put("total", list.size());
						pageArgument.put("totalpage", pagecount);
						pageArgument.put("curpage", curPage);
						pageArgument.put("pagecount", pageSize);
						mp.put("pageArgument", pageArgument);
						mp.put("tbody", tbody);
						mp.put("message", "成功");
					}else{
						
						HashMap<String,Object> pageArgument=new HashMap<>();
						mp.put("result", false);
						mp.put("total", 0);
						pageArgument.put("curpage", curPage);
						pageArgument.put("pagecount", pageSize);
						mp.put("pageArgument", pageArgument);
						mp.put("tbody", tbody);
						mp.put("message", "无相关记录");
					}
			
			}catch(Exception e){
				HashMap<String,Object> pageArgument=new HashMap<>();
				mp.put("result", false);
				mp.put("total", 0);
				pageArgument.put("curpage", curPage);
				pageArgument.put("pagecount", pageSize);
				mp.put("pageArgument", pageArgument);
				mp.put("tbody", "");
				mp.put("message", "查询出错");
				e.printStackTrace();
			}			
			return mp;
		}
		
		
		

		//查看用户详情
		public HashMap<String,Object> getUserDetail(String id) {
			
			HashMap<String,Object> map=new HashMap<>();
			// TODO Auto-generated method stub
			try{
				String SQL = "SELECT * FROM "
							+CommonName.TABLE_ORDINARYUSER
							+" WHERE id ='"+id+"' and state = 1 ";
				Record record=Db.findFirst(SQL);
				if(record!=null){
					map.put("result",true);
					map.put("photo", "/S2014150057/image/userhead/"+record.get("photo"));
					map.put("nickname", record.get("nickname"));
					map.put("sex", record.getInt("sex")==1?"男":"女");
					map.put("mail", record.get("mail"));
					String str =record.get("phone");
					String ss = str.substring(0,str.length()-(str.substring(3)).length())+"****"
								+str.substring(7); 
					map.put("phone", ss);
					map.put("city", record.get("city"));
					
				}else{
					map.put("result", false);
					map.put("message", "查看用户详情出错,该用户不存在");
					
				}
			}catch(Exception e){
				map.put("result", false);
				map.put("message", "查看用户详情出错");
				e.printStackTrace();
			}
			
			
			return map;
		}
		
		
		
		//公众号修改密码接口
		public HashMap<String,Object> changePassword(String id,String newPassword,String oldPassword){
			
			HashMap<String,Object> map=new HashMap<>();
			try{
				String safeOld=MD5.MD5Encode(oldPassword);
				Integer iid=Integer.valueOf(id);
				String SQL = "SELECT id FROM "
							+CommonName.TABLE_PUBLICUSER
							+" WHERE id = ? and password= ?";
				Record record=Db.findFirst(SQL,new Object[]{iid,safeOld});
				if(record!=null)	
				{
					String safeNew=MD5.MD5Encode(newPassword);
					record.set("password", safeNew);
					if(Db.update(CommonName.TABLE_PUBLICUSER, record)){
						map.put("result", true);
						map.put("message", "修改密码成功！");
					}else{
						map.put("result", false);
						map.put("message", "更改密码出错！");
					}		
				
				}
				else 
				{
					map.put("result", false);
					map.put("message", "原密码或用户名错误，更改失败");
				}
				
			}catch(Exception e){
				map.put("result", false);
				map.put("message", "更改密码失败");
				e.printStackTrace();
				return map;
			}
			
			return map;
		}
		
		
		
		//编辑公众号信息
		public Map<String,Object>  editPublic(HashMap<String,Object> parm){
			HashMap<String,Object> map=new HashMap<>();
			try{
					int userid=Integer.valueOf(parm.get("userid").toString());//公众号id
					//编辑
					String nickname=parm.get("nickname").toString();
					String phone=parm.get("phone").toString();
					String sex=parm.get("sex").toString();
					String identity=parm.get("identity").toString();
					String city=parm.get("city").toString();
					String oldhead=parm.get("oldhead").toString();
					String mail=parm.get("mail").toString();
					String newhead=parm.get("newhead").toString();
					
					//检查手机格式：
					if(!CheckFormat.isMobileNO(phone)){
						return CheckFormat.errorMap("手机号码格式不正确");
					}else{
						String SQL = "SELECT * FROM "
									+CommonName.TABLE_PUBLICUSER
									+" WHERE id ='"+userid+"' and state= 1";
						Record record=Db.findFirst(SQL);
						if(record!=null){
							
							//查询昵称是否被占用
							String nick = "SELECT * FROM "
									+CommonName.TABLE_PUBLICUSER
									+" WHERE nickname = ? and state= 1";
							Record r=Db.findFirst(nick,new Object[]{nickname});
							if(r!=null&& r.getInt("id")!=userid){
								 map.put("message", "该昵称已经被占用！请重新输入昵称");
						         map.put("result", false);
							}else{
								//查询手机号是否被占用
								String phonesql = "SELECT * FROM "
										+CommonName.TABLE_PUBLICUSER
										+" WHERE phone = ? and state= 1";
								r=Db.findFirst(phonesql,new Object[]{phone});
								if(r!=null && r.getInt("id")!=userid){
									map.put("message", "该手机号已经被占用！请重新输入手机号");
							        map.put("result", false);
							        return map;
								}
									record.set("nickname", nickname);
									record.set("phone", phone);
									record.set("sex", sex);
									record.set("identity", identity);
									record.set("city", city);
									record.set("mail", mail);//邮箱
									record.set("flag", 1);//标记为完整
									if(!oldhead.equals(newhead))//用户更换了头像
									{	
									  record.set("photo", newhead);
									  if(!"default.jpg".equals(oldhead))
										  CheckFormat.deleteFile(oldhead);//删除旧头像
									}
									if(Db.update(CommonName.TABLE_PUBLICUSER, record)){
								         map.put("message", "修改信息成功");
								         map.put("result", true);
									}
							}
							
							
						}else{
							 map.put("message", "无该用户信息，修改信息失败");
					         map.put("result", false);
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