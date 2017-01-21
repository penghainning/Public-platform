$(function(){


	var cur=getQueryString("cur");//当前页
	var total;//总页数
	var type=getQueryString("type");//搜索类型
	var content;
	
	var url=decodeURI(location.href);
	var tmp1=url.split("?")[1]; 
	if(tmp1!=null){
		var tmp2=tmp1.split("&")[2]; 
		content=tmp2.split("=")[1];
		if (content == "undefined" || content == "null"){ 
			content=null;
			}
		else if(type =="title")
			$("#content").val(content);
		
 
	}
	

	//获取文章列表函数
	function getlist(type,content,curpage){
		
		$.post("/S2014150057/artical/findArticelList",{
			title:content,
			curpage:curpage,
			pagesize:5
			},function(data){
				
				///当session失效的时候，进行重新登录
				if(data.code=="-1"){
					alert(data.message);
					window.parent.location.href="/S2014150057/login";
				}
					
				var tableStr="";
				var len = data.tbody.length;
				tableStr = tableStr + "<table class=\"table\"><tr><th>序号</th><th style=\"width: 30%\">标题</th><th width=\"70px;\">点赞数量</th>"
									+ "  <th >创建时间</th> <th >编辑时间</th><th>操作</th></tr>";
				 for(var i=0 ;i<len ; i++){
					  tableStr = tableStr + "<tr><td>"+ data.tbody[i].num +"</td>"
					  					  +"<td>"+data.tbody[i].title + "</td>"
					  					  +"<td>"+data.tbody[i].love +"</td>" 
					  					  +"<td>"+data.tbody[i].createtime +"</td>" 
					  					 +"<td>"+data.tbody[i].edittime +"</td>" 
					  					  +"<td><div class=\"operate\">"
					  					  +"<a  class=\"see\" href=\"javascript:void(0)\" id=\"aa"
					  					  +data.tbody[i].id 
					  					  +"\"> 查看</a>"
					  					  +"<a  class=\"edit\" href=\"javascript:void(0)\" id=\"bb"
					  					  +data.tbody[i].id 
					  					  +"\"> 编辑</a>"
					  					  +"<a  class=\"del\" href=\"javascript:void(0)\" id=\"cc"
					  					  +data.tbody[i].id 
					  					  +"\"> 删除</a>"
					  					  +"</div></td></tr>";
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
				 $("#list").html(tableStr); 
      
       
				
			     
			},'json');
	}
	getlist(type,content,cur);
	

	//搜索按钮函数
	$("#search").click(function(){
		 type=$("#choose").val();
		 content=$("#content").val();
		getlist(type,content);
	});
	
	
	//查看文章函数
	$(".see").live('click',function(){
		var see=this.id;
		var id=see.slice(2);
		var url="../page/articaldetail.jsp?id="+id+"&type="+type+"&cur="+cur+"&content="+content;
		window.parent.right.location.href=encodeURI(url);
	});

	//编辑文章函数
	$(".edit").live('click',function(){
		var edit=this.id;
		var id=edit.slice(2);
		window.parent.right.location.href="../page/addartical.jsp?id="+id;
	});
	
	
	//删除文章函数
	$(".del").live('click',function(){
		var del=this.id;
		var id=del.slice(2);
		if(confirm("您确定要删除吗?")){

			$.post("/S2014150057/artical/deleteArticle",{
				id:id
		},function(data){
			alert(data.message);
			if(data.result){
				getlist(type,content,cur);
			}
		},'json');
		
		}
	});

	//上一页、下一页操作
	$(".up").live('click',function(){
		var ud=this.id;
		var id=ud.slice(2);
		if(id>0&&id<=total)
			getlist(type,content,id);

	});
	

	

	//获取url中的参数，根据正则表达式
	function getQueryString(name){
		var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if (r!=null) return unescape(r[2]); return null; 
	}
	


	
})





	
	
