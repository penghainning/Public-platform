$(function(){
	
	 
	var oldhead;//用户旧头像名
	var newhead;//用户新头像名、默认新旧头像是一样的
	
	//获取用户基本信息进行填充（需要进行html转义）
	function getmessage(){
		$.post("/S2014150057/user/getAccount",function(data){
			$("#nickname").val(strtoHtml(data.nickname));
			if(data.sex=="男")
				var a=1;
			else
				var a=0;
			 $("#sex").val(a);
			$("#identity").val(data.identity);
			$("#phone").val(data.phone);
			$("#city").val(strtoHtml(data.city));
			$("#mail").val(strtoHtml(data.mail));
			$("#time").val(data.createtime);
			$("#head").attr("src",data.photourl);
			oldhead=data.photo;
			newhead=oldhead;
			
			
		},'json');
	}
	getmessage();
	
	
	   
	   //保存用户信息
		var nickname,sex,identity,phone,city,mail,head;
		$("#save").click(function(){
			var phoneReg = /^1\d{10}$/;//手机格式正则
			var mailReg = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;//邮箱正则表达式
			nickname=$.trim($("#nickname").val())
			identity=$.trim($("#identity").val());
			phone=$.trim($("#phone").val());
			city=$.trim($("#city").val());
			mail=$.trim($("#mail").val());
			head=$("#head").attr("src"); 
			sex=$('#sex option:selected').val();//获取选中的性别
			if(nickname==""){
				alert('昵称不能为空,请输入！', $("#nickname"));
				$("#nickname").focus();
				return ; 
			}
			if(identity == "")
			{
				alert("请输入身份证号！")
				$("#identity").focus();
				return;
			}
		     if (!(/(^\d{15}$)|(^\d{17}([0-9]|X)$)/.test(identity))) 
			 {
			     alert('输入的身份证号长度不对，或者号码不符合规定！\n15位号码应全为数字，18位号码末位可以为数字或X。');
			  	$("#identity").focus();
				return;
			 }
			if(phone==""){
				alert('手机号不能为空,请输入！！', $("#phone"));
				$("#phone").focus();
				return ; 
			}
			if(!phone.match(phoneReg))
				{
					alert("手机格式不正确，请重新输入")
					$("#phone").focus();
					return;
				}
			if(mail == "")
			{
				alert("请输入邮箱！")
				$("#mail").focus();
				return;
			}
		    if(!mail.match(mailReg))
			{
				alert("邮箱格式不正确，请重新输入")
				$("#mail").focus();
				return;
			}
		    if(city == "")
			{
				alert("请输入邮箱！")
				$("#city").focus();
				return;
			}
			$.post("/S2014150057/pub/editPublic",{
				nickname:nickname,
				city:city,
				sex:sex,
				mail:mail,
				phone:phone,
				identity:identity,
				oldhead:oldhead,
				newhead:newhead
				},function(data){
				if(data.result){
					alert("修改信息成功");
					//首页设置更新后的用户信息
				    $("#welcome",window.parent.document).html("欢迎你！尊敬的公众号用户："+strtoHtml(nickname));
					$("#pic",window.parent.document).attr("src",head);
					getmessage();
					
				}else{
					alert(data.message);
					return;
				}
			},"json");
			
			
		})
		
		//重置按钮
		$("#reset").click(function(){
			$("#nickname").val("");
			$("#sex").val(0);
			$("#identity").val("");
			$("#phone").val("");
			$("#city").val("");
			$("#mail").val("");
		})
		
			//上传图片
			$('#sub').click(function(){
				 var s=document.fff.file.value; 
				if(s==""){
					alert("请选择文件");
					return;
				}
					$.ajax({
					    url: '/S2014150057/user/setHead',
					    type: 'POST',
					    cache: false,
					    data: new FormData($('#fileform')[0]),
					    processData: false,
					    contentType: false
					}).done(function(data) {
						
						if(data.result){
							alert("上传成功");
							$("#head").attr("src",data.photourl);
							newhead=data.filename;
						}else{
							alert(data.message);
							return;
						}
					}).fail(function(res) {});
			
				});
		
	  //将html字符转成普通字符	
		  function strtoHtml(str) {
			  if(str==null)
				  return "";
			 var arrEntities={'lt':'<','gt':'>','nbsp':' ','amp':'&','quot':'"'};
			 return str.replace(/&(lt|gt|nbsp|amp|quot);/ig,function(all,t){return arrEntities[t];});
			}
			
		
	
});





	
	
