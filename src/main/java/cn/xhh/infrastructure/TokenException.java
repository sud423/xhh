package cn.xhh.infrastructure;

/**
 * 自定义的RuntimeException Token过期时抛出
 * 
 * @author susd
 *
 */
public class TokenException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private String msg;

	public TokenException(String msg) {
		super();
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
