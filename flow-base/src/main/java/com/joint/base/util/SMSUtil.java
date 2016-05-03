package com.joint.base.util;


import com.fz.us.base.util.CommonUtil;
import com.joint.base.entity.system.Sms;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;


/**
 * 短信发送器，文字内部不要包含签名，并且按照模板来实现
 * */
public class SMSUtil {
	public static Logger logger = LoggerFactory.getLogger(SMSUtil.class);

	public static boolean SENDOPEN = false;	//是否开启短信发送
	
	//GET /Services/MsgSend.asmx/SendMsg?userCode=string&userPass=string&DesNo=string&Msg=string&Channel=string HTTP/1.1
	//Host: 121.199.48.186:1210
	
	/*public static String host = "yes.itissm.com";
	public static String url = "/api/MsgSend.asmx/SendMsg";
	public static int port = 80;
	public static String accountId = "shjd";	//
	public static String password = "shjd5858";
	*/
//	public static String host = "h.1069106.com:1210";
//	public static String url = "/services/msgsend.asmx/SendMsg";
//	public static int port = 1210;
//    public static String accountId = "shjdxx";	//
//    public static String password = "123456";
//	public static String channel = "0";	//企业代码 服务器代码 10657109082938001 002
//	public static String title = "【工程泛联客】";
//	private static final long RECONNECT_TIME = 1 * 1000;
//	private static int closedCount = 0;

	public static String uri="http://222.73.117.158/msg/";
	public static String accountId="Gcflk888";
	public static String password = "Gcflk888";
	public static String title = "【玖达信息】";
	public static boolean needstatus = true;//是否需要状态报告，需要true，不需要false
	public static String product = "349312826";//产品ID
	public static String extno = null;//扩展码


	public static String httpSend(Sms sms) throws Exception {
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod();
		String photoNumber=sms.getPhone();
		if(CommonUtil.isMobile(photoNumber)){
			try {
				URI base = new URI(uri, false);
				method.setURI(new URI(base, "HttpBatchSendSM", false));
				method.setQueryString(new NameValuePair[] {
						new NameValuePair("account", accountId),
						new NameValuePair("pswd", password),
						new NameValuePair("mobile", sms.getPhone()),
						new NameValuePair("needstatus", String.valueOf(needstatus)),
						new NameValuePair("msg", sms.getMsg()),
						new NameValuePair("product", product),
						new NameValuePair("extno", extno),
				});
				int result = client.executeMethod(method);
				if (result == HttpStatus.SC_OK) {
					InputStream in = method.getResponseBodyAsStream();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = in.read(buffer)) != -1) {
						baos.write(buffer, 0, len);
					}
					return URLDecoder.decode(baos.toString(), "UTF-8");
				} else {
					throw new Exception("HTTP ERROR Status: " + method.getStatusCode() + ":" + method.getStatusText());
				}
			} finally {
				method.releaseConnection();
			}
		}
		return null;

	}


//	public static String httpSend(Sms sms) throws Exception{
//		String phoneNumber = sms.getPhone();
//		if(CommonUtil.isMobile(phoneNumber)){
//			String msgText = sms.getMsg() + title;
//			LogUtil.info("{ msgText }"+msgText);
//			String param = "userCode="+accountId+"&userPass="+password+"&DesNo="+phoneNumber+"&Msg="+msgText+"&Channel="+channel;
//			String uri = "http://" + host + ":" + port + url + "?"+param;
//			LogUtil.info(uri);
//			HttpUriRequest httpGet = new HttpGet(uri);
//			CloseableHttpResponse response = HttpClientUtil.getHttpClient().execute(httpGet);
//			String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
//			return responseContent;
//		}
//		return null;
//	}

	public static void main(String[] args) throws Exception {
		Sms sms = new Sms("您的验证码为：123456，请在15分钟内提交验证。", "18301785732", null);
		System.out.println(httpSend(sms));

	}
    
	//私有的默认构造子
    private SMSUtil() {}
 

    public static String getSmsText(){
    	
    	return "";
    }
}
