package com.joint.base.util;

import com.fz.us.base.util.LogUtil;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationManager;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.dispatcher.Dispatcher;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 工具类 - Struts
 * ============================================================================

 * ============================================================================
 */

public class StrutsUtil {
	/**
	 * Cookieの追加
	 * @return
	 * @throws Exception
	 */
	public static void addCookie(HttpServletResponse response,String name,String value){
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(60*60*24*365);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	/**
	 * Cookieの、删除
	 * @return
	 * @throws Exception
	 */
	public static void deleteCookie(HttpServletRequest request,HttpServletResponse response,String name){
		Cookie[] cookies = request.getCookies();
		if(cookies != null){
			for(Cookie cookie : cookies)
			{
				LogUtil.info("size:" + cookies.length + "cookie{getName()}:" + cookie.getName() + " {value}:" + cookie.getValue());
				if(cookie.getName().equals(name))
				{
					cookie.setMaxAge(0);
					cookie.setValue("");
					cookie.setPath("/");
					response.addCookie(cookie);
					//return cookie.getValue();
				}
			}
		}
	}
	/**
	 * Cookieの取得
	 * @return
	 * @throws Exception
	 */
	public static String getCookie(HttpServletRequest request,String name){
		Cookie[] cookies = request.getCookies();
		if(cookies != null){
			for(Cookie cookie : cookies)
			{
				if(cookie.getName().equals(name))
				{
					return cookie.getValue();
				}
			}
		}
		return null;
	}
	/**
	 * 获取Struts2配置信息.
	 * 
	 * @return Struts2 Configuration
	 */
	public static Configuration getConfiguration() {
		Dispatcher dispatcher = Dispatcher.getInstance();
		ConfigurationManager configurationManager = dispatcher.getConfigurationManager();
		return configurationManager.getConfiguration();
	}

	/**
	 * 获取所有namespace名称.
	 * 
	 * @return namespace名称的集合
	 */
	public static Set<String> getAllNamespaces() {
		Set<String> namespaces = new HashSet<String>();
		Configuration configuration = getConfiguration();
		Map<String, Map<String, ActionConfig>> actionConfigs = configuration.getRuntimeConfiguration().getActionConfigs();
		for (String namespace : actionConfigs.keySet()) {
			namespaces.add(namespace);
		}
		return namespaces;
	}

	/**
	 * 获取所有Action名称.
	 * 
	 * @return Action名称的集合
	 */
	public static Set<String> getAllActionName() {
		Set<String> actionNames = new HashSet<String>();
		Configuration configuration = getConfiguration();
		Map<String, Map<String, ActionConfig>> actionConfigs = configuration.getRuntimeConfiguration().getActionConfigs();
		for (String namespace : actionConfigs.keySet()) {
			Map<String, ActionConfig> actionConfigMap = actionConfigs.get(namespace);
			for (String actionName : actionConfigMap.keySet()) {
				actionNames.add(actionName);
			}
		}
		return actionNames;
	}

	/**
	 * 获取所有Action类名称(不包含com.opensymphony.xwork2.ActionSupport类).
	 * 
	 * @return Action类名称的集合
	 */
	public static Set<String> getAllActionClassName() {
		Set<String> actionClassNames = new HashSet<String>();
		Configuration configuration = getConfiguration();
		Map<String, Map<String, ActionConfig>> actionConfigs = configuration.getRuntimeConfiguration().getActionConfigs();
		for (String namespace : actionConfigs.keySet()) {
			Map<String, ActionConfig> actionConfigMap = actionConfigs.get(namespace);
			for (String actionName : actionConfigMap.keySet()) {
				String actionClassName = actionConfigMap.get(actionName).getClassName();
				if (!StringUtils.equals(actionClassName, "com.opensymphony.xwork2.ActionSupport")) {
					actionClassNames.add(actionClassName);
				}
			}
		}
		return actionClassNames;
	}

	/**
	 * 获取所有Action类.
	 * 
	 * @return Action类的集合
	 */
	@SuppressWarnings("unchecked")
	public static Set<Class> getAllActionClass() {
		Set<Class> actionClasss = new HashSet<Class>();
		Configuration configuration = getConfiguration();
		Map<String, Map<String, ActionConfig>> actionConfigs = configuration.getRuntimeConfiguration().getActionConfigs();
		for (String namespace : actionConfigs.keySet()) {
			Map<String, ActionConfig> actionConfigMap = actionConfigs.get(namespace);
			for (String actionName : actionConfigMap.keySet()) {
				Class actionClass = actionConfigMap.get(actionName).getClass();
				actionClasss.add(actionClass);
			}
		}
		return actionClasss;
	}

/**
 * 获取Action类 Method.
 *
 * @return Action类的集合
 */
    /*public String getAllActionMethod() throws ClassNotFoundException {
        String actionClassName = logConfig.getActionClassName();
        Set<String> allActionClassName = StrutsUtil.getAllActionClassName();
        if (allActionClassName.contains(actionClassName)) {
            Class actionClass = Class.forName(actionClassName);
            Method[] methods = actionClass.getDeclaredMethods();
            StringBuilder stringBuilder = new StringBuilder();
            List<LogConfig> logConfigs = logConfigService.getLogConfigList(actionClassName);
            String[] methodNameArray = new String[logConfigs.size()];
            for (int i = 0; i < logConfigs.size(); i++) {
                methodNameArray[i] = logConfigs.get(i).getActionMethodName();
            }
            for (Method method : methods) {
                if (method.getReturnType() == String.class && !ArrayUtils.contains(methodNameArray, method.getName())) {
                    stringBuilder.append("<option value=\"" + method.getName() + "\">" + method.getName() + "</option>");
                }
            }
            if (stringBuilder.length() == 0) {
                stringBuilder.append("<option value=\"noValue\">无可用方法</option>");
            }
            return ajaxText(stringBuilder.toString());
        }
        return null;
    }*/

}