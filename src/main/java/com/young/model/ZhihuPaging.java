package com.young.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ZhihuPaging {
    private Boolean is_end;

    private Boolean is_start;

    private Integer totals;

    private String previous;

    private String next;
}
