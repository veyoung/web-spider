package com.young.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ZhihuSuggestEdit implements Serializable {
    public Boolean status;

    public String reason;

    public String title;

    public String url;

    public String tip;

    public Object unnormal_details;
}
