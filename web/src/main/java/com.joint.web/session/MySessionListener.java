package com.joint.web.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by wei on 2015/11/17.
 */
@Component
@Transactional
public class MySessionListener implements SessionListener {
    @Override
    public void onStart(Session session) {//会话创建时触发
        System.out.println("会话创建：" + session.getId()+"，创建时间:"+session.getStartTimestamp());
    }
    @Override
    public void onExpiration(Session session) {//会话过期时触发
        System.out.println("会话过期：" + session.getId()+",过期时间:"+session.getTimeout());
    }
    @Override
    public void onStop(Session session) {//退出/会话过期时触发
        System.out.println("会话停止：" + session.getId()+",最后时间:"+session.getLastAccessTime());
    }
}
