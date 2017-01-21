$(function(){
	

	 

		
		var url=decodeURI(location.href);
		var tmp1=url.split("?")[1]; 
		var tmp2=tmp1.split("&")[3]; 
		var oldcontent=tmp2.split("=")[1];
		var oldtype=getQueryString("type");
		var oldcur=getQueryString("cur");
		var pubid=getQueryString("pubid");//得到上个页面的公众号Id
		var id=getQueryString("id");//得到上个页面传过来的文章id


		var lovetype=1;//默认为没有点赞
		var cur;//评论列表当前页
		var total;//评论列表总页数
	
		var mycur;//我的评论当前页
		var mytotal;//我的评论总页数

		
		//获取评论列表
		function getComentList(type,content,curpage){
			
			$.post("/S2014150057/artical/findArticelComentList",{
				id:id,
				type:1,
				curpage:curpage,
				pagesize:5
				},function(data){
					var tableStr="";
					var len = data.tbody.length;
					if(len==0){
						tableStr="暂无评论"
					}else{
						tableStr = tableStr + "<table class=\"table\"><tr height=\"50px\";><th>序号</th><th style=\"width: 30%\">昵称</th><th width=\"30%;\">内容</th>"
						+ "  <th >评论时间</th></tr>";
						for(var i=0 ;i<len ; i++){
							tableStr = tableStr + "<tr height=\"80px\";><td>"+ data.tbody[i].num +"</td>"
		  					  +"<td>"+data.tbody[i].nickname + "</td>"
		  					  +"<td>"+data.tbody[i].content +"</td>" 
		  					  +"<td>"+data.tbody[i].time +"</td></tr>";
								
		  					
						}
						tableStr=tableStr+"<tr><td colspan='4'><font color='red'>以上评论由公众号筛选后显示</font></td></tr>"
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
		
		
		
		//获取评论列表
		function getMyComentList(type,content,curpage){
			
			$.post("/S2014150057/artical/findOdrinaryComentList",{
				id:id,
				curpage:curpage,
				pagesize:5
				},function(data){
					var tableStr="";
					var len = data.tbody.length;
					if(len==0){
						tableStr="暂无评论"
					}else{
						tableStr = tableStr + "<table class=\"table\"><tr height=\"50px\";><th>序号</th><th style=\"width: 30%\">昵称</th><th width=\"30%;\">内容</th>"
						+ "  <th >评论时间</th><th>状态</th></tr>";
						for(var i=0 ;i<len ; i++){
							tableStr = tableStr + "<tr height=\"80px\";><td>"+ data.tbody[i].num +"</td>"
		  					  +"<td>"+data.tbody[i].nickname + "</td>"
		  					  +"<td>"+data.tbody[i].content +"</td>" 
		  					  +"<td>"+data.tbody[i].time +"</td>";
							  if(data.tbody[i].state==2)
								  tableStr = tableStr +"<td><font color='red'>未审核</font></td></tr>";
							  else
								  tableStr = tableStr +"<td><font color='green'>已通过</font></td></tr>";
		  					
						}
						mycur=data.pageArgument.curpage;
						mytotal=data.pageArgument.totalpage;
						if(mycur==1)
							var up=0;
						else 
							var up=mycur-1;
						if(mycur==mytotal)
							var down=mycur;
						else 
							var down=mycur+1;
						tableStr = tableStr+"<tr height=\"70px\"><td colspan='8'"
	 					+"<div class='pagelist'><a class=\"up2\" href=\"javascript:void(0)\" id=\"mup"
		  					  +up +"\">上一页</a><a  class=\"up2\" href=\"javascript:void(0)\" id=\"mdo"
		  					  +down +"\">下一页</a></div></td></tr></table>";
						}
				
					 $("#mycomment").html(tableStr); 
	      
	       
					
				     
				},'json');
		}
		getMyComentList();
		
		//获取文章详细信息
		function getArticleDetail(){
			$.post("/S2014150057/artical/getArticleDetailWithLove",{id:id},function(data){
				if(data.code=="-1"){
					alert(data.message);
					window.parent.location.href="/S2014150057/login";
					}
					
				$("#title").text(strtoHtml(data.title));
				$("#divIntro").html(data.content);
				$("#lovename").html(data.lovename);
				$("#lovecount").html(data.lovecount);
			},"json");
			
		}
		getArticleDetail();
		
		
		
		//查询是否点赞
		$.post("/S2014150057/artical/findIsLove",{articalid:id},function(data){
			if(data.result){
				if(data.flag==1){
					$("#love").val("取消点赞");
					lovetype=0;
				}
				
			}
		},"json");
		
		//分页操作
		$(".up").live('click',function(){
			var ud=this.id;
			var id=ud.slice(2);
			if(id>0&&id<=total)
				getComentList(null,null,id);

		});
		
		
		//我的评论分页操作
		$(".up2").live('click',function(){
			var ud=this.id;
			var id=ud.slice(3);
			if(id>0&&id<=mytotal)
				getMyComentList(null,null,id);

		});
		
		
		//提交评论
		$("#submit").click(function(){
			var content=$("#txt").val();
			if($.trim(content)==""){
				alert("评论内容不能为空！请输入");
				$("#txt").focus();
				return;
			}
			$.post("/S2014150057/artical/addArticleComment",{
				articalid:id,
				content:content
			},function(data){
				alert(data.message);
				if(data.result){
					getComentList(null,null,cur);
					getMyComentList(null,null,mycur);
					$("#txt").val('');
					$("#pinglun").css("display","none");
					$("#count").text("200");
				}
			},'json');
			
			
		});
		
		
		//点赞
		$("#love").click(function(){

			$.post("/S2014150057/artical/addArticleLove",{
				articalid:id,
				type:lovetype
			},function(data){
				alert(data.message);
				if(data.result){
					getArticleDetail();
					if(lovetype==0){//刚刚的操作为取消点赞
						$("#love").val("点赞");
						lovetype=1;
					}else{
						$("#love").val("取消点赞");
						lovetype=0;
					}
				}
			},'json');
			
			
		});
		
	
		
		
		
		
		
       //返回
		$("#back").click(function(){
			
			var url="../page/pt_artical.html?type="+oldtype+"&cur="+oldcur+"&content="+oldcontent+"&pubid="+pubid;
			window.parent.right.location.href=encodeURI(url);
		});
		
		
		//点击评论返回
		$("#pl").click(function(){
			if(	$("#pinglun").css("display")=="block")
				$("#pinglun").css("display","none")
			else
				$("#pinglun").css("display","block")
		
		});
		
		
		
		
		//获取url参数
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





	
	
