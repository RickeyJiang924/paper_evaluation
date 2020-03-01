package com.nju.topics.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StartController {

    @RequestMapping("/")
    public String start(){
        return "seg/segment";
    }

    @RequestMapping("/file")
    public String fileManage(){
        return "file/fileUpDown";
    }

    @RequestMapping("/statistics")
    public String statistics(){
        return "statistics/statistics";
    }

    @RequestMapping("/tags")
    public String tags(){
        return "tag/tag";
    }

    @RequestMapping("/lda/evolution")
    public String evolution(){
        return "lda/evolution";
    }
}
