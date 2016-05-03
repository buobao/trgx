package com.joint.base.service;

import com.fz.us.base.service.BaseService;
import com.joint.base.entity.system.AuthCode;


/**
 * Service接口 - 验证码
 * ============================================================================
 * 版权所有 2013 qshihua。
 * ----------------------------------------------------------------------------
 * 
 * @author qshihua
 * 
 * @version 0.1 2013-05-06
 */

public interface AuthCodeService extends BaseService<AuthCode, String> {
	/**
	 * 保存并启用
	 * @param authCode
	 * @return
	 */
	public String saveAndEnable(AuthCode authCode);
	
	/**
	 * 生成验证码
	 * @param key
	 * @return
	 */
	public String createAuthCode(String key);

	
	/**
	 * 匹配验证码（有效时间5min）
	 * @param value
	 * @return
	 */
	public boolean isValid(String key, String value);
	
	/**
	 * 重置authCode
	 * @param key
	 * @return
	 */
	public boolean resetCode(String key);
}