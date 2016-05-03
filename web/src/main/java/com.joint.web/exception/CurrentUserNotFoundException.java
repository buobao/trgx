package com.joint.web.exception;

/**
 * Created with us2 -> com.fz.us.core.
 * User: min_xu
 * Date: 2014-11-27
 * Time: 14:03
 * 说明：
 */
public class CurrentUserNotFoundException extends Exception {
    public CurrentUserNotFoundException(){
        super();
    }

    public CurrentUserNotFoundException(String msg) {
        super(msg);
    }

    public CurrentUserNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public CurrentUserNotFoundException(Throwable cause) {
        super(cause);
    }

}
