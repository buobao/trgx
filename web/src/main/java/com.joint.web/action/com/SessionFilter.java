package com.joint.web.action.com;

import com.fz.us.base.util.LogUtil;
import com.joint.web.action.BaseAdminAction;
import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Created by amin on 2015/5/12.
 */
public class SessionFilter extends BaseAdminAction implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest res=(HttpServletRequest) servletRequest;
        HttpServletResponse resp=(HttpServletResponse)servletResponse;
        HttpSession session= ((HttpServletRequest) servletRequest).getSession();
        String[] paths={"login.action","sendexperienceAuthCode.action","register.action","sendAuthCode.action","reset.action","file!readDoc.action","index.action","file.action","file!getFileName.action","knowledge!webview.action","knowledge!fileview.action"};
        String path=res.getServletPath();
        LogUtil.info("sessionFilter" + path);
        if(!StringUtils.endsWithAny(path, paths) && !res.isRequestedSessionIdValid()){
            LogUtil.info("进入判断");
            resp.setContentType("text/html;charset=utf-8");
            PrintWriter out =servletResponse.getWriter();
            out.println("<script language='javascript'>");
            out.println("alert('您由于长时间没有操作浏览器,请重新登陆');window.location.href='login.action'");
            out.println("</script>");
        }
        else{
            filterChain.doFilter(servletRequest,servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
