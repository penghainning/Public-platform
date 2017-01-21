$(function(){
	
	
	
      //获得并保存上个页面的数据	
		var url=decodeURI(location.href);
		var tmp1=url.split("?")[1]; 
		var tmp2=tmp1.split("&")[2]; 
		var oldcontent=tmp2.split("=")[1];
		var oldtype=getQueryString("type");
		var oldcur=getQueryString("cur");
		var id=getQueryString("id")==null ? "" : getQueryString("id");

	 
		//返回事件
		$("#back").click(function(){
			var url="../page/careuser.html?type="+oldtype+"&cur="+oldcur+"&content="+oldcontent;
			window.parent.right.location.href=encodeURI(url);
		});
		
		
		
	
		//请求接口并且填充数据
			$.post("/S2014150057/pub/getUserDetail",{id:id},function(data){
				//session失效处理
				if(data.code=="-1"){
					alert(data.message);
					window.parent.location.href="/S2014150057/login";
					}
					
				$("#photo").attr("src",data.photo);
				$("#nickname").val(data.nickname);
				$("#sex").val(data.sex);
				$("#mail").val(data.mail);
				$("#phone").val(data.phone);
				$("#city").val(data.city);
			},"json");
		
		
			
		//获取url的参数	
		function getQueryString(name){
			var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
			var r = window.location.search.substr(1).match(reg);
			if (r!=null) return unescape(r[2]); return null; 
		}
			
		
	
})





	
	
