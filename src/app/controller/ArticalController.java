package app.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import app.common.CommonName;
import app.interceptor.AuthInterceptor;
import app.service.ArticalService;
import app.service.UserService;
import app.util.CheckFormat;
import app.util.MD5;

@Before(AuthInterceptor.class)
public class ArticalController extends Controller {

	private ArticalService articalService=new ArticalService();
	
	
	
	//发布文章
	public void addarticle(){
		try{
			
			String id = getPara("id");
			String content = getPara("content");
			String title = getPara("title");
			String userid=getSession().getAttribute("userid").toString();
			HashMap<String,String> parm=new HashMap<>();
			parm.put("id", id);
			parm.put("content", content);
			parm.put("title", title);
			parm.put("userid", userid);
		
			renderJson(articalService.addarticle(parm));
		}catch(Exception e){
			e.printStackTrace();
			renderJson(CheckFormat.errorMap("系统异常"));
		}
	
		
	}
	
	//查看文章详情
	public void getArticleDetail(){
		try{
			
			String id = getPara("id");
			renderJson(articalService.getArticleDetail(id));
		}catch(Exception e){
			e.printStackTrace();
			renderJson(CheckFormat.errorMap("系统异常"));
		}
	
		
	}
	
	//查看文章详情(包含点赞人数和评论列表)
	public void getArticleDetailWithLove(){
		try{
			
			String id = getPara("id");
			renderJson(articalService.getArticleDetailWithLove(id));
		}catch(Exception e){
			e.printStackTrace();
			renderJson(CheckFormat.errorMap("系统异常"));
		}
	
		
	}
	
	

	//获取文章评论列表
	public void findArticelComentList() throws UnsupportedEncodingException{
		HashMap<String, String>mp=new HashMap<>();
		try{
			String id = getPara("id");
			String type = getPara("type");//类型，1为普通用户查看，2位公众号用户查看
			String curPage = getPara("curpage");
			String pageSize = getPara("pagesize");
			mp.put("id", id);
			mp.put("type", type);
			mp.put("curPage", curPage);
			mp.put("pageSize", pageSize);
			renderJson(articalService.findArticelComentList(mp));
		}catch(Exception e){
			e.printStackTrace();
			renderJson(mp);
		}
		
	}
	
	//获取用户自己的评论列表
		public void findOdrinaryComentList() throws UnsupportedEncodingException{
			HashMap<String, String>mp=new HashMap<>();
			try{
				String id = getPara("id");//文章id
				String userid=getSession().getAttribute("userid").toString();//评论人id
				String curPage = getPara("curpage");//当前页
				String pageSize = getPara("pagesize");//总页数
				mp.put("id", id);
				mp.put("userid", userid);
				mp.put("curPage", curPage);
				mp.put("pageSize", pageSize);
				renderJson(articalService.findOdrinaryComentList(mp));
			}catch(Exception e){
				e.printStackTrace();
				renderJson(mp);
			}
			
		}
	

	//删除文章
	public void deleteArticle(){
		try{
			
			String id = getPara("id");
			String userid=getSession().getAttribute("userid").toString();
			renderJson(articalService.deleteArticle(id,userid));
		}catch(Exception e){
			e.printStackTrace();
			renderJson(CheckFormat.errorMap("系统异常"));
		}
	
		
	}
	
	//删除文章评论
		public void deleteArticleComment(){
			try{
				
				String id = getPara("id");
				renderJson(articalService.deleteArticleComment(id));
			}catch(Exception e){
				e.printStackTrace();
				renderJson(CheckFormat.errorMap("系统异常"));
			}
		
			
		}
		
		
		//显示或者不显示文章评论
			public void displayArticleComment(){
				try{
					
					String id = getPara("id");
					renderJson(articalService.displayArticleComment(id));
				}catch(Exception e){
					e.printStackTrace();
					renderJson(CheckFormat.errorMap("系统异常"));
				}
			
				
			}
			
