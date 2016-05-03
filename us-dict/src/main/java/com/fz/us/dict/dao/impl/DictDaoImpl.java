package com.fz.us.dict.dao.impl;

import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.dao.impl.BaseDaoImpl;
import com.fz.us.base.dao.jdbc.JdbcDao;
import com.fz.us.dict.bean.DictBean;
import com.fz.us.dict.dao.DictDao;
import com.fz.us.dict.entity.Dict;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created with us-parent -> com.fz.us.dict.dao.impl.
 * User: min_xu
 * Date: 2014/9/9
 * Time: 22:45
 * 说明：
 */
@Repository
public class DictDaoImpl extends BaseDaoImpl<Dict, String> implements DictDao {

    @Resource
    private JdbcDao jdbcDao;

    @Override
    public List<Dict> listDefault(DictBean.DictEnum dictEnum,BaseEnum.StateEnum... states) {
        Assert.notNull(dictEnum ,"dictEnum is required");
        String hql = "from Dict model where model.dictKey = :dictEnum and (model.companyId is null or model.companyId = '') and model.state in (:states) order by model.sortNo asc";
        return getSession().createQuery(hql).setParameter("dictEnum", dictEnum).setParameterList("states", states).list();
        /*String stateHql = "";
        for(BaseEnum.StateEnum state:states){
            if(StringUtils.isEmpty(stateHql)){
                stateHql += " and ( model.state = '" +state.name() +"'";
            }else{
                stateHql += " or model.state = '"+ state.name()+"'";
            }
        }
        if(StringUtils.isNotEmpty(stateHql)){
            stateHql += ")";
        }
        String hql = "from Dict model where model.dictKey = :dictEnum and model.companyId is null "+stateHql+" order by model.sortNo asc";
        LogUtil.info(hql);
        return getSession().createQuery(hql).setParameter("dictEnum", dictEnum).list();*/

    }

    @Override
    public List<Dict> listDefined(DictBean.DictEnum dictEnum,String companyId,BaseEnum.StateEnum... states) {
        Assert.notNull(companyId ,"companyId is required");
        String sql = null;
        Map filter = new HashMap<String,String>();
        filter.put("companyId", companyId);
        filter.put("dictKey",dictEnum.name());
        filter.put("dictKey2",dictEnum.name());
        if (states != null && states.length == 1) {
            sql = "	 SELECT * from (" +
                    "SELECT dict.*,null as sid,null as sname,null as sstate,null as scompanyId from ss_dict as dict where dictKey = :dictKey and dict.state = 'Enable' and  id not in (" +
                    "SELECT dictId from ss_dictsetting as setting where setting.companyId = :companyId and (state = 'Enable' or state = 'Disenable')" +
                    ") " +
                    "UNION " +
                    "SELECT dict.*,dsetting.id as sid,dsetting.`name` as sname,dsetting.state as sstate,dsetting.companyId as scompanyId  from ss_dict as dict,(SELECT * from ss_dictsetting as setting where companyId = :companyId  and (state = 'Enable')) as dsetting  where dict.dictKey = :dictKey2 and dict.state = 'Enable' and dict.id in (" +
                    "	SELECT dictId from ss_dictsetting as setting where setting.companyId = :companyId  and (state = 'Enable')" +
                    ") and dict.id = dsetting.dictId " +
                    ") as uniDict where (ISNULL(companyId) OR companyId=:companyId OR companyId='') and (state = 'Enable' ) ORDER BY uniDict.sortNo asc" +
                    "";
        }else if(states != null && states.length == 2){
            sql = "	 SELECT * from (" +
                    "SELECT dict.*,null as sid,null as sname,null as sstate,null as scompanyId from ss_dict as dict where dictKey = :dictKey and  dict.state = 'Enable' and id not in (" +
                    "SELECT dictId from ss_dictsetting as setting where setting.companyId = :companyId and (state = 'Enable' or state = 'Disenable')" +
                    ") " +
                    "UNION " +
                    "SELECT dict.*,dsetting.id as sid,dsetting.`name` as sname,dsetting.state as sstate,dsetting.companyId as scompanyId  from ss_dict as dict,(SELECT * from ss_dictsetting as setting where companyId = :companyId  and (state = 'Enable' or state = 'Disenable')) as dsetting  where dict.dictKey = :dictKey2 and dict.state = 'Enable' and dict.id in (" +
                    "	SELECT dictId from ss_dictsetting as setting where setting.companyId = :companyId and (state = 'Enable' or state = 'Disenable')" +
                    ") and dict.id = dsetting.dictId " +
                    ") as uniDict where (ISNULL(companyId) OR companyId=:companyId OR companyId='') and (state = 'Enable' or state = 'Disenable') ORDER BY uniDict.sortNo asc" +
                    "";
        }
        return jdbcDao.query(sql, filter,new RowMapper<Dict>(){
            @Override
            public Dict mapRow(ResultSet rs, int rowNum) throws SQLException {
                // TODO Auto-generated method stub
                return rs2Dict(rs,rowNum);
            }
        });
    }

