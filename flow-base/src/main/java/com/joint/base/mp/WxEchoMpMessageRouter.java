package com.joint.base.mp;

import com.joint.base.service.UsersService;
import fz.me.chanjar.weixin.mp.api.WxMpMessageHandler;
import fz.me.chanjar.weixin.mp.api.WxMpMessageInterceptor;
import fz.me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import fz.me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

/**
 * Created with us2 -> com.fz.us.web.mp.
 * User: min_xu
 * Date: 2014-11-28
 * Time: 15:38
 * 说明：直接覆盖原有的Router,使用该Router增加对Spring的支持
 */
@Service
public class WxEchoMpMessageRouter
{
    private final List<Rule> rules = new ArrayList<Rule>();

    private final ExecutorService es = Executors.newCachedThreadPool();

    @Resource
    private WxMpInCacheConfigStorage wxMpConfigStorage;
    @Resource
    private UsersService usersService;
    @Resource
    private WxEchoMpMessageHandler wxEchoMpMessageHandler;

    /**
     * 开始一个新的Route规则
     * @return
     */
    public Rule rule() {
        return new Rule(this);
    }

    /**
     * 处理微信消息
     * @param wxMessage
     */
    public WxMpXmlOutMessage route(final WxMpXmlMessage wxMessage) {
        final List<Rule> matchRules = new ArrayList<Rule>();
        // 收集匹配的规则
        for (final Rule rule : rules) {
            if (rule.test(wxMessage)) {
                matchRules.add(rule);
                if(!rule.reEnter) {
                   break;
                }
            }
        }
        if (matchRules.size() == 0) {
            return null;
        }

        /*if (matchRules.get(0).async) {
            // 只要第一个是异步的，那就异步执行
            // 在另一个线程里执行
            es.submit(new Runnable() {
                public void run() {
                    for (final Rule rule : matchRules) {
                        rule.service(wxMessage);
                    }
                }
            });
            return null;
        }*/

        WxMpXmlOutMessage res = null;
        for (final Rule rule : matchRules) {
            // 返回最后一个匹配规则的结果
            /*res = rule.service(wxMessage);
            if (!rule.reEnter) {
                break;
            }*/
            // 返回最后一个非异步的rule的执行结果
            if(rule.async) {
                es.submit(new Runnable() {
                    public void run() {
                        rule.service(wxMessage);
                    }
                });
            } else {
                res = rule.service(wxMessage);
            }
        }
        return res;
    }

    public class Rule {

        private final WxEchoMpMessageRouter routerBuilder;

        private boolean async = true;

        private String msgType;

        private String event;

        private String eventKey;

        private String content;

        private String rContent;

        private boolean reEnter = false;

        private List<WxMpMessageHandler> handlers = new ArrayList<WxMpMessageHandler>();

        private List<WxMpMessageInterceptor> interceptors = new ArrayList<WxMpMessageInterceptor>();

        protected Rule(WxEchoMpMessageRouter routerBuilder) {
            this.routerBuilder = routerBuilder;
        }

        /**
         * 设置是否异步执行，默认是true
         * @param async
         * @return
         */
        public Rule async(boolean async) {
            this.async = async;
            return this;
        }

        /**
         * 如果msgType等于某值
         * @param msgType
         * @return
         */
        public Rule msgType(String msgType) {
            this.msgType = msgType;
            return this;
        }

        /**
         * 如果event等于某值
         * @param event
         * @return
         */
        public Rule event(String event) {
            this.event = event;
            return this;
        }

        /**
         * 如果eventKey等于某值
         * @param eventKey
         * @return
         */
        public Rule eventKey(String eventKey) {
            this.eventKey = eventKey;
            return this;
        }

        /**
         * 如果content等于某值
         * @param content
         * @return
         */
        public Rule content(String content) {
            this.content = content;
            return this;
        }

        /**
         * 如果content匹配该正则表达式
         * @param regex
         * @return
         */
        public Rule rContent(String regex) {
            this.rContent = regex;
            return this;
        }

        /**
         * 设置微信消息拦截器
         * @param interceptor
         * @return
         */
        public Rule interceptor(WxMpMessageInterceptor interceptor) {
            return interceptor(interceptor, (WxMpMessageInterceptor[]) null);
        }

        /**
         * 设置微信消息拦截器
         * @param interceptor
         * @param otherInterceptors
         * @return
         */
        public Rule interceptor(WxMpMessageInterceptor interceptor, WxMpMessageInterceptor... otherInterceptors) {
            this.interceptors.add(interceptor);
            if (otherInterceptors != null && otherInterceptors.length > 0) {
                for (WxMpMessageInterceptor i : otherInterceptors) {
                    this.interceptors.add(i);
                }
            }
            return this;
        }

        /**
         * 设置微信消息处理器
         * @param handler
         * @return
         */
        public Rule handler(WxMpMessageHandler handler) {
            return handler(handler, (WxMpMessageHandler[]) null);
        }

        /**
         * 设置微信消息处理器
         * @param handler
         * @param otherHandlers
         * @return
         */
        public Rule handler(WxMpMessageHandler handler, WxMpMessageHandler... otherHandlers) {
            this.handlers.add(handler);
            if (otherHandlers != null && otherHandlers.length > 0) {
                for (WxMpMessageHandler i : otherHandlers) {
                    this.handlers.add(i);
                }
            }
            return this;
        }

        /**
         * 规则结束，代表如果一个消息匹配该规则，那么它将不再会进入其他规则
         * @return
         */
        public WxEchoMpMessageRouter end() {
            this.routerBuilder.rules.add(this);
            return this.routerBuilder;
        }

        /**
         * 规则结束，但是消息还会进入其他规则
         * @return
         */
        public WxEchoMpMessageRouter next() {
            this.reEnter = true;
            return end();
        }

        protected boolean test(WxMpXmlMessage wxMessage) {
            return
                    (this.msgType == null || this.msgType.equals(wxMessage.getMsgType()))
                            &&
                            (this.event == null || this.event.equals(wxMessage.getEvent()))
                            &&
                            (this.eventKey == null || this.eventKey.equals(wxMessage.getEventKey()))
                            &&
                            (this.content == null || this.content.equals(wxMessage.getContent() == null ? null : wxMessage.getContent().trim()))
                            &&
                            (this.rContent == null || Pattern.matches(this.rContent, wxMessage.getContent() == null ? "" : wxMessage.getContent().trim()))
                    ;
        }

        /**
         * 处理微信推送过来的消息
         * @param wxMessage
         * @return true 代表继续执行别的router，false 代表停止执行别的router
         */
        protected WxMpXmlOutMessage service(WxMpXmlMessage wxMessage) {
            Map<String, Object> context = new HashMap<String, Object>();
            // 如果拦截器不通过
            for (WxMpMessageInterceptor interceptor : this.interceptors) {
                if (!interceptor.intercept(wxMessage, context)) {
                    return null;
                }
            }

            // 交给handler处理
            WxMpXmlOutMessage res = null;
            for (WxMpMessageHandler handler : this.handlers) {
                // 返回最后handler的结果
                //LogUtil.info(("{getHttpHost}"+wxMpConfigStorage.getHttpHost()));
                res = wxEchoMpMessageHandler.handle((WxEchoMpMessageHandler)handler,wxMessage, context);

            }
            return res;
        }

    }
}
