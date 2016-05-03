package com.joint.base.service.impl;

import com.fz.us.base.bean.Result;
import com.fz.us.base.dao.BaseDao;
import com.fz.us.base.service.common.ResultService;
import com.fz.us.base.service.impl.BaseServiceImpl;
import com.joint.base.bean.EnumManage;
import com.joint.base.dao.SmsDao;
import com.joint.base.entity.system.Sms;
import com.joint.base.service.SmsService;
import com.joint.base.service.jms.AdvancedNotifyMessageProducer;
import com.joint.base.util.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

/**
 * Service实现类 - 用户账户信息
 * ============================================================================
 * 版权所有 2013 qshihua。
 * ----------------------------------------------------------------------------
 * 
 * @author qshihua
 * 
 * @version 0.1 2013-05-06
 */

@Service
public class SmsServiceImpl extends BaseServiceImpl<Sms, String> implements SmsService {

	@Resource
	private SmsDao smsDao;
    @Resource
    private ResultService resultService;
    @Resource
    private AdvancedNotifyMessageProducer notifyMessageProducer;

    @Override
    public BaseDao<Sms, String> getBaseDao() {
        return smsDao;
    }


    public Result send(String message,String account){
        //发送短信
        Sms sms = new Sms(message, account, null);
        save(sms);
        notifyMessageProducer.sendQueue(sms, EnumManage.NotifyKeyEnum.sms.name());
        return resultService.build(1,0, "短信已发送，请注意查收(如果未收到请查看是否被360等软件拦截)！",null);
    }

    @Override
    public void saveAll(Set<Sms> items) {
        for (Sms item:items) {
            if (StringUtils.isNotEmpty(item.getId())){
                smsDao.update(item);
            } else {
                smsDao.save(item);
            }
        }

        System.out.println("-----------print ids start----------------");
        for (Sms item:items) {
            System.out.println(item.getId());
        }
    }

}