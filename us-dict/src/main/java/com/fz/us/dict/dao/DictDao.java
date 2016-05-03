package com.fz.us.dict.dao;

import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.dao.BaseDao;
import com.fz.us.dict.bean.DictBean;
import com.fz.us.dict.entity.Dict;

import java.util.List;

/**
 * Created with us-parent -> com.fz.us.dict.dao.
 * User: min_xu
 * Date: 2014/9/9
 * Time: 22:45
 * 说明：
 */
public interface DictDao extends BaseDao<Dict, String> {

    public List<Dict> listDefault(DictBean.DictEnum dictEnum, BaseEnum.StateEnum... states);

    public List<Dict> listDefined(DictBean.DictEnum dictEnum, String companyId, BaseEnum.StateEnum... states);

    public List<Dict> listFormDefined(DictBean.DictEnum dictEnum, String companyId, BaseEnum.StateEnum... states);

    public List<Dict> listChildDefined(DictBean.DictEnum dictEnum, String companyId,String dictId,BaseEnum.StateEnum states);

    public String getDefinedName(String id, String companyId);

    public Dict getDefinedDict(String id, String companyId);

    public Dict getByValue(DictBean.DictEnum dictEnum, String value);

    /**
     * @add by zhucx
     * 根据名字获得对象
     * @param dictEnum
     * @param value
     * @return
     */
    public Dict getByName(DictBean.DictEnum dictEnum, String name);
}