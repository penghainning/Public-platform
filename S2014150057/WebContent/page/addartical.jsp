
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
<link rel="stylesheet" href="../css/addartical.css">
<script src="../scrip/jquery.js"></script>
<script type="text/javascript" src="../ueditor/ueditor.config.js"></script>  
<script type="text/javascript" src="../ueditor/ueditor.all.js"></script>  
<script src="../scrip/addartical.js"></script>

</head>
<body>
<table class="defaultTable" cellspacing="0">
					<tbody>
						<tr>
							<td class="column">标题：</td>
							<td class="name " colspan="2"><input name="" type="text" id="title" class="normal_input" style="width:98%" value=""></td>
						</tr>
						
						<tr>
							<td class="column">正文：</td>
							<td class="name" colspan="2">
								<div id="divIntro">
									<textarea class="text_area" id="content" name="content" style="width:99%;height:500px;"></textarea>
									
				    		</div>	
                            </td>
						</tr>
					</tbody>
				</table>
				
				  <div class="submit">
  					<input type="button" id="back" class="btn" value="返回">
  					<input type="button" id="save" class="btn" value="保存">
 				</div>
 				
</body>
</html>