    public List<Dict> listFormDefined(DictBean.DictEnum dictEnum,String companyId,BaseEnum.StateEnum... states){
        Assert.notNull(companyId ,"companyId is required");
        String sql = null;
        Map filter = new HashMap<String,String>();
        filter.put("companyId", companyId);
        filter.put("dictKey",dictEnum.name());
        filter.put("dictKey2",dictEnum.name());
        if (states != null && states.length == 1) {
            sql = "	 SELECT * from (" +
                    "SELECT dict.*,null as sid,null as sname,null as sstate,null as scompanyId from ss_dict as dict where dictKey = :dictKey and dict.state = 'Enable' and dict.forceSelect = 0 and  id not in (" +
                    "SELECT dictId from ss_dictsetting as setting where setting.companyId = :companyId and (state = 'Enable' or state = 'Disenable')" +
                    ") " +
                    "UNION " +
                    "SELECT dict.*,dsetting.id as sid,dsetting.`name` as sname,dsetting.state as sstate,dsetting.companyId as scompanyId  from ss_dict as dict,(SELECT * from ss_dictsetting as setting where companyId = :companyId  and (state = 'Enable')) as dsetting  where dict.dictKey = :dictKey2 and dict.state = 'Enable' and dict.id in (" +
                    "	SELECT dictId from ss_dictsetting as setting where setting.companyId = :companyId  and (state = 'Enable')" +
                    ") and dict.id = dsetting.dictId " +
                    ") as uniDict where (ISNULL(companyId) OR companyId=:companyId OR companyId='') and (state = 'Enable' ) ORDER BY uniDict.sortNo asc" +
                    "";
        }else if(states != null && states.length == 2){
            sql = "	 SELECT * from (" +
                    "SELECT dict.*,null as sid,null as sname,null as sstate,null as scompanyId from ss_dict as dict where dictKey = :dictKey and  dict.state = 'Enable'  and dict.forceSelect = 0 and id not in (" +
                    "SELECT dictId from ss_dictsetting as setting where setting.companyId = :companyId and (state = 'Enable' or state = 'Disenable')" +
                    ") " +
                    "UNION " +
                    "SELECT dict.*,dsetting.id as sid,dsetting.`name` as sname,dsetting.state as sstate,dsetting.companyId as scompanyId  from ss_dict as dict,(SELECT * from ss_dictsetting as setting where companyId = :companyId  and (state = 'Enable' or state = 'Disenable')) as dsetting  where dict.dictKey = :dictKey2 and dict.state = 'Enable' and dict.id in (" +
                    "	SELECT dictId from ss_dictsetting as setting where setting.companyId = :companyId and (state = 'Enable' or state = 'Disenable')" +
                    ") and dict.id = dsetting.dictId " +
                    ") as uniDict where (ISNULL(companyId) OR companyId=:companyId OR companyId='') and (state = 'Enable' or state = 'Disenable') ORDER BY uniDict.sortNo asc" +
                    "";
        }
        return jdbcDao.query(sql, filter,new RowMapper<Dict>(){
            @Override
            public Dict mapRow(ResultSet rs, int rowNum) throws SQLException {
                // TODO Auto-generated method stub
                return rs2Dict(rs,rowNum);
            }
        });
    }

