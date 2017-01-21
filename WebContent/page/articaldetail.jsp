<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta   http-equiv="Expires"   CONTENT="0">
<meta   http-equiv="Cache-Control"   CONTENT="no-cache,must-revalidate">
<meta   http-equiv="Pragma"   CONTENT="no-cache">
<title></title>
<link rel="stylesheet" href="../css/articaldetail.css">
<script src="../scrip/jquery.js"></script>
<script src="../scrip/articaldetail.js"></script>
</head>
<body>
<table class="defaultTable" cellspacing="0">
						<tr>
							<td class="column">标题：</td>
							<td class="name"  colspan="2"><h2 id="title"></h2></td>
						</tr>
						
						<tr height="500px">
							<td class="column">正文：</td>
							<td class="name" colspan="2">
							<div id="divIntro" >
									
				    		</div>	
                            </td>
						</tr>
						
						
						<tr>
							<td class="column">点赞人数：</td>
							<td class="name" colspan="2" id="lovecount">3</td>
						</tr>
						
						<tr>
							<td class="column">点赞用户列表：</td>
							<td class="name" colspan="2" id="lovename">张三，李四，王五</td>
						</tr>
						
						<tr>
							<td class="column">评论列表：</td>
							<td  id="comment" class="name" colspan="2">
							
							</td>
						</tr>
				</table>
                
                
   		<div class="submit">
  					<input type="button" id="back" class="btn" value="返回">
 		</div>           
</body>
				
</html>




















