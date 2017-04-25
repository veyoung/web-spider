package com.young.model;

import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.Date;

@Document(indexName="zhihu",type="answer",indexStoreType="fs",shards=5,replicas=1,refreshInterval="-1")
public class Answer implements Serializable {
    private Long id;

    private String editableContent;

    private String excerpt;

    private String collapsedBy;

    private Date createdTime;

    private Date updatedTime;

    private Integer voteupCount;

    private Boolean isCollapsed;

    private String url;

    private String commentPermission;

    private String content;

    private Integer commentCount;

    private String extras;

    private String reshipmentSettings;

    private Boolean isCopyable;

    private String type;

    private String thumbnail;

    private Boolean isNormal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEditableContent() {
        return editableContent;
    }

    public void setEditableContent(String editableContent) {
        this.editableContent = editableContent == null ? null : editableContent.trim();
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt == null ? null : excerpt.trim();
    }

    public String getCollapsedBy() {
        return collapsedBy;
    }

    public void setCollapsedBy(String collapsedBy) {
        this.collapsedBy = collapsedBy == null ? null : collapsedBy.trim();
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Integer getVoteupCount() {
        return voteupCount;
    }

    public void setVoteupCount(Integer voteupCount) {
        this.voteupCount = voteupCount;
    }

    public Boolean getIsCollapsed() {
        return isCollapsed;
    }

    public void setIsCollapsed(Boolean isCollapsed) {
        this.isCollapsed = isCollapsed;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getCommentPermission() {
        return commentPermission;
    }

    public void setCommentPermission(String commentPermission) {
        this.commentPermission = commentPermission == null ? null : commentPermission.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras == null ? null : extras.trim();
    }

    public String getReshipmentSettings() {
        return reshipmentSettings;
    }

    public void setReshipmentSettings(String reshipmentSettings) {
        this.reshipmentSettings = reshipmentSettings == null ? null : reshipmentSettings.trim();
    }

    public Boolean getIsCopyable() {
        return isCopyable;
    }

    public void setIsCopyable(Boolean isCopyable) {
        this.isCopyable = isCopyable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail == null ? null : thumbnail.trim();
    }

    public Boolean getIsNormal() {
        return isNormal;
    }

    public void setIsNormal(Boolean isNormal) {
        this.isNormal = isNormal;
    }
}