$(function(){
	

	 
	
		
		
		var cur;//评论当前页
		var total;//评论总页数
		
		//获取上个页面的历史数据
		var url=decodeURI(location.href);
		var tmp1=url.split("?")[1]; 
		var tmp2=tmp1.split("&")[3]; 
		var oldcontent=tmp2.split("=")[1];
		var oldtype=getQueryString("type");
		var oldcur=getQueryString("cur");
		var id=getQueryString("id");//得到上个页面传过来的文章id
		
		//返回按钮事件
		$("#back").click(function(){
			var url="../page/artical.html?type="+oldtype+"&cur="+oldcur+"&content="+oldcontent;
			window.parent.right.location.href=encodeURI(url);
		});

		
		//获取评论列表
		function getComentList(type,content,curpage){
			
			$.post("/S2014150057/artical/findArticelComentList?type=2&"+type+"="+content,{
				id:id,
				curpage:curpage,
				pagesize:8
				},function(data){
					
					///当session失效的时候，进行重新登录
					if(data.code=="-1"){
						alert(data.message);
						window.parent.location.href="/S2014150057/login";
					}
					
					var tableStr="";
					var len = data.tbody.length;
					if(len==0){
						tableStr="暂无评论"
					}else{
						tableStr = tableStr + "<table class=\"table\"><tr height=\"50px\";><th>序号</th><th style=\"width: 30%\">昵称</th><th width=\"30%;\">内容</th>"
						+ "  <th >评论时间</th><th>操作</th></tr>";
						for(var i=0 ;i<len ; i++){
							tableStr = tableStr + "<tr height=\"80px\";><td>"+ data.tbody[i].num +"</td>"
		  					  +"<td>"+data.tbody[i].nickname + "</td>"
		  					  +"<td>"+data.tbody[i].content +"</td>" 
		  					  +"<td>"+data.tbody[i].time +"</td>" 
		  					  +"<td><div class=\"operate\">";
							if(data.tbody[i].state==2){
								tableStr = tableStr  +"<a  class=\"allow\" href=\"javascript:void(0)\" id=\"aa"
			  					  +data.tbody[i].id 
			  					  +"\"> 显示</a>"
			  					  +"<a  class=\"delete\" href=\"javascript:void(0)\" id=\"cc"
			  					  +data.tbody[i].id 
			  					  +"\"> 删除</a>"
			  					  +"</div></td></tr>";
							}
							else{
									tableStr = tableStr+"<a  class=\"allow\" href=\"javascript:void(0)\" id=\"aa"
				  					   +data.tbody[i].id 
				  					   +"\"> 不显示</a>"
									   +"<a  class=\"delete\" href=\"javascript:void(0)\" id=\"cc"
					  				   +data.tbody[i].id 
					  				   +"\"> 删除</a>"
					  				   +"</div></td></tr>";
								}
		  					
						}
						cur=data.pageArgument.curpage;
						total=data.pageArgument.totalpage;
						if(cur==1)
							var up=0;
						else 
							var up=cur-1;
						if(cur==total)
							var down=cur;
						else 
							var down=cur+1;
						tableStr = tableStr+"<tr height=\"70px\"><td colspan='8'"
	 					+"<div class='pagelist'><a class=\"up\" href=\"javascript:void(0)\" id=\"up"
		  					  +up +"\">上一页</a><a  class=\"up\" href=\"javascript:void(0)\" id=\"do"
		  					  +down +"\">下一页</a></div></td></tr></table>";
						}
				
					 $("#comment").html(tableStr); 
	      
	       
					
				     
				},'json');
		}
		getComentList();
		
		
		
		//请求接口填充文章信息
		$.post("/S2014150057/artical/getArticleDetailWithLove",{id:id},function(data){
			$("#title").text(strtoHtml(data.title));
			$("#divIntro").html(data.content);
			$("#lovename").html(data.lovename);
			$("#lovecount").html(data.lovecount);
		},"json");
		
		
		
		//评论列表的上一页和下一页
		$(".up").live('click',function(){
			var ud=this.id;
			var id=ud.slice(2);
			if(id>0&&id<=total)
				getComentList(null,null,id);

		});
		
		
		
		//删除评论
		$(".delete").live('click',function(){
			var del=this.id;
			var id=del.slice(2);
			if(confirm("您确定要删除吗?")){

				$.post("/S2014150057/artical/deleteArticleComment",{
					id:id
			},function(data){
				alert(data.message);
				if(data.result){
					getComentList(null,null,cur);
				}
			},'json');
			
			}
		});
		
		
		//允许显示评论
		$(".allow").live('click',function(){
			var allow=this.id;
			var id=allow.slice(2);

				$.post("/S2014150057/artical/displayArticleComment",{
					id:id
			},function(data){
				alert(data.message);
				if(data.result){
					getComentList(null,null,cur);
				}
			},'json');
			
			
		});

		
		
		
		//获取url中的参数，根据正则表达式
		function getQueryString(name){
			var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
			var r = window.location.search.substr(1).match(reg);
			if (r!=null) return unescape(r[2]); return null; 
		}
		
		  //将html字符转成普通字符	
		  function strtoHtml(str) {
			  if(str==null)
				  return "";
			 var arrEntities={'lt':'<','gt':'>','nbsp':' ','amp':'&','quot':'"'};
			 return str.replace(/&(lt|gt|nbsp|amp|quot);/ig,function(all,t){return arrEntities[t];});
			}
			
		
	
})





	
	
