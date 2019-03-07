package cn.xhh.domain;

import java.io.Serializable;

public interface ValueObject<T> extends Serializable {

	/**
	 * ֵ����ͨ��������ֵ���бȽϣ�����û�б�ʶ��
	 * @param other ����ֵ����
	 * @return �������ֵ����ʹ�ֵ�����������ͬ�ͷ���true
	 */
	boolean sameValueAs(T other);
}
