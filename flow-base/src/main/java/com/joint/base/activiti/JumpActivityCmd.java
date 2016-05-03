package com.joint.base.activiti;

/**
 * @author: Henry Yan
 */

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;

public class JumpActivityCmd implements Command<Object> {
    private String activityId;
    private String processInstanceId;
    private int numStatus;
    /**
     * jumpOrigin:退回原因
     */
    private String jumpOrigin;

    public JumpActivityCmd(String processInstanceId, String activityId,int numStatus) {
        this(processInstanceId, activityId, "jump",numStatus);
    }

    public JumpActivityCmd(String processInstanceId, String activityId, String jumpOrigin,int numStatus) {
        this.activityId = activityId;
        this.processInstanceId = processInstanceId;
        this.jumpOrigin = jumpOrigin;
        this.numStatus = numStatus;
    }

    public Object execute(CommandContext commandContext) {

        ExecutionEntity executionEntity = commandContext.getExecutionEntityManager().findExecutionById(processInstanceId);
        executionEntity.setVariable("numStatus",numStatus);
        executionEntity.destroyScope(jumpOrigin);

        ProcessDefinitionImpl processDefinition = executionEntity.getProcessDefinition();
        ActivityImpl activity = processDefinition.findActivity(activityId);

        executionEntity.executeActivity(activity);

        return executionEntity;
    }
}
