package com.joint.base.service;


import com.joint.base.entity.BusinessConfig;
import com.joint.base.entity.Duty;
import com.joint.base.entity.Users;

import java.util.List;

/**
 * Service接口 - 管理员
 * ============================================================================
 * 版权所有 2011 苏州坤朋网络科技有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 
 * @author yan_zhou
 * 
 * @version 0.1 2011-6-13
 */

public interface BusinessConfigService extends BaseEntityService<BusinessConfig, String> {
    /**
     * 通过processDefintionId 来查找最新业务配置文档
     * @param processDefintionId
     * @return
     */
    public BusinessConfig getBusinessConfigByDefintionId(String processDefintionId);

    /**
     *通过processDefintionId 来查找最新业务配置文档
     * @param processDefintionId
     * @return
     */
    public List<BusinessConfig> findBCfgByDefintionId(String processDefintionId);
    /**
     * 查找不同版本的业务配置表集合
     * @param bcfg
     * @return
     */
    public List<BusinessConfig> findByCfg(BusinessConfig bcfg);

    /**
     * 通过processDefintionId,version 来查找最新业务配置文档
     * @param processDefintionId
     * @return
     */
    public BusinessConfig getBusinessConfigByDefintionId(String processDefintionId, int version);

    /**
     * 通过businessKey 来查找最新业务配置文档
     * @param businessKey
     * @return
     */
    public BusinessConfig getByBusinessKey(String businessKey);

    /**
     * 通过key查找不同版本流程定义对应的最新配置文档
     * @param businessKey
     * @return
     */
    public List<BusinessConfig> getConfigsByBusinessKey(String businessKey);

    /**
     * 通过businessKey，version 来查找业务配置文档
     * @param businessKey
     * @param version
     * @return
     */
    public BusinessConfig getByBusinessKey(String businessKey, int version);

    /**
     * 查看pcfg下面对应的文档阅览者
     * @param bcfg
     * todo delete
     * @return
     */
    public List<Duty> findDutyInDocRead(BusinessConfig bcfg);

    /**
     * 查看bcfg下面对应的归档后编辑者
     * @param bcfg
     * todo delete
     * @return
     */
    public List<Duty> findDutyInEdit(BusinessConfig bcfg);

    /**
     * 查看bcfg下面对应的归档后读者
     * @param bcfg
     * todo delete
     * @return
     */
    public List<Duty> findDutyInArchiveRead(BusinessConfig bcfg);

    /**
     *
     * @param loginer --- 用户
     * @param businessKey --- 流程名称
     * loginer是否在归档后的编辑者
     * todo delete
     * @return
     */
    public BusinessConfig getByUserAndBusinessInFEdit(Users loginer, String businessKey);


    /**
     * 查询可阅览的业务
     * @param user
     * todo delete
     * @return
     */
    public List<BusinessConfig> getDocByUser(BusinessConfig bcfg, Users user);
    /**
     * 根据编辑者查看业务
     * @param users
     * todo delete
     * @return
     */
    public List<BusinessConfig> getDocReadByUser(BusinessConfig bcfg, Users users);

    /**
     * 根据归档后的读者查看业务
     * @param users
     * todo delete
     * @return
     */
    public List<BusinessConfig> getDocEditByUser(BusinessConfig bcfg, Users users);

    /**
     * 查看users在group中的职责
     * @param users
     * todo delete
     * @return
     */
    public List<Duty> findDutyInGroup(BusinessConfig bcfg, Users users);

    /**
     * 查看users在Depart中的职责
     * @param users
     * todo delete
     * @return
     */
    public List<Duty> findDutyInDepart(BusinessConfig bcfg, Users users);

    /**
     * 查看users在power中的职责
     * @param users
     * todo delete
     * @return
     */
    public List<Duty> findDutyInPower(BusinessConfig bcfg, Users users);

    /**
     * 查看users在post中的职责
     * @param users
     * todo delete
     * @return
     */
    public List<Duty> findDutyInPost(BusinessConfig bcfg, Users users);

