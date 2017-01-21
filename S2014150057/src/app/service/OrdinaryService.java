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

public class OrdinaryService {




	//普通用户获取已订阅的公众号用户列表（根据相关条件搜索）
	public HashMap<String,Object> findCareUserList(HashMap<String, String> parm) {
		// TODO Auto-generated method stub
		HashMap<String,Object> mp=new HashMap<>();
		ArrayList<HashMap<String,Object>> tbody=new ArrayList<>();
		int curPage=parm.get("curPage")==null||"".equals(parm.get("curPage"))?1:Integer.valueOf(parm.get("curPage"));
		int pageSize=parm.get("pageSize")==null?5:Integer.valueOf(parm.get("pageSize"));
		try{
			String sex=parm.get("sex");//性别
			String city=parm.get("city");//城市
			String nickname=parm.get("nickname");//昵称
			String userid=parm.get("userid");//本人id
			String search="";

		  
			String SQL = "SELECT a.*,b.userid,b.caretime FROM "
					     +CommonName.TABLE_PUBLICUSER
					     +" a,care b where b.state= '1' and a.state='1' and a.id=b.careid and b.userid=' "
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
						map.put("city",rec.get("city")==null?"暂无设置":rec.get("city"));//地区
						map.put("sex", rec.getInt("sex")==1?"男":"女");//性别
						tbody.add(map);		
					}
					HashMap<String,Object> pageArgument=new HashMap<>();
					
					
					
					//找出总记录数，算出总页数
					String page= "SELECT count(*) amount  FROM "
						     +CommonName.TABLE_PUBLICUSER
						     +" a,care b where b.state= '1' and a.state='1' and a.id=b.careid and b.userid=' "
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
	
	
	
	//普通用户获取未订阅的公众号用户列表（根据相关条件搜索）
	public HashMap<String,Object> findNotCareUserList(HashMap<String, String> parm) {
		// TODO Auto-generated method stub
		HashMap<String,Object> mp=new HashMap<>();
		ArrayList<HashMap<String,Object>> tbody=new ArrayList<>();
		int curPage=parm.get("curPage")==null?1:Integer.valueOf(parm.get("curPage"));
		int pageSize=parm.get("pageSize")==null?5:Integer.valueOf(parm.get("pageSize"));
		try{
			String sex=parm.get("sex");//性别
			String city=parm.get("city");//城市
			String nickname=parm.get("nickname");//昵称
			String userid=parm.get("userid");//本人id
			String search="";

			//查询未关注的
			String SQL = "SELECT * FROM "
					     +CommonName.TABLE_PUBLICUSER
					     +"  where state= '1' and id not in (select careid from care where state =1 and userid='"
					     +userid
					     +"')";
		
					   
			if(sex!=null && !"".equals(sex)){
				if("男".equals(sex)){
					SQL+=" and sex = '1' ";
					search+=" and sex = '1' ";
				}
				else if("女".equals(sex)){
					SQL+=" and sex = '0' ";
					search+=" and sex = '0' ";
				}
				
			}
		
			if(city!=null && !"".equals(city)){
				SQL+=" and city like '%"+city+"%' ";
				search+=" and city like '%"+city+"%' ";
				
			}
			
			if(nickname!=null && !"".equals(nickname)){
				SQL+=" and nickname like '%"+nickname+"%' ";
				search+=" and nickname like '%"+nickname+"%' ";
				
			}
			
			SQL+=" order by id asc limit "+(curPage-1)*pageSize+" , "+pageSize;
			int i=0;
			List<Record> list=Db.find(SQL);		
				if(list.size()>0){
					for(Record rec:list){
						i++;
						HashMap<String,Object> map=new HashMap<>();
						map.put("nickname", rec.get("nickname"));//昵称
						map.put("id", rec.get("id"));//用户id
						map.put("num", (curPage-1)*pageSize+i);//序号
						map.put("city",rec.get("city")==null?"暂无设置":rec.get("city"));//地区
						map.put("sex", rec.getInt("sex")==1?"男":"女");//性别
						tbody.add(map);		
					}
					HashMap<String,Object> pageArgument=new HashMap<>();
					
					
					//查找一共多少条记录方便记录总页数
					String page= "SELECT count(*) amount  FROM "
							 +CommonName.TABLE_PUBLICUSER
						     +"  where state= '1' and id not in (select careid from care where state = 1 and userid='"
						     +userid
						     +"')";
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
	
	
	
	
	
	//查看公众号详情
			public HashMap<String,Object> getPubUserDetail(String id) {
				
				HashMap<String,Object> map=new HashMap<>();
				// TODO Auto-generated method stub
				try{
					String SQL = "SELECT * FROM "
								+CommonName.TABLE_PUBLICUSER
								+" WHERE id ='"+id+"' and state = 1 ";
					Record record=Db.findFirst(SQL);
					if(record!=null){
						map.put("result",true);
						map.put("photo", "/S2014150057/image/userhead/"+record.get("photo"));
						map.put("nickname", record.get("nickname"));
						map.put("sex", record.getInt("sex")==1?"男":"女");
						map.put("mail", record.get("mail")==null?"暂无设置":record.get("mail"));
						String str =record.get("phone");
						String ss = str.substring(0,str.length()-(str.substring(3)).length())+"****"
									+str.substring(7); 
						map.put("phone", ss);
						map.put("city", record.get("city")==null?"暂无设置":record.get("city"));
						
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
		
			

			//普通用户订阅公众号
					public HashMap<String,Object> carePubUser(String id,String userid) {
						
						HashMap<String,Object> map=new HashMap<>();
						// TODO Auto-generated method stub
						try{
							SimpleDateFormat smf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String nowtime=smf.format(new Date());//当前日期
							//查询是否有之前的软删除记录
							String SQL = "SELECT id FROM "
										+CommonName.TABLE_CARE
										+" WHERE careid ='"+id+"' and userid = '"+userid+"' and state = 0 ";
							Record record=Db.findFirst(SQL);
							if(record==null){//不存在，则新建一条订阅记录
								
								Record r=new Record();
								r.set("userid", userid);
								r.set("careid", id);
								r.set("caretime", nowtime);
								r.set("state", 1);
								if(Db.save(CommonName.TABLE_CARE, r)){
									map.put("result", true);
									map.put("message", "订阅成功，你可以在文章列表中看到他的文章");
								}else{
									map.put("result", false);
									map.put("message", "订阅公众号出错,请稍后重试");
								}
								
							}else{//修改之前删除过的记录，防止数据库产生脏数据
								record.set("state", 1);
								record.set("caretime", nowtime);
								if(Db.update(CommonName.TABLE_CARE, record)){
									map.put("result", true);
									map.put("message", "订阅成功，你可以在文章列表中看到他的文章");
								}else{
									map.put("result", false);
									map.put("message", "订阅公众号出错,请稍后重试");
								}
								
							}
						}catch(Exception e){
							map.put("result", false);
							map.put("message", "订阅公众号出错");
							e.printStackTrace();
						}
						
						
						return map;
					}
					
					

					//普通用户取消订阅公众号
							public HashMap<String,Object> cancelCarePubUser(String id,String userid) {
								
								HashMap<String,Object> map=new HashMap<>();
								// TODO Auto-generated method stub
								try{
									String SQL = "SELECT id FROM "
												+CommonName.TABLE_CARE
												+" WHERE careid ='"+id+"' and userid = '"+userid+"' and state = 1 ";
									Record record=Db.findFirst(SQL);
									if(record!=null){
										
										
										record.set("state", 0);
										if(Db.update(CommonName.TABLE_CARE, record)){
											map.put("result", true);
											map.put("message", "取消订阅成功，你可以稍后在公众号列表订阅该用户");
										}else{
											map.put("result", false);
											map.put("message", "取消订阅公众号出错,请稍后重试");
										}
										
									}else{
										map.put("result", false);
										map.put("message", "取消订阅公众号出错,该用户没订阅过该公众号");
										
									}
								}catch(Exception e){
									map.put("result", false);
									map.put("message", "取消订阅公众号出错");
									e.printStackTrace();
								}
								
								
								return map;
							}
							
			
			
	
	
			//普通用户修改密码接口
			public HashMap<String,Object> changePassword(String id,String newPassword,String oldPassword){
				
				HashMap<String,Object> map=new HashMap<>();
				try{
					String safeOld=MD5.MD5Encode(oldPassword);
					Integer iid=Integer.valueOf(id);
					String SQL = "SELECT id FROM "
								+CommonName.TABLE_ORDINARYUSER
								+" WHERE id =? and password= ?";
					Record record=Db.findFirst(SQL,new Object[]{iid,safeOld});
					if(record!=null)	
					{
						String safeNew=MD5.MD5Encode(newPassword);
						record.set("password", safeNew);
						if(Db.update(CommonName.TABLE_ORDINARYUSER, record)){
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
			
			
			
			//编辑普通用户信息
			public Map<String,Object>  editOrdinary(HashMap<String,Object> parm){
				HashMap<String,Object> map=new HashMap<>();
				try{
						int userid=Integer.valueOf(parm.get("userid").toString());//用户id
						//编辑
						String nickname=parm.get("nickname").toString();
						String phone=parm.get("phone").toString();
						String sex=parm.get("sex").toString();
						String mail=parm.get("mail").toString();
						String identity=parm.get("identity").toString();
						String city=parm.get("city").toString();
						String newhead=parm.get("newhead").toString();//新头像
						String oldhead=parm.get("oldhead").toString();
						//检查手机格式：
						if(!CheckFormat.isMobileNO(phone)){
							return CheckFormat.errorMap("手机号码格式不正确");
						}else{
							String SQL = "SELECT * FROM "
										+CommonName.TABLE_ORDINARYUSER
										+" WHERE id ='"+userid+"' and state= 1";
							Record record=Db.findFirst(SQL);
							if(record!=null){
								//查询手机号是否被占用
								String phonesql = "SELECT id FROM "
										+CommonName.TABLE_ORDINARYUSER
										+" WHERE phone = ? and state= 1";
								Record r=Db.findFirst(phonesql,new Object[]{phone});
								if(r!=null && r.getInt("id")!=userid){
									map.put("message", "该手机号已经被占用！请重新输入手机号");
							        map.put("result", false);
							        return map;
								}
								record.set("nickname", nickname);
								record.set("phone", phone);
								record.set("sex",sex);
								record.set("identity", identity);
								record.set("city", city);
								record.set("mail", mail);
								record.set("flag", 1);
								if(!oldhead.equals(newhead))//用户更换了头像
									{	
									  record.set("photo", newhead);
									  if(!"default.jpg".equals(oldhead))
										  CheckFormat.deleteFile(oldhead);//删除旧头像
									
									}
								if(Db.update(CommonName.TABLE_ORDINARYUSER, record)){
							         map.put("message", "修改信息成功");
							         map.put("result", true);
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
			
			
	//获取已订阅公众号昵称列表
		public HashMap<String,Object> findPubList(String userid) {
			// TODO Auto-generated method stub
			HashMap<String,Object> mp=new HashMap<>();
			ArrayList<HashMap<String,Object>> tbody=new ArrayList<>();
			try{
		

			  
				String SQL = "SELECT a.nickname,a.id FROM "
						     +CommonName.TABLE_PUBLICUSER
						     +" a,care b where b.state= '1' and a.state='1' and a.id=b.careid and b.userid=' "
						     +userid
						     +"' ";
			
				List<Record> list=Db.find(SQL);		
					if(list.size()>0){
						for(Record rec:list){
							HashMap<String,Object> map=new HashMap<>();
							map.put("nickname", rec.get("nickname"));//昵称
							map.put("num", rec.get("id"));//序号
							tbody.add(map);		
						}
				
						mp.put("result", true);
						mp.put("total", list.size());
						mp.put("tbody", tbody);
						mp.put("message", "成功");
					}else{
						
						mp.put("result", false);
						mp.put("total", 0);
						mp.put("tbody", tbody);
						mp.put("message", "无相关记录");
					}
			
			}catch(Exception e){
				mp.put("result", false);
				mp.put("total", 0);
				mp.put("tbody", "");
				mp.put("message", "查询出错");
				e.printStackTrace();
			}			
			return mp;
		}
}