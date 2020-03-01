package com.nju.topics.serviceImpl;

import com.nju.topics.config.Config;
import com.nju.topics.domain.ResponseInfo;
import com.nju.topics.service.Dict;
import com.nju.topics.service.Segments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DictImpl implements Dict {

    @Autowired
    private Config config;

    @Autowired
    private Segments segments;

//    @Autowired
//    public DictImpl() {
//    }

    @Override
    public void addWord(String dictName, String word) {

        HashMap<String, Integer> dictMap = getAllOriginDict(dictName);
        File dict = null;
        try {
            String dictPath = config.getDownloadPath() + dictName;
            dict = ResourceUtils.getFile(dictPath);
            if (!dictMap.containsKey(word)) {
                OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(dict, true));
                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                bufferedWriter.write(word + " " + Config.DEFAULT_NUM + System.getProperty("line.separator"));
                writer.flush();
                bufferedWriter.flush();
                bufferedWriter.close();
                writer.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void addWordList(String dictName, List<String> words) {
        HashMap<String, Integer> dictMap = getAllOriginDict(dictName);
        File dict = null;
        try {
            String dictPath = config.getDownloadPath() + dictName;
            dict = ResourceUtils.getFile(dictPath);
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(dict, true));
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            for (String word : words) {
                if (!dictMap.containsKey(word)) {
                    bufferedWriter.write(word + " " + Config.DEFAULT_NUM + System.getProperty("line.separator"));
                }
            }
            writer.flush();
            bufferedWriter.flush();
            bufferedWriter.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<String> getAllWords(String dictName) {
        List<String> result = new ArrayList<>();
        File dict = null;
        try {
            String dictPath = config.getDownloadPath() + dictName;
            dict = ResourceUtils.getFile(dictPath);
            if (dict.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(dict), "utf8");
                BufferedReader bufferedReader = new BufferedReader(read);
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    String[] dictData = line.split(" ");
                    result.add(dictData[0]);
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
    public void deleteWord(String dictName, String word) {
        HashMap<String, Integer> dictMap = getAllOriginDict(dictName);
        File dict = null;
        try {
            String dictPath = config.getDownloadPath() + dictName;
            dict = ResourceUtils.getFile(dictPath);
            dictMap.remove(word);
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(dict));
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            for (Map.Entry<String, Integer> entry : dictMap.entrySet()) {
                bufferedWriter.write(entry.getKey() + " " + entry.getValue() + System.getProperty("line.separator"));
            }
            writer.flush();
            bufferedWriter.flush();
            bufferedWriter.close();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public ResponseInfo modifyDictFile(String dictName, List<String> words) {
        ResponseInfo responseInfo=new ResponseInfo();
        HashMap<String, Integer> dictMap = getAllOriginDict(dictName);
        File dict = null;
        try {
            String dictPath = config.getDownloadPath() + dictName;
            dict = ResourceUtils.getFile(dictPath);
            int addNum=0;
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(dict, true));
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
//            for (String word : words) {
//                if (dictMap.containsKey(word)) {
//                    sameNum++;
//                }
//                bufferedWriter.write(word + " " + Config.DEFAULT_NUM + System.getProperty("line.separator"));
//            }

            for (String word : words) {
                if (!dictMap.containsKey(word)) {
                    addNum++;
                    bufferedWriter.write(word + " " + Config.DEFAULT_NUM + System.getProperty("line.separator"));
                    dictMap.put(word,Config.DEFAULT_NUM);
                }
            }

            writer.flush();
            bufferedWriter.flush();
            bufferedWriter.close();
            writer.close();
            if (addNum>150){
                segments.reRunPy(dictName);
                responseInfo.setResult("success");
                responseInfo.setDescription("修改成功，已重新分词");
            }else{
                responseInfo.setResult("success");
                responseInfo.setDescription("修改成功(<150词)");
            }
            return responseInfo;

        } catch (IOException e) {
            e.printStackTrace();
            responseInfo.setResult("error");
            responseInfo.setDescription("写入文件错误");
            return responseInfo;
        }
    }

    public HashMap<String, Integer> getAllOriginDict(String dictName) {
        HashMap<String, Integer> dictMap = new HashMap<>();
        File dict = null;
        try {
            String dictPath = config.getDownloadPath() + dictName;
            dict = ResourceUtils.getFile(dictPath);
            if (dict.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(dict), "utf8");
                BufferedReader bufferedReader = new BufferedReader(read);

                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    String[] dictData = line.split(" ");
                    if (dictData.length > 1 && !dictData[1].equals(" ") && !dictData[1].equals("") && dictData[1].matches("\\d+")) {
                        dictMap.put(dictData[0], Integer.parseInt(dictData[1]));
                    } else if (dictData.length > 1) {
                        dictMap.put(dictData[0], 1);

                    }
                }

                bufferedReader.close();
                read.close();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return dictMap;
    }
}
