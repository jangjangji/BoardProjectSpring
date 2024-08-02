package org.jang.file.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jang.global.entities.BaseMemberEntitiy;

import java.util.UUID;

@Entity
@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class FileInfo extends BaseMemberEntitiy {
    @Id @GeneratedValue
    private Long seq;// 서버에 업로드될 파일 이름 -seq.확장자

    @Column(length = 45, nullable = false)
    private String gid = UUID.randomUUID().toString(); // 그룹 ID

    @Column(length = 45)
    private String location; //그룹 안에 세부 위치

    @Column(length = 80, nullable = false)
    private String fileName;
    @Column(length = 30)
    private String extension; // 파일 확장자

    @Column(length = 80)
    private String contentType;

    private boolean done; // 그룹작업 완성된 파일이라는것을 알려주는것
    @Transient
    private String fileUrl; //파일 접근 url

    @Transient
    private String filePath; //파일 업로드 경로

}
