package org.jang.file.services;

import lombok.RequiredArgsConstructor;
import org.jang.file.constants.FileStatus;
import org.jang.file.entities.FileInfo;
import org.jang.file.exceptions.FileNotFountException;
import org.jang.file.repositories.FileInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileInfoService {
    private final FileInfoRepository infoRepository;
    //파일 한개 조회 파일 등록번호로
    public FileInfo get(Long seq){
        FileInfo item = infoRepository.findById(seq).orElseThrow(FileNotFountException::new);


        return item;
    }
    public List<FileInfo> getList(String gid, String location, FileStatus status){
        return null;
    }
}
