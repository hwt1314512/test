package com.mob.webshare.admin.handler;

import com.mob.commons.json.JSON;
import com.mob.commons.utils.FileUtils;

import java.io.*;
import java.util.*;

/**
 * Created by huangwt on 2017/7/11.
 */
public class Test {

    public  static Map<String, String> fileParsing(File file) {
        if (file == null) {
            return null;
        }

        Map<String, String> fileReadMap = new LinkedHashMap<String, String>();
        String alineString = null;
        BufferedReader bufferedReader = null;
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            while ((alineString=bufferedReader.readLine())!=null){
                System.err.println(alineString);

                String[] arrays = alineString.split(" ");
                if(arrays[1].equals("href")){
                    System.err.println(arrays[1]);
                }
            }

        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {

            try {
                if (null != bufferedReader)
                    bufferedReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (null != fileReader)
                    fileReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fileReadMap;
    }

    public static  String readHtml(String filePath) {
        BufferedReader br=null;
        StringBuffer sb = new StringBuffer();
        try {
            br=new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "utf-8"));
            String temp=null;
            while((temp=br.readLine())!=null){
                sb.append(temp);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    public static  Map<String,String> getTextFromHtml(String filePath) {

        String str= readHtml(filePath);
        StringBuffer buff = new StringBuffer();
        int maxindex = str.length() - 1;
        int begin = 0;
        int end;
        List<String> listName = new ArrayList<String>();
        List<String> listHref = new ArrayList<String>();
        Map<String,String> map = new HashMap<String ,String>();

        while((begin = str.indexOf('<',begin)) < maxindex){
            end = str.indexOf('>',begin);
            if(end - begin > 1){
//                buff.append(str.substring(++begin, end));
                String s = str.substring(++begin,end);
                if(s.indexOf("a href=\"http://")!=-1){
//                        System.err.println(s);
                    listHref.add(s);
                }
                if(s.indexOf("mod-iconitem-tit")!=-1){
//                    System.err.println(s);
//                    System.err.println(str.substring(end+1,str.indexOf('<',end)));
                    listName.add(str.substring(end+1,str.indexOf('<',end)));
                }
            }

            begin = end+1;
            if(begin+10>maxindex)
                break;
        };

        for(int i=0;i<listHref.size()&&i<listName.size();i++){
            map.put(listName.get(i),listHref.get(i));
//            System.err.println(map.get(listName.get(i)));
        }
        return map;
    }
    public static  void writeObject() {
        try {
            Map<String,String> map = new HashMap<String,String>();
            map = getTextFromHtml("D:\\王者荣耀英雄数据库_新英雄_英雄出装_铭文搭配_视频教学_18183王者荣耀专区.html");
            FileOutputStream outStream = new FileOutputStream("D:/data.json");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
            objectOutputStream.writeObject(map);
            outStream.close();
            System.out.println("successful");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String args[]){
        Map<String,String> map = getTextFromHtml("D:\\王者荣耀英雄数据库_新英雄_英雄出装_铭文搭配_视频教学_18183王者荣耀专区.html");


        String ss = JSON.fromJavaObject(map).toJSONString();
        try{
            FileUtils.writeStringToFile(new File("D:/data.json"),ss,"utf-8");
        }catch (Exception e){

        }
    }
}
