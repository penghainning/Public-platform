$(function(){

	var a,b,c;
	var cur=getQueryString("cur");//当前页
	var total;//总页数
	var iscare=getQueryString("iscare");//默认查看已订阅
	if(iscare==null)
		iscare=1;
	$("#opti").val(iscare);
	
	var type=getQueryString("type");//搜索类型
	$("#choose").val(type);
	var content;
	
	//获取历史查询内容
	var url=decodeURI(location.href);
	var tmp1=url.split("?")[1]; 
	if(tmp1!=null){
		var tmp2=tmp1.split("&")[2]; 
		content=tmp2.split("=")[1];
		if (content == "undefined"){
			content=null;
		}
		else if(content !="null")
			$("#content").val(content);
	}

	//获取订阅自己的用户列表函数
	function getlist(type,content,curpage){
		
		getType();
		$.post("/S2014150057/pub/findUserList",{
			nickname:a,
			sex:b,
			city:c,
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
				tableStr = tableStr + "<table class=\"table\"><tr><th>序号</th><th style=\"width: 30%\">昵称</th><th>性别</th>"
									+ "  <th>地区</th><th >订阅时间</th><th>操作</th></tr>";
				 for(var i=0 ;i<len ; i++){
					  tableStr = tableStr + "<tr><td>"+ data.tbody[i].num +"</td>"
					  					  +"<td>"+data.tbody[i].nickname + "</td>"
					  					  +"<td>"+data.tbody[i].sex +"</td>" 
					  					  +"<td>"+data.tbody[i].city +"</td>" 
					  					  +"<td>"+data.tbody[i].caretime +"</td>" 
					  					  +"<td><div class=\"operate\">"
					  					  +"<a  class=\"see\" href=\"javascript:void(0)\" id=\"aa"
					  					  +data.tbody[i].id 
					  					  +"\"> 查看</a>"
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
	

	
	//点击搜索按钮
	$("#search").click(function(){
		type=$("#choose").val();
	    content=$("#content").val();
		getlist(type,content);
	});
	
	
	//点击查看信息
	$(".see").live('click',function(){
		var see=this.id;
		var id=see.slice(2);
		var url="../page/userdetail.html?id="+id+"&type="+type+"&content="+content+"&cur="+cur;
		window.parent.right.location.href=encodeURI(url);
	});


	
	//分页操作
	$(".up").live('click',function(){
		var ud=this.id;
		var id=ud.slice(2);
		if(id>0&&id<=total)
			getlist(type,content,id);

	});
	
	//获取url参数
	function getQueryString(name){
		var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if (r!=null) return unescape(r[2]); return null; 
	}
	
	

	//获取搜索条件
	function getType(){
		if(type=="nickname")
		{
			a=content;
			b="";
			c="";
		}
		else if(type=="sex")
		{
			a="";
			b=content;
			c="";
		}
		else if(type=="city"){
		
			a="";
			b="";
			c=content;
		}

	}

	
})





	
	