    @Override
    public List<Dict> listChildDefined(DictBean.DictEnum dictEnum, String companyId, String dictId, BaseEnum.StateEnum states) {
        Assert.notNull(dictEnum ,"dictEnum is required");
        Assert.notNull(companyId ,"companyId is required");
        String hql="from Dict model where model.dictKey = :dictEnum and model.companyId = :comId and model.superNode=:n and model.state = :state order by model.sortNo asc";
        return getSession().createQuery(hql).setParameter("dictEnum", dictEnum).setParameter("comId",companyId).setParameter("n",dictId).setParameter("state", states).list();
    }

    @Override
    public String getDefinedName(String id, String companyId) {
        Assert.notNull(id ,"id is required");
        Assert.notNull(companyId ,"companyId is required");
        String name = get(id).getName();
        String sql = "select name as sname from ss_dictsetting WHERE companyId = :companyId and dictId = :id";
        //String sql = "select dict.name as name,setting.name as sname from  ss_dictsetting as setting,ss_dict as dict WHERE setting.companyId = :companyId and setting.dictId = dict.id and dict.id = :id";
        Map<String,String> filter = new HashMap<String,String>();
        filter.put("id", id);
        filter.put("companyId", companyId);
        try {
            Map map = jdbcDao.queryForMap(sql, filter);
            if(map != null){
                if(StringUtils.isNotEmpty((String)map.get("sname"))){
                    return (String)map.get("sname");
                }
                return name;
            }
        }catch (Exception e){
            return name;
        }
        return "";
    }

    @Override
    public Dict getDefinedDict(String id, String companyId) {

        return null;
    }

    public Dict getByValue(DictBean.DictEnum dictEnum,String value){
        Assert.notNull(dictEnum ,"dictEnum is required");
        String hql = "from Dict model where model.dictKey = :dictEnum and (model.companyId is null or model.companyId = '') and model.value = :value and model.state = :state order by model.sortNo asc";
        return (Dict)getSession().createQuery(hql).setParameter("dictEnum", dictEnum).setParameter("value", value).setParameter("state", BaseEnum.StateEnum.Enable).uniqueResult();
    }

    @Override
    public Dict getByName(DictBean.DictEnum dictEnum, String name) {
        Assert.notNull(dictEnum ,"dictEnum is required");
        String hql = "from Dict model where model.dictKey = :dictEnum  and model.name = :name and model.state = :state order by model.sortNo asc";
        return (Dict)getSession().createQuery(hql).setParameter("dictEnum", dictEnum).setParameter("name", name).setParameter("state", BaseEnum.StateEnum.Enable).uniqueResult();
    }

    public Dict rs2Dict(ResultSet rs, int rowNum) throws SQLException {
        Dict dict = new Dict();

        dict.setIsLeaf(rs.getString("isLeaf"));
        dict.setLevel(rs.getInt("level"));
        dict.setSuperNode(rs.getString("superNode"));
        dict.setState(BaseEnum.StateEnum.valueOf(rs.getString("state")));

        dict.setName(rs.getString("name"));
        dict.setId(rs.getString("id"));
        dict.setDictKey(DictBean.DictEnum.valueOf(rs.getString("dictKey")));
        dict.setNo(rs.getString("no"));
        dict.setSortNo(rs.getInt("sortNo"));
        dict.setValue(rs.getString("value"));
        dict.setIfKey(rs.getInt("ifKey"));
        dict.setForceSelect(rs.getInt("forceSelect"));
        dict.setRemark(rs.getString("remark"));
        dict.setCompanyId(StringUtils.isEmpty(rs.getString("companyId"))?null:rs.getString("companyId"));

        dict.setScompanyId(rs.getString("scompanyId"));
        dict.setSid(StringUtils.isEmpty(rs.getString("sid")) ? "" : rs.getString("sid"));
        dict.setSstate(rs.getString("sstate")==null?null:BaseEnum.StateEnum.valueOf(rs.getString("sstate")));
        dict.setSname(StringUtils.isEmpty(rs.getString("sname"))?rs.getString("name"):rs.getString("sname"));
        return dict;
    }
}
