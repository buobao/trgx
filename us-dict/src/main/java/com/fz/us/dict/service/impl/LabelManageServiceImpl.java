package com.fz.us.dict.service.impl;

import com.fz.us.base.dao.BaseDao;
import com.fz.us.base.service.impl.BaseServiceImpl;
import com.fz.us.dict.dao.LabelManageDao;
import com.fz.us.dict.entity.LabelManage;
import com.fz.us.dict.service.LabelManageService;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created with us-parent -> com.fz.us.dict.service.
 * User: min_xu
 * Date: 2014/9/10
 * Time: 10:10
 * 说明：
 */
@Repository
public class LabelManageServiceImpl extends BaseServiceImpl<LabelManage, String> implements LabelManageService {
    @Resource
    private LabelManageDao labelManageDao;
    @Override
    public BaseDao<LabelManage, String> getBaseDao() {
        return labelManageDao;
    }
}
