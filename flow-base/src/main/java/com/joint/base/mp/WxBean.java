package com.joint.base.mp;

/**
 * Created with us2 -> com.fz.us.web.mp.
 * User: min_xu
 * Date: 2014-11-28
 * Time: 14:52
 * 说明：
 */
public class WxBean {
    public enum EventKeyEnum {
        account ("账号",1),
        active ("动态",2),
        feedback("反馈",3),
        version("版本",4),
        download("下载APP",5),
        location("定位",6),
        text("多客服",7),
        none("其他",8);

        private final String value;
        private final int key;


        private EventKeyEnum(final String value, final int key) {
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
