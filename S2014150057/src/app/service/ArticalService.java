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

public class ArticalService {




	//发布文章
	public HashMap<String,Object> addarticle(HashMap<String,String> parm){
		HashMap<String,Object> map=new HashMap<>();
		int userid=Integer.valueOf(parm.get("userid"));//作者号
		String id=parm.get("id");//文章id
		String title=CheckFormat.replace(parm.get("title"));//标题
		String content=parm.get("content");//正文
		SimpleDateFormat smf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowtime=smf.format(new Date());//当前日期
		
		try{
			//编辑
			if(content.length()>5000){
				map.put("result", false);
				map.put("message", "发布文章失败，字数超过限制,请修改");
			}else{
				if(!"".equals(id)&&id!=null){
					String SQL = "SELECT id FROM article WHERE id ='"+id+"'";
					Record record=Db.findFirst(SQL);
				
						record.set("content",content);
						record.set("title", title);
						record.set("state", 1);
						record.set("edittime",nowtime);
						if(Db.update("article",record)){
							map.put("result", true);
							map.put("message", "成功成功");
						}else{
							map.put("result", false);
							map.put("message", "发布文章失败，请重新尝试");
						}
						
					
				}else{//新增
						Record record=new Record();		
						record.set("content",content);
						record.set("title", title);
						record.set("state", 1);
						record.set("createtime",nowtime);
						record.set("authorid", userid);
						record.set("edittime", nowtime);
						if(Db.save("article", record)){
							map.put("result", true);
							map.put("message", "新增成功");
						}else{
							map.put("result", false);
							map.put("message", "发布文章失败，请重新尝试");
						}
					}
					
				
			}
			
			
		}catch(Exception e){
			map.put("result", false);
			map.put("message", "发布文章出错,请检查所填数据格式是否正确");
			e.printStackTrace();
		}			
		return map;
		
	}

	//查看文章详情
	public HashMap<String,Object> getArticleDetail(String id) {
		
		HashMap<String,Object> map=new HashMap<>();
		// TODO Auto-generated method stub
		try{
			String SQL = "SELECT * FROM article WHERE id =? and state = ? ";
			Record record=Db.findFirst(SQL,new Object[]{id,1});
			if(record!=null){
				map.put("result",true);
				map.put("title", record.get("title"));
				map.put("content", record.get("content"));
			}else{
				map.put("result", false);
				map.put("message", "查看文章详情出错,该文章不存在");
			}
		}catch(Exception e){
			map.put("result", false);
			map.put("message", "查看文章详情出错");
			e.printStackTrace();
		}
		
		
		return map;
	}
	
	
	//查看文章详情(含点赞列表)
	public HashMap<String,Object> getArticleDetailWithLove(String id) {
		
		HashMap<String,Object> map=new HashMap<>();
		// TODO Auto-generated method stub
		try{
			String SQL = "SELECT * FROM article WHERE id = ? and state = 1 ";
			Record record=Db.findFirst(SQL,new Object[]{id});
			String lovename="";
			if(record!=null){
				//查询点赞人数
			   String loveSql="select a.nickname from "
								+CommonName.TABLE_ORDINARYUSER
								+" a,love b where b.state=1 and b.article_id =? "
								+" and b.user_id = a.id";
				List<Record> lovelist=Db.find(loveSql,new Object[]{id});
				int i=0;
				if(lovelist.size()>0){
					for(Record rec:lovelist){
						i++;
						lovename=lovename+" . "+rec.get("nickname")+"<br>";
					}
				}
			    if(!"".equals(lovename))
			    	map.put("lovename", lovename);
			    else
			    	map.put("lovename", "暂无人点赞");
			    map.put("lovecount", i);
				map.put("result",true);
				map.put("title", record.get("title"));
				map.put("content", record.get("content"));
			}else{
				map.put("result", false);
				map.put("message", "查看文章详情出错,该文章不存在");
			}
		}catch(Exception e){
			map.put("result", false);
			map.put("message", "查看文章详情出错");
			e.printStackTrace();
		}
		
		
		return map;
	}
	
