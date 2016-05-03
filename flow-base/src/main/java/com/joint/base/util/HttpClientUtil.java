package com.joint.base.util;

import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.cookie.*;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.protocol.HttpContext;

/**
 * Created with us2 -> com.fz.us.core.util.
 * User: min_xu
 * Date: 2014-12-11
 * Time: 15:50
 * 说明：
 */
public class HttpClientUtil {
    public static CloseableHttpClient getHttpClient(){
        BasicCookieStore cookieStore = new BasicCookieStore();
        CookieSpecProvider easySpecProvider = new CookieSpecProvider() {
            public CookieSpec create(HttpContext context) {
                return new BrowserCompatSpec() {
                    @Override
                    public void validate(Cookie cookie, CookieOrigin origin)
                            throws MalformedCookieException {
                        // Oh, I am easy
                    }
                };
            }
        };
        Registry<CookieSpecProvider> r = RegistryBuilder
                .<CookieSpecProvider> create()
                .register(CookieSpecs.BEST_MATCH, new BestMatchSpecFactory())
                .register(CookieSpecs.BROWSER_COMPATIBILITY,
                        new BrowserCompatSpecFactory())
                .register("easy", easySpecProvider).build();
        RequestConfig requestConfig = RequestConfig.custom()
                .setCookieSpec("easy").setSocketTimeout(10000)
                .setConnectTimeout(10000).build();

        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCookieSpecRegistry(r)
                .setDefaultRequestConfig(requestConfig)
                .setDefaultCookieStore(cookieStore)
                .build();
        return httpclient;
    }
}
