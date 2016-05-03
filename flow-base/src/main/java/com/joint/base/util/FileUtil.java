package com.joint.base.util;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Created by ZhuChunXiao on 2015/7/2.
 */
public class FileUtil {


    public static void getImageByUrl(String urlstr, String filepath) throws IOException {
        int num = urlstr.indexOf('/',8);
        int extnum = urlstr.lastIndexOf('.');
        String u = urlstr.substring(0,num);
       // String ext = urlstr.substring(extnum+1,urlstr.length());
      //  System.out.println("ext:"+ext);
        URL url = new URL(urlstr);
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("referer", u);       //通过这个http头的伪装来反盗链
        BufferedImage image = ImageIO.read(connection.getInputStream());
        FileOutputStream fout=new FileOutputStream(filepath);
       // if("gif".equals(ext)|| "png".equals("ext") || "bmp".equals(ext)|| "jpg".equals("ext"))
        //{
         //   ImageIO.write(image, ext, fout);
      //  }
        ImageIO.write(image, "jpg", fout);
        fout.flush();
        fout.close();
    }
    /**
     * 生成目录
     * @param dir
     */
    public static void makeDir(File dir) {
        if(! dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdir();
    }

    public static void getFileByUrl(String str,String path) throws IOException {
        URL url = new URL(str);
        HttpURLConnection connection =(HttpURLConnection)url.openConnection();
        DataInputStream in = new DataInputStream(connection.getInputStream());
        DataOutputStream out=new DataOutputStream(new FileOutputStream(path));
        int tempbyte;
        while ((tempbyte = in.read()) != -1) {
           out.write(tempbyte);
        }
        out.close();
        in.close();
    }

    /**
     * 根据url下载文件，保存到filepath中
     * @param url
     * @param filepath
     * @return
     */
    public static String download(String url, String filepath) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = client.execute(httpget);

            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();

            File file = new File(filepath);

            ByteArrayOutputStream baos = new ByteArrayOutputStream( 1000 );
            file.getParentFile().mkdirs();
            FileOutputStream fileout = new FileOutputStream(file);
            /**
             * 根据实际运行效果 设置缓冲区大小
             */
            byte[] buffer=new byte[1024];
            int ch = 0;
            while ((ch = is.read(buffer)) != -1) {
                fileout.write(buffer,0,ch);
            }
            is.close();
            fileout.flush();
            fileout.close();
            BufferedImage bi = ImageIO.read(file);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取response header中Content-Disposition中的filename值
     * @param response
     * @return
     */
    public static String getFileName(HttpResponse response) {
        Header contentHeader = response.getFirstHeader("Content-Disposition");
        String filename = null;
        if (contentHeader != null) {
            HeaderElement[] values = contentHeader.getElements();
            if (values.length == 1) {
                NameValuePair param = values[0].getParameterByName("filename");
                if (param != null) {
                    try {
                        //filename = new String(param.getValue().toString().getBytes(), "utf-8");
                        //filename=URLDecoder.decode(param.getValue(),"utf-8");
                        filename = param.getValue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return filename;
    }
    /**
     * 获取随机文件名
     * @return
     */
    public static String getRandomFileName() {
        return String.valueOf(System.currentTimeMillis());
    }

    public static void outHeaders(HttpResponse response) {
        Header[] headers = response.getAllHeaders();
        for (int i = 0; i < headers.length; i++) {
            System.out.println(headers[i]);
        }
    }


    /**
     * APDPlat中的重要打包机制
     * 将jar文件中的某个文件夹里面的内容复制到某个文件夹
     * @param jar 包含静态资源的jar包
     * @param subDir jar中包含待复制静态资源的文件夹名称
     * @param loc 静态资源复制到的目标文件夹
     * @param force 目标静态资源存在的时候是否强制覆盖
     */
    public static void unZip(String jar, String subDir, String loc, boolean force){
        try {
            File base=new File(loc);
            if(!base.exists()){
                base.mkdirs();
            }

            ZipFile zip=new ZipFile(new File(jar));
            Enumeration<? extends ZipEntry> entrys = zip.entries();
            while(entrys.hasMoreElements()){
                ZipEntry entry = entrys.nextElement();
                String name=entry.getName();
                if(!name.startsWith(subDir)){
                    continue;
                }
                //去掉subDir
                name=name.replace(subDir,"").trim();
                if(name.length()<2){

                    continue;
                }
                if(entry.isDirectory()){
                    File dir=new File(base,name);
                    if(!dir.exists()){
                        dir.mkdirs();
                        System.out.println("创建目录");
                    }else{
                        System.out.println("目录已经存在");
                    }
                    System.out.println(name+" 是目录");
                }else{
                    File file=new File(base,name);
                    if(file.exists() && force){
                        file.delete();
                    }
                    if(!file.exists()){
                        InputStream in=zip.getInputStream(entry);
                        //FileUtils.copyFile(in,file);
                        System.out.println("创建文件");
                    }else{
                        System.out.println("文件已经存在");
                    }
                    System.out.println(name+" 不是目录");
                }
            }
        } catch (ZipException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 创建ZIP文件
     * @param sourcePath 文件或文件夹路径
     * @param zipPath 生成的zip文件存在路径（包括文件名）
     */
    public static void createZip(List<String> list, String zipPath) {
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(zipPath);
            zos = new ZipOutputStream(fos);
            for(String sp:list){
                writeZip(new File(sp), "", zos);
            }

        } catch (FileNotFoundException e) {
            System.out.println("创建ZIP文件失败");
        } finally {
            try {
                if (zos != null) {
                    zos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private static void writeZip(File file, String parentPath, ZipOutputStream zos) {

        if(file.exists()){
            if(file.isDirectory()){//处理文件夹
                parentPath+=file.getName()+File.separator;
                File [] files=file.listFiles();
                for(File f:files){
                    writeZip(f, parentPath, zos);
                }
            }else{
                FileInputStream fis=null;
                try {
                    fis=new FileInputStream(file);
                   // System.out.println(parentPath + file.getName());
                    ZipEntry ze = new ZipEntry(parentPath + file.getName());
                    zos.putNextEntry(ze);
                    byte [] content=new byte[1024];
                    int len;
                    while((len=fis.read(content))!=-1){
                        zos.write(content,0,len);
                        zos.flush();
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    try {
                        if(fis!=null){
                            fis.close();
                        }
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 删除文件
     * @param filepath
     * @throws IOException
     */
    public static void del(String filepath) throws IOException {
        File f = new File(filepath);// 定义文件路径
        if (f.exists() && f.isDirectory()) {// 判断是文件还是目录
            if (f.listFiles().length == 0) {// 若目录下没有文件则直接删除
                f.delete();
            } else {// 若有则把文件放进数组，并判断是否有下级目录
                File delFile[] = f.listFiles();
                int i = f.listFiles().length;
                for (int j = 0; j < i; j++) {
                    if (delFile[j].isDirectory()) {
                        del(delFile[j].getAbsolutePath());// 递归调用del方法并取得子目录路径
                    }
                    delFile[j].delete();// 删除文件
                }
            }
        }
    }

    /**
     * 移动文件
     * @param srcFile
     * @param destPath
     * @return
     */
    public static boolean Move(String srcFile, String destPath) {
        // File (or directory) to be moved
        File file = new File(srcFile);

        // Destination directory
        File dir = new File(destPath);

        // Move file to new directory
        boolean success = file.renameTo(new File(dir, file.getName()));

        return success;
    }

    /**
     * 复制文件
     * @param oldPath
     * @param newPath
     */
    public static void Copy(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                InputStream inStream = new FileInputStream(oldPath);
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("error  ");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
//      String url = "http://bbs.btwuji.com/job.php?action=download&pid=tpc&tid=320678&aid=216617";
     //   String url="http://www.iteye.com/upload/logo/user/777519/fd16aed5-c794-3852-9821-946083dee6cc.jpg?1355209845";
       String url = "http://localhost:8089/ec-web/com/file!readDoc.action?gridId=557e3fced3e6b06638d5f3ab";
    //    String url = "http://cst.9joint-eco.com/ec-web/com/file!readDoc.action?keyId=8a2117aa4e00fadb014e049e2810001f";
//      String filepath = "D:\\test\\a.torrent";
        String filepath = "c:/add3322344233.jpg";
        FileUtil.getFileByUrl(url, filepath);
    }
}
