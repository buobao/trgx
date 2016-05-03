
package com.joint.base.bean.event;

import com.fz.us.base.bean.BaseEvent;
import com.joint.base.entity.module.Resume;


/**
 * 履历的生成事件
 * */
public class ResumeEvent extends BaseEvent {
    public ResumeEvent(Resume resume) {
        super(resume);
    }
}
