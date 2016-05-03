package com.joint.base.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PDFConfigUtil {
	private final static String file="resource/PDFConfig.propertise";
	private static Properties pr=new Properties();
	public static String getValues(String key){
		try {
			/*
			InputStream is=this.getClass().getResourceAsStream(file);   
			
	        BufferedReader br=new BufferedReader(new InputStreamReader(is));   
	        String s=""; 
	        StringBuilder output = new StringBuilder();  
	        while((s=br.readLine())!=null)  {
	        	 output.append(s+"\n");  
	        }*/
	      //  URL fileURL=this.getClass().getResource(file);    
	      //  System.out.println(fileURL.getFile());   
            pr.load(new FileInputStream(file));
			return pr.getProperty(key);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

    public static void main(String[] args) {
        System.out.println(PDFConfigUtil.getValues("type"));
    }
}
