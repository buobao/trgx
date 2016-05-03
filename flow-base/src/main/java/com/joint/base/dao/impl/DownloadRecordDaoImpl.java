package com.joint.base.dao.impl;

import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.bean.Pager;
import com.joint.base.dao.DownloadRecordDao;
import com.joint.base.entity.Company;
import com.joint.base.entity.DownloadRecord;
import com.joint.base.entity.Users;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/**
 * Dao实现类 - 用户账号Download日志
 * ============================================================================
 * 版权所有 2013 qshihua。
 * 
 * @author qshihua
 * @version 0.1 2013-05-06
 * ============================================================================
 */

@Repository
public class DownloadRecordDaoImpl extends BaseEntityDaoImpl<DownloadRecord, String> implements DownloadRecordDao {

    public Pager getRootPager(Pager pager,Users users,Company company){
        Assert.notNull(company,"company is null");
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DownloadRecord.class);
        if (company != null) {
            detachedCriteria.createAlias("company", "company");
            detachedCriteria.add(Restrictions.or(
                    Restrictions.eq("company", company),
                    Restrictions.eq("company.id", company.getId())));
        }
        if (users != null) {
            detachedCriteria.createAlias("creater", "creater");
            detachedCriteria.add(Restrictions.or(
                    Restrictions.eq("creater", users),
                    Restrictions.eq("creater.id", users.getId())));
        }
        detachedCriteria.add(Restrictions.isNull("parentId"));
        BaseEnum.StateEnum[] states = new BaseEnum.StateEnum[]{BaseEnum.StateEnum.Enable, BaseEnum.StateEnum.Disenable};
        //通用处理单元
        if(states!=null&&states.length>0){
            Criterion[] criterions = new Criterion[states.length];
            for(int i=0;i<states.length;i++){
                criterions[i]= Restrictions.eq("state", states[i]);
            }
            detachedCriteria.add(Restrictions.or(criterions));
        }
        return findByPager(pager,detachedCriteria);
    }
}