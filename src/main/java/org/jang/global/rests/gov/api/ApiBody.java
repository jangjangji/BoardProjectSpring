package org.jang.global.rests.gov.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class ApiBody {
    private ApiItems items;
    private Integer numOfRows;
    private Integer pageNo;
    private Integer totalCount;
}
