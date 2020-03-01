package com.nju.topics.web;

import com.nju.topics.config.Config;
import com.nju.topics.domain.DictFileInfo;
import com.nju.topics.domain.ResponseInfo;
import com.nju.topics.service.FileService;
import com.nju.topics.service.Segments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;

@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    private Config config;

    @Resource
    private FileService fileService;

    @Autowired
    private Segments segments;

    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "上传失败，请选择文件";
        }
        return fileService.uploadSegmentFile(file);
    }

    @RequestMapping("/getAllSegmentFiles")
    @ResponseBody
    public ArrayList<String> getAllSegmentFiles(){
        return fileService.getAllSegmentFile();
    }

    @RequestMapping("/getAllDictFiles")
    @ResponseBody
    public ArrayList<DictFileInfo> getAllDictFiles(){
        return fileService.getAllDictFile();
    }

    @RequestMapping("/download/{fileName}")
    @ResponseBody
    public void download( HttpServletResponse response,@PathVariable("fileName")String fileName){
        String filePath=config.getDownloadPath()+fileName;
        File file=new File(filePath);
        //当文件存在
        if(file.exists()){
            fileService.downLoadFile(response,file);
        }
    }

    @RequestMapping("/downloadSource/{fileName}")
    @ResponseBody
    public void downloadSource( HttpServletResponse response,@PathVariable("fileName")String fileName){
        String filePath=config.getUploadPath()+fileName;
        File file=new File(filePath);
        //当文件存在
        if(file.exists()){
            fileService.downLoadFile(response,file);
        }
    }

    @RequestMapping("/downloadTimes/{dictFile}")
    @ResponseBody
    public void downloadTimes(HttpServletResponse response, @PathVariable("dictFile")String fileName){
        String xlsName=fileName.split("\\.")[0]+".xls";
        String filePath=config.getDictStatisticsPath()+xlsName;
        File file=new File(filePath);
        if (file.exists()){
            fileService.downLoadFile(response,file);
        }
    }

    @RequestMapping("/hasTimes/{dictFile}")
    @ResponseBody
    public ResponseInfo hasTimes(@PathVariable("dictFile")String fileName){
        ResponseInfo responseInfo=new ResponseInfo();
        String xlsName=fileName.split("\\.")[0]+".xls";
        String filePath=config.getDictStatisticsPath()+xlsName;
        File file=new File(filePath);
        if (file.exists()){
            responseInfo.setResult("success");
            responseInfo.setDescription("已获取到下载内容");
        }else{
//            segments.reRunPy(fileName);
            responseInfo.setResult("error");
            responseInfo.setDescription("暂无词频文件!");
        }
        return responseInfo;
    }

    @RequestMapping("/getDictLog/{fileName}")
    @ResponseBody
    public String getDictLog(@PathVariable("fileName")String fileName){
        return fileService.getDictLog(fileName);
    }

}
