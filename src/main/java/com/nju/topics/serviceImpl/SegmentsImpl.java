package com.nju.topics.serviceImpl;

import com.nju.topics.config.Config;
import com.nju.topics.domain.PageDataInfo;
import com.nju.topics.domain.Segment;
import com.nju.topics.domain.StatisticsInfo;
import com.nju.topics.service.Dict;
import com.nju.topics.service.Segments;
import com.nju.topics.service.StatisticsService;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@Service
public class SegmentsImpl implements Segments {

    @Autowired
    private Config config;

    @Autowired
    private Dict dict;

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    public SegmentsImpl() {
    }

    @Override
    public PageDataInfo getAllSegments(String segName) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("接收到请求segment，文件：" + segName + ",时间：" + dateFormat.format((new Date())));
        List<Segment> result = getSegmentsNoneCondition(segName);

        System.out.println("获取到所有segment，文件：" + segName + ",时间：" + dateFormat.format((new Date())));

        PageDataInfo pageDataInfo=new PageDataInfo();
        if (result.size()>config.getPerPageNum()){
            int pages=(int)Math.ceil(result.size()*1.0/config.getPerPageNum());
            List<Segment> finalResList=new ArrayList<>();
            for(int i=0;i<config.getPerPageNum();i++){
                finalResList.add(result.get(i));
            }
            pageDataInfo.setPageNum(pages);
            pageDataInfo.setPageData(finalResList);
        }else {
            pageDataInfo.setPageNum(1);
            pageDataInfo.setPageData(result);

        }

