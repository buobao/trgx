package com.joint.base.mp;

import com.fz.us.base.util.LogUtil;
import com.joint.base.bean.SystemConfig;
import com.joint.base.service.LocationsService;
import com.joint.base.service.UsersService;
import fz.me.chanjar.weixin.common.api.WxConsts;
import fz.me.chanjar.weixin.mp.api.WxMpMessageHandler;
import fz.me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import fz.me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import fz.me.chanjar.weixin.mp.bean.WxMpXmlOutTextMessage;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created with miner -> me.chanjar.weixin.mp.api.
 * User: min_xu
 * Date: 2014-10-31
 * Time: 14:25
 * 说明：//这里需要重写一个Handle到Spring的XML配置
 *
 * 处理微信消息，并放回一个结果
 */

public class WxEchoMpMessageHandler implements WxMpMessageHandler {

    @Resource
    private WxMpInCacheConfigStorage wxMpConfigStorage;
    @Resource
    private SystemConfig systemConfig;
    @Resource
    private UsersService usersService;
    @Resource
    private LocationsService locationsService;

    public WxBean.EventKeyEnum getEventKeyEnum() {
        return eventKeyEnum;
    }

    public void setEventKeyEnum(WxBean.EventKeyEnum eventKeyEnum) {
        this.eventKeyEnum = eventKeyEnum;
    }

    private WxBean.EventKeyEnum eventKeyEnum;

    public WxEchoMpMessageHandler() {

    }
    public WxEchoMpMessageHandler(WxBean.EventKeyEnum eventKeyEnum){
        this.eventKeyEnum = eventKeyEnum;
    }

    public WxEchoMpMessageHandler getHandler(WxBean.EventKeyEnum eventKeyEnum){
        WxEchoMpMessageHandler handler = new WxEchoMpMessageHandler(eventKeyEnum);
        return handler;
    }

