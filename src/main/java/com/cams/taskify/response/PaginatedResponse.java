package com.cams.taskify.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PaginatedResponse<T> {
    private T data;
    private int count;
    private int page;
    private long totalRecords;
    private int totalPages;
}
