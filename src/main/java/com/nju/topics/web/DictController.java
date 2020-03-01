package com.nju.topics.web;

import com.nju.topics.domain.ResponseInfo;
import com.nju.topics.service.Dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dict")
public class DictController {

    @Autowired
    private Dict dict;

//    public DictController(Dict dict) {
//        this.dict = dict;
//    }

    @GetMapping("/getDict/{dictName}")
    public List<String> getAllWords(@PathVariable("dictName")String dictName) {
        return dict.getAllWords(dictName);
    }

    @PostMapping("/addDict/{dictName}/{newSeg}")
    public void addWord(@PathVariable("dictName")String dictName,@PathVariable("newSeg") String word) {
        dict.addWord(dictName,word);
    }

    @PostMapping("/addDictList/{dictName}/{newSeg}")
    public void addWordList(@PathVariable("dictName")String dictName,@PathVariable("newSeg")List<String> word) {
        dict.addWordList(dictName,word);
    }

    @RequestMapping("/modifyDictFile/{dictName}/{segs}")
    public ResponseInfo modifyDictFile(@PathVariable("dictName")String dictName, @PathVariable("segs")List<String> segs){
         return dict.modifyDictFile(dictName,segs);
    }

    @PostMapping("/delete/{dictName}/{seg}")
    public void deleteWord(@PathVariable("dictName")String dictName,@PathVariable("seg") String word) {
        dict.deleteWord(dictName,word);
    }
}
