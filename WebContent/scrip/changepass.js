$(function(){
	
	 
	//获取当前用户的信息	
	function getmessage(){
		$.post("/S2014150057/user/getAccount",function(data){
			///当session失效的时候，进行重新登录
			if(data.code=="-1"){
				alert(data.message);
				window.parent.location.href="/S2014150057/login";
			}
			$("#account").text(data.account);
		},'json');
	}
	getmessage();
	
	
	   //修改密码的函数
		var oldpass,newpass,renewpass;
		$("#sure").click(function(){
			var reg = /^(?![^a-zA-Z]+$)(?!\D+$).{7,20}/;//用户名密码正则表达式
			oldpass=$.trim($("#oldpass").val())
			newpass=$.trim($("#newpass").val());
			renewpass=$.trim($("#renewpass").val());
			if(oldpass==""){
				alert('原始密码不能为空,请输入！', $("#oldpass"));
				$("#oldpass").focus();
				return ; 
			}
			if(newpass==""){
				alert('新密码不能为空,请输入！！', $("#newpass"));
				$("#newpass").focus();
				return ; 
			}
			if(renewpass==""){
				alert('确认密码不能为空,请输入！！！', $("#renewpass"));
				$("#renewpass").focus();
				return ; 
			}
			if(newpass!=renewpass){
				alert('两次密码不相同,请重新输入输入！！！', $("#renewpass"));
				$("#renewpass").focus();
				return ; 
			}
		    if(!newpass.match(reg)){
					alert("密码必须包含数字和字母，且长度为7-20")
					$("#newpass").focus();
					return;
				}
				
			$.post("/S2014150057/pub/changePassword",{newpassword:newpass,oldpassword:oldpass},function(data){
				if(data.result){
					alert("修改密码成功,请重新登陆！");
					 $.post("/S2014150057/user/logout",{
						},function(data){
						  if(data.result)
						  {
							  window.parent.location.href="/S2014150057/login";
							  
						  }else{
							  alert(data.message);
						  }
						     
						},"json");	
				}else{
					alert(data.message);
					return;
				}
			},"json");
			
			
		})
		
	
})





	
	
