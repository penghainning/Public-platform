$(function(){

	
	var a,b,c;//搜索条件
	var cur=getQueryString("cur");//当前页
	var total;//总页数
	var pubid=getQueryString("pubid");//公众号id
	if(pubid==null)
		pubid=-1;
	var type=getQueryString("type");//搜索类型
	$("#choose").val(type);
	var content;
	
	var url=decodeURI(location.href);
	var tmp1=url.split("?")[1]; 
	if(tmp1!=null){
		var tmp2=tmp1.split("&")[2]; 
		content=tmp2.split("=")[1];
		if (content == "undefined"){ 
			content=null;
		}
		else if(type =="title")
		$("#content").val(content);
		
	   
	}
	

   


	//根据公众号id获取文章列表
	function getlist(type,content,curpage){

		getType();
		$.post("/S2014150057/artical/findArticelListByOrdinary",{
			love:a,
			title:b,
			comment:c,
			curpage:curpage,
			pubid:pubid,
			pagesize:5
			},function(data){
				var tableStr="";
				var len = data.tbody.length;
				if(len==0){
					 $("#list").html("&nbsp;&nbsp;&nbsp;暂无文章"); 
					 return;
				}
				tableStr = tableStr + "<table class=\"table\"><tr><th>序号</th><th style=\"width: 30%\">标题</th><th width=\"120px;\">公众号名称</th><th >点赞数量</th>"
									+ "  <th >发布时间</th><th>操作</th></tr>";
				 for(var i=0 ;i<len ; i++){
					  tableStr = tableStr + "<tr><td>"+ data.tbody[i].num +"</td>"
					  					  +"<td>"+data.tbody[i].title + "</td>"
					  					 +"<td>"+data.tbody[i].nickname + "</td>"
					  					  +"<td>"+data.tbody[i].love +"</td>" 
					  					  +"<td>"+data.tbody[i].edittime +"</td>" 
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
	

	
	//获取订阅的公众号名字列表并填充到下拉列表中
	function getNamelist(){
		
		$.post("/S2014150057/ordinary/findPubList",{
			
			},function(data){
				if(data.code=="-1"){
					alert(data.message);
					window.parent.location.href="/S2014150057/login";
				}
				var Str="";
				Str =Str+"<option value=\"-1\">所有</option>";
				if(data.result){	
					var len = data.total;
					
				    if(len>0){
				    	 for(var i=0 ;i<len ; i++){
							  Str = Str + "<option value=\""
							  			+data.tbody[i].num
							  			+"\">"
							  			+data.tbody[i].nickname
							  			+"</option>";
							  }
				    }
				    else{
				    	$("#pubcontent").attr("placeholder","暂无订阅公众号");
				    }
					
	      
	       
				}
				 $("#pub").html(Str); 	
				 $("#pub").val(pubid);
				 var op = $("#pub option:selected").text();
				 $("#pubcontent").val(op);
				
				
				
			     
			},'json');
	}
	getNamelist();
	
	 
	
	//搜索按钮事件
	$("#search").click(function(){
		 type=$("#choose").val();
		 if(type=="comment" || type =="love")
			 content=1;
		 else
			 content=$("#content").val();
		getlist(type,content);
	});
	
	
	//查看文章详情事件
	$(".see").live('click',function(){
		var see=this.id;
		var id=see.slice(2);
		var url="../page/pt_articaldetail.html?id="+id+"&type="+type+"&cur="+cur+"&content="+content+"&pubid="+pubid;
		window.parent.right.location.href=encodeURI(url);
	});

	

	//分页事件
	$(".up").live('click',function(){
		var ud=this.id;
		var id=ud.slice(2);
		if(id>0&&id<=total)
			getlist(type,content,id);

	});
	
	//当选择特定公众号时
	 $("#pub").change(function(){

	      var op = $("#pub option:selected").text();
	      pubid=$("#pub option:selected").val();
	      $("#pubcontent").val(op);
	});
	 
	 
	 
	 
		
	 //获取url传的参数
		function getQueryString(name){
			var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
			var r = window.location.search.substr(1).match(reg);
			if (r!=null) return unescape(r[2]); return null; 
		}
		
		
		function getType(){
			if(type=="title")
			{
				a="";
				b=content;
				c="";
			}
			else if(type=="comment")
			{
				a="";
				b=null;
				c=content;
			}
			else if(type=="love"){
			
				a=content;
				b=null;
				c="";
			}

		}
		


	
	


	
})





	
	
