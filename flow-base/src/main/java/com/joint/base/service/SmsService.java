package com.joint.base.service;

import com.fz.us.base.bean.Result;
import com.fz.us.base.service.BaseService;
import com.joint.base.entity.system.Sms;

import java.util.Set;

/**
 * Service接口 - 用户账户信息
 * ============================================================================
 * 版权所有 2013 qshihua。
 * ----------------------------------------------------------------------------
 * 
 * @author qshihua
 * 
 * @version 0.1 2013-05-06
 */

public interface SmsService extends BaseService<Sms, String> {


    public Result send(String message, String account);

    public void saveAll(Set<Sms> items);
}