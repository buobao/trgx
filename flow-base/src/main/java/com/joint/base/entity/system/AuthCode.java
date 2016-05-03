package com.joint.base.entity.system;

import com.fz.us.base.entity.Entity;

import javax.persistence.Table;

/**
 * Created with us-parent -> com.fz.us.core.entity.common.
 * User: min_xu
 * Date: 2014/9/28
 * Time: 16:02
 * 说明：
 */
@javax.persistence.Entity
@Table(name="ss_authcode")
public class AuthCode extends Entity {

    private static final long serialVersionUID = 2040694217488793004L;

    /**
     * 构造函数
     */
    public AuthCode() {
        super();
    }

    /**
     * 构造函数
     * @param
     * @param
     */
    public AuthCode(String codeKey, String codeValue) {
        super();
        this.codeKey = codeKey;
        this.codeValue = codeValue;
    }

//	/**
//	 * 返回对象属性Map集合
//	 */
//	public Map<String,Object> toMap() {
//		Map<String,Object> row = new HashMap<String,Object>();
//		row.put("name", name);
//		row.put("state", state.value());
//		row.put("id", super.getId());
//		return row;
//	}
    /**
     * 手机号/账号
     */
    private String codeKey;
    /**
     * 名称
     */
    private String codeValue;


    public String getCodeKey() {
        return codeKey;
    }

    public void setCodeKey(String codeKey) {
        this.codeKey = codeKey;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

}