		//评论文章
		public void addArticleComment(){
			try{
				String userid=getSession().getAttribute("userid").toString();//评论人id
				String id = getPara("articalid");//文章id
				String content = CheckFormat.replace(getPara("content"));//评论内容
				renderJson(articalService.addArticleComment(id,userid,content));
			}catch(Exception e){
				e.printStackTrace();
				renderJson(CheckFormat.errorMap("系统异常"));
			}
				
					
		}
	
	
		//点赞或者取消点赞文章
		public void addArticleLove(){
			
			try{
				String userid=getSession().getAttribute("userid").toString();//登录用户Id
				String id = getPara("articalid");//文章id
				String type = getPara("type");//类型，0为取消，1为点赞
				renderJson(articalService.addArticleLove(id,userid,type));
			}catch(Exception e){
				e.printStackTrace();
				renderJson(CheckFormat.errorMap("系统异常"));
			}
				
			
					
		}
		

		//查询用户是否点赞某个文章
		public void findIsLove(){
			try{
				HashMap<String,Object> map=new HashMap<>();
				String userid="";
				if(getSession().getAttribute("userid")==null){
					render("/login");
					return;
				}else{
					userid=getSession().getAttribute("userid").toString();//评论人id
				}
				String id = getPara("articalid");//文章id
				//查询点赞人数
				 String loveSql="select id from "
									+CommonName.TABLE_LOVE
									+" where state=1 and article_id = '"
									+id
									+"' and user_id ='"
									+userid
									+"'";
					Record love=Db.findFirst(loveSql);
					if(love!=null){
						map.put("result", true);
						map.put("flag", 1);
						map.put("message", "已经点赞");
					}else{
						map.put("result", true);
						map.put("flag", 0);
						map.put("message", "未点赞");
					}
			  renderJson(map);
			}catch(Exception e){
				e.printStackTrace();
				renderJson(CheckFormat.errorMap("系统异常"));
			}
				
				
					
		}
	
	

	//获取自己发的文章列表（根据相关条件搜索）
	public void findArticelList() throws UnsupportedEncodingException{
		HashMap<String, String>mp=new HashMap<>();
		try{
			String userid="";
			if(getSession().getAttribute("userid")==null){
				render("/login");
			}else{
				userid=getSession().getAttribute("userid").toString();
			}
			String title = getPara("title");
			String id = getPara("id");
			String curPage = getPara("curpage");
			String pageSize = getPara("pagesize");
			mp.put("id", id);
			mp.put("userid", userid);
			mp.put("title",CheckFormat.replace(title));
			mp.put("curPage", curPage);
			mp.put("pageSize", pageSize);
			renderJson(articalService.findArticelList(mp));
		}catch(Exception e){
			e.printStackTrace();
			renderJson(mp);
		}
		
	}
	
	
	
	
	//获取已订阅公众号发的文章列表（根据相关条件搜索）
	public void findArticelListByOrdinary() throws UnsupportedEncodingException{
		HashMap<String, String>mp=new HashMap<>();
		try{
		
			String userid=getSession().getAttribute("userid").toString();
			String title = getPara("title");
			String pubid = getPara("pubid");//公众号id -1为查询全部订阅文章
			String curPage = getPara("curpage");
			String pageSize = getPara("pagesize");
			String comment = getPara("comment");//是否查询评论过的
			String love =getPara("love");//是否查询点赞过的
			mp.put("pubid", pubid);
			mp.put("userid", userid);
			mp.put("title", CheckFormat.replace(title));
			mp.put("curPage", curPage);
			mp.put("pageSize", pageSize);
			mp.put("comment", comment);
			mp.put("love", love);
			renderJson(articalService.findArticelListByOrdinary(mp));
		}catch(Exception e){
			e.printStackTrace();
			renderJson(mp);
		}
		
	}
	


}
