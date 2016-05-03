package com.fz.us.dict.service.impl;

import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.bean.Pager;
import com.fz.us.base.dao.BaseDao;
import com.fz.us.base.service.impl.BaseServiceImpl;
import com.fz.us.base.util.LogUtil;
import com.fz.us.dict.bean.DictBean;
import com.fz.us.dict.dao.DictDao;
import com.fz.us.dict.entity.Dict;
import com.fz.us.dict.entity.DictSetting;
import com.fz.us.dict.service.DictService;
import com.fz.us.dict.service.DictSettingService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with us-parent -> com.fz.us.dict.service.
 * User: min_xu
 * Date: 2014/9/10
 * Time: 10:10
 * 说明：
 */
@Repository
public class DictServiceImpl  extends BaseServiceImpl<Dict, String> implements DictService {
    @Resource
    private DictDao dictDao;

    @Override
    public BaseDao<Dict, String> getBaseDao() {
        return dictDao;
    }

    @Resource
    private DictSettingService dictSettingService;

    @Override
    public List<Dict> listDefaultAll(DictBean.DictEnum dictEnum) {
        return dictDao.listDefault(dictEnum, BaseEnum.StateEnum.Enable,BaseEnum.StateEnum.Disenable);
    }

    @Override
    public List<Dict> listDefaultEnable(DictBean.DictEnum dictEnum) {
        return dictDao.listDefault(dictEnum, BaseEnum.StateEnum.Enable);
    }

    @Override
    public List<Dict> listDefinedAll(DictBean.DictEnum dictEnum,String companyId) {
        BaseEnum.StateEnum[] states = new BaseEnum.StateEnum[]{BaseEnum.StateEnum.Enable,BaseEnum.StateEnum.Disenable};
        return dictDao.listDefined(dictEnum, companyId, BaseEnum.StateEnum.Enable, BaseEnum.StateEnum.Disenable);
    }

    @Override
    public List<Dict> listDefinedEnable(DictBean.DictEnum dictEnum,String companyId) {
        //BaseEnum.StateEnum[] states = new BaseEnum.StateEnum[]{BaseEnum.StateEnum.Enable};
        return dictDao.listDefined(dictEnum,companyId, BaseEnum.StateEnum.Enable);
    }

    @Override
    public List<Dict> listChildDefined(DictBean.DictEnum dictEnum, String companyId,String dictId, BaseEnum.StateEnum states) {
        return dictDao.listChildDefined(dictEnum,companyId,dictId,states);
    }

    @Override
    public String getDefinedName(String id, String companyId) {
        return dictDao.getDefinedName(id, companyId);
    }

    @Override
    public Dict getDefinedDict(String id, String companyId) {
        return dictDao.getDefinedDict(id, companyId);
    }


    @Override
    public List<Dict> listFormDefinedEnable(DictBean.DictEnum dictEnum,String companyId){
        return dictDao.listFormDefined(dictEnum, companyId, BaseEnum.StateEnum.Enable);
    }
    @Override
    public Dict saveAndEnable(Dict dict) {
        dict.setState(BaseEnum.StateEnum.Enable);
        if (dict.getId() == null && StringUtils.isEmpty(dict.getId())) {
            dict.setIsLeaf("true");
            if (dict.getSuperNode() == null || "".equals(dict.getSuperNode())) {
                dict.setLevel(0);
                dict.setSuperNode("0");
            } else {
                Dict parent = this.get(dict.getSuperNode());
                dict.setLevel(parent.getLevel() + 1);
                parent.setIsLeaf("false");
                this.update(parent);
            }
        }
        if(StringUtils.isEmpty(dict.getId())){
            save(dict);
        }else{
            update(dict);
        }
        return dict;
    }

    public Dict getByValue(DictBean.DictEnum dictEnum,String value){
        return dictDao.getByValue(dictEnum,value);
    }

    @Override
    public Dict getByName(DictBean.DictEnum dictEnum, String name) {
        return dictDao.getByName(dictEnum, name);
    }

    public Dict addDefault(DictBean.DictEnum dictEnum,String name,String value,int sortNo,int ifKey,String remark, String superNode){
        Dict dict = new Dict(dictEnum, name, value, sortNo, ifKey, remark, superNode);

        return saveAndEnable(dict);
    }

    public Dict addDefined(DictBean.DictEnum dictEnum,String name,String value,int sortNo,String remark ,String companyId, String superNode){
        Dict dict = new Dict(dictEnum, name, value, sortNo, 0, remark, companyId,superNode);
        return saveAndEnable(dict);
    }

