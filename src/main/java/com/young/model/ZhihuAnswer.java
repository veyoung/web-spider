package com.young.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ZhihuAnswer implements Serializable{
    private Long id;

    private ZhihuRelationShip relationship;

    private String editable_content;

    private List<String> mark_infos;

    private String excerpt;

    private String collapsed_by;

    private Date created_time;

    private Date updated_time;

    private Integer voteup_count;

    private ZhihuCanComment can_comment;

    private Boolean is_collapsed;

    private ZhihuAuthor author;

    private String url;

    private String comment_permission;

    private ZhihuQuestion question;

    private ZhihuSuggestEdit suggest_edit;

    private String content;

    private Integer comment_count;

    private String extras;

    private String reshipment_settings;

    private Boolean is_copyable;

    private String type;

    private String thumbnail;

    private Boolean is_normal;
}
