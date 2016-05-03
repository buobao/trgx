package com.joint.base.util;

/**
 * Created by zhucx on 2015/8/5.
 */

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlUtil {
    public static Map<String,Object> xmlElements(String xmlDoc,String arr[]) {
        Map<String,Object> map = new HashMap<String,Object>();
        //创建一个新的字符串
        StringReader read = new StringReader(xmlDoc);
        //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
        InputSource source = new InputSource(read);
        //创建一个新的SAXBuilder
        SAXBuilder sb = new SAXBuilder();
        try {
            //通过输入源构造一个Document
            Document doc = null;
            try {
                doc = sb.build(source);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //取的根元素
            Element root = doc.getRootElement();
            //  System.out.println(root.getName());//输出根元素的名称（测试）
            //得到根元素所有子元素的集合
            List jiedian = root.getChildren();
            //获得XML中的命名空间（XML中未定义可不写）
            Namespace ns = root.getNamespace();
            Element et = null;
            for(int i=0;i<jiedian.size();i++){
                et = (Element) jiedian.get(i);//循环依次得到子元素
                for(int j=0;j<arr.length;j++){
                    map.put(arr[j], et.getChild(arr[j],ns).getText());
                }

            }
            return map;
            /*
            et = (Element) jiedian.get(0);
            List zjiedian = et.getChildren();
            for(int j=0;j<zjiedian.size();j++){
                Element xet = (Element) zjiedian.get(j);
               // System.out.println(xet.getName());
            }*/
        } catch (JDOMException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
        return null;
    }

}