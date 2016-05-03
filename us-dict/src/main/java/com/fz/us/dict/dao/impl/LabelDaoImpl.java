package com.fz.us.dict.dao.impl;

import com.fz.us.base.dao.impl.BaseDaoImpl;
import com.fz.us.dict.bean.SystemLabel;
import com.fz.us.dict.dao.LabelDao;
import com.fz.us.dict.entity.Label;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;


/**
 * Created with us-parent -> com.fz.us.dict.dao.impl.
 * User: min_xu
 * Date: 2014/9/9
 * Time: 22:45
 * 说明：
 */
@Repository
public class LabelDaoImpl extends BaseDaoImpl<Label, String> implements LabelDao {

    public Label getLabel(String manageId,SystemLabel.LabelEnum labelEnum,String address){
        String hql = " from Label label where label.manageId = :manageId and label.labelEnum = :labelEnum and label.address = :address";
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("manageId",manageId);
        param.put("labelEnum",labelEnum);
        param.put("address",address);

        try {
            return (Label) getUniqueResult(hql,param);
        }catch (Exception e){
            return null;
        }
    }
}
