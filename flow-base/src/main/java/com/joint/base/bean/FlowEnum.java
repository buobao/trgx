package com.joint.base.bean;

public class FlowEnum {
    /**
     * 流程配置的状态
     */
    public enum ProcessState{
        Running("流转中",1),
        Backed("退回中",5),
        Finished("已归档",2),
        Deny("被否决",3),
        Destroy("被废止",6),
        Draft("草稿",4);

        private final String value;
        private final int key;

        ProcessState(final String value, final int key) {
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
