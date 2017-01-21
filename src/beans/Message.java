package beans;

import java.util.ArrayList;

public class Message {

	private int  id;//留言信息Id
	private String name;//留言者名字
	private String mail;//留言者邮箱
	private String content;//留言内容
	private String creatrtime;//留言时间
	private int state;//是否回复
	private ArrayList<Reply> reply;//管理员回复内容
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreatrtime() {
		return creatrtime;
	}
	public void setCreatrtime(String creatrtime) {
		this.creatrtime = creatrtime;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public ArrayList<Reply> getReply() {
		return reply;
	}
	public void setReply(ArrayList<Reply> reply) {
		this.reply = reply;
	}
}
