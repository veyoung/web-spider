package com.young.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ZhihuCanComment implements Serializable {
    public Boolean status;

    public String reason;
}