    /**
     * 查看users在cfg中的职责
     * @param users
     * todo delete
     * @return
     */
    public List<Duty> findDutyInCreateByConfig(BusinessConfig bcfg, Users users);

    /**
     * 查找业务配置表中对应的文档阅览者中群组
     * @param bcfg
     * todo delete
     * @return
     */
    public List<Duty> findDutyInDocReadAndGroup(BusinessConfig bcfg);

    /**
     * 查找业务配置表中对应的文档阅览者中部门
     * @param bcfg
     * todo delete
     * @return
     */
    public List<Duty> findDutyInDocReadAndDepart(BusinessConfig bcfg);

    /**
     * 查找业务配置表中对应的文档阅览者中岗位
     * @param bcfg
     * todo delete
     * @return
     */
    public List<Duty> findDutyInDocReadAndPost(BusinessConfig bcfg);

    /**
     * 查找业务配置表中对应的文档阅览者中职权
     * @param bcfg
     * todo delete
     * @return
     */
    public List<Duty> findDutyInDocReadAndPower(BusinessConfig bcfg);

    /**
     * 查找业务配置表中对应的归档后编辑者中群组
     * @param bcfg
     * todo delete
     * @return
     */
    public List<Duty> findDutyInEditAndGroup(BusinessConfig bcfg);

    /**
     * 查找业务配置表中对应的归档后编辑者中部门
     * @param bcfg
     * todo delete
     * @return
     */
    public List<Duty> findDutyInEditAndDepart(BusinessConfig bcfg);

    /**
     * 查找业务配置表中对应的归档后编辑者中岗位
     * @param bcfg
     * todo delete
     * @return
     */
    public List<Duty> findDutyInEditAndPost(BusinessConfig bcfg);

    /**
     * 查找业务配置表中对应的归档后编辑者中职权
     * @param bcfg
     * todo delete
     * @return
     */
    public List<Duty> findDutyInEditAndPower(BusinessConfig bcfg);


    /**
     * 查找业务配置表中对应的归档后编辑者中群组
     * @param bcfg
     * todo delete
     * @return
     */
    public List<Duty> findDutyInReadAndGroup(BusinessConfig bcfg);

    /**
     * 查找业务配置表中对应的归档后读者中部门
     * @param bcfg
     * todo delete
     * @return
     */
    public List<Duty> findDutyInReadAndDepart(BusinessConfig bcfg);

    /**
     * 查找业务配置表中对应的归档后读者中岗位
     * @param bcfg
     * todo delete
     * @return
     */
    public List<Duty> findDutyInReadAndPost(BusinessConfig bcfg);

    /**
     * 查找业务配置表中对应的归档后读者中职权
     * @param bcfg
     * todo delete
     * @return
     */
    public List<Duty> findDutyInReadAndPower(BusinessConfig bcfg);

    /**
     * 根据businessConfig，users查找在阅览者不同版本bcf的
     * @param bcfg
     * @param users
     *
     * @return
     */
    public List<BusinessConfig> findByBcfgAndUserInDoc(BusinessConfig bcfg, Users users);


    /**
     * 根据当前bcfg查找申请人的职责
     * 用于判断当前人是否具有新建权限
     * @param bcfg
     * @param users
     * @return
     */
    public List<Duty> findDutyByBcfgAndUserInCreate(BusinessConfig bcfg, Users users);

    /**
     * 根据businessConfig，users查找在归档编辑者权限不同版本bcf的
     * @param bcfg
     * @param users
     * @return
     */
    public List<BusinessConfig> findByBcfgAndUserInArchEdit(BusinessConfig bcfg, Users users);

    /**
     * 根据businessConfig，users查找在归档读者权限不同版本bcf的
     * @param bcfg
     * @param users
     * @return
     */
    public List<BusinessConfig> findByBcfgAndUserInArchRead(BusinessConfig bcfg, Users users);

}