    public WxMpXmlOutMessage handle(WxEchoMpMessageHandler handler,WxMpXmlMessage wxMessage, Map<String, Object> context) {
        eventKeyEnum = handler.getEventKeyEnum();
        String content = "";
        if(StringUtils.equals(eventKeyEnum.name(), WxBean.EventKeyEnum.account.name())){
            content = usersService.getMpAccount(null, wxMessage.getFromUserName());
        }else if(StringUtils.equals(eventKeyEnum.name(), WxBean.EventKeyEnum.active.name())){
//           content = usersService.getMpActive(null, wxMessage.getFromUserName());
        }else if(StringUtils.equals(eventKeyEnum.name(), WxBean.EventKeyEnum.feedback.name())){
            content = getFeedback();
        }else if(StringUtils.equals(eventKeyEnum.name(), WxBean.EventKeyEnum.version.name())){
            content = getVersion();
        }else if(StringUtils.equals(eventKeyEnum.name(), WxBean.EventKeyEnum.download.name())) {
            content = getDownload();
        }else if(StringUtils.equals(eventKeyEnum.name(), WxBean.EventKeyEnum.location.name())) {
            LogUtil.info("wechat locate"+wxMessage.getLatitude());
//            Locations locations = new Locations();
//            locations.setLatitude(new BigDecimal(wxMessage.getLatitude()));
//            locations.setLongitude(new BigDecimal(wxMessage.getLongitude()));
//            locations.setOpenId(wxMessage.getFromUserName());
//            locations.setRadius(new BigDecimal(wxMessage.getPrecision()));
//            locations.setCoordType(EnumManage.CoordTypeEnum.wgs84);
//            locationsService.save(locations);
            return null;
        }else if(StringUtils.equals(eventKeyEnum.name(), WxBean.EventKeyEnum.text.name())) {
            //多客服消息
            WxMpXmlOutTextMessage m = WxMpXmlOutMessage.TEXT()
                    .content(content)
                    .fromUser(wxMessage.getToUserName())
                    .toUser(wxMessage.getFromUserName())
                    .build();
            m.setMsgType(WxConsts.XML_TRANSFER_CUSTOMER_SERVICE);
            return m;
        }else{
            if(StringUtils.equals(wxMessage.getEvent(), WxConsts.EVT_SUBSCRIBE)){
                content = getSubscribe(wxMessage.getFromUserName());
            }else{
                content = "你好！";
            }
        }
        WxMpXmlOutTextMessage m
                = WxMpXmlOutMessage.TEXT()
                .content(content)
                .fromUser(wxMessage.getToUserName())
                .toUser(wxMessage.getFromUserName())
                .build();
        //LogUtil.info("{WxMpXmlOutMessage}"+m.toXml());
        return m;
    }

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context) {
        LogUtil.info("{eventKeyEnum.name()}" + eventKeyEnum.name());
        String content = "";
        if(StringUtils.equals(eventKeyEnum.name(), WxBean.EventKeyEnum.account.name())){
           content = usersService.getMpAccount(null, wxMessage.getFromUserName());
        }else if(StringUtils.equals(eventKeyEnum.name(), WxBean.EventKeyEnum.active.name())){
//            content = usersService.getMpActive(null, wxMessage.getFromUserName());
        }else if(StringUtils.equals(eventKeyEnum.name(), WxBean.EventKeyEnum.feedback.name())){
            content = getFeedback();
        }else if(StringUtils.equals(eventKeyEnum.name(), WxBean.EventKeyEnum.version.name())){
            content = getVersion();
        }else if(StringUtils.equals(eventKeyEnum.name(), WxBean.EventKeyEnum.download.name())){
            content = getDownload();
        }else{
            if(StringUtils.equals(wxMessage.getEvent(), WxConsts.EVT_SUBSCRIBE)){
                content = getSubscribe(wxMessage.getFromUserName());
            }else{
                content = "你好！";
            }
        }
        WxMpXmlOutTextMessage m
                = WxMpXmlOutMessage.TEXT()
                .content(content)
                .fromUser(wxMessage.getToUserName())
                .toUser(wxMessage.getFromUserName())
                .build();
        LogUtil.info("{WxMpXmlOutMessage}" + m.toXml());
        return m;
    }

    /**
     *
     * */
    public String getVersion(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("当前版本：" + wxMpConfigStorage.getVersion()).append("\n");
        //buffer.append("查看更多请点击:  <a href=\"" + wxMpConfigStorage.getHttpHost()+"/"+systemConfig.getWebroot()+"/mp/update.html\">更新日志</a>").append("\n");
        return buffer.toString();
    }

    /**
     * 您好！感谢您的关注。
     验证身份（超链接到手机绑定页面）即可使用“智慧销售”微信版所有功能。
     进入首页（超链接到首页）可了解更多销售7步内容。
     * */
    public String getSubscribe(String openId){
        StringBuffer buffer = new StringBuffer();
        buffer.append("[微笑] 您好，感谢您的关注。").append("\n\n");
        buffer.append("请进入“我的-帐号”进行帐号绑定。").append("\n\n");
        //buffer.append("验证身份<a href=\""+wxMpConfigStorage.getHttpHost()+"/"+systemConfig.getWebroot()+"/mp/login.action?openId="+openId+"\">（超链接到手机绑定页面）</a>即可使用“工程泛联客”微信版所有功能。").append("\n\n");
       // buffer.append("进入首页<a href=\""+wxMpConfigStorage.getHttpHost()+"/"+systemConfig.getWebroot()+"/mp/homepage!index.action\">（超链接到首页）</a>可了解更多内容。").append("\n\n");
        buffer.append("如有任何疑问，请直接回复留言给我们。").append("\n");

        return buffer.toString();
    }
    /**
     * 下载的消息提示
     * */
    public String getDownload(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("工程泛联客 安卓客户端已经在91市场、豌豆荚、google play、应用宝(手机qq动态内)上线，您也可以点击下面链接下载：").append("\n\n");
        buffer.append("<a href=\"http://a.app.qq.com/o/simple.jsp?pkgname=com.sales.app&g_f=991653\">应用宝直接下载</a>").append("\n");
        return buffer.toString();
    }
    /**
     * 反馈
     * */
    public String getFeedback(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("[微笑] 请直接回复您的建议，我们将尽快解决！").append("\n\n");
        return buffer.toString();
    }

}
