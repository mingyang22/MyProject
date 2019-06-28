package net.yangming.chat;


public class MessageInfo {
	private String from = null ; 
	private String to = null;
//	private String nickname;
//	private String userIcon;
	public String id = null;
	private String textContent;
	private String file; //文件网络地址
	private String filePath; //文件本地地址
	private String fileType; //文件类型   image audio
	private String dateline; //发送文件时间
	private String flag = "0";    // flag 1 发送    0 接收  
	private String isRead = "0";  // 0未读  1已读
	private String isSuccess = "0"; //是否发送成功  1成功  0失败
	public String sType = "0"; // 0，点对点消息，1，群消息, 2, 邀请活动, 3,邀请群
	
	
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getIsRead() {
		return isRead;
	}
	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
//	public String getNickname() {
//		return nickname;
//	}
//	public void setNickname(String nickname) {
//		this.nickname = nickname;
//	}
//	public String getUserIcon() {
//		return userIcon;
//	}
//	public void setUserIcon(String userIcon) {
//		this.userIcon = userIcon;
//	}
	public String getTextContent() {
		return textContent;
	}
	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getDateline() {
		return dateline;
	}
	public void setDateline(String dateline) {
		this.dateline = dateline;
	}
	public String getIsSuccess() {
		return isSuccess;
	}
	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}
	
	@Override
	public String toString() {
		return  "f=" +  from +
		"t="+ to  +
		"网络="+file + 
		"本地=" + filePath +
		"类型=" + fileType + 
//		"nic=" + nickname +
//		"ico=" + userIcon +
		"tc=" + textContent +
		"时间=" +dateline +
		"1发0收="+flag + 
		"1读0未=" + isRead  +
		"1成0败=" + isSuccess ;
	}
}
