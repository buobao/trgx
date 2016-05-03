package com.fz.us.dict.service.impl;

import com.fz.us.base.dao.BaseDao;
import com.fz.us.base.service.impl.BaseServiceImpl;
import com.fz.us.dict.bean.SystemLabel;
import com.fz.us.dict.dao.LabelDao;
import com.fz.us.dict.entity.Label;
import com.fz.us.dict.service.LabelService;
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
public class LabelServiceImpl extends BaseServiceImpl<Label, String> implements LabelService {
    @Resource
    private LabelDao labelDao;
    @Override
    public BaseDao<Label, String> getBaseDao() {
        return labelDao;
    }

    public Label getLabel(String manageId,SystemLabel.LabelEnum labelEnum,String address){
        return labelDao.getLabel(manageId,labelEnum,address);
    }
}
