package com.fz.us.dict.service;

import com.fz.us.base.service.BaseService;
import com.fz.us.dict.bean.SystemLabel;
import com.fz.us.dict.entity.Label;

/**
 * Created with us-parent -> com.fz.us.dict.service.
 * User: min_xu
 * Date: 2014/9/10
 * Time: 10:09
 * 说明：
 */
public interface LabelService extends BaseService<Label,String> {
    public Label getLabel(String manageId, SystemLabel.LabelEnum labelEnum, String address);

}
