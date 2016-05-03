package com.joint.web.quartz;

public interface Job {
    public void evalExpire();

    public void doProAttendDeal();

    public void doTaskRemind();
}