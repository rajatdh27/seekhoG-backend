package com.seekhog.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class ExportRequest {
    private String userId;
    private List<Long> logIds;
}