	//删除文章
    public HashMap<String,Object> deleteArticle(String id,String userid) {
		
		HashMap<String,Object> map=new HashMap<>();
		// TODO Auto-generated method stub
		try{
			SimpleDateFormat smf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowtime=smf.format(new Date());//当前日期
			String SQL = "SELECT * FROM article WHERE id = ? and state = 1 ";
			Record record=Db.findFirst(SQL,new Object[]{id});
			if(record!=null){
				System.out.println(userid+"#"+record.get("authorid"));
				if(!userid.equals(record.get("authorid").toString())){
					map.put("result", false);
					map.put("message", "删除文章出错,作者和登录用户不同");
				}else{
					record.set("state", 0);
					record.set("edittime", nowtime);
					Db.update("article",record);
					map.put("result", true);
					map.put("message", "删除成功");
				}
			}else{
				map.put("result", false);
				map.put("message", "删除文章出错,该文章不存在");
			}
		}catch(Exception e){
			map.put("result", false);
			map.put("message", "删除文章出错");
			e.printStackTrace();
		}
		
		
		return map;
	}
    
    
    

	//删除评论
    public HashMap<String,Object> deleteArticleComment(String id) {
		
		HashMap<String,Object> map=new HashMap<>();
		// TODO Auto-generated method stub
		try{
			String SQL = "SELECT * FROM comment WHERE id = ? and state > 0 ";
			Record record=Db.findFirst(SQL,new Object[]{id});
			if(record!=null){
				
					record.set("state", 0);
					Db.update("comment",record);
					map.put("result", true);
					map.put("message", "删除成功");
				
			}else{
				map.put("result", false);
				map.put("message", "删除评论出错,该评论不存在");
			}
		}catch(Exception e){
			map.put("result", false);
			map.put("message", "删除评论出错");
			e.printStackTrace();
		}
		
		
		return map;
	}
    

	//显示评论
    public HashMap<String,Object> displayArticleComment(String id) {
		
		HashMap<String,Object> map=new HashMap<>();
		// TODO Auto-generated method stub
		try{
			String SQL = "SELECT * FROM comment WHERE id = ? and state >0";
			Record record=Db.findFirst(SQL,new Object[]{id});
			if(record!=null){
				
				if(record.getInt("state")==2){
					record.set("state", 1);
					Db.update("comment",record);
					map.put("result", true);
					map.put("message", "操作成功，所有用户都可看到这条评论");
				}else{
					record.set("state", 2);
					Db.update("comment",record);
					map.put("result", true);
					map.put("message", "操作成功，其他用户不可看到这条评论");
				}
				
			}else{
				map.put("result", false);
				map.put("message", "操作出错,该评论不存在");
			}
		}catch(Exception e){
			map.put("result", false);
			map.put("message", "操作出错");
			e.printStackTrace();
		}
		
		
		return map;
	}
    

	//评论文章
    public HashMap<String,Object> addArticleComment(String articalid,String userid,String content) {
		
		HashMap<String,Object> map=new HashMap<>();
		// TODO Auto-generated method stub
		try{
				Record record=new Record();
				SimpleDateFormat smf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String nowtime=smf.format(new Date());//当前日期
				record.set("user_id", userid);//评论人Id
				record.set("article_id", articalid);//被评论的文章id
				record.set("time", nowtime);//评论时间
				record.set("content", content);//评论正文
				record.set("state", 2);
				
				if(Db.save(CommonName.TABLE_COMMENT, record)){
					map.put("result", true);
					map.put("message", "评论成功，您的评论需要作者审核后才能被大家看到");
				}else{
					map.put("result", false);
					map.put("message", "系统错误，请重新尝试");
				}
				
			
		}catch(Exception e){
			map.put("result", false);
			map.put("message", "操作出错");
			e.printStackTrace();
		}
		
		
		return map;
	}
    
    
  //点赞或者取消点赞文章
    public HashMap<String,Object> addArticleLove(String articalid,String userid,String type) {
		
		HashMap<String,Object> map=new HashMap<>();
		// TODO Auto-generated method stub
		try{
			    //取消点赞
				if(Integer.valueOf(type)==0){
					//找到点赞记录并设置state为0
					String lovesql="select * from love where state = 1 and article_id=?"
			    			  +" and user_id = ?";
				    Record love=Db.findFirst(lovesql,new Object[]{articalid,userid});
				    if(love==null){
				    	map.put("result", false);
						map.put("message", "操作错误，无记录");
				    }else{
							love.set("state", 0);			
						if(Db.update(CommonName.TABLE_LOVE, love)){
							map.put("result", true);
							map.put("message", "取消点赞文章成功！你可以再次点赞！");
						}else{
							map.put("result", false);
							map.put("message", "系统错误，请重新尝试");
						}
				    }
				}else{//点赞
					//查询是否有之前软删除记录
				   String lovesql="select id from love where state = 0 and article_id=?"
			    			  +" and user_id = ?";  			  
				    Record love=Db.findFirst(lovesql,new Object[]{articalid,userid});
				    if(love!=null){//存在直接更新之前的软删除记录
				    	love.set("state", 1);
				    	SimpleDateFormat smf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String nowtime=smf.format(new Date());//当前日期
						love.set("time", nowtime);//点赞时间		
						if(Db.update(CommonName.TABLE_LOVE, love)){
							map.put("result", true);
							map.put("message", "点赞文章成功！");
						}else{
							map.put("result", false);
							map.put("message", "系统错误，请重新尝试");
						}
				    }else{
				    	//新建记录
				    	Record record=new Record();
						SimpleDateFormat smf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String nowtime=smf.format(new Date());//当前日期
						record.set("user_id", userid);//评论人Id
						record.set("article_id", articalid);//被评论的文章id
						record.set("time", nowtime);//点赞时间
						record.set("state", 1);
						
						if(Db.save(CommonName.TABLE_LOVE, record)){
							map.put("result", true);
							map.put("message", "点赞文章成功！");
						}else{
							map.put("result", false);
							map.put("message", "系统错误，请重新尝试");
						}
				    }
					
				
				}
			  
			
		}catch(Exception e){
			map.put("result", false);
			map.put("message", "操作出错");
			e.printStackTrace();
		}
		
		
		return map;
	}
    
    

