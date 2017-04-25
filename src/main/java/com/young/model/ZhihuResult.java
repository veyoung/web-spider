package com.young.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ZhihuResult {
    private ZhihuPaging paging;

    private List<ZhihuAnswer> data;
}
