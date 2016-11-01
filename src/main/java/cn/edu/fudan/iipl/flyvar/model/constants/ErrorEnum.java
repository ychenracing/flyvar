package cn.edu.fudan.iipl.flyvar.model.constants;


public enum ErrorEnum {

	PAGE_NOT_FOUND(404, "page not found"),
	NOT_SUPPORTED(405, "method not supported"),
	SYSTEM_ERROR(500, "system error"),
	INVALID_ACCESS(200, "invalid access");

	private Integer code;

	private String msg;

	ErrorEnum(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
