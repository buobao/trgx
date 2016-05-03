package com.joint.web.shiro.filter;

import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.bean.Result;
import com.fz.us.base.util.LogUtil;
import com.joint.base.entity.system.Admin;
import com.joint.base.service.AdminService;
import com.joint.base.service.UsersService;
import com.joint.base.util.StrutsUtil;
import com.joint.web.shiro.Constants;
import fz.me.chanjar.weixin.common.api.WxConsts;
import fz.me.chanjar.weixin.common.exception.WxErrorException;
import fz.me.chanjar.weixin.mp.api.WxMpService;
import fz.me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.web.util.UriUtils;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 微信的链接权限验证，所有的链接经过该拦截后，不通过的进入到登录界面
 */
public class WxAuthcCookieFilter extends AccessControlFilter {
    @Resource
    private AdminService adminService;
    @Resource
    private UsersService usersService;
    @Resource
    private WxMpService wxMpService;
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        System.out.println("进入WxAuthcCookieFilter");
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;

        //从登录过来的带有addCookie的response，在这里转化成resquest的cookie
        String openId = (String) httpServletRequest.getAttribute(Constants.SHIRO_AUTHC_WXOPENID);
        if(StringUtils.isEmpty(openId)){
            openId = StrutsUtil.getCookie(httpServletRequest, Constants.SHIRO_AUTHC_WXOPENID);
        }
        LogUtil.info("cookie openId -> " + openId);
        if(openId == null){
            String error = "正在获取微信授权验证";
            onLoginFail(response,error);
            return false;
        }
        //根据cookie获取admin，确保数据库的openId的Admin唯一
        Admin admin = adminService.getAdminByOpenId(openId);
//        if (null == admin) {
//            String error = "正在生成微信授权验证";
//            //将该用户注册成微信账号
//            //根据用户的openId注册一个新用户，检查是否注册
//            try {
//                WxMpUser wxMpUser = wxMpService.userInfo(openId, WxConsts.CN);
//                if(null != wxMpUser){
//                    Result result = usersService.registerWxUserAndAdmin(wxMpUser.getNickname(),openId);
//                    if(result.getState() == 1){
//                        admin = adminService.get(result.getId());
//                    }
//                }
//            }catch (WxErrorException e){
//                LogUtil.error(e.getMessage(), e);
//            }
//        }else{
//            String error = "";
//        }
        request.setAttribute(Constants.CURRENT_USER, admin);
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
    private void onLoginFail(ServletResponse response,String error) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.addHeader(Constants.SHIRO_AUTHC_ERROR,error);
        error = UriUtils.encodeQueryParam(error, "utf8");
        String login = getLoginUrl().indexOf("?")==-1?getLoginUrl()+"?error="+error:getLoginUrl()+"&error="+error;
        setLoginUrl(httpResponse.encodeURL(login));
        //LogUtil.info(login);
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
