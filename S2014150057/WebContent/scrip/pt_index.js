$(function(){
				$(".setting").click(function(){
				
				var a=document.getElementById("setting_ul").style.display;
				if(a=="none")
  				document.getElementById("setting_ul").style.display="block";
				else
				document.getElementById("setting_ul").style.display="none";
			})

			$(".article").click(function(){
				
				var a=document.getElementById("article_ul").style.display;
				if(a=="none")
  				document.getElementById("article_ul").style.display="block";
				else
				document.getElementById("article_ul").style.display="none";
			})
			
			$("#article_ul li a,#setting_ul li a").click(function(){
				$("#article_ul li a,#setting_ul li a").removeClass("on");
				$("#title").text($(this).text());
				$(this).addClass("on");
			});
				
				
				
				$("#out").click(function(){
					logout();
				});
				
				
				function logout(){
					
					 $.post("/S2014150057/user/logout",{
						},function(data){
						  if(data.result)
						  {
							  window.location.href="/S2014150057/login";
							  
						  }else{
							  alert(data.message);
						  }
						     
						},"json");	
				}

				 $.post("/S2014150057/user/getAccount",{
					},function(data){
					  if(data.result)
					  {
						 $("#welcome").text("欢迎你！亲爱的用户："+strtoHtml(data.nickname));
						 $("#pic").attr('src',data.photourl); 
						 if(data.flag==0){
							 alert("你的用户信息还不完善，请到基本设置中完善个人资料")
						 }
						 
						  
					  }else{
						  alert(data.message);
						  window.location.href="/S2014150057/login";
					  }
					     
					},"json");	
				 
				//将html字符转成普通字符	
					function strtoHtml(str) {
					 var arrEntities={'lt':'<','gt':'>','nbsp':' ','amp':'&','quot':'"'};
					 return str.replace(/&(lt|gt|nbsp|amp|quot);/ig,function(all,t){return arrEntities[t];});
					}		

				 
		});
		
		
		
		