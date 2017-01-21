package app.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfinal.kit.PathKit;

public class CheckFormat {

	
	//检验手机格式
	public static boolean isMobileNO(String mobiles){ 
    	if(null == mobiles)
    		return false;
        Pattern p = Pattern.compile("^([0-9])\\d{10}$");     
        Matcher m = p.matcher(mobiles);     
        return m.matches();     
    } 
	
	//检验邮箱格式
    public static boolean isEmail(String email){
    	if(null == email)
    		return false;
     String str="^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2,})*$";
        Pattern p = Pattern.compile(str);     
        Matcher m = p.matcher(email);     
        return m.matches();     
    } 
    
    

    
	/**
	 * 返回成功MAP*/
	
	public static Map<String, Object> successMap(String errorStr){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", true);
		map.put("message", errorStr);
		return map;
	}
	
	/**
	 * 返回失败MAP
	 */
	public static Map<String, Object> errorMap(String errorStr){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", false);
		map.put("message", errorStr);
		return map;
	}
	
	
	//特殊字符转义
		public static String replace(String a){
			if(a==null){
				return null;
			}
			 String html = a;
			     html = html.replaceAll( "&", "&amp;");
			     html = html.replace( "\"", "&quot;");  //"
			     html = html.replace( "\t", "&nbsp;&nbsp;");// 替换跳格
			     html = html.replace( " ", "&nbsp;");// 替换空格
			     html = html.replace("<", "&lt;");
			     html = html.replaceAll( ">", "&gt;");
			     return html;

		}
		
		
		//转码
		public static String creSinFormAndByts(String str) throws UnsupportedEncodingException{
			if(null == str || "".equals(str.trim()))
				return "";
			return new String(str.getBytes("8859_1"),"UTF-8").trim().replace("'", "\\'");
		}
		
		
		//检查图片名是否以jpg、png、gif结尾
		public static boolean checkPicName(String picName){
			if(null==picName || "".equals(picName))
				return false;
			String[] format={".jpg",".png",".gif",".jpeg","bmp"};
			boolean flag=false;
			for(String s:format){
				if(picName.endsWith(s)){
					flag=true;
					break;
				}
			}
			return flag;
		}
		
		
		//删除文件（头像）
		public static final String deleteFile(String filePath) throws Exception {
			String realPath = PathKit.getWebRootPath() +"/image/userhead/"+ filePath;
			//System.out.println(realPath);
			File file = new File(realPath); 
			if (file.exists()) {
				if (file.delete()) {
					System.out.println("文件删除成功：" + realPath);
				} else {
					System.out.println("文件删除失败：" + realPath);
				}
			}
			return null;
		}
		

}
