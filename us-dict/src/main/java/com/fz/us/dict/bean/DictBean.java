package com.fz.us.dict.bean;

/**
 * Created with us-parent -> com.fz.us.dict.
 * User: min_xu
 * Date: 2014/9/10
 * Time: 21:45
 * 说明：
 * 1、定义系统字典的类型
 * 2、定义每一个系统字典中存在的关键key的信息
 * 3、系统字典增加一项是否可选项
 */
public class DictBean {
    /**
     * 系统字典
     */
    public static enum DictEnum {
        ClientType("客户性质", 1),
        ProInfoCategory("项目类别", 2),
        ProBackCategory("反馈类别",3),
        SuperKnowledgeType("知识大类",4),
        SubKnowledgeType("知识小类",5),
        ContractAttribute("合同属性",6),
        UseFunction("使用功能",7),
        ConstructionType("建筑工程类别",8),
        MunicipalEngineType("市政工程类别",9),
        WaterProjectType("水利工程类别",10),
        LandScapeProjectType("园林绿化工程类别",11),
        InstallProjectType("安装工程类别",12),
        PublicPipelineProjectType("公用管线工程类别",13),
        CivilDefenseEngineType("人防工程类别",14),
        FangXiuEngineType("房修工程类别",15),
        ConsultServiceType("咨询服务类型",16),
        ClientSort("客户分类",17),
        Capital("资金来源",18),
        CenterCapital("中央投资资金来源",19),
        UseofFoundsType("资金使用方式",20),
        ProjectType("工程类别",21),
        AgentType("代理类别",22),
        Industry("所属行业",23),
        TenderType("招标方式",24),
        TaskType("任务性质",25),
        ValuationType("计价类型",26),
        ValuationDepth("计价深度",27),
        ProjectPlate("所属项目板块",29),
        ConsultTask("后继咨询任务",30),
        SuperviseDepart("监管部门",31),
        ProjectNature("项目性质",32),
        DataListType("资料移交类型",33),
        WorkType("专业工种",34),
        NoticeMediaType("公告媒体库",35),
        Expert("评标专家库",36);
        private final String value;
        private final int key;

        private DictEnum(final String value, final int key) {
            this.value = value;
            this.key = key;
        }
        public String value() {
            return this.value;
        }
        public int key() {
            return this.key;
        }
    }



}
