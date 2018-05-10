package com.tiansu.eam.modules.sso;


/*通用的返回结果实体类*/
public class ResponseBaseResult {
	
	// 是否执行成功
	private boolean success;
	// 执行过程中的异常信息
	private String  exceptionMsg;
	// 执行的时间
	private String  excuteDateTime;
	// 返回执行的结果
	private Boolean resultBody;
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getExceptionMsg() {
		return exceptionMsg;
	}
	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}
	public String getExcuteDateTime() {
		return excuteDateTime;
	}
	public void setExcuteDateTime(String excuteDateTime) {
		this.excuteDateTime = excuteDateTime;
	}
	public Boolean getResultBody() {
		return resultBody;
	}
	public void setResultBody(Boolean resultBody) {
		this.resultBody = resultBody;
	}
}
