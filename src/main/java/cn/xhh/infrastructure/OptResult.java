package cn.xhh.infrastructure;

/**
 * ҵ���߼�����ͳһ���ؽ��
 * @author TY
 *
 */
public class OptResult {
	public OptResult(int errcode,Object result) {
		this.errcode=errcode;
		this.result=result;
	}
	
	//�������
	private int errcode;
	
	//�������
	private Object result;

	/**
	 * ��ȡ�������
	 * @return ���ؽ������
	 */
	public int getCode() {
		return this.errcode;
	}
	
	/**
	 * ���ý������
	 * @param errcode �������
	 */
	public void setCode(int errcode) {
		this.errcode=errcode;
	}
	
	/**
	 * ��ȡ�������
	 * @return ���ؽ������
	 */
	public Object getResult() {
		return result;
	}

	/**
	 * ���ý������
	 * @param result �������
	 */
	public void setResult(Object result) {
		this.result = result;
	}
	
	/**
	 * ����ɹ�
	 * @param msg �ɹ���Ϣ
	 * @return �ɹ����
	 */
	public static OptResult Successed(Object msg) {
		OptResult res= new OptResult(0,msg);		
		return res;
	}
	
	/**
	 * ����ɹ�
	 * @return �ɹ����
	 */
	public static OptResult Successed() {
		OptResult res= new OptResult(0,"ok");
		return res;
	}
	
	/**
	 * ����ʧ��
	 * @param errcode �������
	 * @param errmsg ������Ϣ
	 * @return ���ش�����
	 */
	public static OptResult Failed(int errcode,Object errmsg) {
		OptResult res= new OptResult(errcode,errmsg);
		return res;
	}
	
	/**
	 * ����ʧ��
	 * @param errcode �������
	 * @return ���ش�����
	 */
	public static OptResult Failed(Object errmsg) {
		OptResult res= new OptResult(50001,errmsg);
		return res;
	}
}
