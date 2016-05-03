package com.joint.web.shiro.filter;


import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.util.LogUtil;
import com.joint.base.bean.SystemConfig;
import com.joint.base.mp.WxMpInCacheConfigStorage;
import com.joint.base.service.AdminService;
import com.joint.base.service.UsersService;
import com.joint.base.util.StrutsUtil;
import com.joint.web.shiro.Constants;
import com.joint.web.shiro.realm.StatelessToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.web.util.UriUtils;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信的链接权限验证，所有的链接经过该拦截后，不通过的进入到登录界面
 * 微信采用cookie验证方案，使用username，经过加密的code（使用salt+openId），在登录的时候生成两个cookie
 */
public class WxAuthcFilter extends AccessControlFilter {
    @Resource
    private AdminService adminService;
    @Resource
    private UsersService usersService;
    @Resource
    private SystemConfig systemConfig;
    @Resource
    private WxMpInCacheConfigStorage wxMpInCacheConfigStorage;

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    //onAccessDenied：表示当访问拒绝时是否已经处理了；如果返回true表示需要继续处理；如果返回false表示该拦截器实例已经处理了，将直接返回即可。
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        System.out.println("进入WxAuthFilter");
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;

        //检查是否微信账号直接登录
        if(checkWxUsersSession(httpServletRequest, httpServletResponse)){
            return true;
        }

        //从登录过来的带有addCookie的response，在这里转化成resquest的cookie
        String clientDigest = StrutsUtil.getCookie(httpServletRequest, Constants.PARAM_DIGEST);
        String username = StrutsUtil.getCookie(httpServletRequest, Constants.PARAM_USERNAME);
        LogUtil.info("{digest}"+clientDigest);


        //通过Admin的openId来实现
        String openId = "";
        if(StringUtils.isNotEmpty(username))openId = usersService.openId(username);
        if(StringUtils.isEmpty(openId)){
            LogUtil.error("{ openId not found}");
            String error = "用户名/密码错误";
            onLoginFail(request,response,error);
            return false;
        }

        LogUtil.info("{openId}"+openId);
        Map params = new HashMap<String,String>();
        params.put(Constants.PARAM_OPENID,openId);
        params.put(Constants.PARAM_USERNAME,username);

        //4、生成无状态Token
        StatelessToken token = new StatelessToken(username, params, clientDigest);
        try {
            //5、委托给Realm进行登录
            Subject subject = getSubject(request, response);
            subject.login(token);
        } catch (Exception e) {
            String error = null;
            if( e instanceof UnknownAccountException ){
                error = "用户名/密码错误，请重新登录验证";
            }else if(e instanceof IncorrectCredentialsException){
                error = "登录信息发生变更，请重新登录验证";
            }else{
                error = "登录信息发生变更，请重新登录验证";
            }
            onLoginFail(request,response,error); //6、登录失败
            //saveRequestAndRedirectToLogin(request, response);
            return false;
        }
        return true;
    }

    private boolean checkWxUsersSession(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse){
        //生成request的username
        HttpSession session = httpServletRequest.getSession();
        String id = (String) session.getAttribute(Constants.SHIRO_AUTHC_WXSESSION);
        if(StringUtils.isNotEmpty(id)){
            httpServletRequest.setAttribute(Constants.ISCURRENT_WXUSER, BaseEnum.AccountEnum.WEIXIN.name());
            httpServletRequest.setAttribute(Constants.CURRENT_USER, adminService.get(id));
            return true;
        }
        return false;
    }

    //登录失败时默认返回401状态码
    private void onLoginFail(ServletRequest request,ServletResponse response,String error) throws IOException, ServletException {
        LogUtil.info("error:"+error);
        //HttpServletResponse httpResponse = (HttpServletResponse) response;
        //httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.addHeader(Constants.SHIRO_AUTHC_ERROR,error);
        error = UriUtils.encodeQueryParam(error, "utf8");

      //  String login = getLoginUrl().indexOf("?")==-1?getLoginUrl()+"?error="+error:getLoginUrl()+"&error="+error;
        String login = getLoginUrl();
//        String login =wxMpInCacheConfigStorage.getOauth2redirectUri()+"?error="+error;
        LogUtil.info("{login}" + login);
        setLoginUrl(httpResponse.encodeURL(login));

//        request.getRequestDispatcher(login).forward(request, response);
      //  LogUtil.info(login);
        httpResponse.sendRedirect(login);
    }

    //下面是继承来源于AccessControlFilter的方法重写
    /**
     * Simple default login URL equal to <code>/login.jsp</code>, which can be overridden by calling the
     * {@link #setLoginUrl(String) setLoginUrl} method.
     */
    public static final String DEFAULT_LOGIN_URL = "login.action";

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
        request.setAttribute(DefaultSubjectContext.SESSION_CREATION_ENABLED, Boolean.TRUE);
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
