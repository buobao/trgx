package com.joint.base.service.impl;

import com.fz.us.base.dao.BaseDao;
import com.fz.us.base.service.impl.BaseServiceImpl;
import com.joint.base.dao.AuthCodeDao;
import com.joint.base.entity.system.AuthCode;
import com.joint.base.service.AuthCodeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Locale;

/**
 * Service实现类 - duanxin
 * ============================================================================
 * 版权所有 2013 qshihua。
 * ----------------------------------------------------------------------------
 * 
 * @author qshihua
 * 
 * @version 0.1 2013-05-06
 */

@Service
public class AuthCodeServiceImpl extends BaseServiceImpl<AuthCode, String> implements AuthCodeService {

	@Resource
	private AuthCodeDao authCodeDao;
	@Override
	public BaseDao<AuthCode, String> getBaseDao() {
		return authCodeDao;
	}
	@Override
	public String saveAndEnable(AuthCode authCode) {
		return authCodeDao.save(authCode);
	}

	@Override
	public String createAuthCode(String key) {
		AuthCode authCode = null;
		String random = String.valueOf(Math.random());
		random = random.substring(random.indexOf(".")+1, random.indexOf(".")+7);
		authCode = authCodeDao.get("codeKey", key);
		if(authCode == null){
			authCode = new AuthCode();
			authCode.setCodeKey(key);
			authCode.setCodeValue(random);
			saveAndEnable(authCode);
		}else{
			authCode.setCodeValue(random);
			authCodeDao.update(authCode);
		}
		return random;
	}



	@Override
	public boolean isValid(String key, String value) {
		AuthCode authCode = null;
		authCode = authCodeDao.get("codeKey", key);
		if(authCode!=null){
			if(StringUtils.isNotEmpty(authCode.getCodeValue())&&StringUtils.equals(authCode.getCodeValue(), value)){
				Calendar calendar = Calendar.getInstance(Locale.CHINA);
				if(calendar.getTimeInMillis()-authCode.getModifyDate().getTime()<=15*60*1000){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean resetCode(String key) {
		AuthCode authCode = null;
		authCode = authCodeDao.get("codeKey", key);
		if(authCode!=null){
			authCode.setCodeValue(null);
			update(authCode);
			return true;
		}
		return false;
	}


}