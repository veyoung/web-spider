package com.young.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class ZhihuResult implements Serializable {
    private ZhihuPaging paging;

    private List<ZhihuAnswer> data;
}
