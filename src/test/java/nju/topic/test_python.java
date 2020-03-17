package nju.topic;

import com.nju.topics.entity.ModelEntity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class test_python {
    public static void main(String[] args){
        try {
            ModelEntity me = new ModelEntity();
            me.setLanguage("python");
            me.setPath("E:\\study\\PycharmProjects\\lda_project\\citation_lda\\test\\remote.py");
            me.setParameter(new ArrayList<String>(){{
                add("a");
                add("b");
            }});
            if(me.getLanguage().equals("java")){
                //调用jar包
                System.out.println("执行结果:" + 1);
            }
            //python模型
            else{
                int size = 2 + me.getParameter().size();
                String[] cmdArr = new String[size];
                cmdArr[0] = me.getLanguage();
                cmdArr[1] = me.getPath();
                for(int i = 2; i < size; i++){
                    cmdArr[i] = me.getParameter().get(i-2);
                }
                Process process = Runtime.getRuntime().exec(cmdArr);
                //line表示接收到python代码输出的值
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while( ( line = in.readLine() ) != null ) {
                    System.out.println(line);
                }
                in.close();
                //0，表示python代码正常执行;
                //1，表示python代码报错了;
                //2，表示没有找到python文件
                int result = process.waitFor();
                System.out.println("执行结果:" + result);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
