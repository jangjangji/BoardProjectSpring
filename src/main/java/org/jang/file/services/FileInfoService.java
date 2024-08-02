package org.jang.file.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jang.file.constants.FileStatus;
import org.jang.file.entities.FileInfo;
import org.jang.file.exceptions.FileNotFoundException;
import org.jang.file.repositories.FileInfoRepository;
import org.jang.global.configs.FileProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(FileProperties.class)
public class FileInfoService {
    private final FileInfoRepository infoRepository;
    private final FileProperties properties;
    private final HttpServletRequest request;
    //파일 한개 조회 파일 등록번호로
    public FileInfo get(Long seq){
        FileInfo item = infoRepository.findById(seq).orElseThrow(FileNotFoundException::new);
        addFileInfo(item);

        return item;
    }
    public List<FileInfo> getList(String gid, String location, FileStatus status){
        return null;
    }

    public void addFileInfo(FileInfo item){
        String fileUrl = getFileUrl(item);
        String filePath = getFilePath(item);
        item.setFileUrl(fileUrl);
        item.setFilePath(filePath);
    }
    //브라우저 접근 주소
    public String getFileUrl(FileInfo item){
       return request.getContextPath() + properties.getUrl() +"/"+ getFolder(item.getSeq()) + "/" + getFileName(item);
    }
    //서버 업로드 경로
    public String getFilePath(FileInfo item){
        return  properties.getPath()+ "/" + getFolder(item.getSeq()) + "/" + getFileName(item);
    }
    public String getFolder(long seq){
        return String.valueOf(seq % 10L);
    }
    public String getFileName(FileInfo item){
        String fileName = item.getSeq() + Objects.requireNonNullElse(item.getExtension(),"");
        return fileName;
    }
}
