package org.jang.global.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data

@ConfigurationProperties(prefix = "file.upload")
public class FileProperties {
    private String path;
    private String url;
}

