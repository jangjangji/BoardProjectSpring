package org.jang.global.rests.gov.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
public class ApiResult {
    private ApiResponse response;
}
