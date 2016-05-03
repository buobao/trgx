package com.fz.us.dict.service.impl;

import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.dao.BaseDao;
import com.fz.us.base.service.impl.BaseServiceImpl;
import com.fz.us.dict.dao.DictSettingDao;
import com.fz.us.dict.entity.DictSetting;
import com.fz.us.dict.service.DictSettingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created with us-parent -> com.fz.us.dict.service.impl.
 * User: min_xu
 * Date: 2014/9/10
 * Time: 10:14
 * 说明：
 */
@Service
public class DictSettingServiceImpl extends BaseServiceImpl<DictSetting, String> implements DictSettingService {
    @Resource
    private DictSettingDao dictSettingDao;

    @Override
    public BaseDao<DictSetting, String> getBaseDao() {
        return dictSettingDao;
    }

    @Override
    public DictSetting saveAndEnable(DictSetting dictSetting) {
        dictSetting.setState(BaseEnum.StateEnum.Enable);
        if(StringUtils.isEmpty(dictSetting.getId())){
            save(dictSetting);
        }else{
            update(dictSetting);
        }
        return dictSetting;
    }
    @Override
    public DictSetting addDefined(String dictId,String name,String companyId){
        DictSetting dictSetting = new DictSetting(dictId,name,companyId);

        return saveAndEnable(dictSetting);
    }

    public void enable(DictSetting dictSetting){
        dictSetting.setState(BaseEnum.StateEnum.Enable);
        update(dictSetting);
    }

    public void disable(DictSetting dictSetting){
        dictSetting.setState(BaseEnum.StateEnum.Disenable);
        update(dictSetting);
    }
}