    //companyId == null for default,defined时同时启用setting
    public void enable(Dict dict,String companyId){
        if(StringUtils.isNotEmpty(dict.getCompanyId())){
            //处理dictSetting
            dict.setState(BaseEnum.StateEnum.Enable);
            update(dict);
        }else{

        }
    }

    public void disable(Dict dict,String companyId){
        LogUtil.info(dict.getId() + " " + dict.getSortNo() + " " + companyId);
        if(StringUtils.isNotEmpty(dict.getCompanyId())){
            //处理dictSetting
            if(null != dict){
                dict.setState(BaseEnum.StateEnum.Disenable);
                update(dict);
            }
        }else{

        }
    }


    @Override
    public void sort(Dict dict,int sortNo){
        if(dict.getSortNo() != sortNo){
            dict.setSortNo(sortNo);
            update(dict);
        }
    }
    @Override
    public void sort(String sortList,String companyId){
        if(StringUtils.isNotEmpty(sortList)){
            JSONObject tempObject = JSONObject.fromObject(sortList);
            JSONArray sortArray = tempObject.getJSONArray("sortList");
            if(sortArray!=null&&sortArray.size()>0){
                for(int i=0;i<sortArray.size();i++){
                    JSONObject temp = sortArray.getJSONObject(i);
                    String keyId = temp.getString("id");
                    int sortNo = temp.getInt("sortNo");

                    Dict dict = get(keyId);
                    sort(dict,sortNo);
                }
            }
        }
    }
    @Override
    public void createAndEdit(String type,String keyId,String did,String name,String companyId,String dictName, String superNode){
        if(type.equals("save")){
            Dict dict = addDefined(DictBean.DictEnum.valueOf(dictName), name, null, 0, null, companyId, superNode);
        }else if(type.equals("edit")){
            //did
            Dict dict = get(keyId);
            if(dict.getCompanyId()!=null){
                dict.setName(name);
                saveAndEnable(dict);
            }
            //did
            if (StringUtils.isEmpty(did)){
                dictSettingService.addDefined(did,name,companyId);
            }else{
                DictSetting setting = dictSettingService.get(did);
                setting.setName(name);
                //setting.setState(StateEnum.Enable);
                dictSettingService.saveAndEnable(setting);
            }
        }

    }

    public void methodEnableAndDisabled(String type,String keyId,String did,String name,String companyId,String dictName){
        if(type.equals("disabled")){
//            Dict dict = get(keyId);
//            name = StringUtils.isNotEmpty(dict.getSname())?dict.getSname():dict.getName();
//            //disable(dict, companyId);
//            //did
//            if (StringUtils.isEmpty(did)){
//                //new disabled setting
//                DictSetting setting = dictSettingService.addDefined(keyId,name,companyId);
//                //dictSettingService.disable(setting);
//            }else{
//                DictSetting setting = dictSettingService.get(did);
//                //dictSettingService.disable(setting);
//            }
            disableSetting(keyId);
        }else if(type.equals("enable")){
            Dict dict = get(keyId);
            //enable(dict, companyId);
            if (StringUtils.isEmpty(did)){
                DictSetting setting = dictSettingService.addDefined(keyId,name,companyId);
                dictSettingService.enable(setting);
            }else{
                DictSetting setting = dictSettingService.get(did);
                dictSettingService.enable(setting);
            }
        }
    }

    private void disableSetting(String keyId){
        Dict dict = get(keyId);
        String name = StringUtils.isNotEmpty(dict.getSname())?dict.getSname():dict.getName();
        Map<String,Object> param = new HashMap<String, Object>();
        param.put("dictId", dict.getId());
        List<DictSetting> list = (List<DictSetting>) dictSettingService.findByPager(new Pager(0),param).getList();
        DictSetting setting;
        if (list != null && list.size() > 0){
            setting = list.get(0);
        }else{
            setting = dictSettingService.addDefined(keyId,name,dict.getCompanyId());
        }
        dictSettingService.disable(setting);

        Map<String,Object> param1 = new HashMap<String, Object>();
        param1.put("superNode",dict.getId());
        List<Dict> dictList = (List<Dict>) this.findByPager(new Pager(0),param1).getList();
        if(dictList != null && dictList.size()>0){
            for (Dict d:dictList){
                disableSetting(d.getId());
            }
        }
    }

    @Deprecated
    public String getName(String id,String name,String companyId){
        return getDefinedName(id,companyId);
    }



}
