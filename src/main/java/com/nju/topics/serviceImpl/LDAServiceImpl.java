package com.nju.topics.serviceImpl;

import com.nju.topics.config.Config;
import com.nju.topics.domain.EvolutionDataInfo;
import com.nju.topics.service.LDAService;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class LDAServiceImpl implements LDAService {

    @Autowired
    private Config config;

    @Override
    public JSONObject getLDARelation(String fileName) {

        JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(getLDARelationString(fileName));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return jsonObject;
    }

    @Override
    public String getLDARelationString(String parentPath) {
        String jsonString=new String();
        String encoding = "UTF-8";
        try {
            File file=ResourceUtils.getFile(parentPath+"json");

            Long filelength = file.length();
            byte[] filecontent = new byte[filelength.intValue()];
            try {
                FileInputStream in = new FileInputStream(file);
                in.read(filecontent);
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            jsonString=new String(filecontent);
//            System.out.println(jsonString);
        }catch (FileNotFoundException e){
            System.err.println(parentPath+"json 文件不存在");
        }

        return jsonString;
    }

    @Override
    public EvolutionDataInfo getEvolutionData(String parentPath, String id) {
        EvolutionDataInfo evolutionDataInfo=new EvolutionDataInfo();
        try {
            File file=new File(parentPath+"summary");
            BufferedReader bufferedReader=new BufferedReader(new FileReader(file));
            String line=null;
            String needToFindId="["+"Topic: "+id+"]"+":";
            while ((line=bufferedReader.readLine())!=null){
                if (line.contains(needToFindId)){
                    evolutionDataInfo.setId(id);
//                    System.err.println(needToFindId);
                    String[] temps=line.split("\\["+"Topic: "+id+"]"+":");
                    String topicInfo=temps[1];
                    String[] topicInfoSplit=topicInfo.split("  ");
                    evolutionDataInfo.setWeight(topicInfoSplit[0]);
                    evolutionDataInfo.setYear(topicInfoSplit[1].split("year=")[1]);

                    List paperList=new ArrayList();
                    List topicList=new ArrayList();
                    for(int i=0;i<config.getEVOLUTION_SUMMARY_PAPER()+config.getEVOLUTION_SUMMARY_TOPIC();i++){
                        String paperListLine=bufferedReader.readLine();
//                        System.out.println(paperListLine);
                        if (paperListLine.contains("Doc:")){
                            String paperListLineInfo=paperListLine.split("Doc:")[1];
                            String paperWeight=paperListLineInfo.split(":\\[")[0];
                            String paperName=paperListLineInfo.split("]:")[1];
                            String paperInfo=paperWeight+":"+paperName;
                            paperList.add(paperInfo);
                        }else if (paperListLine.contains("Tok:")){
                            String topicLine=paperListLine;
                            String topicLineInfo=topicLine.split("Tok:")[1];
                            topicList.add(topicLineInfo);
                        }else{
                            break;
                        }

                    }
                    evolutionDataInfo.setTopicPaper(paperList);
                    evolutionDataInfo.setTopicWords(topicList);
                    break;
                }
            }
            return evolutionDataInfo;
        }catch (FileNotFoundException fe){
            System.err.println("file:"+parentPath+"summary not found!");
            fe.printStackTrace();
            return null;
        }catch (IOException ie){
            System.err.println("read evolution summary file:"+parentPath+"summary exception!");
            ie.printStackTrace();
            return null;
        }

    }
}
