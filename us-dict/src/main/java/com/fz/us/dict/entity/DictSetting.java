package com.fz.us.dict.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created with us-parent -> com.fz.us.dict.entity.
 * User: min_xu
 * Date: 2014/9/10
 * Time: 10:06
 * 说明：
 */
@Entity
@Table(name="ss_dictsetting")
public class DictSetting extends BaseEntity {
    private static final long serialVersionUID = -370316274269471216L;

    public DictSetting(){
        super();
    }

    public DictSetting(String dictId,String name,String companyId){
        super();

        this.setDictId(dictId);
        this.setName(name);
        this.setCompanyId(companyId);
    }

    /**
     * 编号
     */
    private String no;

    /**
     * 系统字典对应的Id
     */
    private String dictId;
    /**
     * 系统字典对应的class
     */
    private String clazz;

    public String getNo() {
        return no;
    }
    public void setNo(String no) {
        this.no = no;
    }
    public String getDictId() {
        return dictId;
    }
    public void setDictId(String dictId) {
        this.dictId = dictId;
    }
    public String getClazz() {
        return clazz;
    }
    public void setClazz(String clazz) {
        this.clazz = clazz;
    }
}
