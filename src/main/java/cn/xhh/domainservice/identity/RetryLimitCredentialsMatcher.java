package cn.xhh.domainservice.identity;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ��֤���������˵�¼����У�鹦��
 */
public class RetryLimitCredentialsMatcher extends HashedCredentialsMatcher {

	private static final Logger log = LoggerFactory.getLogger(RetryLimitCredentialsMatcher.class);

	// ��Ⱥ�п��ܻᵼ�³�����֤���5�ε�������ΪAtomicIntegerֻ�ܱ�֤���ڵ㲢��
	private Cache<String, AtomicInteger> lgoinRetryCache;

	private int maxRetryCount = 5;

	public void setMaxRetryCount(int maxRetryCount) {
		this.maxRetryCount = maxRetryCount;
	}

	public RetryLimitCredentialsMatcher(CacheManager cacheManager, String loginRetryCacheName) {
		lgoinRetryCache = cacheManager.getCache(loginRetryCacheName);
	}

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {

		FreesecretToken freeToken = (FreesecretToken) token;
		if (freeToken.getType().equals(LoginType.NOPASSWD)) {
			return true;
		}

		String username = (String) token.getPrincipal();
		// retry count + 1
		AtomicInteger retryCount = lgoinRetryCache.get(username);
		if (null == retryCount) {
			retryCount = new AtomicInteger(0);
			lgoinRetryCache.put(username, retryCount);
		}
		if (retryCount.incrementAndGet() > maxRetryCount) {
			log.warn("username: " + username + " tried to login more than 5 times in period");
			throw new ExcessiveAttemptsException();
		}
		boolean matches = super.doCredentialsMatch(token, info);
		if (matches) {
			// clear retry data
			lgoinRetryCache.remove(username);
		}
		return matches;
	}
}
