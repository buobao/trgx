package com.joint.web.shiro.credentials;

import com.fz.us.base.cache.CacheConstant;
import com.fz.us.base.service.memcached.SpyMemcachedClient;
import com.joint.web.shiro.realm.StatelessToken;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by hpj on 2014/12/29.
 */
public class RetryLimitHashedCredentialsMatcher  extends HashedCredentialsMatcher {
    private Cache<String,AtomicInteger> passwordRetryCache;

    public RetryLimitHashedCredentialsMatcher() {
    }
    @Resource
    private SpyMemcachedClient spyMemcachedClient;

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String)token.getPrincipal();
        String passwordRetryMemCache = CacheConstant.SHIRO_PASSWORD_RETRY + username;   //"passwordRetryCache_"+ username;
        int time = 1*10*60;
        //10分钟
        if(spyMemcachedClient.incr(passwordRetryMemCache,1,1,time) > 10){
            throw new ExcessiveAttemptsException();
        }
        if(spyMemcachedClient.get(passwordRetryMemCache) == null){
            spyMemcachedClient.set(passwordRetryMemCache, time, "1");
        }
        //判断token StatelessToken直接判断是否相等
        boolean matches = true;
        if(token instanceof StatelessToken){
            matches = ((StatelessToken) token).getClientDigest().equals(info.getCredentials());
        }else{
            matches = super.doCredentialsMatch(token, info);
        }
        if(matches) {
            //clear retry count
            //passwordRetryCache.remove(username);
            spyMemcachedClient.set(passwordRetryMemCache, time, "1");
        }
        return matches;
    }
}