    //查找文章列表
	public HashMap<String,Object> findArticelList(HashMap<String, String> parm) {
		// TODO Auto-generated method stub
		HashMap<String,Object> mp=new HashMap<>();
		ArrayList<HashMap<String,Object>> tbody=new ArrayList<>();
		int curPage=parm.get("curPage")==null||"".equals(parm.get("curPage"))?1:Integer.valueOf(parm.get("curPage"));
		int pageSize=parm.get("pageSize")==null?8:Integer.valueOf(parm.get("pageSize"));
		int id=parm.get("id")==null||"".equals(parm.get("id"))?-1:Integer.valueOf(parm.get("id"));//文章号
		try{
			String title=parm.get("title");//标题
			String userid=parm.get("userid");//作者id
			String search="";

		  
			String SQL = "SELECT * FROM article where state= '1' and authorid =' "+userid+"'";;
			if(title!=null && !"".equals(title)){
				SQL+=" and title like '%"+title+"%' ";
				search+=" and title like '%"+title+"%' ";
				
			}
		
			if(id!=-1){
				SQL+=" and id = '"+id+"' ";
				search+=" and id = '"+id+"' ";
				
			}
			
			
			SQL+=" order by edittime desc limit "+(curPage-1)*pageSize+" , "+pageSize;
			int i=0;
			List<Record> list=Db.find(SQL);		
				if(list.size()>0){
					for(Record rec:list){
						i++;
						HashMap<String,Object> map=new HashMap<>();
						map.put("title", rec.get("title"));//标题
						map.put("id", rec.get("id"));//文章号
						map.put("createtime", rec.get("createtime"));//创建时间
						map.put("edittime", rec.get("edittime"));//修改时间
						map.put("num", (curPage-1)*pageSize+i);//序号
						String loveSql="select count(*) amount from love where state = 1 and article_id= '"+rec.get("id")+"'";
						Record r=Db.findFirst(loveSql);
						map.put("love", r.get("amount"));//点赞数
						tbody.add(map);		
					}
					HashMap<String,Object> pageArgument=new HashMap<>();
					
					
					//算出总页数
					String page="select count(*) amount from article where authorid= '"+userid+"' and state= '1' ";
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
					mp.put("title", title);
					mp.put("message", "成功");
				}else{
					
					HashMap<String,Object> pageArgument=new HashMap<>();
					mp.put("result", false);
					mp.put("total", 0);
					pageArgument.put("curpage", curPage);
					pageArgument.put("pagecount", pageSize);
					mp.put("pageArgument", pageArgument);
					mp.put("tbody", tbody);
					mp.put("title", title);
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
	
	

    //查找普通用户关注的文章列表
	public HashMap<String,Object> findArticelListByOrdinary(HashMap<String, String> parm) {
		// TODO Auto-generated method stub
		HashMap<String,Object> mp=new HashMap<>();
		ArrayList<HashMap<String,Object>> tbody=new ArrayList<>();
		int curPage=parm.get("curPage")==null||"".equals(parm.get("curPage"))?1:Integer.valueOf(parm.get("curPage"));
		int pageSize=parm.get("pageSize")==null?8:Integer.valueOf(parm.get("pageSize"));
		int pubid=parm.get("pubid")==null||"".equals(parm.get("pubid"))?-1:Integer.valueOf(parm.get("pubid"));//公众号id
		try{
			String title=parm.get("title");//标题
			String userid=parm.get("userid");//用户id
			String comment=parm.get("comment");//用户评论过得
			String love=parm.get("love");//是否点赞过得
			String search="";
			
			String SQL="";
			//查询所有
			if(pubid==-1){
				
				SQL="select a.*,b.nickname from "
					+CommonName.TABLE_ARTICLE 
					+" a, publicUser b where a.state= 1 and  b.id=a.authorid and a.authorid in ( SELECT careid from care where userid= '"
					+userid
					+"' and state = 1 ) " ;
				
				//如果搜索条件有标题
				if(title!=null && !"".equals(title)){
					SQL+=" and title like '%"+title+"%' ";
					search+=" and title like '%"+title+"%' ";
				}
				
				//如果搜索条件有已评论过	
				if("1".equals(comment)){
					SQL+=" and a.id in ( select article_id from comment  where state >0 and user_id= '"
							+userid
							+"') " ;
					search+=" and id in ( select article_id from comment  where  state >0 and user_id= '"
							+userid
							+"') " ;
				}
				
				//如果搜索条件有已点赞过
				if("1".equals(love)){
					SQL+=" and a.id in ( select article_id from love  where  state =1 and user_id= '"
					+userid
					+"') " ;
					search+=" and id in ( select article_id from love  where  state =1 and user_id= '"
							+userid
							+"') " ;
				}
					
					
				
			
			}else{//
				SQL="select a.*,b.nickname from "
						+CommonName.TABLE_ARTICLE 
						+" a,publicUser b where a.state= 1 and b.id=a.authorid and authorid = '"
						+pubid
						+"'" ;
				
				if(title!=null && !"".equals(title)){
					SQL+=" and title like '%"+title+"%' ";
					search+=" and title like '%"+title+"%' ";
					
				}
				
				
				//如果搜索条件有已评论过	
				if("1".equals(comment)){
					SQL+=" and a.id in ( select article_id from comment  where state >0 and user_id= '"
							+userid
							+"') " ;
					search+=" and id in ( select article_id from comment  where  state >0 and user_id= '"
							+userid
							+"') " ;
				}
				
				//如果搜索条件有已点赞过
				if("1".equals(love)){
					SQL+=" and a.id in ( select article_id from love  where  state =1 and user_id= '"
					+userid
					+"') " ;
					search+=" and id in ( select article_id from love  where  state =1 and user_id= '"
							+userid
							+"') " ;
				}
					
					
				
			}

		 	
			
			SQL+=" order by a.edittime desc limit "+(curPage-1)*pageSize+" , "+pageSize;
			
			int i=0;
			List<Record> list=Db.find(SQL);		
				if(list.size()>0){
					for(Record rec:list){
						i++;
						HashMap<String,Object> map=new HashMap<>();
						map.put("title", rec.get("title"));//标题
						map.put("id", rec.get("id"));//文章号
						map.put("createtime", rec.get("createtime"));//创建时间
						map.put("edittime", rec.get("edittime"));//上次修改时间
						map.put("num", (curPage-1)*pageSize+i);//序号
						map.put("nickname", rec.get("nickname"));//作者名
						String loveSql="select count(*) amount from love where article_id= '"+rec.get("id")+"' and state = 1 ";
						Record r=Db.findFirst(loveSql);
						map.put("love", r.get("amount"));//点赞数
						tbody.add(map);		
					}
					HashMap<String,Object> pageArgument=new HashMap<>();
					//根据不同条件算出总页数
					String page="";
					if(pubid==-1){
						 page="select count(*) amount from article  where state = '1' and authorid in ( SELECT careid from care where userid= '"
									+userid
									+" ' and state = 1 ) " ;
					}else{
						 page="select count(*) amount from article  where state = '1' and authorid = '"
								+userid
								+"'" ;
					}
					
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
	
	
	
	

	 //查找评论列表
		public HashMap<String,Object> findArticelComentList(HashMap<String, String> parm) {
			// TODO Auto-generated method stub
			HashMap<String,Object> mp=new HashMap<>();
			ArrayList<HashMap<String,Object>> tbody=new ArrayList<>();
			int type=Integer.valueOf(parm.get("type"));
			int curPage=parm.get("curPage")==null?1:Integer.valueOf(parm.get("curPage"));
			int pageSize=parm.get("pageSize")==null?8:Integer.valueOf(parm.get("pageSize"));
			int id=parm.get("id")==null||"".equals(parm.get("id"))?-1:Integer.valueOf(parm.get("id"));//文章号
			try{
			
			  String commentSql="";
			  if(type==1){//普通用户只能看到显示的
				   commentSql="select a.nickname, b.* from "
							+CommonName.TABLE_ORDINARYUSER
							+" a,comment b where b.article_id = '"
							+id
							+"' and b.user_id = a.id and b.state = 1"
				 			+" order by b.state asc limit "+(curPage-1)*pageSize+" , "+pageSize;
			  }else{
				  commentSql="select a.nickname, b.* from "
							+CommonName.TABLE_ORDINARYUSER
							+" a,comment b where b.article_id = '"
							+id
							+"' and b.user_id = a.id and b.state>0"
				 			+" order by b.state asc limit "+(curPage-1)*pageSize+" , "+pageSize;
			  }
				
					List<Record> commentlist=Db.find(commentSql);
				
				
		
				int i=0;
					if(commentlist.size()>0){
						for(Record rec:commentlist){
							i++;
							HashMap<String,Object> map=new HashMap<>();
							map.put("nickname", rec.get("nickname"));//昵称
						    map.put("num", (curPage-1)*pageSize+i);//序号
						    map.put("content", rec.get("content"));//内容
						    map.put("time", rec.get("time"));//评论时间
						    map.put("state", rec.get("state"));//状态
						    map.put("id", rec.get("id"));//id
							tbody.add(map);		
						}
						HashMap<String,Object> pageArgument=new HashMap<>();
						
						String page="select count(*) amount from "
									 +CommonName.TABLE_COMMENT
									 +" where article_id= '"+id+"' and state > 0";
						Record r=Db.findFirst(page);
						long total=r.get("amount");
						int pagecount =(int) total / pageSize;
								if (total % pageSize != 0)
								{
								pagecount ++;
								}
						mp.put("result", true);
						mp.put("total", commentlist.size());
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
		
		
		
		
		
		
		
		 //查找评论列表
		public HashMap<String,Object> findOdrinaryComentList(HashMap<String, String> parm) {
			// TODO Auto-generated method stub
			HashMap<String,Object> mp=new HashMap<>();
			ArrayList<HashMap<String,Object>> tbody=new ArrayList<>();
			int userid=Integer.valueOf(parm.get("userid"));
			int curPage=parm.get("curPage")==null?1:Integer.valueOf(parm.get("curPage"));
			int pageSize=parm.get("pageSize")==null?8:Integer.valueOf(parm.get("pageSize"));
			int id=parm.get("id")==null||"".equals(parm.get("id"))?-1:Integer.valueOf(parm.get("id"));//文章号
			try{
			
					String  commentSql="select a.nickname, b.* from "
							+CommonName.TABLE_ORDINARYUSER
							+" a,comment b where b.article_id = '"
							+id
							+"' and b.user_id = '"
							+userid
							+" ' and a.id=b.user_id and b.state >0"
				 			+" order by b.time desc limit "+(curPage-1)*pageSize+" , "+pageSize;
					List<Record> commentlist=Db.find(commentSql);
				
				
		
				int i=0;
					if(commentlist.size()>0){
						for(Record rec:commentlist){
							i++;
							HashMap<String,Object> map=new HashMap<>();
							map.put("nickname", rec.get("nickname"));//昵称
						    map.put("num", (curPage-1)*pageSize+i);//序号
						    map.put("content", rec.get("content"));//内容
						    map.put("time", rec.get("time"));//评论时间
						    map.put("state", rec.get("state"));//状态
						    map.put("id", rec.get("id"));//id
							tbody.add(map);		
						}
						HashMap<String,Object> pageArgument=new HashMap<>();
						
						String page="select count(*) amount from "
									 +CommonName.TABLE_COMMENT
									 +" where article_id= '"+id+"' and user_id='"+userid+"' and state > 0";
						Record r=Db.findFirst(page);
						long total=r.get("amount");
						int pagecount =(int) total / pageSize;
								if (total % pageSize != 0)
								{
								pagecount ++;
								}
						mp.put("result", true);
						mp.put("total", commentlist.size());
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
}