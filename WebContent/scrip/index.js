$(function(){
	
	
	          
	       //点击基本设置时、更换css样式展开菜单
			$(".setting").click(function(){
				
				var a=document.getElementById("setting_ul").style.display;
				if(a=="none")
  				document.getElementById("setting_ul").style.display="block";
				else
				document.getElementById("setting_ul").style.display="none";
			})

			
			  //点击文章列表时、更换css样式展开菜单
			$(".article").click(function(){
				
				var a=document.getElementById("article_ul").style.display;
				if(a=="none")
  				document.getElementById("article_ul").style.display="block";
				else
				document.getElementById("article_ul").style.display="none";
			})
			
			
			
			//点击子菜单时，添加样式
			$("#article_ul li a,#setting_ul li a").click(function(){
				$("#article_ul li a,#setting_ul li a").removeClass("on");
				$("#title").text($(this).text());
				$(this).addClass("on");
			});
				
		
			//点击退出登录时相关操作
		  $("#out").click(function(){
				logout();
			});
				
				
		  
		  
		  //注销函数
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
			

				//首页获取用户基本信息并且显示
				function getMessage(){
					var timestamp = Date.parse(new Date());
					 $.post("/S2014150057/user/getAccount?a="+timestamp,{
						},function(data){
							//登陆失效重新登录
							if(data.code==-1){
								alert(data.message)
								window.location.herf="/S2014150057/login"
							}
						  if(data.result)
						  {
							
							 $("#welcome").html("欢迎你！尊敬的公众号用户："+data.nickname);
							 $("#pic").attr('src',data.photourl); 
							 if(data.flag==0){
								 alert("你的用户信息还不完善，请到基本设置中完善个人资料")
							 }
							 
							  
						  }else{
							  alert(data.message);
							  window.location.href="/S2014150057/login";
						  }
						     
						},"json");
				}
				getMessage();
				
				

		})
		
		
		