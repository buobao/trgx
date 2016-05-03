package com.fz.us.dict.bean;

/**
 * Bean类 - 系统配置
 * ============================================================================
 * 版权所有 2013 qshihua。
 * 
 * @author qshihua
 * @version 0.1 2013-1-16
 * ============================================================================
 */

public class SystemLabel {
    public static enum ManageEnum {
        normal("标准版", 1),
        normal_en("标准英文版",2);

        private final String value;
        private final int key;

        private ManageEnum(final String value, final int key) {
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

    //销售七步的字段简写，用于对比class - > address
    public static enum FormEnum {
        client("客户", 1),
        linkman("联系人", 2),
        reservation("预约", 3),
        negotiation("洽谈",4),
        business("业务",5),
        contract("成交",6),
        proceedsplan("履约",7);

        private final String value;
        private final int key;

        private FormEnum(final String value, final int key) {
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

    public static enum LabelEnum {
        form("表单", 1),
        view("视图", 2),
        menu("菜单", 3);

        private final String value;
        private final int key;

        private LabelEnum(final String value, final int key) {
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