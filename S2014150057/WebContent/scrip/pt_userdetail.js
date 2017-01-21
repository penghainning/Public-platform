$(function(){
	
	
	var url=decodeURI(location.href);
	var tmp1=url.split("?")[1]; 
	var tmp2=tmp1.split("&")[2]; 
	var oldcontent=tmp2.split("=")[1];
	var oldtype=getQueryString("type");
	var oldcur=getQueryString("cur");
	var iscare=getQueryString("iscare");//得到上个页面的已订阅还是未订阅
	var id=getQueryString("id")==null ? "" : getQueryString("id");

	 
	
	//返回事件
		$("#back").click(function(){
			var url="../page/pt_careuser.html?type="+oldtype+"&cur="+oldcur+"&content="+oldcontent+"&iscare="+iscare;
			window.parent.right.location.href=encodeURI(url);
		});
		
		
		
	
			$.post("/S2014150057/ordinary/getPubUserDetail",{id:id},function(data){
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
		
		
		function getQueryString(name){
			var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
			var r = window.location.search.substr(1).match(reg);
			if (r!=null) return unescape(r[2]); return null; 
		}
			
		
	
})





	
	
