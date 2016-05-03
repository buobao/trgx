package com.joint.base.mp;

import com.fz.us.base.service.memcached.CacheConsts;
import com.fz.us.base.service.memcached.SpyMemcachedClient;
import com.fz.us.base.util.Identities;
import com.fz.us.base.util.LogUtil;
import com.joint.base.bean.EnumManage;
import com.joint.base.bean.SystemConfig;
import com.joint.base.entity.Users;
import com.joint.base.service.UsersService;
import com.joint.base.service.jms.AdvancedNotifyMessageProducer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import fz.me.chanjar.weixin.common.bean.WxAccessToken;
import fz.me.chanjar.weixin.common.bean.WxJsapiSignature;
import fz.me.chanjar.weixin.common.bean.result.WxError;
import fz.me.chanjar.weixin.common.exception.WxErrorException;
import fz.me.chanjar.weixin.common.util.crypto.SHA1;
import fz.me.chanjar.weixin.common.util.http.SimpleGetRequestExecutor;
import fz.me.chanjar.weixin.common.util.http.URIUtil;
import fz.me.chanjar.weixin.mp.api.WxMpServiceImpl;
import fz.me.chanjar.weixin.mp.bean.WxMpCustomMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.StringReader;
import java.security.NoSuchAlgorithmException;

/**
 * Created with us2 -> com.fz.us.core.mp.
 * User: min_xu
 * Date: 2014-12-10
 * Time: 17:28
 * 说明：
 */
public class WxEchoMpServiceImpl extends WxMpServiceImpl{

    @Resource
    private UsersService usersService;
    @Resource
    private SpyMemcachedClient spyMemcachedClient;
    @Resource
    private AdvancedNotifyMessageProducer notifyMessageProducer;
    @Resource
    private WxMpInCacheConfigStorage wxMpConfigStorage;
    @Resource
    private SystemConfig systemConfig;

    @Override
    public void accessTokenRefresh() throws WxErrorException {
        String configAppId = wxMpConfigStorage.getAppId();
        String token_cache = CacheConsts.WXMP_ACCESS_TOKEN_STATUS + "#" + configAppId;

//        if(spyMemcachedClient.get(token_cache) == null){
//            spyMemcachedClient.set(token_cache, 24*60*60, "0");
//        }
//        if(spyMemcachedClient.incr(token_cache, 1, 1, 24 * 60 * 60) <= 1){
//            try {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential"
                + "&appid=" + wxMpConfigStorage.getAppId()
                + "&secret=" + wxMpConfigStorage.getSecret()
                ;
        try {
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpclient.execute(httpGet);
            String resultContent = new BasicResponseHandler().handleResponse(response);
            WxError error = WxError.fromJson(resultContent);
            if (error.getErrorCode() != 0) {
                throw new WxErrorException(error);
            }
            WxAccessToken accessToken = WxAccessToken.fromJson(resultContent);
            wxMpConfigStorage.updateAccessToken(accessToken.getAccessToken(), accessToken.getExpiresIn());
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//            } finally {
//                spyMemcachedClient.set(token_cache, 24*60*60, "0");
//            }
//        }else{
//            LogUtil.info(token_cache + "现在正在被其他的服务器更新，等待...");
//            // 每隔100ms检查一下是否刷新完毕了
//            while (spyMemcachedClient.incr(token_cache, 1, 1, 24 * 60 * 60) >= 2) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
//            }
        // 刷新完毕了，读取一下cachetoken后就没他什么事儿了
        wxMpConfigStorage.getAccessToken();
//        }
    }

    public void sendFormReadMessage(Users receiver, String content, String target, String targetId) throws WxErrorException {
        if("proceedsPlan".equalsIgnoreCase(target)){
            target = "proceeds-plan";
        }else{
            target = target.toLowerCase();
        }
        if(StringUtils.isNotEmpty(usersService.openId(receiver))){
            String targetUrl = wxMpConfigStorage.getHttpHost()+"/"+systemConfig.getWebroot()+"/mp/ajax-" + target+"!read.action?keyId="+targetId;
            String text = receiver.getName() + "，您有一条新消息：" + content + "\n\n <a href=\"" + targetUrl + "\">点击链接查看</a>\n";

            WxMpCustomMessage wxMpCustomMessage = WxMpCustomMessage.TEXT()
                    .toUser(usersService.openId(receiver))
                    .content(text)
                    .build();

            notifyMessageProducer.sendQueue(wxMpCustomMessage, EnumManage.NotifyKeyEnum.wxMpCustomMessage.name());
        }
    }

    public String oauth2buildAuthorizationUrl(String scope, String state,String redirectURI) {
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" ;
        url += "appid=" + wxMpConfigStorage.getAppId();
        if(StringUtils.isEmpty(redirectURI)){
            url += "&redirect_uri=" + URIUtil.encodeURIComponent(wxMpConfigStorage.getOauth2redirectUri());
        }else{
            url += "&redirect_uri=" + wxMpConfigStorage.getHttpHost() +redirectURI;
        }
        url += "&response_type=code";
        url += "&scope=" + scope;
        if (state != null) {
            url += "&state=" + state;
        }
        url += "#wechat_redirect";
        return url;
    }

    public String getJsapiTicket() throws WxErrorException {
        return getJsapiTicket(false);
    }

    public String getJsapiTicket(boolean forceRefresh) throws WxErrorException {
        if (forceRefresh) {
            wxMpConfigStorage.expireJsapiTicket();
        }
        if (wxMpConfigStorage.isJsapiTicketExpired()) {
            String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi";
            String responseContent = execute(new SimpleGetRequestExecutor(), url, null);
            // LogUtil.info(responseContent);
            JsonElement tmpJsonElement = Streams.parse(new JsonReader(new StringReader(responseContent)));
            JsonObject tmpJsonObject = tmpJsonElement.getAsJsonObject();
            String jsapiTicket = tmpJsonObject.get("ticket").getAsString();
            int expiresInSeconds = tmpJsonObject.get("expires_in").getAsInt();
            wxMpConfigStorage.updateJsapiTicket(jsapiTicket, expiresInSeconds);
            return jsapiTicket;
        }
        return wxMpConfigStorage.getJsapiTicket();
    }

    public WxJsapiSignature createJsapiSignature(String url) throws WxErrorException {
        long timestamp = System.currentTimeMillis() / 1000;
        String noncestr = Identities.randomLongAsString();
        String jsapiTicket = getJsapiTicket(false);
        LogUtil.info("jsapiTicket---->" + jsapiTicket);
        try {
            String signature = SHA1.genWithAmple(
                    "jsapi_ticket=" + jsapiTicket,
                    "noncestr=" + noncestr,
                    "timestamp=" + timestamp,
                    "url=" + url
            );
            WxJsapiSignature jsapiSignature = new WxJsapiSignature();
            jsapiSignature.setTimestamp(timestamp);
            jsapiSignature.setNoncestr(noncestr);
            jsapiSignature.setUrl(url);
            jsapiSignature.setSignature(signature);
            return jsapiSignature;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
