package com.joint.base.util.security;

/**
 * Created by 文豪 on 2015/5/29.
 */

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetWork {
    public static String getUrlData(String url) {
        StringBuffer temp = new StringBuffer();
        try {

           // String url = "http://officeweb365.com/o/?i=1817&furl=http://test.9joint.com/ec-web/com/file!readDoc.action?keyId=402831814d9e207b014d9e21a5560000";
            HttpURLConnection uc = (HttpURLConnection)new URL(url).
                    openConnection();
            uc.setConnectTimeout(10000);
            uc.setDoOutput(true);
            uc.setRequestMethod("GET");
            uc.setUseCaches(false);
            DataOutputStream out = new DataOutputStream(uc.getOutputStream());
            /*
            // 要传的参数
            String s = URLEncoder.encode("ra", "GB2312") + "=" +
                    URLEncoder.encode(leibie, "GB2312");
            s += "&" + URLEncoder.encode("keyword", "GB2312") + "=" +
                    URLEncoder.encode(num, "GB2312");*/
            // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写道流里面
            //out.writeBytes(s);
            out.flush();
            out.close();
            InputStream in = new BufferedInputStream(uc.getInputStream());
            Reader rd = new InputStreamReader(in, "utf-8");
            int c = 0;
            while ((c = rd.read()) != -1) {
                temp.append((char) c);
            }
            in.close();
            return  temp.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp.toString();
    }

    public static void main(String[] a){
       // NetWork.cc("","");
    }

}