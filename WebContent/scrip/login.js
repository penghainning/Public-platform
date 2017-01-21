$(function(){
	
	
	//登录函数
	$("#login").click(function(){
		login();
	});
	
	$("#regist").click(function(){
		regist();
	});
	
	$("#yzm").click(function(){
		refresh();
	});
	
	
	function regist()
	{
		window.location="regist" ;
	}
	

	function refresh()
	{
		 document.getElementById("yzm").src="page/yzm.jsp?nocache="+new Date().getTime();    
		 //location.reload()
	}

	function login(){
		var account=$("#account").val();
		var password=$("#password").val();
		var code=$("#code").val();
		var type=$('#type option:selected').val();//获取选中的type类型
		if(document.getElementById("remember").checked){
		  var remember=true;
		}
		else
			var remember =false;

		if($.trim(account)== ""){
			alert("请输入您的账号");
			$("#account").focus();
			return;
		}
		if($.trim(password)== ""){
			alert("请输入密码");
			$("#password").focus();
			return;
		}
		if($.trim(code)== ""){
			alert("请输入验证码");
			$("#code").focus();
			return;
		}
		 $.post("/S2014150057/user/login",{
			  username:account ,
			  password:password ,
			  code :code,
			  type :type,
			  remember:remember
			},function(data){
			  if(data.result)
			  {
				 window.location.href="/S2014150057/user/home?nocache="+new Date().getTime();
				  
				  //$.post("/S2014150057/user/home",{},function(data){},"json");
			  }else{
				  alert(data.message);
			  }
			     
			},"json");
		
		
	}	
});





