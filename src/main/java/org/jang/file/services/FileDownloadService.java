package org.jang.file.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jang.file.entities.FileInfo;
import org.jang.file.exceptions.FileNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class FileDownloadService {
    private final FileInfoService infoService;
    private final HttpServletResponse response;
    public void download(Long seq) {
        FileInfo data =infoService.get(seq);
        String filePath = data.getFilePath();
        String fileName = new String(data.getFileName().getBytes(), StandardCharsets.ISO_8859_1);
        //한글안꺠지게 2바이트로 바꾸는 코드
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        String contentType = data.getContentType();
        contentType = StringUtils.hasText(contentType) ? contentType : "application/octet-stream";
        try(FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis))
        {
            response.setHeader("Content-Disposition","attachment; filename=" + fileName); // 헤더에 다운로드하라고 넣음
            response.setContentType(contentType);
            response.setIntHeader("Expires", 0); // 만료시간 x
            response.setHeader("Cache-Control", "must-revalidate");
            response.setContentLengthLong(file.length());

            OutputStream out = response.getOutputStream();
            out.write(bis.readAllBytes());

        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