        return pageDataInfo;
    }

    @Override
    public List<Segment> getSegmentsNoneCondition(String segName) {
        List<Segment> result = new ArrayList<>();
        File segmentsFile = null;
        try {
            String segPath = config.getUploadPath() + segName;
//            System.out.println("请求获取分词文件：路径是——"+segPath);
            segmentsFile = ResourceUtils.getFile(segPath);
//            segmentsFile = ResourceUtils.getFile("classpath:documents/segments.txt");
            if (segmentsFile.exists()) {

                InputStreamReader read = new InputStreamReader(new FileInputStream(segmentsFile), "utf8");
                BufferedReader bufferedReader = new BufferedReader(read);

                String line = null;
                long tempNum = 0;
                while ((line = bufferedReader.readLine()) != null) {
                    tempNum = tempNum + 1;
                    if (tempNum >= config.getPreGetNum()) {
                        String[] segData = line.split(" ");
                        if (segData.length == 2) {
                            String[] segs = segData[1].split(",");
                            Segment segment = new Segment();
                            segment.setTitle(segData[0]);
                            List<String> segments = new ArrayList<>();
                            Collections.addAll(segments, segs);
                            segment.setSegments(segments);
                            result.add(segment);
                        }

                    }

                }

                bufferedReader.close();
                read.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Segment> getSegmentsByPage(String segName, int pageNum) {
        int begin=(pageNum-1)*config.getPerPageNum();
        int end=begin+config.getPerPageNum();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("接收到请求segment分页，文件：" + segName+"页数:"+pageNum + ",时间：" + dateFormat.format((new Date())));
        List<Segment> result = new ArrayList<>();
        File segmentsFile = null;
        try {
            String segPath = config.getUploadPath() + segName;
//            System.out.println("请求获取分词文件：路径是——"+segPath);
            segmentsFile = ResourceUtils.getFile(segPath);
//            segmentsFile = ResourceUtils.getFile("classpath:documents/segments.txt");
            if (segmentsFile.exists()) {

                InputStreamReader read = new InputStreamReader(new FileInputStream(segmentsFile), "utf8");
                BufferedReader bufferedReader = new BufferedReader(read);

                String line = null;
                long tempNum = 0;
                while ((line = bufferedReader.readLine()) != null) {
                    if (tempNum >= begin && tempNum<end) {
                        String[] segData = line.split(" ");
                        if (segData.length == 2) {
                            String[] segs = segData[1].split(",");
                            Segment segment = new Segment();
                            segment.setTitle(segData[0]);
                            List<String> segments = new ArrayList<>();
                            Collections.addAll(segments, segs);
                            segment.setSegments(segments);
                            result.add(segment);
                        }

                    }
                    tempNum = tempNum + 1;
                }

                bufferedReader.close();
                read.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("获取到请求segment分页，文件：" + segName+"页数:"+pageNum + ",时间：" + dateFormat.format((new Date())));
        return result;
    }

    @Override
    public List<Segment> getPreSegments(String segName) {
        List<Segment> result = new ArrayList<>();
        File segmentsFile = null;
        try {
            String segPath = config.getUploadPath() + segName;
//            System.out.println("请求获取分词文件：路径是——"+segPath);
            segmentsFile = ResourceUtils.getFile(segPath);
//            segmentsFile = ResourceUtils.getFile("classpath:documents/segments.txt");
            if (segmentsFile.exists()) {

                InputStreamReader read = new InputStreamReader(new FileInputStream(segmentsFile), "utf8");
                BufferedReader bufferedReader = new BufferedReader(read);

                String line = null;
                long tempNum = 0;
                while ((line = bufferedReader.readLine()) != null && tempNum < config.getPreGetNum()) {
                    tempNum = tempNum + 1;
                    String[] segData = line.split(" ");
                    if (segData.length == 2) {
                        String[] segs = segData[1].split(",");
                        Segment segment = new Segment();
                        segment.setTitle(segData[0]);
                        List<String> segments = new ArrayList<>();
                        Collections.addAll(segments, segs);
                        segment.setSegments(segments);
                        result.add(segment);
                    }

                }
                bufferedReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String reRunPy(String segFileName) {
        System.out.println("RERUN:" + segFileName);
        long oldTime = config.getLastPyRunTime();
        System.out.println("oldTime:" + oldTime);
        long nowTime = System.currentTimeMillis();
        System.out.println("nowTime:" + nowTime);

        long duration = (nowTime - oldTime) / (1000 * 60);
        if (duration < config.getIntervalTime()) {
            return "距离上一次重新分词:" + duration + "分钟，请在 " + (config.getIntervalTime() - duration + 1) + " 分钟后再试一下";
        }
        config.setLastPyRunTime(nowTime);

//      记录日志文件
//        rerunLong:time[Long]
//        rerun:time[Date]
//        add:addNum:[addWords]
//        remove:removeNum:[removeWords]
//        all:allNum:[allWords]
        String dictLogsFilePath = config.getDictLogPath() + segFileName;
        File dictLogFile = new File(dictLogsFilePath);
        if (!dictLogFile.exists()) {
            try {
                dictLogFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return "日志记录错误";
            }
        }

        try {
            OutputStreamWriter logWriter = new OutputStreamWriter(new FileOutputStream(dictLogFile, true));
            BufferedWriter logBufferedWriter = new BufferedWriter(logWriter);
            logBufferedWriter.write("rerunLong:" + nowTime + "\r\n");
            Date date = new Date(nowTime);
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            logBufferedWriter.write("rerun:" + format.format(date) + "\r\n");
            if (dictLogFile.length() == 0) {

                List<String> nowAllDicts = dict.getAllWords(segFileName);
                int addNum = nowAllDicts.size();
                logBufferedWriter.write("add:" + addNum + ":" + nowAllDicts + "\r\n");
                logBufferedWriter.write("remove:0:" + "\r\n");
                String allWords = "";
                for (String eachW : nowAllDicts) {
                    allWords = allWords + eachW + ";";
                }
                logBufferedWriter.write("all:" + addNum + ":" + allWords + "\r\n");
                logBufferedWriter.write("\r\n");
                logBufferedWriter.close();
            } else {
                //            找出上一次的记录
                //            先找出上一次的时间
                ArrayList<Long> timeList = new ArrayList<>();
                InputStreamReader read = new InputStreamReader(new FileInputStream(dictLogFile), "utf8");
                BufferedReader bufferedReader = new BufferedReader(read);
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.contains("rerunLong")) {
                        String[] longSplit = line.split(":");
                        timeList.add(Long.parseLong(longSplit[1]));
                    }
                }
                bufferedReader.close();
                Long latesTime = new Long(0);
                for (int i = 0; i < timeList.size(); i++) {
                    if (timeList.get(i) > latesTime) {
                        latesTime = timeList.get(i);
                    }
                }
//                找出上一次所有的分词
                InputStreamReader logRead = new InputStreamReader(new FileInputStream(dictLogFile), "utf8");
                BufferedReader logBufferedReader = new BufferedReader(logRead);
                String logLine = null;
                ArrayList<String> preAllDicts = new ArrayList<>();
                while ((logLine = logBufferedReader.readLine()) != null) {
                    if (logLine.contains("rerunLong:" + latesTime.toString())) {
                        String specificLine = null;
                        while ((specificLine = logBufferedReader.readLine()) != null) {
                            if (specificLine.contains("all:")) {
                                String[] allSplit = specificLine.split(":");
                                if (allSplit.length > 2 && allSplit[2] != null && allSplit[2].length() > 1) {
                                    String[] preAllStrings = allSplit[2].split(";");
                                    for (int j = 0; j < preAllStrings.length; j++) {
                                        if (preAllStrings[j].length() > 0) {
                                            preAllDicts.add(preAllStrings[j]);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                logBufferedReader.close();

                ArrayList<String> addWords = new ArrayList<>();
                List<String> nowAllDicts = dict.getAllWords(segFileName);
//                System.err.println("pre:"+preAllDicts.toString());
//                System.err.println("now:"+nowAllDicts.toString());
                for (int i = 0; i < nowAllDicts.size(); i++) {
                    boolean hasSame = false;
                    for (int j = 0; j < preAllDicts.size(); j++) {
                        if ((nowAllDicts.get(i)).equals(preAllDicts.get(j))) {
                            hasSame = true;
                            break;
                        }
                    }
                    if (!hasSame && nowAllDicts.get(i).length()>0) {
                        addWords.add(nowAllDicts.get(i));
                    }
                }
                int addNum = addWords.size();
                logBufferedWriter.write("add:" + addNum + ":" + addWords + "\r\n");

                ArrayList<String> removeWords = new ArrayList<>();
                for (int m = 0; m < preAllDicts.size(); m++) {
                    boolean exists = false;
                    for (int n = 0; n < nowAllDicts.size(); n++) {
                        if ((nowAllDicts.get(n)).equals(preAllDicts.get(m))) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        removeWords.add(preAllDicts.get(m));
                    }
                }
                int removeNum = removeWords.size();
                logBufferedWriter.write("remove:" + removeNum + ":" + removeWords + "\r\n");
                String allWords = "";
                for (String eachW : nowAllDicts) {
                    allWords = allWords + eachW + ";";
                }
                logBufferedWriter.write("all:" + nowAllDicts.size() + ":" + allWords + "\r\n");
                logBufferedWriter.write("\r\n");
                logBufferedWriter.close();
            }


        } catch (Exception fe) {
            fe.printStackTrace();
            return "Failed to write new log to logFile";
        }


//        源文件
        String oldSegPath = config.getUploadPath() + segFileName;
        String oldDictPath = config.getDownloadPath() + segFileName;
//        备份文件
        String newSegBackUpPath = config.getSegBackUpPath() + (segFileName.split("\\."))[0] + nowTime + ".txt";
        String newDictBackUpPath = config.getDictBackUpPath() + (segFileName.split("\\."))[0] + nowTime + ".txt";

        FileReader segReader = null;
        FileWriter segWriter = null;
        FileReader dictReader = null;
        FileWriter dictWriter = null;

        try {
            segReader = new FileReader(oldSegPath);
            segWriter = new FileWriter(newSegBackUpPath);
            dictReader = new FileReader(oldDictPath);
            dictWriter = new FileWriter(newDictBackUpPath);
            int segLen = 0;
            while ((segLen = segReader.read()) != -1) {
                segWriter.write((char) segLen);
            }

            int dictLen = 0;
            while ((dictLen = dictReader.read()) != -1) {
                dictWriter.write((char) dictLen);
            }
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
            return "FileNotFound";
        } catch (IOException ie) {
            ie.printStackTrace();
            return "backUpFailure";
        } finally {
            if (segReader != null) {
                try {
                    segReader.close();
                } catch (IOException ie1) {
                    ie1.printStackTrace();
                }
            }

            if (segWriter != null) {
                try {
                    segWriter.close();
                } catch (IOException ie2) {
                    ie2.printStackTrace();
                }
            }

            if (dictReader != null) {
                try {
                    dictReader.close();
                } catch (IOException ie3) {
                    ie3.printStackTrace();
                }
            }


            if (dictWriter != null) {
                try {
                    dictWriter.close();
                } catch (IOException ie4) {
                    ie4.printStackTrace();
                }
            }
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String cmd = "python " + config.getPyPath() + config.getReRunFileName() + " " + oldSegPath + " " + oldDictPath;
        Process process;
        try {
            System.out.println("开始执行命令:" + cmd+ ",时间：" + dateFormat.format((new Date())));
            process = Runtime.getRuntime().exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line+ ",时间：" + dateFormat.format((new Date())));
            }
            in.close();
            System.out.println("结束执行命令:" + cmd+ ",时间：" + dateFormat.format((new Date())));
            process.waitFor();
            System.out.println("结束waitfor,时间：" + dateFormat.format((new Date())));

            List<StatisticsInfo> statisticsInfos=statisticsService.getAllKeyWordsByES("history");
            writeExcel(statisticsInfos,segFileName);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("执行python时发生异常");
            return "执行失败";
        } catch (InterruptedException ie) {
            ie.printStackTrace();
            System.err.println("执行waitFor异常");
            return "执行失败";
        }

        return "success";
    }
    public void writeExcel(List<StatisticsInfo> statisticsInfos,String segName){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("开始写excel" + dateFormat.format((new Date())));
                        //开始写入excel,创建模型文件头
                String[] titleA = {"关键词","词频"};
                //创建Excel文件
                String filePath=config.getDictStatisticsPath()+segName.split("\\.")[0]+".xls";
                File fileA = new File(filePath);
                if(fileA.exists()){
                    //如果文件存在就删除
                    fileA.delete();
                }
                try {
                    fileA.createNewFile();
                    //创建工作簿
                    WritableWorkbook workbookA = Workbook.createWorkbook(fileA);
                    //创建sheet
                    WritableSheet sheetA = workbookA.createSheet("词频统计", 0);
                    Label labelA = null;
                    //设置列名
                    for (int i = 0; i < titleA.length; i++) {
                        labelA = new Label(i,0,titleA[i]);
                        sheetA.addCell(labelA);    
                    }            
                    //获取数据源
                    for (int i = 1; i < statisticsInfos.size(); i++) {
                        labelA = new Label(0,i,statisticsInfos.get(i).getKey());
                        sheetA.addCell(labelA);
                        labelA = new Label(1,i,String.valueOf(statisticsInfos.get(i).getNum()));
                        sheetA.addCell(labelA);

                    }
                    workbookA.write();    //写入数据        
                    workbookA.close();   //关闭连接
                    System.out.println("成功写入文件，请前往E盘查看文件！");

                } catch (Exception e) {
                    System.err.println("文件写入失败，报异常...");
                }
    }
}


