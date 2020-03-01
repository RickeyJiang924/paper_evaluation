package com.nju.topics.service;

import com.nju.topics.domain.DictFileInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;

public interface FileService {
    public ArrayList<String> getAllSegmentFile();

    public ArrayList<DictFileInfo> getAllDictFile();

    public String uploadSegmentFile(MultipartFile file);

    public String deleteSegmentFile(String name);

    public String getDictLog(String name);

    public void downLoadFile(HttpServletResponse response,File file);
}
