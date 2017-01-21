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
   
	//获取已关注用户列表
	function getCarelist(type,content,curpage){
		getType();
		$.post("/S2014150057/ordinary/findCareUserList",{
			nickname:a,
			sex:b,
			city:c,
			curpage:curpage,
			pagesize:5
			},function(data){
				
				//session失效处理
				if(data.code=="-1"){
					alert(data.message);
					window.parent.location.href="/S2014150057/login";
				}
				var tableStr="";
				var len = data.tbody.length;
				tableStr = tableStr + "<table class=\"table\"><tr><th>序号</th><th style=\"width: 30%\">昵称</th><th width='10%'>性别</th>"
				+ "  <th width='10%'>地区</th><th >订阅时间</th><th>操作</th></tr>";
				 for(var i=0 ;i<len ; i++){
					  tableStr = tableStr + "<tr><td>"+ data.tbody[i].num +"</td>"
					  					  +"<td>"+data.tbody[i].nickname + "</td>"
					  					  +"<td>"+data.tbody[i].sex +"</td>" 
					  					  +"<td>"+data.tbody[i].city +"</td>" 
					  					  +"<td>"+data.tbody[i].caretime +"</td>" 
					  					  +"<td><div class=\"operate\">"
					  					  +"<a  class=\"a0\" href=\"javascript:void(0)\" id=\"aa"
					  					  +data.tbody[i].id 
					  					  +"\"> 取消订阅</a>"
					  					  +"<a  class=\"a1\" href=\"javascript:void(0)\" id=\"bb"
					  					  +data.tbody[i].id 
					  					  +"\"> 订阅Ta</a>"
					  					  +"<a  class=\"a2\" href=\"javascript:void(0)\" id=\"cc"
					  					  +data.tbody[i].id 
					  					  +"\"> 查看信息</a>"
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
	if(iscare==1){
		getCarelist(type,content,cur);
	}else{
		getNoCarelist(type,content,cur);
	}

	
	

	//获取未关注用户列表
	function getNoCarelist(type,content,curpage){
		getType();
		$.post("/S2014150057/ordinary/findNotCareUserList",{
			nickname:a,
			sex:b,
			city:c,
			curpage:curpage,
			pagesize:5
			},function(data){
				
				//session失效处理
				if(data.code=="-1"){
					alert(data.message);
					window.parent.location.href="/S2014150057/login";
					}
					
				var tableStr="";
				var len = data.tbody.length;
				tableStr = tableStr + "<table class=\"table\"><tr><th>序号</th><th style=\"width: 30%\">昵称</th><th width='10%'>性别</th>"
				+ "  <th width='10%'>地区</th><th>操作</th></tr>";
				 for(var i=0 ;i<len ; i++){
					  tableStr = tableStr + "<tr><td>"+ data.tbody[i].num +"</td>"
					  					  +"<td>"+data.tbody[i].nickname + "</td>"
					  					  +"<td>"+data.tbody[i].sex +"</td>" 
					  					  +"<td>"+data.tbody[i].city +"</td>" 
					  					  +"<td><div class=\"operate\">"
					  					  +"<a  class=\"a0\" href=\"javascript:void(0)\" id=\"aa"
					  					  +data.tbody[i].id 
					  					  +"\"> 取消订阅</a>"
					  					  +"<a  class=\"a1\" href=\"javascript:void(0)\" id=\"bb"
					  					  +data.tbody[i].id 
					  					  +"\"> 订阅TA</a>"
					  					  +"<a  class=\"a2\" href=\"javascript:void(0)\" id=\"cc"
					  					  +data.tbody[i].id 
					  					  +"\"> 查看信息</a>"
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
				   $(".a1").css("display","inline-block");
			       $(".a0").css("display","none");
			     
			},'json');
	}
	

	
	//点击搜索
	$("#search").click(function(){
		type=$("#choose").val();
		content=$("#content").val();
		if(iscare==1){
			getCarelist(type,content);
		}else{
			getNoCarelist(type,content);
		}
		
	});
	
	//查看信息
	$(".a2").live('click',function(){
		var see=this.id;
		var id=see.slice(2);
		var url="../page/pt_userdetail.html?id="+id+"&type="+type+"&content="+content+"&cur="+cur+"&iscare="+iscare;
		window.parent.right.location.href=encodeURI(url);
	});


	


	
	//订阅公众号
	$(".a1").live('click',function(){
		var a=this.id;
		var id=a.slice(2);
		$.post("/S2014150057/ordinary/carePubUser",{
			id:id,
		},function(data){
			//session失效处理
			if(data.code=="-1"){
				alert(data.message);
				window.parent.location.href="/S2014150057/login";
				}
				
			alert(data.message);
			if(data.result){
				if(iscare==1){
					getCarelist(type,content,cur);
				}else{
					getNoCarelist(type,content,cur);
				}
			}
		},'json');
	});

	//取消订阅
	$(".a0").live('click',function(){
		var a=this.id;
		var id=a.slice(2);
		if(confirm("您确定要取消订阅吗?")){

			$.post("/S2014150057/ordinary/cancelCarePubUser",{
				id:id,
		},function(data){
			//session失效处理
			if(data.code=="-1"){
				alert(data.message);
				window.parent.location.href="/S2014150057/login";
				}
				
			alert(data.message);
			if(data.result){
				if(iscare==1){
					getCarelist(type,content,cur);
				}else{
					getNoCarelist(type,content,cur);
				}
			}
		},'json');
		
		}
	});
	//下一页
	$(".up").live('click',function(){
		var ud=this.id;
		var id=ud.slice(2);
		if(id>0&&id<=total){
			if(iscare==1){
				getCarelist(type,content,id);
			}else{
				getNoCarelist(type,content,id);
			}
		}
	});
	
	
	
	//已订阅或者未订阅改变时。分别请求不同接口
	 $("#opti").change(function(){

	      var op = $("#opti option:selected").val();
	  	iscare=op;
	    if(op==1){
	    	getCarelist(type,content,cur);
	    }else{
	    	getNoCarelist(type,content,cur);
	       
	    }

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





	
	
