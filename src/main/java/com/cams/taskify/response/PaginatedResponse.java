package com.cams.taskify.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PaginatedResponse<T> {
    private List<T> data;
    private int count;
    private int page;
    private int totalRecords;
    private int totalPages;
}
