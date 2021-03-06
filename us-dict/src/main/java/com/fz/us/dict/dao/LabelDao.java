package com.fz.us.dict.dao;

import com.fz.us.base.dao.BaseDao;
import com.fz.us.dict.bean.SystemLabel;
import com.fz.us.dict.entity.Label;

/**
 * Created with us-parent -> com.fz.us.dict.dao.
 * User: min_xu
 * Date: 2014/9/9
 * Time: 22:45
 * 说明：
 */
public interface LabelDao extends BaseDao<Label, String> {
    public Label getLabel(String manageId, SystemLabel.LabelEnum labelEnum, String address);
}