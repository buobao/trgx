package com.joint.base.entity;


import com.joint.base.parent.BaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Set;


/**
 * 实体类 - 业务配置表
 * ============================================================================
 * 版权所有 2014 hpj。
 * 
 * @author hpj
 * 
 * @version 0.1 2014-12-06
 * ============================================================================
 */
@Entity
@Table(name="sys_businessconfig")
public class BusinessConfig extends BaseEntity {
    /**
     *
     */
    private static final long serialVersionUID = 4685567410425008815L;

    private String processDefinitionId;
    /**
     * 权限发生变化的时候 0(未勾选)--所有文档更新，1(勾选)--老文档不更新
     */
    private int ptype;
    /**
     * 当勾选更新设置的时候，每次修改会保存新的业务配置信息，会生成不同的版本号，业务保存的时候会从业务配置表里面读取版本号，默认读取最新版本号
     */

    private int version;
    /**
     * 经办人是否可见，0不可见，1可见，默认可见
     */
    private int otype;
    /**
     * 流程配置
     */
    private Set<ProcessConfig> processConfigSet;
    /**
     * 申请者
     */
    private CommonConfig createConfig;
    /**
     * 读者
     */
    private CommonConfig readConfig;
    /**
     * 编辑者
     */
    private CommonConfig editConfig;
    /**
     * 归档后的读者
     */
    private CommonConfig docConfig;

    /**
     * 关联表
     * @return
     */
    private String tableKey;


    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getTableKey() {
        return tableKey;
    }

    public void setTableKey(String tableKey) {
        this.tableKey = tableKey;
    }

    public int getPtype() {
        return ptype;
    }

    public void setPtype(int ptype) {
        this.ptype = ptype;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getOtype() {
        return otype;
    }

    public void setOtype(int otype) {
        this.otype = otype;
    }


    @OneToOne(cascade=CascadeType.ALL)
    public CommonConfig getReadConfig() {
        return readConfig;
    }

    public void setReadConfig(CommonConfig readConfig) {
        this.readConfig = readConfig;
    }

    @OneToOne(cascade=CascadeType.ALL)
    public CommonConfig getEditConfig() {
        return editConfig;
    }

    public void setEditConfig(CommonConfig editConfig) {
        this.editConfig = editConfig;
    }

    @OneToOne(cascade=CascadeType.ALL)
    public CommonConfig getCreateConfig() {
        return createConfig;
    }

    public void setCreateConfig(CommonConfig createConfig) {
        this.createConfig = createConfig;
    }

    @OneToOne(cascade=CascadeType.ALL)
    public CommonConfig getDocConfig() {
        return docConfig;
    }

    public void setDocConfig(CommonConfig docConfig) {
        this.docConfig = docConfig;
    }

}
