package com.joint.base.mp;

import com.fz.us.base.service.memcached.CacheConsts;
import com.fz.us.base.service.memcached.SpyMemcachedClient;
import fz.me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.InputSource;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.InputStream;

/**
 * @author min_xu
 *
 * 微信配置
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class WxMpInCacheConfigStorage extends WxMpInMemoryConfigStorage {
    private final static String wxMpConfigStorage_token = CacheConsts.WXMP_ACCESS_TOKEN_TEXT;
    private final static String wxMpConfigJSAPI_token = CacheConsts.WXMP_JSAPI_TOKEN;

    //访问微信的域名 没有http://
    protected String host;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    protected String version;
    protected static String http = "http://";
    public String getHttpHost() {
        return http+host;
    }
    public void setHttpHost(String httpHost) {
        this.httpHost = httpHost;
    }
    protected  String httpHost;

    @Resource
    private SpyMemcachedClient spyMemcachedClient;

    @Override
    public String toString() {
        return "SimpleWxConfigProvider [appId=" + appId + ", secret=" + secret + ", accessToken=" + accessToken
                + ", expiresIn=" + expiresIn + ", token=" + token + ", aesKey=" + aesKey + "]";
    }


    public static WxMpInCacheConfigStorage fromXml(InputStream is) throws JAXBException {
        Unmarshaller um = JAXBContext.newInstance(WxMpInCacheConfigStorage.class).createUnmarshaller();
        InputSource inputSource = new InputSource(is);
        inputSource.setEncoding("utf-8");
        return (WxMpInCacheConfigStorage) um.unmarshal(inputSource);
    }

    @Override
    public void updateAccessToken(String accessToken, int expiresIn) {
        this.setAccessToken(accessToken);
        this.setExpiresIn(expiresIn);

        setTokenToCache(accessToken, expiresIn);
    }

    @Override
    public String getAccessToken(){
        return getTokenFromCache();
    }

    public String getTokenFromCache(){
        String _accessToken = spyMemcachedClient.get(wxMpConfigStorage_token +"#" + getAppId());
        this.setAccessToken(_accessToken);
        return _accessToken;
    }

    public void setTokenToCache(String accessToken, int expiresIn) {
        this.setAccessToken(accessToken);
        spyMemcachedClient.set(wxMpConfigStorage_token + "#" + getAppId(), expiresIn, accessToken);
    }

    public void deleteTokenFromCache(){
        this.setAccessToken("");
        spyMemcachedClient.delete(wxMpConfigStorage_token + "#" + getAppId());
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }


    //---------------------------------------------------//
    protected volatile String jsapiTicket;
    protected volatile long jsapiTicketExpiresTime;
    public String getJsapiTicket() {
        String jsTicket =  spyMemcachedClient.get(wxMpConfigJSAPI_token +"#" + getAppId());
        return jsTicket;
    }

    public void setJsapiTicket(String jsapiTicket) {
        this.jsapiTicket = jsapiTicket;
    }

    public long getJsapiTicketExpiresTime() {
        return jsapiTicketExpiresTime;
    }

    public void setJsapiTicketExpiresTime(long jsapiTicketExpiresTime) {
        this.jsapiTicketExpiresTime = jsapiTicketExpiresTime;
    }

    public boolean isJsapiTicketExpired() {
        String _accessToken = spyMemcachedClient.get(wxMpConfigJSAPI_token +"#" + getAppId());
        //return System.currentTimeMillis() > this.jsapiTicketExpiresTime;
        return StringUtils.isEmpty(_accessToken);
    }

    public void updateJsapiTicket(String jsapiTicket, int expiresInSeconds) {
        // 预留200秒的时间
        //this.jsapiTicketExpiresTime = System.currentTimeMillis() + (expiresInSeconds - 600) * 1000l;
        this.jsapiTicketExpiresTime = expiresInSeconds - 1200;
        //wxMpConfigJSAPI_token
        spyMemcachedClient.set(wxMpConfigJSAPI_token + "#" + getAppId(), (int)jsapiTicketExpiresTime, jsapiTicket);
        setJsapiTicket(jsapiTicket);
    }

    public void expireJsapiTicket() {
        //this.jsapiTicketExpiresTime = 0;
        this.jsapiTicket = "";
        spyMemcachedClient.delete(wxMpConfigJSAPI_token + "#" + getAppId());
    }
}
