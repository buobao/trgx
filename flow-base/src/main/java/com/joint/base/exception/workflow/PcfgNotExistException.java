package com.joint.base.exception.workflow;

/**
 * 业务配置信息不存在
 * Created by hpj on 2015/4/8.
 */
public class PcfgNotExistException extends FlowException{

    public PcfgNotExistException() {
        super("500", null);
    }
}
