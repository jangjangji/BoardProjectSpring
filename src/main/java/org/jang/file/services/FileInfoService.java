package org.jang.file.services;

import com.querydsl.core.BooleanBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jang.file.constants.FileStatus;
import org.jang.file.entities.FileInfo;
import org.jang.file.entities.QFileInfo;
import org.jang.file.exceptions.FileNotFoundException;
import org.jang.file.repositories.FileInfoRepository;
import org.jang.global.configs.FileProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static org.springframework.data.domain.Sort.Order.asc;

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
        QFileInfo fileInfo = QFileInfo.fileInfo;
        BooleanBuilder andBuilder = new BooleanBuilder();
        andBuilder.and(fileInfo.gid.eq(gid));

        if(StringUtils.hasText(location)){
            andBuilder.and(fileInfo.location.eq(location));
        }
        if(status != FileStatus.ALL){
            andBuilder.and(fileInfo.done.eq(status == FileStatus.DONE));
        }
        List<FileInfo> items = (List<FileInfo>) infoRepository.findAll(andBuilder, Sort.by(asc("createdAt")));//등록일자 기준 내림 차순 정렬

        //2차 추가 데이터 처리
        items.forEach(this::addFileInfo);

        return items;
    }
    public List<FileInfo> getList(String gid, String location){
        return getList(gid, location, FileStatus.DONE);

    }//가장 많이 조회하는거 done
    public List<FileInfo> getList(String gid){
        return getList(gid, null, FileStatus.DONE);
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
