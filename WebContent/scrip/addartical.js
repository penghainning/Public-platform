$(function(){
	var id=getQueryString("id")==null ? "" : getQueryString("id");//获取到的文章id
	
//	//初始化富文本编辑器
//	 KindEditor.ready(function(K) {
//		 var options = {
//				 	imageUploadJson : '../editor/jsp/upload_json.jsp',
//					fileManagerJson : '../editor/jsp/file_manager_json.jsp',
//					resizeType:'1',
//					allowFileManager : true,
//					afterCreate : function() {//设置编辑器创建后执行的回调函数。
//						this.sync();
//					},
//					afterChange : function() {//设置编辑器改变时执行的回调函数
//						this.sync();
//					},
//					afterBlur:function(){
//						this.sync();  
//					},
//					allowImageUpload: true//显示图片上传按钮
//			};
//		    editor = K.create('#content', options);
//   
// });
	 
	
		 UEDITOR_CONFIG.UEDITOR_HOME_URL = '../ueditor/';
		 var ue=UE.getEditor('content');  
	 
        //返回按钮时间	 
		$("#back").click(function(){
			window.parent.right.location.href="../page/artical.html";
		});
		
		
		//编辑文章模式，将文章内容填充进编辑器
		if(id != ""){
			$.post("/S2014150057/artical/getArticleDetail",{id:id},function(data){
				$("#title").val(strtoHtml(data.title));
				//var ue =  UE.getEditor('content');  
				//对编辑器的操作最好在编辑器ready之后再做
				ue.ready(function() {
				    //设置编辑器的内容
				    ue.setContent(data.content);
				 
				});
				//editor.html(data.content);
			},"json");
		}
		
		
		
		//保存文章，将数据提交到后台插入数据库
		var title,content;
		$("#save").click(function(){
			title=$.trim($("#title").val())
			//content=$.trim($("#content").val());
			content=ue.getContent();
			if(title==""){
				alert('标题不能为空,请输入！', $("#title"));
				$("#title").focus();
				return ; 
			}
			if(title.length>50){
				alert('标题长度不能超过50字,请重新输入！', $("#title"));
				$("#title").focus();
				return ; 
			}
			if(content==""){
				alert('正文不能为空,请编辑！', $("#content"));
				$("#content").focus();
				return ; 
			}
			$.post("/S2014150057/artical/addarticle",{id:id,title:title,content:content},function(data){
				
				///当session失效的时候，进行重新登录
				if(data.code=="-1"){
					alert(data.message);
					window.parent.location.href="/S2014150057/login";
				}
				if(data.result){
					alert("保存成功，即将返回列表");
					window.parent.right.location.href="../page/artical.html";
				}else{
					alert(data.message);
					return;
				}
			},"json");
			
			
		})
		
		
		//根据正则表达式获得参数值
		function getQueryString(name){
			var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
			var r = window.location.search.substr(1).match(reg);//获取参数字符串 形如id=1&parm2=b
			if (r!=null)
				return unescape(r[2]); //进行解码，将通过找到形式为 %xx 和 %uxxxx 的字符序列（x 表示十六进制的数字），用 Unicode 字符 \u00xx 和 \uxxxx 替换
			return null; 
		}
		
		  //将html字符转成普通字符	
		  function strtoHtml(str) {
			  if(str==null)
				  return "";
			 var arrEntities={'lt':'<','gt':'>','nbsp':' ','amp':'&','quot':'"'};
			 return str.replace(/&(lt|gt|nbsp|amp|quot);/ig,function(all,t){return arrEntities[t];});
			}
			
		
	
})





	
	
