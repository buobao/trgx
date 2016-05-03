package com.fz.us.dict.service;

import com.fz.us.base.service.BaseService;
import com.fz.us.dict.entity.DictSetting;

/**
 * Created with us-parent -> com.fz.us.dict.service.
 * User: min_xu
 * Date: 2014/9/10
 * Time: 10:12
 * 说明：
 */
public interface DictSettingService extends BaseService<DictSetting, String> {


    public DictSetting saveAndEnable(DictSetting dictSetting);

    public DictSetting addDefined(String dictId, String name, String companyId);

    public void enable(DictSetting dictSetting);

    public void disable(DictSetting dictSetting);

}

