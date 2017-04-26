package com.young.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class ZhihuQuestion implements Serializable {
    private Long id;

    private String question_type;

    private Long created;

    private String url;

    private String title;

    private String type;

    private Date updated_time;
}
