package cn.xhh.controllers;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cn.xhh.infrastructure.OptResult;
import cn.xhh.infrastructure.TokenException;


/**
 * ȫ���쳣����
 * 
 * @author susd
 *
 */
@ControllerAdvice
@ResponseBody
public class ExceptionAspect {
	private static final Logger log = Logger.getLogger(ExceptionAspect.class);

	/**
	 * 400 - Bad Request
	 * 
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public OptResult handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		log.error("json�ַ�����ʽ����", e);

		return OptResult.Failed(50400, "json�ַ�����ʽ����");
	}

	/**
	 * 400 - Bad Request
	 * 
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public OptResult handleValidationException(MethodArgumentNotValidException e) {
		log.error("��������ʧ��", e);

		return OptResult.Failed(50400, "��������ʧ��");
	}

	/**
	 * 405 - Method Not Allowed��HttpRequestMethodNotSupportedException
	 * ��ServletException������,��ҪServlet API֧��
	 */
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public OptResult handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		log.error("��֧�ֵ�ǰ���󷽷�", e);

		return OptResult.Failed(50405, "��֧�ֵ�ǰ���󷽷�");
	}

	/**
	 * 415 - Unsupported Media Type��HttpMediaTypeNotSupportedException
	 * ��ServletException������,��ҪServlet API֧��
	 */
	@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	@ExceptionHandler({ HttpMediaTypeNotSupportedException.class })
	public OptResult handleHttpMediaTypeNotSupportedException(Exception e) {
		log.error("��֧�ֵ�ǰý������", e);
		return OptResult.Failed(50415, "��֧�ֵ�ǰý������");
	}

	/**
	 * 500 - Internal Server Error
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public OptResult handleException(Exception e) {
		log.error("�ڲ��������", e);
		return OptResult.Failed(50500, "�ڲ��������");
	}

	/**
	 * 500 - Internal Server Error
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(TokenException.class)
	public OptResult handleTokenException(Exception e) {
		log.error("��Ч��token", e);
		return OptResult.Failed(50500, "��Ч��token");
	}

}
