package com.nju.topics.web;

import com.nju.topics.config.Config;
import com.nju.topics.domain.EvolutionDataInfo;
import com.nju.topics.service.LDAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/lda")
public class LdaController {

    @Autowired
    private LDAService ldaService;

    @Autowired
    private Config config;

    @RequestMapping("/getJson")
    @ResponseBody
    public String getLdaJson(){
        return ldaService.getLDARelationString(config.getEvolutionFolderPath());
    }

    @RequestMapping("/getEvolutionData/{id}")
    @ResponseBody
    public EvolutionDataInfo getEvolutionData(@PathVariable("id")String id){
        String parentPath=config.getEvolutionFolderPath();
        return ldaService.getEvolutionData(parentPath,id);
    }
}
