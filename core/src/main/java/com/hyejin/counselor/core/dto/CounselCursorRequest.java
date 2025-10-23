package com.hyejin.counselor.core.dto;

import lombok.Data;

@Data
public class CounselCursorRequest {

    private String lastRegDate;
    private String status;
    private int pageSize = 10;

}
