package cn.xhh.domain;



/**
 * ʵ��
 * @author sushu
 *
 * @param <T> 
 */
public interface Entity<T> {
	
	/**
	 * ͨ����ݽ�����ʵ��Ƚϣ�������ͨ������
	 * @param other ���Ƚϵ�ʵ��
	 * @return �������������������������ͬ�򷵻�true
	 */
	boolean sameIdentityAs(T other);
}
