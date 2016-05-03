package com.joint.web.shiro.filter;

import com.fz.us.base.bean.Result;
import com.fz.us.base.service.common.ResultService;
import com.fz.us.base.util.Identities;
import com.fz.us.base.util.LogUtil;
import com.joint.base.bean.EnumManage;
import com.joint.base.bean.SystemConfig;
import com.joint.base.entity.system.Admin;
import com.joint.base.mp.WxMpInCacheConfigStorage;
import com.joint.base.service.AdminService;
import com.joint.base.service.UsersService;
import com.joint.base.service.jms.AdvancedNotifyMessageProducer;
import com.joint.base.util.StrutsUtil;
import com.joint.web.shiro.Constants;
import com.joint.web.shiro.codec.HmacSHA256Utils;
import com.joint.web.shiro.realm.StatelessFormToken;
import fz.me.chanjar.weixin.common.api.WxConsts;
import fz.me.chanjar.weixin.mp.bean.WxMpCustomMessage;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信的绑定动作和用户验证
 *
 */
public class WxFormAuthcFilter extends AccessControlFilter {
    @Resource
    private ResultService resultService;
    @Resource
    private UsersService usersService;
    @Resource
    private AdminService adminService;
    @Resource
    private WxMpInCacheConfigStorage wxMpConfigStorage;
    @Resource
    private SystemConfig systemConfig;
    @Resource
    private AdvancedNotifyMessageProducer notifyMessageProducer;

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }
    //onAccessDenied：表示当访问拒绝时是否已经处理了；如果返回true表示需要继续处理；如果返回false表示该拦截器实例已经处理了，将直接返回即可。
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        System.out.println("进入WxFormAuthcFilter");
        //拿到用户名和密码
        String username = request.getParameter(Constants.APP_USERNAME);
        String password = request.getParameter(Constants.APP_PASSWORD);

        LogUtil.info(username + " " + password);
        //4、生成无状态Token
        StatelessFormToken token = new StatelessFormToken(username, password);
        Boolean flag=true;
        try {
            //5、委托给Realm进行登录
            Subject subject = getSubject(request, response);
            subject.login(token);
            flag=onLoginSuccess(token,subject,request, response);
        } catch (Exception e) {
            String error = null;
            if( e instanceof UnknownAccountException ){
                error = "用户名/密码错误";
            }else if(e instanceof IncorrectCredentialsException){
                error = "用户名/密码错误";
            }else if(e instanceof ExcessiveAttemptsException){
                error = "密码输入错误超过5次，请10分钟后再试";
            }else if(e instanceof AuthenticationException){
                error = "您的账号没有访问权限";
            }else{
                error = "账号异常";
            }
            onLoginFail(response,error); //6、登录失败
            return false;
        }
        return flag;
    }

    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        //LogUtil.info("{token}"+token.getPrincipal());
        //在这里设置cookie
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;
        StatelessFormToken formToken = (StatelessFormToken)token;
        String openId = request.getParameter(Constants.PARAM_OPENID);
        Admin admin = adminService.getByMobile((String) token.getPrincipal());
       // Users users = usersService.getByAdmin(admin);
        Result result = usersService.linkUser(httpServletResponse,admin, openId);
        if(result.getState() == 1){
            //
            //LogUtil.info("当前登录前的{u}"+StrutsUtil.getCookie(httpServletRequest, Constants.PARAM_USERNAME) + " {digest}" + StrutsUtil.getCookie(httpServletRequest, Constants.PARAM_DIGEST));
            //清除的是response的cookie，如果request的cookie还在，在connectMp中的Action还会存在，但是response已经删除了
            StrutsUtil.deleteCookie(httpServletRequest, httpServletResponse, Constants.PARAM_USERNAME);
            StrutsUtil.deleteCookie(httpServletRequest, httpServletResponse, Constants.PARAM_DIGEST);

            //生成cookie
            Map map = new HashMap<String,String>();
            map.put(Constants.PARAM_OPENID,openId);
            map.put(Constants.PARAM_USERNAME,formToken.getUsername());

            //response中生效
            StrutsUtil.addCookie(httpServletResponse, Constants.PARAM_DIGEST, HmacSHA256Utils.digest(admin.getSalt(), map));
            StrutsUtil.addCookie(httpServletResponse, Constants.PARAM_USERNAME, formToken.getUsername());
            //发送一个mp的客服消息，利用JMS，这里暂时测试用
            WxMpCustomMessage message = new WxMpCustomMessage();
            message.setMsgType(WxConsts.CUSTOM_MSG_TEXT);
            message.setToUser(openId);
            String link = "<a href=\""+wxMpConfigStorage.getHttpHost()+"/"+systemConfig.getWebroot()+"/mp/account!resetMp.action?_="+ Identities.randomBase62(8)+"\">设置我的账号</a>";
            message.setContent("[微笑] 您的工程泛联客账号成功绑定到该微信公众号，点击\n\n" + link + "\n\n可以查看和设置账号。");
            notifyMessageProducer.sendQueue(message, EnumManage.NotifyKeyEnum.wxMpCustomMessage.name());

        }else{
            String error="该帐号已绑定微信号";
            onLoginFail(response,error);
            return  false;
        }
        return true;
    }

    //登录失败时默认返回401状态码
    private void onLoginFail(ServletResponse response,String error) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        //如果账号密码为空，则认为是打开登录页面，返回true
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            // httpResponse.getWriter().write("登录失败了,可以返回JSON error");
            String type = "application/json";
            Result result = resultService.build(0,1,error,null);
            try {
                httpResponse.setContentType(type + ";charset=UTF-8");
                httpResponse.setHeader("Pragma", "No-cache");
                httpResponse.setHeader("Cache-Control", "no-cache");
                httpResponse.setDateHeader("Expires", 0);
                httpResponse.getWriter().write(resultService.toAjax(result));
                httpResponse.getWriter().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    //下面是继承来源于AccessControlFilter的方法重写
    /**
     * Simple default login URL equal to <code>/login.jsp</code>, which can be overridden by calling the
     * {@link #setLoginUrl(String) setLoginUrl} method.
     */
    public static final String DEFAULT_LOGIN_URL = "/login.jsp";

    /**
     * Constant representing the HTTP 'GET' request method, equal to <code>GET</code>.
     */
    public static final String GET_METHOD = "GET";

    /**
     * Constant representing the HTTP 'POST' request method, equal to <code>POST</code>.
     */
    public static final String POST_METHOD = "POST";

    /**
     * The login url to used to authenticate a user, used when redirecting users if authentication is required.
     */
    private String loginUrl = DEFAULT_LOGIN_URL;

    /**
     * Returns the login URL used to authenticate a user.
     * <p/>
     * Most Shiro filters use this url
     * as the location to redirect a user when the filter requires authentication.  Unless overridden, the
     * {@link #DEFAULT_LOGIN_URL DEFAULT_LOGIN_URL} is assumed, which can be overridden via
     * {@link #setLoginUrl(String) setLoginUrl}.
     *
     * @return the login URL used to authenticate a user, used when redirecting users if authentication is required.
     */
    public String getLoginUrl() {
        return loginUrl;
    }

    /**
     * Sets the login URL used to authenticate a user.
     * <p/>
     * Most Shiro filters use this url as the location to redirect a user when the filter requires
     * authentication.  Unless overridden, the {@link #DEFAULT_LOGIN_URL DEFAULT_LOGIN_URL} is assumed.
     *
     * @param loginUrl the login URL used to authenticate a user, used when redirecting users if authentication is required.
     */
    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    /**
     * Convenience method that acquires the Subject associated with the request.
     * <p/>
     * The default implementation simply returns
     * {@link SecurityUtils#getSubject() SecurityUtils.getSubject()}.
     *
     * @param request  the incoming <code>ServletRequest</code>
     * @param response the outgoing <code>ServletResponse</code>
     * @return the Subject associated with the request.
     */
    protected Subject getSubject(ServletRequest request, ServletResponse response) {
        return SecurityUtils.getSubject();
    }

    /**
     * Returns <code>true</code> if
     * {@link #isAccessAllowed(ServletRequest, ServletResponse,Object) isAccessAllowed(Request,Response,Object)},
     * otherwise returns the result of
     * {@link #onAccessDenied(ServletRequest, ServletResponse,Object) onAccessDenied(Request,Response,Object)}.
     *
     * @return <code>true</code> if
     *         {@link #isAccessAllowed(ServletRequest, ServletResponse, Object) isAccessAllowed},
     *         otherwise returns the result of
     *         {@link #onAccessDenied(ServletRequest, ServletResponse) onAccessDenied}.
     * @throws Exception if an error occurs.
     */
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        request.setAttribute(DefaultSubjectContext.SESSION_CREATION_ENABLED, Boolean.FALSE);
        return isAccessAllowed(request, response, mappedValue) || onAccessDenied(request, response, mappedValue);
    }

    /**
     * Returns <code>true</code> if the incoming request is a login request, <code>false</code> otherwise.
     * <p/>
     * The default implementation merely returns <code>true</code> if the incoming request matches the configured
     * {@link #getLoginUrl() loginUrl} by calling
     * <code>{@link #pathsMatch(String, String) pathsMatch(loginUrl, request)}</code>.
     *
     * @param request  the incoming <code>ServletRequest</code>
     * @param response the outgoing <code>ServletResponse</code>
     * @return <code>true</code> if the incoming request is a login request, <code>false</code> otherwise.
     */
    protected boolean isLoginRequest(ServletRequest request, ServletResponse response) {
        return pathsMatch(getLoginUrl(), request);
    }

    /**
     * Convenience method for subclasses to use when a login redirect is required.
     * <p/>
     * This implementation simply calls {@link #saveRequest(ServletRequest) saveRequest(request)}
     * and then {@link #redirectToLogin(ServletRequest, ServletResponse) redirectToLogin(request,response)}.
     *
     * @param request  the incoming <code>ServletRequest</code>
     * @param response the outgoing <code>ServletResponse</code>
     * @throws IOException if an error occurs.
     */
    protected void saveRequestAndRedirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        saveRequest(request);
        redirectToLogin(request, response);
    }

    /**
     * Convenience method merely delegates to
     * {@link WebUtils#saveRequest(ServletRequest) WebUtils.saveRequest(request)} to save the request
     * state for reuse later.  This is mostly used to retain user request state when a redirect is issued to
     * return the user to their originally requested url/resource.
     * <p/>
     * If you need to save and then immediately redirect the user to login, consider using
     * {@link #saveRequestAndRedirectToLogin(ServletRequest, ServletResponse)
     * saveRequestAndRedirectToLogin(request,response)} directly.
     *
     * @param request the incoming ServletRequest to save for re-use later (for example, after a redirect).
     */
    protected void saveRequest(ServletRequest request) {
        WebUtils.saveRequest(request);
    }

    /**
     * Convenience method for subclasses that merely acquires the {@link #getLoginUrl() getLoginUrl} and redirects
     * the request to that url.
     * <p/>
     * <b>N.B.</b>  If you want to issue a redirect with the intention of allowing the user to then return to their
     * originally requested URL, don't use this method directly.  Instead you should call
     * {@link #saveRequestAndRedirectToLogin(ServletRequest, ServletResponse)
     * saveRequestAndRedirectToLogin(request,response)}, which will save the current request state so that it can
     * be reconstructed and re-used after a successful login.
     *
     * @param request  the incoming <code>ServletRequest</code>
     * @param response the outgoing <code>ServletResponse</code>
     * @throws IOException if an error occurs.
     */
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        String loginUrl = getLoginUrl();
        WebUtils.issueRedirect(request, response, loginUrl);
    }
}
