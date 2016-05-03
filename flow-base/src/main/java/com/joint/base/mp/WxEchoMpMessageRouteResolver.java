package com.joint.base.mp;

import com.fz.us.base.util.LogUtil;
import fz.me.chanjar.weixin.common.api.WxConsts;

/**
 * Created with miner -> me.chanjar.weixin.mp.api.
 * User: min_xu
 * Date: 2014-10-31
 * Time: 14:25
 * 说明：//处理微信消息的路由配置
 */
public class WxEchoMpMessageRouteResolver {
    public WxEchoMpMessageRouteResolver() {

    }

    public WxEchoMpMessageHandler getWxEchoMpMessageHandler() {
        return wxEchoMpMessageHandler;
    }

    public void setWxEchoMpMessageHandler(WxEchoMpMessageHandler wxEchoMpMessageHandler) {
        this.wxEchoMpMessageHandler = wxEchoMpMessageHandler;
    }

    private WxEchoMpMessageHandler wxEchoMpMessageHandler;
    private WxEchoMpMessageRouter wxEchoMpMessageRouter;
    public boolean isAsync() {
        return async;
    }
    public void setAsync(boolean async) {
        this.async = async;
    }
    private boolean async;

    public void prepareRoute(){
        LogUtil.info("prepare rule handler,all rules defined here");
        getWxEchoMpMessageRouter()
            .rule() //账号
                .async(async)
                .msgType(WxConsts.XML_MSG_EVENT)
                .eventKey(WxBean.EventKeyEnum.account.name())
                .handler(new WxEchoMpMessageHandler(WxBean.EventKeyEnum.account))
                .end()
                .rule() //主动上报定位消息
                .async(async)
                .msgType(WxConsts.XML_MSG_EVENT)
                .event(WxConsts.EVT_LOCATION)
                .handler(new WxEchoMpMessageHandler(WxBean.EventKeyEnum.location))
                .end()
                .rule() //普通定位信息
                .async(async)
                .msgType(WxConsts.XML_MSG_LOCATION)
                .event(WxConsts.XML_MSG_EVENT)
                .handler(new WxEchoMpMessageHandler(WxBean.EventKeyEnum.location))
                .end()

                .rule() //动态
                .async(async)
                .msgType(WxConsts.XML_MSG_EVENT)
                .eventKey(WxBean.EventKeyEnum.active.name())
                .handler(new WxEchoMpMessageHandler(WxBean.EventKeyEnum.active))
                .end()
                .rule() //反馈
                .async(async)
                .msgType(WxConsts.XML_MSG_EVENT)
                .eventKey(WxBean.EventKeyEnum.feedback.name())
                .handler(new WxEchoMpMessageHandler(WxBean.EventKeyEnum.feedback))
                .end()
                .rule() //版本
                .async(async)
                .msgType(WxConsts.XML_MSG_EVENT)
                .eventKey(WxBean.EventKeyEnum.version.name())
                .handler(new WxEchoMpMessageHandler(WxBean.EventKeyEnum.version))
                .end()
                .rule() //下载
                .async(async)
                .msgType(WxConsts.XML_MSG_EVENT)
                .eventKey(WxBean.EventKeyEnum.download.name())
                .handler(new WxEchoMpMessageHandler(WxBean.EventKeyEnum.download))
                .end()
                .rule() //关注
                .async(async)
                .msgType(WxConsts.XML_MSG_EVENT)
                .event(WxConsts.EVT_SUBSCRIBE)
                .handler(new WxEchoMpMessageHandler(WxBean.EventKeyEnum.none))
                .end()

                .rule() //多客服
                .async(async)
                .msgType(WxConsts.XML_MSG_TEXT)
                .handler(new WxEchoMpMessageHandler(WxBean.EventKeyEnum.text))
                .end()
        ;
    }

    public WxEchoMpMessageRouter getWxEchoMpMessageRouter() {
        return wxEchoMpMessageRouter;
    }
    public void setWxEchoMpMessageRouter(WxEchoMpMessageRouter wxEchoMpMessageRouter) {
        this.wxEchoMpMessageRouter = wxEchoMpMessageRouter;
    }
}
