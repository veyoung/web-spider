package com.young.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class ZhihuAuthor implements Serializable {
    private String id;

    private String avatar_url_template;

    private String name;

    private String headline;

    private Integer gender;

    private String user_type;

    private String url_token;

    private Boolean is_advertiser;

    private String avatar_url;

    private Boolean is_org;

    private String type;

    private String url;

    private Object badge;

}
