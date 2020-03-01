package com.nju.topics.web;

import com.nju.topics.domain.PageDataInfo;
import com.nju.topics.domain.Segment;
import com.nju.topics.service.Segments;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/seg")
public class SegmentsController {

    private final Segments segments;

    public SegmentsController(Segments segments) {
        this.segments = segments;
    }

    @GetMapping("/getSegments/{segName}")
    public PageDataInfo getSegments(@PathVariable("segName")String segName){
//        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println("接收到请求segment，文件："+segName+",时间："+dateFormat.format((new Date())));
        return segments.getAllSegments(segName);

    }

    @GetMapping("/getPageSegments/{segName}/{page}")
    public List<Segment> getPreSegments(@PathVariable("segName")String segName,@PathVariable("page")String page){
        int pageNum=Integer.parseInt(page);
        return segments.getSegmentsByPage(segName,pageNum);
    }

    @GetMapping("/getPreSegments/{segName}")
    public List<Segment> getPreSegments(@PathVariable("segName")String segName){
        return segments.getPreSegments(segName);
    }

    @RequestMapping("/rerun/{segFileName}")
    public String reRunPy(@PathVariable("segFileName")String segFileName){
        return segments.reRunPy(segFileName);
    }
}
