package com.nju.topics.serviceImpl;

import com.nju.topics.config.Config;
import com.nju.topics.domain.DictFileInfo;
import com.nju.topics.service.Dict;
import com.nju.topics.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;


@Component
public class FileServiceImpl implements FileService {

    @Autowired
    private Config config;

    @Override
    public ArrayList<String> getAllSegmentFile() {
        ArrayList<String> segFileNames=new ArrayList<>();
        String folder=config.getUploadPath();
        File segFolder=new File(folder);
        if (!segFolder.exists() || !segFolder.isDirectory()){
            segFolder.mkdirs();

        }

        File[] files=segFolder.listFiles();
        for (File file:files){
            segFileNames.add(file.getName());
        }
        return segFileNames;
    }

    @Override
    public ArrayList<DictFileInfo> getAllDictFile() {
        ArrayList<DictFileInfo> dictFileInfos=new ArrayList<>();
        String folder=config.getDownloadPath();
        File dictFolder=new File(folder);
        if(!dictFolder.exists() || !dictFolder.isDirectory()){
            dictFolder.mkdirs();

        }

        File[] files=dictFolder.listFiles();
        for(File file:files){
            String name=file.getName();
            long byteLen=file.length();
            long kbLen=byteLen/1024;
            String path=file.getAbsolutePath();
            DictFileInfo dictFileInfo=new DictFileInfo(name,kbLen,path);
            dictFileInfos.add(dictFileInfo);
        }
        return dictFileInfos;
    }

    @Override
    public String uploadSegmentFile(MultipartFile file) {
        String name=file.getOriginalFilename();
        System.out.println(name);
        ArrayList<String> existFileNames=getAllSegmentFile();
        boolean flag=false;
        if(existFileNames!=null && existFileNames.size()>0){
            for(int i=0;i<existFileNames.size();i++){
                if (name.equals(existFileNames.get(i))){
                    flag=true;
                    break;
                }
            }
        }

        if (flag){
            String originFilePath=config.getUploadPath()+name;
            File originFile=new File(originFilePath);
            originFile.delete();
        }
        String filePath=config.getUploadPath()+name;
        File newSegFile=new File(filePath);

        String folder=config.getDownloadPath();
        File dictFolder=new File(folder);
        if(!dictFolder.exists() || !dictFolder.isDirectory()){
            dictFolder.mkdirs();

        }
        String dictPath=config.getDownloadPath()+name;
        File dictFile=new File(dictPath);
        try {
            dictFile.createNewFile();
            file.transferTo(newSegFile);
            return "success";
        }catch (IOException e){
            e.printStackTrace();
        }
        return "error";
    }

    @Override
    public String deleteSegmentFile(String name) {
        String path=config.getUploadPath()+name;
        File file=new File(path);
        if (file.exists()){
            file.delete();
        }
        return "success";
    }

    @Override
    public String getDictLog(String name) {
        String res="";
        String dictLogPath=config.getDictLogPath()+name;
        File dictLogFile=new File(dictLogPath);
        if (!dictLogFile.exists()){
            return "NOFILE";
        }

        String line="";
        BufferedReader bufferedReader=null;
        try {
            bufferedReader=new BufferedReader(new FileReader(dictLogFile));
            while ((line=bufferedReader.readLine())!=null){
                if(line.contains("add:")||line.contains("all:")){
                    String[] wordLine=line.split(":");
                    line=wordLine[0]+":"+wordLine[1];
                    res=res+line+"<br/>";
                }else{
                    res=res+line+"<br/>";
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                bufferedReader.close();

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return res;
    }

    @Override
    public void downLoadFile(HttpServletResponse response, File file) {
        //首先设置响应的内容格式是force-download，那么你一旦点击下载按钮就会自动下载文件了
        response.setContentType("application/force-download");
        //通过设置头信息给文件命名，也即是，在前端，文件流被接受完还原成原文件的时候会以你传递的文件名来命名
        response.addHeader("Content-Disposition",String.format("attachment; filename=\"%s\"", file.getName()));
        //进行读写操作
        byte[]buffer=new byte[1024];
        FileInputStream fis=null;
        BufferedInputStream bis=null;
        try{
            fis=new FileInputStream(file);
            bis=new BufferedInputStream(fis);
            OutputStream os=response.getOutputStream();
            //从源文件中读
            int i=bis.read(buffer);
            while(i!=-1){
                //写到response的输出流中
                os.write(buffer,0,i);
                i=bis.read(buffer);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            //善后工作，关闭各种流
            try {
                if(bis!=null){
                    bis.close();
                }
                if(fis!=null){
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
