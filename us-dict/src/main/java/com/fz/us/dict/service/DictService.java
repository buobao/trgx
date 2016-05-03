package com.fz.us.dict.service;

import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.service.BaseService;
import com.fz.us.dict.bean.DictBean;
import com.fz.us.dict.entity.Dict;

import java.util.List;

/**
 * Created with us-parent -> com.fz.us.dict.service.
 * User: min_xu
 * Date: 2014/9/10
 * Time: 10:09
 * 说明：
 */
public interface DictService extends BaseService<Dict,String> {

    public List<Dict> listDefaultAll(DictBean.DictEnum dictEnum);

    public List<Dict> listDefaultEnable(DictBean.DictEnum dictEnum);

    public List<Dict> listDefinedAll(DictBean.DictEnum dictEnum, String companyId);

    public List<Dict> listDefinedEnable(DictBean.DictEnum dictEnum, String companyId);

    public List<Dict> listChildDefined(DictBean.DictEnum dictEnum, String companyId,String dictId ,BaseEnum.StateEnum states);

    public String getDefinedName(String id, String companyId);

    public Dict getDefinedDict(String id, String companyId);

    /**
     * 根据关键字取得系统字典
     * */
    public Dict getByValue(DictBean.DictEnum dictEnum, String value);

    /**
     * 根据名字取得系统字典
     * */
    public Dict getByName(DictBean.DictEnum dictEnum, String name);
    /**
     * 读取可被用户在业务表单中选择的列表
     * */
    public List<Dict> listFormDefinedEnable(DictBean.DictEnum dictEnum, String companyId);

    public Dict saveAndEnable(Dict dict);


    public Dict addDefault(DictBean.DictEnum dictEnum, String name, String value, int sortNo, int ifKey, String remark, String SuperNode);

    public Dict addDefined(DictBean.DictEnum dictEnum, String name, String value, int sortNo, String remark, String companyId, String SuperNode);

    //companyId == null for default,defined时同时启用setting
    public void enable(Dict dict, String companyId);

    public void disable(Dict dict, String companyId);

    public void sort(Dict dict, int sortNo);

    public void sort(String sortList, String companyId);

    @Deprecated
    public void createAndEdit(String type, String keyId, String did, String name, String companyId, String dictName, String SuperNode);
    @Deprecated
    public void methodEnableAndDisabled(String type, String keyId, String did, String name, String companyId, String dictName);
    @Deprecated
    public String getName(String id, String name, String companyId);
}
