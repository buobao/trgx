package com.joint.base.util;

import com.fz.us.base.util.DataUtil;
import fz.me.chanjar.weixin.common.util.http.Utf8ResponseHandler;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.cookie.*;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BaiduMapUtil {
	//百度地图请求
	public static final String BAIDUKEY = "C3a71f7e660f174ddcbf0ef71d8a3764";

	public static CloseableHttpClient getHttpClient(){
		BasicCookieStore cookieStore = new BasicCookieStore();
		CookieSpecProvider easySpecProvider = new CookieSpecProvider() {
			public CookieSpec create(HttpContext context) {
				return new BrowserCompatSpec() {
					@Override
					public void validate(Cookie cookie, CookieOrigin origin)
							throws MalformedCookieException {
							// Oh, I am easy
					}
				};
			}
		};
		Registry<CookieSpecProvider> r = RegistryBuilder
				.<CookieSpecProvider> create()
				.register(CookieSpecs.BEST_MATCH, new BestMatchSpecFactory())
				.register(CookieSpecs.BROWSER_COMPATIBILITY,
						new BrowserCompatSpecFactory())
				.register("easy", easySpecProvider).build();
		RequestConfig requestConfig = RequestConfig.custom()
				.setCookieSpec("easy").setSocketTimeout(10000)
				.setConnectTimeout(10000).build();

		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultCookieSpecRegistry(r)
				.setDefaultRequestConfig(requestConfig)
				.setDefaultCookieStore(cookieStore)
				.build();
		return httpclient;
	}

	private static final double EARTH_RADIUS = 6378.137;//赤道半径

	public static String baiduMap(String address) throws Exception{
		//String param = "param1=" + URLEncoder.encode("中国", "UTF-8") + "&param2=value2";   
		//URI uri = URIUtils.createURI("http", "localhost", 8080,   "/sshsky/index.html", param, null);   
		//System.out.println(uri);  
		/*
		MultipartEntity entity = new MultipartEntity();  
		entity.addPart("param1", new StringBody("中国", Charset.forName("UTF-8")));  
		entity.addPart("param2", new StringBody("value2", Charset.forName("UTF-8")));  
		//entity.addPart("param3", new FileBody(new File("C:\\1.txt")));  
		HttpPost request = new HttpPost(“http://localhost/index.html”);  
		request.setEntity(entity);  
		*/
		//http://localhost:8080/fmplatform/main/dict!deliveryLimit.action
		//核心应用类
		//HttpClient httpClient = HttpClients.createDefault();
		//URI uri = URIUtils.createURI("http", "localhost", -1, "/index.html",   "param1=value1&param2=value2", null);  

		//测试用数据
		//String param = "param1=" + URLEncoder.encode("中国", "UTF-8") + "&param2=value2";  
		//URI uri = URIUtils.createURI("http", "localhost", 8080, "/fmplatform/main/dict!deliveryLimit.action", param, null);  

		String param = "address="+URLEncoder.encode(address, "UTF-8")+"&output=json&ak="+BAIDUKEY;//+"&callback=showLocation";
		//URI uri = URIUtils.createURI("http", "api.map.baidu.com", 80, "/geocoder/v2/", param, null);
		//System.out.println(uri);
		String uri = "http://api.map.baidu.com/geocoder/v2/?"+param;
		//http://localhost/index.html?param1=value1&param2=value2
		/*HttpUriRequest request = new HttpGet(uri);
		System.out.println(request.getURI());

		//HttpUriRequest request = ...;
		HttpResponse response = httpClient.execute(request);

		// 从response中取出HttpEntity对象
		HttpEntity entity = response.getEntity();
		// 查看entity的各种指标
		System.out.println(entity.getContentType());
		System.out.println(entity.getContentLength());
		System.out.println(EntityUtils.getContentCharSet(entity));

		// 取出服务器返回的数据流
		InputStream stream = entity.getContent();
		// 以任意方式操作数据流stream
		// 调用方式 略
		//InputStream stream = new FileInputStream(new File("D"));
		return InputStreamUtils.InputStreamTOString(stream);*/

		HttpUriRequest httpGet = new HttpGet(uri);
		CloseableHttpResponse response = httpclient.execute(httpGet);
		String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
		return responseContent;
	}

	/**
	 * 地址转化成坐标
	 **/
	public static String a2p(String city, String address) throws Exception{
		//核心应用类
		//HttpClient httpClient = HttpClients.createDefault();
		String param = "city="+city+"&address="+URLEncoder.encode(address, "UTF-8")+"&output=json&ak="+BAIDUKEY;//+"&callback=showLocation";
		//URI uri = URIUtils.createURI("http", "api.map.baidu.com", 80, "/geocoder/v2/", param, null);
		String uri = "http://api.map.baidu.com/geocoder/v2/?"+param;
		/*HttpUriRequest request = new HttpGet(uri);
		HttpResponse response = httpClient.execute(request);
		// 从response中取出HttpEntity对象
		HttpEntity entity = response.getEntity();
		// 取出服务器返回的数据流
		InputStream stream = entity.getContent();
		// 以任意方式操作数据流stream
		*//*
		 {"status":0,"result":{"location":{"lng":116.30814954222,"lat":40.056885091681},"precise":1,"confidence":80,"level":"\u5546\u52a1\u5927\u53a6"}}
		 * *//*
		return InputStreamUtils.InputStreamTOString(stream);*/

		HttpUriRequest httpGet = new HttpGet(uri);
		CloseableHttpResponse response = httpclient.execute(httpGet);
		String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
		return responseContent;
	}
	/**
	 * 坐标转化成地址
	 **/
	public static String p2a(String location) throws Exception{
		//核心应用类
		//HttpClient httpClient = HttpClients.createDefault();
		String param = "location="+location+"&pois=1&output=json&ak="+BAIDUKEY+"&pois=0";//+"&callback=showLocation";

		//URI uri = URIUtils.createURI("http", "api.map.baidu.com", 80, "/geocoder/v2/", param, null);
		String uri = "http://api.map.baidu.com/geocoder/v2/?"+param;
		/*HttpUriRequest request = new HttpGet(uri);
		HttpResponse response = httpClient.execute(request);
		// 从response中取出HttpEntity对象
		HttpEntity entity = response.getEntity();
		// 取出服务器返回的数据流
		InputStream stream = entity.getContent();
		// 以任意方式操作数据流stream
		*//*
		 {"status":0,"result":{"location":{"lng":116.322987,"lat":39.983424071404},"formatted_address":"北京市海淀区中关村大街27号1101-08室","business":"人民大学,中关村,苏州街","addressComponent":{"city":"北京市","district":"海淀区","province":"北京市","street":"中关村大街","street_number":"27号1101-08室"},"pois":[{"addr":"中关村西区南侧（中关村科技园区内）","cp":"mix","distance":"0.050000","name":"中关村大厦","poiType":"办公大厦,商务大厦","point":{"x":116.32298658484,"y":39.983423843929},"tel":"(010)82856666","uid":"bd045712cc428d06b6265537","zip":"100000"},{"addr":"中关村大街27号","cp":"NavInfo","distance":"0.050000","name":"眉州东坡酒楼中关村店","poiType":"中餐馆,餐饮","point":{"x":116.32298658484,"y":39.983423843929},"tel":"(010)82856948","uid":"95540906d327cb7527d2bb0a","zip":""},{"addr":"中关村大街27号","cp":"NavInfo","distance":"0.050000","name":"中国人民财产保险中关村营业部","poiType":"中国人民财产保险,保险公司,金融","point":{"x":116.32298658484,"y":39.983423843929},"tel":"(010)82856779","uid":"04b48c6cd2a493b92a06ce64","zip":"100000"},{"addr":"北京市海淀区","cp":"NavInfo","distance":"94.432081","name":"光合作用书房","poiType":"图书音像,购物","point":{"x":116.32239334388,"y":39.983890240676},"tel":"","uid":"f4e732a1ad2e8a57f31937fb","zip":""},{"addr":"中关村大街27号","cp":"NavInfo","distance":"42.195731","name":"建行中关村支行","poiType":"中国建设银行,银行,金融","point":{"x":116.32292037972,"y":39.983711118168},"tel":"","uid":"4eabebb07d31784b4d6d461f","zip":"100000"},{"addr":"北京市海淀区","cp":"NavInfo","distance":"62.342644","name":"海淀医院-激光整形美容部","poiType":"美容美发,生活服务","point":{"x":116.32317954086,"y":39.98301950182},"tel":"","uid":"a676b6567d51c145374aae32","zip":""},{"addr":"中关村大街19号新中关购物中心1楼","cp":"NavInfo","distance":"112.983688","name":"星巴克新中关店","poiType":"星巴克,咖啡厅,休闲餐饮,餐饮","point":{"x":116.32218215226,"y":39.983899777278},"tel":"(010)82486056","uid":"93749e45ae0a139ece06a409","zip":""}],"cityCode":131}}

		 * *//*
		return InputStreamUtils.InputStreamTOString(stream);*/

		HttpUriRequest httpGet = new HttpGet(uri);
		CloseableHttpResponse response = httpclient.execute(httpGet);
		String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
		return responseContent;
	}


	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 坐标根据坐标测直线距离（km）
	 **/
	public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
		s = s * EARTH_RADIUS;
//	   s = Math.round(s * 10000)/10000 ;
		s = Math.round(s * 1000) / 1000.00;
		return s;
	}

	/**
	 * 坐标根据坐标返回百度步行路线
	 * @param city1
	 * @param lat1
	 * @param lng1
	 * @param city2
	 * @param lat2
	 * @param lng2
	 *
	 * @return
	 */
	public static String getBaiDuWalkingDirection(String city1, double lat1, double lng1,String city2, double lat2, double lng2) throws Exception {
		//核心应用类
		//HttpClient httpClient = HttpClients.createDefault();
		String param = "mode=walking&origin="+lat1+","+lng1+"&destination="+lat2+","+lng2+"&origin_region="+city1+"&destination_region="+city2+"&output=json&ak="+BAIDUKEY;//+"&callback=showLocation";
		//URI uri = URIUtils.createURI("http", "api.map.baidu.com", 80, "/direction/v1", param, null);
		String uri = "http://api.map.baidu.com/direction/v1?"+param;
		/*HttpUriRequest request = new HttpGet(uri);
		HttpResponse response = httpClient.execute(request);
		// 从response中取出HttpEntity对象
		HttpEntity entity = response.getEntity();
		// 取出服务器返回的数据流
		InputStream stream = entity.getContent();
		// 以任意方式操作数据流stream
		return InputStreamUtils.InputStreamTOString(stream);*/

		HttpUriRequest httpGet = new HttpGet(uri);
		CloseableHttpResponse response = httpclient.execute(httpGet);
		String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
		return responseContent;
	}

	/**
	 * 坐标根据坐标返回百度步行距离（m）
	 * @param city1
	 * @param lat1
	 * @param lng1
	 * @param city2
	 * @param lat2
	 * @param lng2
	 *
	 * @return
	 */
	public static double getBaiDuWalkingDistance(String city1, double lat1, double lng1,String city2, double lat2, double lng2){
		try {
			String result = getBaiDuWalkingDirection(city1, lat1, lng1, city2, lat2, lng2);
			JSONObject direction =  JSONObject.fromObject(result);
			if(direction.getInt("status")==0){
				return direction.getJSONObject("result").getJSONArray("routes").getJSONObject(0).getDouble("distance");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;

	}

	/**
	 * 坐标根据坐标测距
	 **/
	public static String distance(String slng,String slat,String elng,String elat,String... param) throws Exception{

		return null;
	}

	/*
	 一般状况：车价=起步价【12元】+(里程数 – 起步里程数【3公里】)*每公里单价【2.4元】 (3<里程数<10) 
	车价=起步价【12元】 (里程数<3)
	远程状况：车价=起步价【12元】+(远程里程标准【10公里】 – 起步里程数【3公里】)*每公里单价 + (里程数 – 远程里程标准【10】) *远程每公里单价【3.6元】 (里程数>10) 提醒：在5:00—23:00期间，起步费14元(包括一元的燃油费)，可运营3公里，超过3公里后每公里2.40元，总里程超过10公里后超过部分按每公里3.60元计算。 
	在23:00—到次日5:00期间，起步费18元(包括一元的燃油费)，可运营3公里，超过3公里后每公里3.10元，总里程超过10公里后超过部分按每公里4.70元计算。
	重量：1KG 每增加1KG加2快
	 * */
	//根据条件计算金额
	public static String getMoney(BigDecimal distance,BigDecimal dweight,String dtype,BigDecimal declareValue,boolean ifPremium){
		//4.831 2 40288a813ef40db6013ef40deac40023 immediately
		//配送时效
		//if(dlimit.getNo().equals("001")){}
		double freight;
		double declareVal;
		double money = 0.00;

		Map<String, Object> data = new HashMap<String, Object>();

		freight = 12.00;
		Double distanced = distance.doubleValue();

		int weight = dweight.intValue();
		if(weight>1){
			freight+= (weight-1)*2;
		}
		if(distanced>3 && distanced<10){
			freight+= (distanced-3)*2.4;
		}else if(distanced>10){
			freight+= (10-3)*2.4;
			freight+= (distanced-10)*3.6;
		}
		if(dtype.equals("immediately")){
			money = freight;
			data.put("freight", ArithUtil.round(money,0));
		}
		double immediately = freight;

		freight = 12.00;
		weight = dweight.intValue();
		if(weight>1){
			freight+= (weight-1)*2;
		}
		/*
		if(dtype.equals("everyotherday")){
			money = freight;
			data.put("freight", ArithUtil.round(money,0));
		}
		*/
		double everyotherday = freight;

		//保价
		if(ifPremium){
			declareVal = ArithUtil.round(declareValue.intValue()/100,0);
			if(declareVal<3){
				declareVal = 3;
			}
			data.put("premium",declareVal);
			data.put("sendMoney", ArithUtil.round(money,0));
			data.put("money", ArithUtil.round(money+declareVal,0));

			immediately = ArithUtil.round(immediately+declareVal,0);
			everyotherday = ArithUtil.round(everyotherday+declareVal,0);
		}else{
			data.put("money", ArithUtil.round(money,0));
			data.put("premium",0);
			data.put("sendMoney", ArithUtil.round(money,0));

			immediately = ArithUtil.round(immediately,0);
			everyotherday = ArithUtil.round(everyotherday,0);
		}
		//添加比价服务
		data.put("immediately",immediately);
		data.put("everyotherday",everyotherday);

		data.put("distance", ArithUtil.round(distance.doubleValue(),2));

		//增加预计时间
		Date preGetTime;
		Date preSendTime;
		Calendar ca = Calendar.getInstance();
		if(dtype.equals("immediately")){
			ca.add(Calendar.MINUTE, 22);	//设置取件日期+20分钟 + 2分钟间隔
		}else{
			ca.add(Calendar.MINUTE, 122);	//设置取件日期+20分钟 + 2分钟间隔
		}
		preGetTime = ca.getTime();
		//比较收件日期
		ca.setTime(preGetTime);
		ca.add(Calendar.MINUTE,  ArithUtil.convertsToInt(distanced/10*60));
		preSendTime = ca.getTime();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		data.put("preGetTime", format.format(preGetTime));
		data.put("preSendTime", format.format(preSendTime));

		data.put("state", "200");
		data.put("message","金额计算成功");
		return JSONObject.fromObject(data).toString();
		//return "{'state':'200','message':'','freight':'28','money':'100'}";
	}

	/**
	 * 计算预计取件日期和预计收件日期，从前台计算
	 * @param tmpPreGetTime
	 * @param tmpPreSendTime
	 * @param dis
	 */
	public static void getPreDates(String tmpPreGetTime,String tmpPreSendTime,double dis){
		Date preGetTime;
		Date preSendTime;

		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.MINUTE, 20);	//设置取件日期+20分钟
		preGetTime = ca.getTime();

		//比较取件日期
		if(!StringUtils.isEmpty(tmpPreGetTime)){
			if(DataUtil.StringToDate(tmpPreGetTime, "yyyy-MM-dd HH:mm").before(preGetTime)){
				//return ajaxHtmlCallback("400","预计取件日期必须大于 [" + DataUtil.DateToString(preGetTime, "yyyy-MM-dd HH:mm") +"]");
			}else{
				preGetTime = DataUtil.StringToDate(tmpPreGetTime, "yyyy-MM-dd HH:mm");
			}
		}

		//比较收件日期
		ca.setTime(preGetTime);
		ca.add(Calendar.MINUTE,  ArithUtil.convertsToInt(dis/10*60));
		preSendTime = ca.getTime();

		if(!StringUtils.isEmpty(tmpPreSendTime)){
			if(DataUtil.StringToDate(tmpPreSendTime, "yyyy-MM-dd HH:mm").before(preSendTime)){
				//return ajaxHtmlCallback("400","预计送达日期必须大于 [" + DataUtil.DateToString(preSendTime, "yyyy-MM-dd HH:mm") +"]");
			}else{
				preSendTime = DataUtil.StringToDate(tmpPreSendTime, "yyyy-MM-dd HH:mm");
			}
		}
	}

	//根据ip返回城市
	public static String getIpLocation(String ip) throws Exception{
		//http://api.map.baidu.com/location/ip?ak=F454f8a5efe5e577997931cc01de3974&ip=202.198.16.3&coor=bd09ll
		//HttpClient httpClient = HttpClients.createDefault();
		String param = "ip=" + ip + "&coor=bd09ll&ak=" + BAIDUKEY;
		//URI uri = URIUtils.createURI("http", "api.map.baidu.com", 80, "/location/ip", param, null);

		String uri = "http://api.map.baidu.com/location/ip?"+param;
		/*HttpUriRequest request = new HttpGet(uri);
		HttpResponse response = httpClient.execute(request);
		// 从response中取出HttpEntity对象
		HttpEntity entity = response.getEntity();
		// 取出服务器返回的数据流
		InputStream stream = entity.getContent();
		// 以任意方式操作数据流stream
		return InputStreamUtils.InputStreamTOString(stream);*/
		HttpUriRequest httpGet = new HttpGet(uri);
		CloseableHttpResponse response = httpclient.execute(httpGet);
		String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
		return responseContent;
	}

	public static final CloseableHttpClient httpclient = HttpClients.createDefault();

	//http://api.map.baidu.com/geoconv/v1/?coords=121.49168400,31.24983000&from=1&to=5&ak=你的密钥
    /**
     * 各地图API坐标系统比较与转换;
     * WGS84坐标系：即地球坐标系，国际上通用的坐标系。设备一般包含GPS芯片或者北斗芯片获取的经纬度为WGS84地理坐标系,
     * 谷歌地图采用的是WGS84地理坐标系（中国范围除外）;
     * GCJ02坐标系：即火星坐标系，是由中国国家测绘局制订的地理信息系统的坐标系统。由WGS84坐标系经加密后的坐标系。
     * 谷歌中国地图和搜搜中国地图采用的是GCJ02地理坐标系; BD09坐标系：即百度坐标系，GCJ02坐标系经加密后的坐标系;
     * 搜狗坐标系、图吧坐标系等，估计也是在GCJ02基础上加密而成的。 chenhua
     */
	public static String wgs84ToBd09ll(String x,String y) throws Exception {
		String param = "coords="+x+","+y+"&from=1&to=5&ak="+BAIDUKEY;
		//URI uri = URIUtils.createURI("http", "api.map.baidu.com", 80, "geoconv/v1/", param, null);
		String uri = "http://api.map.baidu.com/geoconv/v1/?"+param;
		HttpUriRequest httpGet = new HttpGet(uri);
		CloseableHttpResponse response = getHttpClient().execute(httpGet);
		String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
		return responseContent;
		/*HttpResponse response = httpClient.execute(request);
		// 从response中取出HttpEntity对象
		HttpEntity entity = response.getEntity();
		// 取出服务器返回的数据流
		InputStream stream = entity.getContent();*/
		// 以任意方式操作数据流stream
		/*
		 {
	status : 0,
	result :
	[
		{
			x : 121.50275678424,
			y : 31.253496587336
		}
		]
		 * */
		//return InputStreamUtils.InputStreamTOString(stream);
	}

	public static Map<String,String> wgs84ToBd09llWithMap(String x,String y) throws Exception{
		String result = wgs84ToBd09ll(x,y);
		try {
			JSONObject direction =  JSONObject.fromObject(result);
			if(direction.getInt("status")==0){
				Map map = new HashMap<String,String>();
				map.put("x", direction.getJSONArray("result").getJSONObject(0).getString("x"));
				map.put("y", direction.getJSONArray("result").getJSONObject(0).getString("y"));
				return map;
			}
		} catch (Exception e) {

		}
		return null;
	}
	public static Map<String,String> getBd0911AddressMapFromWgs84(String lng,String lat){
		try {
			Map<String,String> map = wgs84ToBd09llWithMap(lng, lat);
			String result = p2a(map.get("y")+","+map.get("x"));
			JSONObject direction =  JSONObject.fromObject(result);
			if(direction.getInt("status")==0){
				map.put("address", direction.getJSONObject("result").getString("formatted_address"));
			}else{
				map.put("address","");
			}
			map.put("json",result);
			return map;
		}catch (Exception e){
			return null;
		}
	}

    public static String localSearch(String city,String add) throws IOException {
        String param = "ak="+BAIDUKEY+"&address="+add+"&city="+city+"&output=json";
        String uri = "http://api.map.baidu.com/geocoder/v2/?"+param;
        HttpUriRequest httpGet = new HttpGet(uri);
        CloseableHttpResponse response = getHttpClient().execute(httpGet);
        String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
        return responseContent;
    }
	public static void main(String[] args) throws Exception {

//		try {
//			JSONObject direction =  JSONObject.fromObject(getBaiDuWalkingDirection("", 31.210840968341, 121.44720094609, "", 31.198114018239, 121.43391770251));
//			if(direction.getInt("status")==0){
//				System.out.println(direction.getJSONObject("result").getJSONArray("routes").getJSONObject(0).getDouble("distance"));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		//System.out.println(getDistance(31.210840968341, 121.44720094609, 31.198114018239, 121.43391770251));

		//System.out.println(getIpLocation(IPUtil.));

//		System.out.println(wgs84ToBd09llWithMap("121.49168400","31.24983000").get("x"));
//		System.out.println(wgs84ToBd09llWithMap("121.49168400","31.24983000").get("y"));

        String json = localSearch("上海市","2222");
        System.out.println("{json }" + json);

	}



}
