
$(function(){
	
	$("#regist").click(function(){
		regist();
	});
	
	


	function regist(){
		var account=$("#account").val();
		var phone=$("#phone").val();
		var password2=$("#password2").val();
		var password=$("#password").val();
		var nickname=$("#nickname").val();
		var type=$('#type option:selected').val();//获取选中的type类型
		var sex=$('#sex option:selected').val();//获取选中的性别类型
		var accountreg=/(^[a-zA-Z0-9]+$)/; //账号正则匹配，只能由字母和数字组成且长度为7-20
		var reg = /^(?![^a-zA-Z]+$)(?!\D+$).{7,20}/;//用户名密码正则表达式
		var phoneReg = /^1\d{10}$/;//手机格式正则

		

		if(account == "")
		{
			alert("请输入账号！")
			$("#account").focus();
			return;
		}
		else if(!account.match(accountreg))
		{
			alert("账号只能含数字和字母！")
			$("#account").focus();
			return;
		}
		else if(account.length>20||account.length<6)
		{
			alert("账号长度必须在6-20位之间")
			$("#account").focus();
			return;
		}
		else if(phone == "")
		{
			alert("请输入手机号码")
			$("#phone").focus();
			return;
		}
		
		else if(!phone.match(phoneReg))
		{
			alert("手机格式不正确，请重新输入")
			$("#phone").focus();
			return;
		}


		else if(password == "")
		{
			alert("请输入密码")
			$("#password").focus();
			return;
		}

		else if(password2 == "")
		{
			alert("请确认密码！")
			$("#password2").focus();
			return;
		}
		else if(password2!=password)
		{
			alert("两次密码不相同，请重新输入！")
			$("#password2").focus();
			return;
		}
		
		else if(!password.match(reg))
		{
			alert("密码必须包含数字和字母，且长度为7-20")
			document.registform.password.focus();
			return;
		}
		else if(nickname == "")
		{
			alert("请输入昵称！")
			$("#nickname").focus();
			return;
		}
		else if(nickname.length>20)
		{
			alert("昵称长度不能大于20")
			$("#nickname").focus();
			return;
		}

		 $.post("/S2014150057/user/regist",{
			  account:account ,
			  phone:phone ,
			  password:password ,
			  nickname:nickname ,
			  sex:sex ,
			  type:type
			},function(data){
				alert(data.message);
				if(data.result) 
				  window.location.href="/S2014150057/login";
				  
			  
			     
			},"json");
				
	}

})



	