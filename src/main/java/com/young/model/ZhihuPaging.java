package com.young.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ZhihuPaging implements Serializable {
    private Boolean is_end;

    private Boolean is_start;

    private Integer totals;

    private String previous;

    private String next;
}
