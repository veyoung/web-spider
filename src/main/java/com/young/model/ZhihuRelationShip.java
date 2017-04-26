package com.young.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ZhihuRelationShip implements Serializable {

    private List<String> upvoted_followees;

    private Boolean is_author;

    private Boolean is_nothelp;

    private Boolean is_authorized;

    private Integer voting;

    private Boolean is_thanked;
}
